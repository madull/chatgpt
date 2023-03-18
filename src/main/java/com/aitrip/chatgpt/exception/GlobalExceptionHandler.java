package com.aitrip.chatgpt.exception;

import com.aitrip.chatgpt.model.Result;
import com.unfbx.chatgpt.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.aitrip.chatgpt.util.ResultUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@SuppressWarnings("ALL")
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Result exception(HttpServletRequest request,
                               HttpServletResponse response,
                               Exception exception) {
        return ResultUtils.fail(response.getStatus(),exception.getMessage());
    }

    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result baseException(HttpServletRequest request,
                                HttpServletResponse response,
                                BaseException baseException) {
        return ResultUtils.fail(response.getStatus(),baseException.getMessage());
    }

}
