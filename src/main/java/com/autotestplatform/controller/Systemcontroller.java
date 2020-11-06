package com.autotestplatform.controller;

import com.autotestplatform.service.impl.SystemService;
import com.autotestplatform.utils.RestApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class Systemcontroller {
    @Autowired
    private SystemService systemService;

    @GetMapping("/")
    public RestApiResult error(){
        log.error("error");
        return systemService.error();
    }
}
