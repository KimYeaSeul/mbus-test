package com.kinx.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class GetToken {
    @JsonProperty("email")
    private String email;

    @JsonProperty("expire")
    private String expire;

    @JsonProperty("token")
    private String token;
}
