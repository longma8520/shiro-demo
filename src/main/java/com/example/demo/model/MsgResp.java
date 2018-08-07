package com.example.demo.model;

import lombok.Data;

@Data
public class MsgResp {
    private int code;

    private String msg;

    private Object data;

    public MsgResp(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
