package edu.zju.ccnt.service.vo;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class ServiceSelfVo {        //用户服务提供方服务列表信息

    private Integer serviceId;

    private String serviceName;

    private String introduction;    //服务简要介绍

    private Integer userCount; //被购买次数

    private Integer commentCount;  //服务评价人数

    private BigDecimal score;   //评分

    private String serviceImg;	//服务照片地址

    private Integer categoryId;  //类别id

    private String categoryName;  //类别名

    private Integer status;     //状态

    private Integer checked;   //审核状态

    private String remarks;     //审核备注，主要是记录为通过的原因

    private String createTime;  //服务添加时间

    private Integer userId;  //服务提交者id

    private String userName;    //服务提交者姓名
}
