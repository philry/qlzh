package com.sy.controller;


import com.sy.constant.HttpStatusConstant;
import com.sy.entity.Role;
import com.sy.entity.WorkType;
import com.sy.service.WorkTypeService;
import com.sy.vo.JsonResult;
import com.sy.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/worktype")
@RestController
public class WorkTypeController {

    @Autowired
    WorkTypeService workTypeService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public PageResult getList(WorkType workType) {
        List<WorkType> list = workTypeService.selectWorkTypeList(workType);
        return PageResult.getPageResult(list);
    }

    @RequestMapping(value = "/querybyid/{id}", method = RequestMethod.GET)
    public JsonResult selectWorkTypeById(@PathVariable("id")Integer id) {
        return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, workTypeService.selectWorkTypeById(id));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResult add(WorkType workType) {
        return JsonResult.getJson(workTypeService.addWorkType(workType));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public JsonResult edit(WorkType workType) {
        return JsonResult.getJson(workTypeService.changeWorkType(workType));
    }

    @RequestMapping(value = "/removebyid/{id}", method = RequestMethod.GET)
    public JsonResult removeById(@PathVariable("id")Integer id) {
        try {
            workTypeService.deleteWorkTypeById(id);
            return JsonResult.getJson(1);
        } catch (Exception e) {
            return JsonResult.buildFailure(HttpStatusConstant.FAIL, e.getMessage());
        }
    }
}
