package com.mbhatia.akkahttp.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    @JsonProperty("id")
    private long Id;
    @JsonProperty("name")
    private String name;
}
