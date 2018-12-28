package edu.zju.ccnt.user.controller;


import edu.zju.ccnt.user.common.Const;
import edu.zju.ccnt.user.common.ServerResponse;
import edu.zju.ccnt.user.model.User;
import edu.zju.ccnt.user.service.IUserService;
import edu.zju.ccnt.user.util.PasswordUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/manage/")
@Api(value = "UserManageController", description = "管理员进行用户管理相关的api")
public class UserManageController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "check.do",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "用户审核",notes = "对注册用户进行审核,若审核未通过显示未通过原因")
    @RequiresPermissions("user_manage")
//    @MyAnnotation("用户审核")
    public ServerResponse check(Integer userId, boolean checked, String uncheckedCause){
        return iUserService.checkUser(userId,checked,uncheckedCause);
    }

    @RequestMapping(value = "list.do",method = RequestMethod.GET)
    @ResponseBody
    @RequiresPermissions("user_manage")
    //"用户列表"
    @ApiOperation(value = "用户列表",notes = "查看用户列表，用mybatis进行分页，可以按用户名模糊查询，按照是否是普通用户查询")
    public ServerResponse list( @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                                String username,Integer checked){
        return iUserService.list(pageNum,pageSize,username,checked);
    }

    @RequestMapping(value = "search.do",method = RequestMethod.GET)
    @ResponseBody
    @RequiresPermissions("user_manage")
    //@MyAnnotation("用户信息查看")
    @ApiOperation(value = "查看用户信息",notes = "查看用户详细信息")
    public ServerResponse search(Integer userId){
        return iUserService.search(userId);
    }

    @RequestMapping(value = "delete.do",method = RequestMethod.GET)
    @ResponseBody
    @RequiresPermissions("user_manage")
    @ApiOperation(value = "删除用户",notes = "主要用来删除恶意注册用户、无用账户等")
  //  @MyAnnotation("删除用户")
    public ServerResponse delete(Integer userId){
        return iUserService.delete(userId);
    }

    @RequestMapping(value = "login.do",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("用户登录")
   // @MyAnnotation("用户登录")
    public ServerResponse login(String username, String password){
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return ServerResponse.createByErrorMessage("用户名或者密码不能为空");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        new PasswordUtil().encryptPassword(user);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token=new UsernamePasswordToken(username,user.getPassword());
        try {
            subject.login(token);
            user  = (User) subject.getPrincipals().getPrimaryPrincipal();
            if(!user.getRoleId().equals(Const.Role.SUPER_ADMIN.getCode())) {
                subject.logout();
                return ServerResponse.createByErrorMessage("只能管理员登录");
            }
            user.setPassword(StringUtils.EMPTY);
            return ServerResponse.createBySuccess("登录成功",user);
        }catch (AuthenticationException e) {
            token.clear();
            return ServerResponse.createByErrorMessage("用户名或者密码不正确");
        }
    }

    //退出登录
    @RequestMapping(value = "logout.do",method = RequestMethod.GET)
    @ResponseBody
    @RequiresPermissions("user_manage")
    @ApiOperation(value = "退出登录",notes = "用户退出登录")
  //  @MyAnnotation("退出登录")
    public ServerResponse<String> logout(){
        Subject subject =  SecurityUtils.getSubject();
        subject.logout();
        return ServerResponse.createBySuccessMessage("退出登录成功");
    }

    //添加用户
    @RequestMapping(value = "add.do",method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("user_manage")
    @ApiOperation(value = "添加用户",notes = "添加用户")
    //@MyAnnotation("添加用户")
    public ServerResponse<String> managerAdd(@RequestBody User user){

        return iUserService.managerAdd(user);
    }

}
