package com.mbhatia.akkahttp.messages;

import com.mbhatia.akkahttp.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class CreateUserMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private final User user;
}
