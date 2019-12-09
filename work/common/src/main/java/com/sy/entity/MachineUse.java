package com.sy.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "machine_use", schema = "qlzh", catalog = "")
public class MachineUse {
    private int id;
    private Integer machineId;
    private Integer time;
    private String rate;
    private Double voltage;
    private Double current;
    private Integer counts;

    public MachineUse() {
    }

    public MachineUse(int id) {
        this.id = id;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "machine_id")
    public Integer getMachineId() {
        return machineId;
    }

    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
    }

    @Basic
    @Column(name = "time")
    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    @Basic
    @Column(name = "rate")
    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @Basic
    @Column(name = "voltage")
    public Double getVoltage() {
        return voltage;
    }

    public void setVoltage(Double voltage) {
        this.voltage = voltage;
    }

    @Basic
    @Column(name = "current")
    public Double getCurrent() {
        return current;
    }

    public void setCurrent(Double current) {
        this.current = current;
    }

    @Basic
    @Column(name = "counts")
    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MachineUse that = (MachineUse) o;
        return id == that.id &&
                Objects.equals(machineId, that.machineId) &&
                Objects.equals(time, that.time) &&
                Objects.equals(rate, that.rate) &&
                Objects.equals(voltage, that.voltage) &&
                Objects.equals(current, that.current) &&
                Objects.equals(counts, that.counts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, machineId, time, rate, voltage, current, counts);
    }
}
