package com.aitrip.chatgpt.model;

import lombok.Data;

@Data
public class StreamMsg {
    private String uid;
    private String role;
    private String msg;
}
