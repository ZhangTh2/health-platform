package edu.zju.ccnt.service.model;


import lombok.Data;

@Data
public class ServiceCallDetail {

    private Integer serviceId;  //服务id

    private Integer callCount;  //服务调用总次数

    private Integer success;   //服务调用成功次数

    private Integer failure;   //服务调用失败次数
}
