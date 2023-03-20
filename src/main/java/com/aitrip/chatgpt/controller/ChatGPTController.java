package com.aitrip.chatgpt.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aitrip.chatgpt.model.StreamMsg;
import com.aitrip.chatgpt.util.LocalCache;
import com.aitrip.chatgpt.listener.OpenAIEventSourceListener;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.exception.CommonError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


@Slf4j
@Controller
@RequestMapping("/chatGPT")
public class ChatGPTController {

    @Autowired
    private OpenAiClient openAiClient;

    @Autowired
    private OpenAiStreamClient openAiStreamClient;


    @CrossOrigin
    @RequestMapping(value="/chat",produces="text/event-stream;charset=UTF-8")
    public SseEmitter chat(@RequestBody() StreamMsg streamMsg, @RequestHeader Map<String, String> headers) throws IOException {
        //默认30秒超时,设置为0L则永不超时
        SseEmitter sseEmitter = new SseEmitter(-1L);
        String uid = headers.get("uid");
        if (StrUtil.isBlank(uid)) {
            throw new BaseException(CommonError.SYS_ERROR);
        }
        String msg = streamMsg.getMsg();
        if (StrUtil.isBlank(msg)) {
            throw new BaseException(CommonError.SYS_ERROR);
        }
        String messageContext = (String) LocalCache.CACHE.get(uid);
        List<Message> messages = new ArrayList<>();
        if (StrUtil.isNotBlank(messageContext)) {
            messages = JSONUtil.toList(messageContext, Message.class);
            if (messages.size() >= 10) {
                messages = messages.subList(1, 10);
            }
            Message currentMessage = Message.builder().content(msg).role(Message.Role.USER).build();
            messages.add(currentMessage);
        } else {
            Message currentMessage = Message.builder().content(msg).role(Message.Role.USER).build();
            messages.add(currentMessage);
        }
        //sseEmitter.send(SseEmitter.event().id(uid).name("连接成功！！！！").data("").reconnectTime(3000));
        sseEmitter.onCompletion(() -> {
            log.info(LocalDateTime.now() + ", uid#" + uid + ", on completion");
        });
        sseEmitter.onTimeout(() -> log.info(LocalDateTime.now() + ", uid#" + uid + ", on timeout#" + sseEmitter.getTimeout()));
        sseEmitter.onError(
                throwable -> {
                    try {
                        log.info(LocalDateTime.now() + ", uid#" + "765431" + ", on error#" + throwable.toString());
                        sseEmitter.send(SseEmitter.event().id("765431").name("发生异常！").data(throwable.getMessage()).reconnectTime(3000));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        OpenAIEventSourceListener openAIEventSourceListener = new OpenAIEventSourceListener(sseEmitter);
        openAiStreamClient.streamChatCompletion(messages, openAIEventSourceListener);
        LocalCache.CACHE.put(uid, JSONUtil.toJsonStr(messages), LocalCache.TIMEOUT);
        return sseEmitter;
    }




    /*@ResponseBody
    @PostMapping("/cc")
    public Result<List<String>> chatCompletion(@RequestBody ChatCompletionMsg chatCompletionMsg) {
        Message.Role role;
        if(Message.Role.ASSISTANT.getName().equals(chatCompletionMsg.getRole())){
            role = Message.Role.ASSISTANT;
        }else if(Message.Role.USER.getName().equals(chatCompletionMsg.getRole())){
            role = Message.Role.USER;
        }else if(Message.Role.SYSTEM.getName().equals(chatCompletionMsg.getRole())){
            role = Message.Role.SYSTEM;
        }else {
            return new Result<>(chatCompletionMsg.getRequestId(), HttpStatus.BAD_REQUEST,"Role is illegal", Collections.EMPTY_LIST);
        }

        Message message = Message.builder().role(role).content(chatCompletionMsg.getContent()).build();
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(message)).build();
        ChatCompletionResponse chatCompletionResponse = openAiClient.chatCompletion(chatCompletion);
        List<String> list = new ArrayList<>();
        chatCompletionResponse.getChoices().forEach(e -> {
            list.add(e.getMessage().getContent());
        });

        return new Result<>(chatCompletionMsg.getRequestId(),HttpStatus.OK,"",list);
    }*/






}
