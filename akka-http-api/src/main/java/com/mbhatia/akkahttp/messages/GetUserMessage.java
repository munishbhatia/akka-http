package com.mbhatia.akkahttp.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class GetUserMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private final long userId;
}
