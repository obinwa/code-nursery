package com.aclive.codenursery.enums;

import lombok.Getter;

@Getter
public enum RequestType {

    SAVE_CODE("Save Initial Code"),
    UPDATE_CODE("Update Code"),
    RUN_CODE("Run Code");


    RequestType(String value) {
        this.value = value;
    }

    private String value;
}
