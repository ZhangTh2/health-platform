package edu.zju.ccnt.user.service;

import edu.zju.ccnt.user.common.ServerResponse;
import edu.zju.ccnt.user.model.User;
import edu.zju.ccnt.user.vo.UserInfoVo;
import com.github.pagehelper.PageInfo;

public interface IUserService {
    ServerResponse register(User user);

    ServerResponse<User> login(String username, String password);

    ServerResponse<User> update(User user);

    ServerResponse<String> checkInfo(String type, String str);

    ServerResponse<UserInfoVo> getUserInfo(Integer userId);

    ServerResponse isAdmin(Integer userId);

    ServerResponse<PageInfo> list(Integer pageNum, Integer pageSize, String username, Integer checked);

    ServerResponse<UserInfoVo> search(Integer userId);

    User selectByUsername(String userName);

    ServerResponse getUserList(Integer userId);     //查询服务提供方用户能够查看的用户信息列表

    ServerResponse checkUser(Integer userId,boolean checked,String uncheckedCause); //审核用户

    ServerResponse delete(Integer userId);

    ServerResponse managerAdd(User user);

    ServerResponse modifyPassword(Integer userId, String newPassword, String oldPassword);

    boolean isChecked(Integer userId);


}
