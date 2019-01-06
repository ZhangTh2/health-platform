package edu.zju.ccnt.order.service;

import edu.zju.ccnt.order.common.ServerResponse;

public interface IOrderService {
    ServerResponse add(Integer userId, Integer serviceId, String format, Integer quantity);

//    ServerResponse list(Integer userId,Integer pageNum,Integer pageSize);
//
//    ServerResponse listOrders(Integer userId,Integer pageNum,Integer pageSize);
//
//    ServerResponse superAdminList(String orderNo, Integer pageNum, Integer pageSize);
//
//    ServerResponse superAdminDetail(String orderNo);
//
//    ServerResponse commentOrder(Integer userId, String orderNo, double score);
}
