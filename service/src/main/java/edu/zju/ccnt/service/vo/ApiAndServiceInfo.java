package edu.zju.ccnt.service.vo;

import lombok.Data;

/**
 * Created by LXY on 2017/11/2.
 */
@Data
public class ApiAndServiceInfo {

    private Integer apiId;

    private String name;

    private String mark;

    private String apiAddress;      //原始的api

    private String apiUrl;      //平台合成的api

    private String path;

    private Integer stripPrefix;

    private String description;

    private String apiCallWay;

    private String apiIntroduction;

    private Integer status;

    private Integer serviceId;

    private Integer checked;

    private String remarks;

    private String arguments;

    private Integer callLimit;

    private Integer callIpLimit;

    private String resultArguments;

    private String errorCode;

    private String result;

    private String returnStyle;

    private String callExample;

    private String createTime;

    private String updateTime;


    /*服务相关信息*/
    private Integer categoryId;

    private String serviceName;

    private Integer servicId;

    private String introduction;    //服务简要介绍
}
