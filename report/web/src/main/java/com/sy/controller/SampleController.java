package com.sy.controller;


import com.sy.starter.Starter;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SampleController {

//    Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    Logger logger = Logger.getLogger(SampleController.class);

    @GetMapping(value = "/sample/testlog")
    @ResponseBody
    Object testlog() {
        logger.info("统计异常池数量异常,异常信息如下:e.getStackTrace().toString()");
        return "ok";
    }
}
