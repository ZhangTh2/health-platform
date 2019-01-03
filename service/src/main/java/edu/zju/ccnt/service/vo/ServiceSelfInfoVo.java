package edu.zju.ccnt.service.vo;

import edu.zju.ccnt.service.common.Const;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class ServiceSelfInfoVo {

    private Integer serviceId;

    private String serviceName;

    private String format;      //服务购买规格

    private BigDecimal price;       //服务单次调用最便宜的价格

    private String introduction;        //服务简介

    private String detailIntroduction;  //服务详细介绍

    private Integer userCount; //被购买次数

    private Integer commentCount;  //服务评价人数

    private BigDecimal score;   //评分

    private String serviceImg;	//服务照片地址

    private String imgUrl;      //图片地址

    private Integer categoryId;

    private String categoryName;  //服务类别名称

    private Integer status;     //状态

    private Integer checked;   //审核状态

    private String remarks;

    private String createTime;  //服务添加时间

    private String updateTime;   //服务更新时间

    private Integer userId;  //服务提交者id

    private String userName;    //服务提交者姓名

    private List<Const.SystemErrorCode> systemErrorCode;     //还需要一个系统错误码


    private List<ApiInfoVo> apiInfoList;  //服务api的信息

}
