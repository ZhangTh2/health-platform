package edu.zju.ccnt.service.vo;

import lombok.Data;

import java.util.List;


@Data

public class ServiceInfoByCategory {

    public int categoryId;

    public String categoryName;

    public List<ServiceVo> serviceVos;


}



