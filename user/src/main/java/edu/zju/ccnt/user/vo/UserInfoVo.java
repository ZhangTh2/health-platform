package edu.zju.ccnt.user.vo;

import lombok.Data;

/**
 * Created by LXY on 2017/11/3.
 */
@Data
public class UserInfoVo {       //管理员查看用户信息，若是企业用户显示企业用户的信息

    private Integer userId;     //用户id

    private String username;           //用户名

    private String phone;       //电话

    private String email;       //邮箱

    private String industry;        //所属行业

    private Integer organizationId; //组织id

    private String organization;      //所属组织

    private Integer roleId;  //角色id

    private String Role;        //角色

    private Integer checked;        //是否审核

    private String remarks;

    private String createTime;      //用户注册时间

    private String updateTime;

}
