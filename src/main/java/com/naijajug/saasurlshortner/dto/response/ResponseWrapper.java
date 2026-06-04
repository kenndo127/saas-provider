package com.naijajug.saasurlshortner.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatusCode;

@Builder
@Getter
@Setter
@ToString
public class ResponseWrapper <T> {
    private T data;
    private String message;
    private HttpStatusCode statusCode;
}
