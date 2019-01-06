package edu.zju.ccnt.service.controller;

import edu.zju.ccnt.service.common.ServerResponse;
import edu.zju.ccnt.service.service.IServiceApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service")
public class ServiceApiController {
    @Autowired
    private IServiceApiService iServiceApiService;

    @RequestMapping(value = "validate",method = RequestMethod.GET)
    public ServerResponse validateService(Integer serviceId) {
        return iServiceApiService.validateService(serviceId);
    }
}
