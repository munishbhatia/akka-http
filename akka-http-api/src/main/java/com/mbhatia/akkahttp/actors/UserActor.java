package com.mbhatia.akkahttp.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.FI;
import com.mbhatia.akkahttp.messages.ActionPerformed;
import com.mbhatia.akkahttp.messages.CreateUserMessage;
import com.mbhatia.akkahttp.messages.GetUserMessage;
import com.mbhatia.akkahttp.messages.GetUsersMessage;
import com.mbhatia.akkahttp.services.implementations.UserService;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserActor extends AbstractActor {
    private UserService userService = new UserService();

    public static Props props(){
        return Props.create(UserActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CreateUserMessage.class, createUser())
                .match(GetUserMessage.class, getUser())
                .match(GetUsersMessage.class, getUsers())
                .build();
    }

    private FI.UnitApply<GetUsersMessage> getUsers() {
        return getUsersMessage -> {
            sender().tell(userService.getUsers(), getSelf());
        };
    }

    private FI.UnitApply<CreateUserMessage> createUser() {
        return createUserMessage -> {
            userService.createUser(createUserMessage.getUser());
            sender().tell(new ActionPerformed(String.format("User %s created.", createUserMessage.getUser())), getSelf());
        };
    }

    private FI.UnitApply<GetUserMessage> getUser() {
        return  getUserMessage -> {
            sender().tell(userService.getUserById(getUserMessage.getUserId()), getSelf());
        };
    }
}
