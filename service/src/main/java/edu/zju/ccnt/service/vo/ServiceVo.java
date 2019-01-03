package edu.zju.ccnt.service.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ServiceVo implements Serializable {

    private Integer categoryId;

    private Integer serviceId;

    private String serviceName;

    private String introduction;    //服务简要介绍

    private Integer userCount; //被购买次数

    private Integer commentCount;  //被评论次数

    private BigDecimal score;   //评分

    private String serviceImg;	//服务照片地址

    /**
     * mark
     *需要交互的
     */
    //private List<ApiInfoVo> apiInfoVoList;

    private String createTime;  //创建时间

}
