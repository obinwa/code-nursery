package com.aclive.codenursery.enums;

import lombok.Getter;

@Getter
public enum RequestType {

    SAVE_CODE("Save Initial Code"),
    UPDATE_CODE("Save Initial Code");


    RequestType(String value) {
        this.value = value;
    }

    private String value;
}
