package com.aitrip.chatgpt.model;

import lombok.Data;

@Data
public class ChatCompletionMsg {
    private String requestId;
    private String msgId;
    private String role;
    private String content;



}
