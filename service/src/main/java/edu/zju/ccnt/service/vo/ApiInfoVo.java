package edu.zju.ccnt.service.vo;

import lombok.Data;

/**
 * Created by LXY on 2017/11/2.
 */
@Data
public class ApiInfoVo {        //普通用户只显示封装的url，api拥有者还需要显示原来的url
    private Integer id;

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

    private String appkey;
}
