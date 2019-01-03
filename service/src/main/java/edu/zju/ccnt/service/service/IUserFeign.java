package edu.zju.ccnt.service.service;

import edu.zju.ccnt.service.common.ServerResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author zth
 * 2019.1.2
 * 调用user的服务
 */
@FeignClient(value = "USER-SERVICE")
public interface IUserFeign {

    @RequestMapping(value = "/api/user/getRole",method = RequestMethod.GET)
    public ServerResponse<Integer> getRole(@RequestParam Integer userId);

    @RequestMapping(value = "/api/user/judgeChecked",method = RequestMethod.GET)
    public ServerResponse<String> judgeChecked(@RequestParam Integer userId);

}
