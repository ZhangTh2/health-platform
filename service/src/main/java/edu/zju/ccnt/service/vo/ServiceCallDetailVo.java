package edu.zju.ccnt.service.vo;

import lombok.Data;

@Data
public class ServiceCallDetailVo {

    private Integer serviceId;   //服务id

    private String serviceName;  //服务名

    private String category;    //服务类别

    private Integer totalCall;  //服务总调用次数

    private Integer successCall;    //成功的调用次数

    private Integer failureCall;    //失败的调用次数
}
