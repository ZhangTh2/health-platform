package edu.zju.ccnt.orginazation.controller;


import edu.zju.ccnt.orginazation.common.ServerResponse;
import edu.zju.ccnt.orginazation.service.IOrganizationApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organization/")
@Api(value = "组织提供给系统内部的Rest API")
public class OrganizationApiController {
    @Autowired
    private IOrganizationApiService iOrganizationApiService;

    //根据orginazationid判断组织是否存在
    @RequestMapping(value = "judge",method = RequestMethod.GET)
    @ApiOperation(value = "判断组织是否存在",notes = "id来判断")
    public ServerResponse judgebyId(@RequestParam("id") Integer organizationId){
        return iOrganizationApiService.judgebyId(organizationId);
    }

    //根据orginazationid获取组织名称
    @RequestMapping(value = "getname",method = RequestMethod.GET)
    @ApiOperation(value = "获得组织名称",notes = "传入组织id")
    public ServerResponse getbyId(@RequestParam("id") Integer organizationId){
        return iOrganizationApiService.judgebyId(organizationId);
    }

}
