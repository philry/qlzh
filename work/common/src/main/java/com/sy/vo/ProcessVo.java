package com.sy.vo;

import com.sy.entity.ProcessCondition;
import com.sy.entity.ProcessDetail;

import java.util.List;

public class ProcessVo {

    private List<ProcessDetail> liuchengList;

    private List<ProcessCondition> tiaojianList;

    public List<ProcessDetail> getLiuchengList() {
        return liuchengList;
    }

    public void setLiuchengList(List<ProcessDetail> liuchengList) {
        this.liuchengList = liuchengList;
    }

    public List<ProcessCondition> getTiaojianList() {
        return tiaojianList;
    }

    public void setTiaojianList(List<ProcessCondition> tiaojianList) {
        this.tiaojianList = tiaojianList;
    }

}
