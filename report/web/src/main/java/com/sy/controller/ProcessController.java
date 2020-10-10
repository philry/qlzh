package com.sy.controller;

import com.sy.constant.HttpStatusConstant;
import com.sy.entity.Process;
import com.sy.service.ProcessService;
import com.sy.vo.JsonResult;
import com.sy.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/process")
@RestController
public class ProcessController {

        @Autowired
        ProcessService processService;

        @RequestMapping(value = "/list", method = RequestMethod.GET)
        public PageResult getList(Process process) {
            List<Process> list = processService.selectProcessList(process);
            return PageResult.getPageResult(list);
        }

        @RequestMapping(value = "/querybyid/{id}", method = RequestMethod.GET)
        public JsonResult selectWorkTypeById(@PathVariable("id")Integer id) {
            return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, processService.selectProcessById(id));
        }

        @RequestMapping(value = "/add", method = RequestMethod.POST)
        public JsonResult add(Process process) {
            return JsonResult.getJson(processService.addProcess(process));
        }

        @RequestMapping(value = "/edit", method = RequestMethod.POST)
        public JsonResult edit(Process process) {
            return JsonResult.getJson(processService.changeProcess(process));
        }

        @RequestMapping(value = "/removebyid/{id}", method = RequestMethod.GET)
        public JsonResult removeById(@PathVariable("id")Integer id) {
            try {
                processService.deleteProcessById(id);
                return JsonResult.getJson(1);
            } catch (Exception e) {
                return JsonResult.buildFailure(HttpStatusConstant.FAIL, e.getMessage());
            }
        }
}
