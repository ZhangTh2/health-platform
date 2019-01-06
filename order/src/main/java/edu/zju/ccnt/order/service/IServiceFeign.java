package edu.zju.ccnt.order.service;

import edu.zju.ccnt.order.common.ServerResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zth
 * Service-Feign
 */
@FeignClient(value = "SERVICE-SERVICE")
public interface IServiceFeign {

    //验证服务是否可用
    @RequestMapping(value = "/api/service/validate",method = RequestMethod.GET)
    public ServerResponse<String> validateService(@RequestParam("serviceId") Integer serviceId);

    //更新服务的购买次数
    @RequestMapping(value ="/api/service/buy",method = RequestMethod.GET)
    public  ServerResponse serviceBuy(@RequestParam("serviceId")Integer serviceId,@RequestParam("count")Integer count);
}
