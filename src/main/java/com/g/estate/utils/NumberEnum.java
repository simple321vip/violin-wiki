package com.g.estate.utils;

public enum NumberEnum {

    T_BOOKMARK("t_bookmark"),
    T_BLOG("t_blog")
    ;

    private String tableName;

    NumberEnum(String tableName) {
        this.tableName = tableName;
    }

    public String value() {
        return tableName;
    }
}
