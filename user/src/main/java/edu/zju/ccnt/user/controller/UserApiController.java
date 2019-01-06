package edu.zju.ccnt.user.controller;


import edu.zju.ccnt.user.common.ServerResponse;
import edu.zju.ccnt.user.service.IUserService;
import edu.zju.ccnt.user.service.IUerApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zth
 * 2019.1.3
 * 用来给内部系统的其他微服务提供用户服务的接口
 */

@RestController
@RequestMapping("/api/user/")
@Api(value = "UserApiController",description = "USER-SERVICE提供给内部微服务使用的接口")
public class UserApiController {

    @Autowired
    private IUerApiService iUerApiService;

    @RequestMapping(value = "getRole",method = RequestMethod.GET)
    @ApiOperation(value = "获取用户角色",notes = "提供userId，返回角色Id")
    public ServerResponse getRole(Integer userId) {
    return iUerApiService.getRoleIdbyUserId(userId);
    }

    @RequestMapping(value = "getOrganization",method = RequestMethod.GET)
    @ApiOperation(value = "获取用户所属组织id",notes = "提供userId，返回所属组织Id")
    public ServerResponse getOrganization(Integer userId) {
        return iUerApiService.getOrganizationIdbyUId(userId);
    }

    @RequestMapping(value = "judgeChecked",method = RequestMethod.GET)
    @ApiOperation(value = "判断用户是否被审核",notes = "提供userId,被审核的话返回String true,否则String false")
    public ServerResponse judgeChecked(Integer userId) {
        return iUerApiService.ischecked(userId);
    }



}
