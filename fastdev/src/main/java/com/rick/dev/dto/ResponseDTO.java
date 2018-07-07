package com.rick.dev.dto;

/**
 * Created by rick on 2017/7/18.
 */
public class ResponseDTO {

    public static final boolean STATUS_SUCCESS = true;

    public static final boolean STATUS_FAIL = false;

    private boolean status;


    private Object data;

    public boolean isStatus() {
        return status;
    }

    public ResponseDTO setStatus(boolean status) {
        this.status = status;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ResponseDTO setData(Object data) {
        this.data = data;
        return this;
    }
}
