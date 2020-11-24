package com.sy.entity;

import java.util.List;

public class ZRole {

    private int id;

    private String name;

    private int pid;

    private int level;

    private List<ZRole> roles;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<ZRole> getRoles() {
        return roles;
    }

    public void setRoles(List<ZRole> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pid=" + pid +
                ", level=" + level +
                '}';
    }

}
