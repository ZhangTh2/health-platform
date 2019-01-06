package edu.zju.ccnt.order.service.Impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import edu.zju.ccnt.order.common.Const;
import edu.zju.ccnt.order.common.ResponseCode;
import edu.zju.ccnt.order.common.ServerResponse;
import edu.zju.ccnt.order.dao.OrderMapper;
import edu.zju.ccnt.order.model.Order;
import edu.zju.ccnt.order.service.IServiceFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service("iOrderService")
public class OrderServiceImpl {


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private IServiceFeign iServiceFeign;

    //订单添加
    @Transactional
    public ServerResponse add(Integer userId, Integer serviceId, String format, Integer quantity){
        log.info("订单添加");

        /**
         *审核过的用户才能下单觉得不合理
        User user = userMapper.selectByPrimaryKey(userId);
        if(!user.getChecked().equals(Const.CheckStatus.CHECKED))
            return ServerResponse.createByErrorMessage("只能已通过审核用户才能下单");
         */

        /**
         *逻辑：
         * 如果ServiceId不存在  错误
         * 由Servcie-Service提供的服务验证确认服务是否可用
         * ServiceFeign
         */
        if(serviceId == null)
            return ServerResponse.createByErrorMessage(ResponseCode.ILLEGLE_ARGUMENT.getDesc());
        ServerResponse serviceServerResponse = iServiceFeign.validateService(serviceId);
        String serviceName;
        if(serviceServerResponse.getStatus()==1)
            return ServerResponse.createByErrorMessage(serviceServerResponse.getMsg());
        else
            serviceName=serviceServerResponse.getMsg();
        /**
         * design
         * 服务的format需要重新设计
         *
        List<String> formatList  = Lists.newArrayList(service.getFormat().split(","));
        if(!formatList.contains(format))
            return ServerResponse.createByErrorMessage("不存在该规格");
         */
        if(quantity <=0){
            return ServerResponse.createByErrorMessage("数量不能为空");
        }

        Integer totalCall = Integer.parseInt(format.substring(format.lastIndexOf('/')+1,format.indexOf('次'))) * quantity;
        BigDecimal totalPrice = new BigDecimal(Integer.parseInt(format.substring(0,format.lastIndexOf('元'))) * quantity);

        Order order = new Order();
        order.setOrderNo(generateOrderNo().toString());
        order.setUserId(userId);
        order.setServiceId(serviceId);
        order.setServiceName(serviceName);
        order.setBuyTime(new Date());
        order.setFormat(format);
        order.setApikey(generateApiKey());
        order.setQuantity(quantity);
        order.setTotalCall(totalCall);
        order.setCanCall(totalCall);
        order.setUseCall(0);
        order.setTotalPrice(totalPrice);
        /**
         * mark
         *直接设置为订单已完成不合适
         */
        order.setStatus(Const.OrderStatusEnum.ORDER_SUCCESS.getCode());     //直接设置为订单已完成

        int resultCount = orderMapper.insert(order);
        if(resultCount > 0){
            /**
             * mark
             * 原来的设计是服务的次数直接+1，+quantity是不是更好
             */

            /**
             * Service-Feign
             */

            int updatestatus = iServiceFeign.serviceBuy(serviceId,quantity).getStatus();   //订单添加成功，服务购买次数增加
            if(updatestatus==ResponseCode.SUCCESS.getCode()) {
                return ServerResponse.createBySuccessMessage("订单添加成功");
            }else
                return ServerResponse.createByErrorMessage("订单添加失败");
        }
        return ServerResponse.createByErrorMessage("订单添加失败");
    }

    private Long generateOrderNo(){     //暂时以时间来创建订单
        long currentTime = System.currentTimeMillis();
        return currentTime + new Random().nextInt(100);
    }

    private String generateApiKey(){        //产生apiKey
        long currentTime = System.currentTimeMillis();
        return UUID.randomUUID().toString() + new Random().nextInt(100);
    }


//    //普通用户查看本人的订单列表
//    public ServerResponse list(Integer userId,Integer pageNum,Integer pageSize){
//        log.info("用户查看本人的订单列表");
//        User user = userMapper.selectByPrimaryKey(userId);
//        List<Integer> userIdList = Lists.newArrayList();
////        if(user.getRoleId().equals(Const.Role.SERVICE_ADMIN.getCode()) || user.getRoleId().equals(Const.Role.SERVICE_LEADER.getCode())){
////            userIdList = userMapper.selectByOrginationAndRoleId(user.getOrganizationId(),Lists.newArrayList(Const.Role.SERVICE_PROVIDER.getCode()));
////        }else
//        userIdList = Lists.newArrayList(userId);
//        PageHelper.startPage(pageNum,pageSize);
//        List<Order> orderList = orderMapper.selectByUserIds(userIdList);
//        PageInfo pageInfo = new PageInfo(orderList);
//        List<OrderVo> orderVoList = assembleOrderVoList(orderList);
//        pageInfo.setList(orderVoList);
//        return ServerResponse.createBySuccess("查询成功",pageInfo);
//    }
//
//    /**
//     * 服务提供方查看订单列表，管理员和领导能查看本方所有的订单
//     * @param userId  用户id
//     * @param pageNum
//     * @param pageSize
//     * @return
//     */
//    public ServerResponse listOrders(Integer userId,Integer pageNum,Integer pageSize){
//        User user = userMapper.selectByPrimaryKey(userId);
//        List<Integer> userIdList = Lists.newArrayList();
//        if(user.getRoleId().equals(Const.Role.SERVICE_ADMIN.getCode()) || user.getRoleId().equals(Const.Role.SERVICE_LEADER.getCode())){
//            userIdList = userMapper.selectByOrginationAndRoleId(user.getOrganizationId(),Lists.newArrayList(Const.Role.SERVICE_PROVIDER.getCode()));
//        }else
//            userIdList = Lists.newArrayList(userId);
//
//        //查询拥有的服务
//        List<Integer> serviceList = serviceMapper.selectServiceByUserLists(userIdList);
//        List<Order> orderList = Lists.newArrayList();
//        PageHelper.startPage(pageNum,pageSize);
//        if(serviceList.size()!=0)
//            orderList = orderMapper.selectByServiceIds(serviceList);
//        PageInfo pageInfo = new PageInfo(orderList);
//        List<OrderVo> orderVoList = assembleOrderVoList(orderList);
//        pageInfo.setList(orderVoList);
//        return ServerResponse.createBySuccess("查询成功",pageInfo);
//    }
//
//
//
//    private List<OrderVo> assembleOrderVoList( List<Order> orderList){            //生成orderVoList
//        List<OrderVo> orderVoList = Lists.newArrayList();
//        for(Order order  : orderList){
//            orderVoList.add(assembleOrderVo(order));
//        }
//        return orderVoList;
//    }
//
//    private OrderVo assembleOrderVo(Order order){
//        OrderVo orderVo = new OrderVo();
//        orderVo.setOrderNo(order.getOrderNo());
//        orderVo.setServiceId(order.getServiceId());
//        orderVo.setServiceName(order.getServicename());
//        orderVo.setBuyTime(DateTimeUtil.dateToStr(order.getBuyTime()));
//        orderVo.setFormat(order.getFormat());
//        orderVo.setApikey(order.getApikey());
//        orderVo.setQuantity(order.getQuantity());
//        orderVo.setTotalCall(order.getTotalCall());
//        orderVo.setCanCall(order.getCanCall());
//        orderVo.setScore(order.getScore());     //为空代表还未评论
//        orderVo.setTotalPrice(order.getTotalPrice());
//        orderVo.setStatus(order.getStatus());
//        return orderVo;
//    }
//
//    public ServerResponse superAdminList(String orderNo, Integer pageNum, Integer pageSize){
//        log.info("superAdmin查看订单列表");
//        if(orderNo != null){
//            orderNo = new StringBuilder().append("%").append(orderNo).append("%").toString();
//        }else{
//            orderNo = new StringBuilder().append("%").append("%").toString();
//        }
//        PageHelper.startPage(pageNum,pageSize);
//        List<Order> orderList = orderMapper.selectBySuperAdmin(orderNo);
//        PageInfo pageInfo = new PageInfo(orderList);
//        List<OrderVo> orderVoList = assembleOrderVoList(orderList);
//        pageInfo.setList(orderVoList);
//        return ServerResponse.createBySuccess("查询成功",pageInfo);
//    }
//
//    public ServerResponse superAdminDetail(String orderNo){
//        log.info("查看订单详情");
//        Order order = orderMapper.selectByOrderNo(orderNo);
//        if(order == null)
//            return ServerResponse.createBySuccessMessage("该订单不存在");
//        OrderVo orderVo = assembleOrderVo(order);
//        return ServerResponse.createBySuccess("查询成功",orderVo);
//    }
//
//
//    @Transactional
//    public ServerResponse commentOrder(Integer userId,String orderNo,double score){
//        log.info("用户对订单进行评价");
//        Order order = orderMapper.selectByOrderNo(orderNo);
//        if(order == null){
//            return ServerResponse.createByErrorMessage("该订单不存在");
//        }
//        if(!order.getUserId().equals(userId))
//            return ServerResponse.createByErrorMessage("只能对自己订单进行评价");
//        if(order.getScore() != null) return ServerResponse.createByErrorMessage("订单已经评价过");
//        BigDecimal score1 = new BigDecimal(score);
//        if(score1.compareTo(BigDecimal.ZERO)==-1 || score1.compareTo(new BigDecimal(5))==1)  //不在0-5之间的评分报错
//            return ServerResponse.createByErrorMessage("评分只能在0到5之间");
//        Order newOrder = new Order();
//        newOrder.setId(order.getId());
//        newOrder.setScore(score1);
//        int resultCount = orderMapper.updateByPrimaryKeySelective(newOrder);
//        if(resultCount >0){
//            //更新服务中评价人数和评分字段
//            Service service = serviceMapper.selectByPrimaryKey(order.getServiceId());
//            service.setCommentCount(service.getCommentCount()+1);  //评价人数+1
//            Double serviceTotalScore = orderMapper.countTotalScore(order.getServiceId(),Const.OrderStatusEnum.ORDER_SUCCESS.getCode());  //求服务成功的已评价的订单的总评分
//            BigDecimal serviceScore = BigDecimalUtil.div(serviceTotalScore,service.getCommentCount()+1);
//            service.setScore(serviceScore);
//            resultCount = serviceMapper.updateByPrimaryKeySelective(service);
//            if(resultCount >0)
//                return ServerResponse.createBySuccessMessage("评价成功");
//            else
//                return ServerResponse.createByErrorMessage("评价失败");
//        }
//        return ServerResponse.createByErrorMessage("评价失败");
//    }
}
