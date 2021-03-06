package edu.zju.ccnt.user.service.Impl;

import edu.zju.ccnt.user.common.ServerResponse;
import edu.zju.ccnt.user.dao.UserMapper;
import edu.zju.ccnt.user.model.User;
import edu.zju.ccnt.user.service.IUerApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zth
 * 2019.1.3
 */

@Service("iUserApiService")
@Slf4j
public class UserApiServiceImpl implements IUerApiService {

    @Autowired
    private UserMapper userMapper;


    public ServerResponse<Integer> getRoleIdbyUserId(Integer userId) {
        log.info("根据用户id获取角色id");
        User user = userMapper.selectByPrimaryKey(userId);
        if(user==null)
            return ServerResponse.createByErrorMessage("该用户不存在");
        else
            return ServerResponse.createBySuccess(user.getRoleId());
    }



    public ServerResponse<String> ischecked(Integer userId) {
        log.info("判断用户是否已经被审核");
        User user = userMapper.selectByPrimaryKey(userId);
        if(user==null)
            return ServerResponse.createByErrorMessage("该用户不存在");
        else {
            Integer ischecked = user.getChecked();
            if(ischecked==1) return ServerResponse.createBySuccess("true");
            else return ServerResponse.createBySuccess("false");
        }
    }

    public ServerResponse<Integer> getOrganizationIdbyUId(Integer userId) {
        log.info("获取组织id");
        User user = userMapper.selectByPrimaryKey(userId);
        if(user==null)
            return ServerResponse.createByErrorMessage("该用户不存在");
        else
            return ServerResponse.createBySuccess(user.getOrganizationId());
    }


}
