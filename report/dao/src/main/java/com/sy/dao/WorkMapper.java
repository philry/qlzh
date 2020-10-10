package com.sy.dao;

import com.sy.entity.Work;

import java.util.List;

public interface WorkMapper {
    List<Work> getCloseWorkByCreateTime1(String time);
}
