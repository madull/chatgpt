package com.aitrip.chatgpt.model;

import lombok.Data;
import org.springframework.http.HttpStatus;


@Data
public class Result<T> {
    private String requestId;
    private Integer code;
    private String msg;
    private T data;


    public Result() {
    }

    public Result(String requestId, HttpStatus status, String msg, T data) {
        this.requestId = requestId;
        this.code = status.value();
        this.msg = msg;
        this.data = data;
    }


}
