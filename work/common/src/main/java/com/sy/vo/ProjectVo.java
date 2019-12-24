package com.sy.vo;

import java.util.List;

public class ProjectVo {

    private String workNo;

    private int workerNo;

    private String power;

    private String Proportion;

    private List<AppVo> appVos;

    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }

    public int getWorkerNo() {
        return workerNo;
    }

    public void setWorkerNo(int workerNo) {
        this.workerNo = workerNo;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getProportion() {
        return Proportion;
    }

    public void setProportion(String proportion) {
        Proportion = proportion;
    }

    public List<AppVo> getAppVos() {
        return appVos;
    }

    public void setAppVos(List<AppVo> appVos) {
        this.appVos = appVos;
    }
}
