package com.aitrip.chatgpt.util;

import com.aitrip.chatgpt.model.Result;
import org.springframework.http.HttpStatus;

public class ResultUtils {


    public static <T> Result<T> success(T data) {
        Result result = new Result();
        result.setCode(HttpStatus.OK.value());
        result.setData(data);
        return result;
    }

    public static <T> Result<T> fail(HttpStatus status,String msg) {
        Result result = new Result();
        result.setCode(status.value());
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> fail(int status,String msg) {
        Result result = new Result();
        result.setCode(status);
        result.setMsg(msg);
        return result;
    }


}
