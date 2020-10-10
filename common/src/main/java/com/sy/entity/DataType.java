package com.sy.entity;

public class DataType {

    private String name;

    private String type;

    private String typeName;

    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "DataType{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", typeName='" + typeName + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
