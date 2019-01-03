package edu.zju.ccnt.service.vo;

import edu.zju.ccnt.service.common.Const;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class ServiceInfoVo {      //服务详细信息

    private Integer id;  //服务id

    private String serviceName; //服务名

    private String serviceImg;  //服务图片

    private Integer categoryId;    //服务分类ID

    private String categoryName;    //服务分类

    private Integer userId;     //服务商id

    private Integer organizationId;     //服务提供者组织id

    private String enterpriseName;      //服务商名

    private String format;      //服务购买规格

    private BigDecimal price;       //服务单次调用最便宜的价格

    private String introduction;        //服务简介

    private String detailIntroduction;  //服务详细介绍

    private Integer userCount;   //服务被购买次数

    private Integer commentCount;  //服务评价人数

    private BigDecimal score;   //服务评分



    private Const.SystemRequestParam systemRequestParam;    //系统级别的请求参数

    private List<Const.SystemErrorCode> systemErrorCode;     //还需要一个系统错误码

    /**
     * mark 需要交互
     */
    //private List<ApiInfoVo> apiInfoList;  //服务api的信息

   // private List<SdkVo> sdkVoList;      //sdk列表
}
