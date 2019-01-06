package edu.zju.ccnt.service.service;


import edu.zju.ccnt.service.common.ServerResponse;

/**
 * @author zth
 * 给系统其他微服务提供的服务
 */
public interface IServiceApiService {

    //根据服务id验证服务是否可用，服务验证失败返回失败代码和信息，成功则返回成功代码和服务名
    ServerResponse<String> validateService(Integer serviceId);

    //服务购买后购买次数字段要增加
    /**
     * mark
     * 要实现
    ServerResponse buyservice(Integer servidId,Integer count);
     */
}
