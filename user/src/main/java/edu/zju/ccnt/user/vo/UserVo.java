package edu.zju.ccnt.user.vo;


import lombok.Data;

/**
 * Created by LXY on 2017/11/2.
 */
@Data
public class UserVo {

    private Integer userId;

    private String username;

    private String phone;

    private String email;

    private String industry;

    private Integer organizationId;

    private String organizationName;

    private String createTime;

    private Integer roleId;

    private String roleName;

    private Integer checked;

    private String remarks;
}
