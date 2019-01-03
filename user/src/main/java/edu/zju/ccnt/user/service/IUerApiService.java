package edu.zju.ccnt.user.service;

import edu.zju.ccnt.user.common.ServerResponse;

/**
 * @author zth
 * 2019.1.3
 * 提供系统内部的接口
 */
public interface IUerApiService {
    //根据用户id获取角色id
    ServerResponse<Integer> getRoleIdbyUserId(Integer userId);
    //判断用户是否已经被审核
    ServerResponse<String> ischecked(Integer userId);
}
