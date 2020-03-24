package com.mbhatia.akkahttp.controllers;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.HttpApp;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;
import com.mbhatia.akkahttp.actors.UserActor;
import com.mbhatia.akkahttp.entities.User;
import com.mbhatia.akkahttp.messages.ActionPerformed;
import com.mbhatia.akkahttp.messages.CreateUserMessage;
import com.mbhatia.akkahttp.messages.GetUserMessage;
import com.mbhatia.akkahttp.messages.GetUsersMessage;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static akka.http.javadsl.server.PathMatchers.segment;
import static akka.http.javadsl.server.PathMatchers.longSegment;

public class UserController extends HttpApp {
    private final ActorRef userActor;
    Duration duration = Duration.ofSeconds(5);

    UserController(ActorRef userActor){
        this.userActor = userActor;
    }

    @Override
    protected Route routes() {
        return path("users", this::postUser)
                .orElse(path(segment("users").slash(longSegment()), id ->
                        route(getUser(id))))
                .orElse(path("users", this::getUsers));
    }

    private Route getUsers() {
        return get(() -> {
            CompletionStage<List<User>> users = Patterns.ask(userActor, new GetUsersMessage(), duration)
                    .thenApply(obj -> (List<User>) obj);
            return onSuccess(() -> users, result -> complete(StatusCodes.OK, result, Jackson.marshaller()));
        });
    }

    private Route getUser(long id) {
        return get(() -> {
            CompletionStage<Optional<User>> user = Patterns.ask(userActor, new GetUserMessage(id), duration)
                    .thenApply(obj -> (Optional<User>) obj);


            return onSuccess(() -> user, result -> {
                if(result.isPresent())
                    return complete(StatusCodes.OK, result.get(), Jackson.marshaller());
                else
                    return complete(StatusCodes.NOT_FOUND);
            });
        });
    }

    private Route postUser() {
        return post(() -> entity(Jackson.unmarshaller(User.class), user -> {
            CompletionStage<ActionPerformed> userCreated = Patterns.ask(userActor, new CreateUserMessage(user), duration)
                    .thenApply(obj -> (ActionPerformed) obj);
            return onSuccess(userCreated, result -> complete(StatusCodes.CREATED, result, Jackson.marshaller()));
        }));
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ActorSystem actorSystem = ActorSystem.create("userServer");
        ActorRef userActor = actorSystem.actorOf(UserActor.props(), "userActor");
        UserController userController = new UserController(userActor);
        userController.startServer("localhost", 8080, actorSystem);
    }
}
