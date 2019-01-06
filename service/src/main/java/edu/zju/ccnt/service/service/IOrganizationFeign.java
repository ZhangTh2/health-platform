package edu.zju.ccnt.service.service;

import edu.zju.ccnt.service.common.ServerResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zth
 * 2018.12.22
 * 调用orginazation的服务
 */

@FeignClient(value = "orginazation-service")
public interface IOrganizationFeign {

    //根据id判断组织是否存在
    @RequestMapping(value="/api/organization/judge",method = RequestMethod.GET)
    public ServerResponse<String> judgebyId(@RequestParam("id") Integer id);

    //由id获取组织的名字
    @RequestMapping(value="/api/organization/getname",method = RequestMethod.GET)
    public ServerResponse<String> getbyId(@RequestParam("id") Integer id);

}
