package com.g.estate.utils;

public class Item {

    private double No;

    private String itemName;

    private String columnName;

    private String prop;

    private double len;

    private String desc;

    private String defualt;

    public double getNo() {
        return No;
    }

    public void setNo(double no) {
        No = no;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public double getLen() {
        return len;
    }

    public void setLen(double len) {
        this.len = len;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDefualt() {
        return defualt;
    }

    public void setDefualt(String defualt) {
        this.defualt = defualt;
    }
}
