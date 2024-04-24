package com.example.log_collect.parent;

import java.io.Serializable;

public abstract class BaseDTO implements Serializable {
    private String id;

    public BaseDTO() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }
}