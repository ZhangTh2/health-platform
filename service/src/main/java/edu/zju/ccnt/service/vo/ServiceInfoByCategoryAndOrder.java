package edu.zju.ccnt.service.vo;

import lombok.Data;


import java.util.List;


@Data
public class ServiceInfoByCategoryAndOrder {

    public int categoryId;

    public String categoryName;

    public List<ServiceVoByOrder> serviceVoByOrders;


}



