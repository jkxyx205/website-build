package com.rick.dev.plugin.syntable.model;

/**
 * Created by Rick.Xu on 2016/02/23.
 */
public enum ChangeStatus {
    SUCCESSFULLY(1),FAILED(2);
    private int status;
    ChangeStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }
}
