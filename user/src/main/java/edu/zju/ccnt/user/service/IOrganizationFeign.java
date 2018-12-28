package edu.zju.ccnt.user.service;


import edu.zju.ccnt.user.common.ServerResponse;
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
    @RequestMapping(value="/manage/organization/judge.do",method = RequestMethod.GET)
    public ServerResponse judgebyId(@RequestParam Integer id);

    //由id获取组织的对象
    @RequestMapping(value="/manage/organization/getname.do",method = RequestMethod.GET)
    public ServerResponse getbyId(@RequestParam Integer id);


}
