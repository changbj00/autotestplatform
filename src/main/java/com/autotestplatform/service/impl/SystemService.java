package com.autotestplatform.service.impl;

import com.autotestplatform.utils.RestApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SystemService {
    @Autowired
    private RestApiResult restApiResult;

    public RestApiResult error(){
        return restApiResult.faild();
    }
}
