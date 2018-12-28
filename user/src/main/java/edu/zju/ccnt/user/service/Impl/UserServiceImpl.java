package edu.zju.ccnt.user.service.Impl;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import edu.zju.ccnt.user.common.Const;
import edu.zju.ccnt.user.common.ServerResponse;
import edu.zju.ccnt.user.dao.UserMapper;
import edu.zju.ccnt.user.model.User;
import edu.zju.ccnt.user.service.IOriganizationFeign;
import edu.zju.ccnt.user.service.IUserService;
import edu.zju.ccnt.user.util.DateTimeUtil;
import edu.zju.ccnt.user.util.PasswordUtil;
import edu.zju.ccnt.user.vo.UserInfoVo;
import edu.zju.ccnt.user.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.zju.ccnt.user.util.MD5Util;
import edu.zju.ccnt.user.util.UserInfoPaserUtil;
import java.util.List;

@Service("iUserService")
@Slf4j
public class UserServiceImpl implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    /**
     * 调用oginazation
     */
    @Autowired
    private IOriganizationFeign iOriganizationFeign;


    public ServerResponse register(User user) {
        logger.info("用户注册");

        //1、需要校验用户名和手机号、邮箱是否已存在
        ServerResponse<String> serverResponse = this.checkInfo("USERNAME",user.getUsername());
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }

        serverResponse = this.checkInfo("PHONE",user.getPhone());
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }

        serverResponse = this.checkInfo("EMAIL",user.getEmail());
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }

        if(user.getOrganizationId().equals(0)) user.setOrganizationId(0);  //普通用户的组织id为0
        else{
           /**
            用到了ognazation服务的根据organizationid
            */
             if (iOriganizationFeign.judgebyId(user.getOrganizationId()).getMsg().equals("false"))
             return ServerResponse.createByErrorMessage("所选组织不存在");
            //注册服务提供者的时候需要先注册服务提供方的管理员
            int count = userMapper.selectByOrganizationAndRole(user.getOrganizationId(),Const.Role.SERVICE_ADMIN.getCode(),Const.CheckStatus.CHECKED);
            if(count < 1)
                return ServerResponse.createByErrorMessage("该组织无管理员，还不能注册组织的服务开发者");
        }
//
//        new PasswordUtil().encryptPassword(user);    //shiro密码加密
        user.setChecked(Const.UserChecked.WAIT_CHECK);      //待审核
        if(user.getRoleId() == null)
            user.setRoleId(Const.Role.COMMON_USER.getCode()); //未选择默认为普通用户
        else if(user.getRoleId() <1 || user.getRoleId()>5)
            return ServerResponse.createByErrorMessage("不存在该类型的角色");
        if(user.getRoleId().equals(1))
            return ServerResponse.createByErrorMessage("不能选择管理员权限");

        user.setRemarks(StringUtils.EMPTY);
        int resultCount = userMapper.insert(user);

        if(resultCount ==0 ){
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功，待管理员审核");
    }


    public ServerResponse<String> checkInfo(String type, String str) {      //检查是否已存在
        logger.info("检查用户名、手机号或者邮箱是否存在");
        if(StringUtils.isNotBlank(type)){
            if(type.equals("USERNAME")){
                int resultCount = userMapper.checkUserName(str);
                if(resultCount>0){
                    return ServerResponse.createByErrorMessage("username已存在");
                }
            }
            if(type.equals("PHONE")){
                int resultCount = userMapper.checkPhone(str);
                if(resultCount>0){
                    return ServerResponse.createByErrorMessage("手机号已存在");
                }
            }
            if(type.equals("EMAIL")){
                int resultCount = userMapper.checkEmail(str);
                if(resultCount>0){
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }
        }
        logger.info("检查手机号及邮箱格式是否正确");
        if(StringUtils.isNotBlank(type)){
            if(type.equals("PHONE")){
                System.out.println(str);
                if(!UserInfoPaserUtil.IsHandset(str) && !UserInfoPaserUtil.IsTelephone(str)){
                    System.out.println(str);
                    return ServerResponse.createByErrorMessage("电话格式不正确");
                }
            }else if(type.equals("EMAIL")){
                if(!UserInfoPaserUtil.isEmail(str)){
                    return ServerResponse.createByErrorMessage("邮箱格式不正确");
                }
            }
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }


    public ServerResponse<User> login(String username, String password){
        logger.info("用户登录");
        int userCount = userMapper.checkUserName(username);
        if(userCount==0){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        //密码转md5
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.login(username,md5Password);
        if(user==null){
            return ServerResponse.createByErrorMessage("密码错误");
        }
        //隐藏密码
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);
    }

    public ServerResponse<User> update(User user){
        logger.info("用户更新");
        logger.info("先判断phone或者email格式是否合法");
        if(!UserInfoPaserUtil.IsHandset(user.getPhone()) && !UserInfoPaserUtil.IsTelephone(user.getPhone())){
            return ServerResponse.createByErrorMessage("电话格式不正确");
        }
        if(!UserInfoPaserUtil.isEmail(user.getEmail())){
            return ServerResponse.createByErrorMessage("邮箱格式不正确");
        }

        User oldUser = userMapper.selectByPrimaryKey(user.getId());
        int resultCount = userMapper.checkUsernameAndUserid(user.getUsername(),user.getId());
        if(resultCount>0){
            return ServerResponse.createByErrorMessage("该用户名已被使用");
        }
        //验证邮箱和手机号，除了当前用户外，其他用户不可以有
        resultCount = userMapper.checkEmailAndUserid(user.getEmail(),user.getId());
        if(resultCount>0){
            return ServerResponse.createByErrorMessage("该email已被使用");
        }

        resultCount = userMapper.checkPhoneAndUserid(user.getPhone(),user.getId());
        if(resultCount>0){
            return ServerResponse.createByErrorMessage("该手机号已被使用");
        }

        User newUser = new User();      //更新普通信息不需要重新审核
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPhone(user.getPhone());
        newUser.setIndustry(user.getIndustry());
        newUser.setId(user.getId());
        if(!(oldUser.getRoleId().equals(user.getRoleId()) && oldUser.getOrganizationId().equals(user.getOrganizationId()))){  //更改角色或者所属组织需要重新审核
            newUser.setChecked(0); //重置状态为待审核
        }

        /**
         * 角色部分不确定要不要加
         */
//        //判断所选角色是否存在
//        Role role = roleMapper.selectByPrimaryKey(user.getRoleId());
//        if(role==null) return ServerResponse.createByErrorMessage("不存在该角色");
        newUser.setRoleId(user.getRoleId());

        //判断所选组织是否存在
        if (iOriganizationFeign.judgebyId(user.getOrganizationId()).getMsg().equals("false"))
        return ServerResponse.createByErrorMessage("所选组织不存在");
        newUser.setOrganizationId(user.getOrganizationId());

        int updateCount = userMapper.updateByPrimaryKeySelective(newUser);
        if(updateCount>0){
            User user1 = userMapper.selectByPrimaryKey(user.getId());
            return ServerResponse.createBySuccess("信息更新成功",user1);
        }
        return ServerResponse.createByErrorMessage("更新失败");
    }

    public ServerResponse<UserInfoVo> getUserInfo(Integer userId){
        logger.info("获取用户信息"+userId);
        User user = userMapper.selectByPrimaryKey(userId);
        if(user ==null){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        user.setPassword(StringUtils.EMPTY);

        UserInfoVo userVo = assembleUserInfoVo(user);
        return ServerResponse.createBySuccess("查找成功",userVo);
    }

    //验证用户是否进行企业认证审核
    public ServerResponse isEnterprise(Integer userId){       //严重用户是否是已审核的企业用户
        logger.info("验证用户是否进行企业认证审核");
        User user = userMapper.selectByPrimaryKey(userId);
        if(user.getChecked() == 1){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("需要用户已进行企业认证审核");
    }

    //验证用户是否是管理员
    public ServerResponse isAdmin(Integer userId){
        logger.info("验证用户是否是管理员");
        User user = userMapper.selectByPrimaryKey(userId);
        if(user.getRoleId() == 1){
            return ServerResponse.createBySuccess();
        }
        //return ServerResponse.createBySuccess();
        return ServerResponse.createByErrorMessage("用户没有管理员权限");
    }




    //管理员查看用户信息
    public ServerResponse<PageInfo> list(Integer pageNum, Integer pageSize, String username, Integer checked){
        logger.info("管理员查看用户信息列表");
        PageHelper.startPage(pageNum,pageSize);
        if(username == null)
            username  = new StringBuilder().append("%").append("%").toString();
        else
            username = new StringBuilder().append("%").append(username).append("%").toString();

        List<User> userList = userMapper.selectByAdmin(username,checked);
        PageInfo pageInfo = new PageInfo(userList);
        List<UserVo> userVoList = assembleUserVoList(userList);
        pageInfo.setList(userVoList);
        return ServerResponse.createBySuccess("查询成功",pageInfo);
    }

    private List<UserVo> assembleUserVoList(List<User> userList){       //生成userVo对象
        List<UserVo> userVoList = Lists.newArrayList();
        for(User user : userList){
            UserVo userVo = new UserVo();
            userVo.setUserId(user.getId());
            userVo.setUsername(user.getUsername());
            userVo.setPhone(user.getPhone());
            userVo.setEmail(user.getEmail());
            userVo.setIndustry(user.getIndustry());
            userVo.setOrganizationId(user.getOrganizationId());
            userVo.setRoleId(user.getRoleId());
            userVo.setChecked(user.getChecked());
            userVo.setRemarks(user.getRemarks());
//            if(user.getOrganizationId()!=0){  //服务提供方用户
//                Organization organization = organizationMapper.selectByPrimaryKey(user.getOrganizationId());
//                if(organization != null){
//                    userVo.setOrganizationName(organization.getOrganizationName());     //企业用户显示组织信息
//                }else{
//                    userVo.setOrganizationName("所在组织不存在");
//                }
//            }
//            Role role = roleMapper.selectByPrimaryKey(user.getRoleId());
//            if(role != null){
//                userVo.setRoleName(role.getRoleName());
//            }else
//                userVo.setRoleName("所属角色不存在");
            userVo.setCreateTime(DateTimeUtil.dateToStr(user.getCreateTime()));
            userVoList.add(userVo);
        }
        return userVoList;
    }

    //管理员查看用户信息
    public ServerResponse<UserInfoVo> search(Integer userId){
        //判断是否是进行企业认证的用户，若是需要将企业认证信息添加到返回结果中
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null)
            return ServerResponse.createByErrorMessage("该用户不存在");
        return ServerResponse.createBySuccess("查询成功",assembleUserInfoVo(user));
    }

    private UserInfoVo assembleUserInfoVo(User user){       //生成用户返回对象
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setUserId(user.getId());
        userInfoVo.setUsername(user.getUsername());
        userInfoVo.setPhone(user.getPhone());
        userInfoVo.setEmail(user.getEmail());
        userInfoVo.setIndustry(user.getIndustry());
        userInfoVo.setCreateTime(DateTimeUtil.dateToStr(user.getCreateTime()));
        userInfoVo.setChecked(user.getChecked());
        if(user.getChecked().equals(Const.CheckStatus.WAIT_CHECK)){      //未审核返回本组织管理员电话
            //返回超级管理员联系方式
            List<User> userList = userMapper.selectAdmins(Const.Role.SUPER_ADMIN.getCode());
            User adminUser = userList.get(0);
            String res = "电话：" + adminUser.getPhone() + " ;邮箱：" +  adminUser.getEmail() ;
            userInfoVo.setRemarks(res);
        }else
            userInfoVo.setRemarks(user.getRemarks());
        userInfoVo.setUpdateTime(DateTimeUtil.dateToStr(user.getUpdateTime()));
//        Organization organization = organizationMapper.selectByPrimaryKey(user.getOrganizationId());
//        userInfoVo.setOrganizationId(user.getOrganizationId());
//        if(organization != null){
//            userInfoVo.setOrganization(organization.getOrganizationName());
//        }
//        Role role = roleMapper.selectByPrimaryKey(user.getRoleId());
//        userInfoVo.setRoleId(role.getId());
//        userInfoVo.setRole(role.getRoleName());
        return userInfoVo;
    }


    //根据用户名查找用户信息
    public User selectByUsername(String userName){
        User user = userMapper.selectByUsername(userName);
        return user;
    }

    //服务方管理员或者领导查看本组织的信息
    @Transactional
    public ServerResponse getUserList(Integer userId) {
          List<UserInfoVo> userInfoVoList = Lists.newArrayList();
//        User user = userMapper.selectByPrimaryKey(userId);
//        Organization organization = organizationMapper.selectByPrimaryKey(user.getOrganizationId());
//        List<Integer> roleList = Lists.newArrayList();      //查询该角色所能查询的 下面的角色
//        roleList.add(Const.Role.SERVICE_PROVIDER.getCode());
//        if(user.getRoleId() == Const.Role.SERVICE_LEADER.getCode())     //领导还能看到管理员的信息
//            roleList.add(Const.Role.SERVICE_ADMIN.getCode());
//        List<User> userList = userMapper.selectByUserOrginationAndRoleId(organization.getId(),roleList);
//
//        for(User userInfo:userList){
//            UserInfoVo userInfoVo = new UserInfoVo();
//            userInfoVo = assembleUserInfoVo(userInfo);
//            userInfoVoList.add(userInfoVo);
//        }
       return ServerResponse.createBySuccess("查询成功",userInfoVoList);
    }


//
//    int WAIT_CHECK = 0;      //待审核
//    int CHECKED = 1;      //已审核
//    int FAILED_CHECKED = 2;      //审核失败
    //用户审核
    public ServerResponse checkUser(Integer userId,boolean checked,String uncheckedCause){
        logger.info("用户审核");
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null)
            return ServerResponse.createByErrorMessage("该用户不存在");
        Integer check = checked?1:2;
        if(check.equals(user.getChecked()))
            return ServerResponse.createByErrorMessage("不能进行相同审核");
        if(check.equals(2) && StringUtils.isBlank(uncheckedCause))
            return ServerResponse.createByErrorMessage("审核不通过需要说明原因");
//        if(check.equals(Const.CheckStatus.CHECKED) && StringUtils.isNoneBlank(uncheckedCause))
//            return ServerResponse.createByErrorMessage("审核通过不需要原因");
        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setChecked(check);
        if(check.equals(2)) newUser.setRemarks(uncheckedCause);
        else newUser.setRemarks(StringUtils.EMPTY);
        int resultCount = userMapper.updateByPrimaryKeySelective(newUser);
        if(resultCount > 0)
            return ServerResponse.createBySuccessMessage("审核成功");
        return ServerResponse.createByErrorMessage("审核失败");
    }


    //用户删除
    public ServerResponse delete(Integer userId){
        log.info("用户删除");
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null)
            return ServerResponse.createByErrorMessage("用户不存在");
        if(user.getRoleId().equals( Const.Role.SUPER_ADMIN.getCode()))
            return ServerResponse.createByErrorMessage("不能删除超级管理员");
        int resultCount = userMapper.deleteByPrimaryKey(userId);
        if(resultCount > 0)
            return ServerResponse.createBySuccessMessage("删除成功");
        return ServerResponse.createByErrorMessage("删除失败");
    }

    public ServerResponse managerAdd(User user){
        logger.info("管理员添加用户");

        //1、需要校验用户名和手机号、邮箱是否已存在
        ServerResponse<String> serverResponse = this.checkInfo(Const.USERNAME,user.getUsername());
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }

        serverResponse = this.checkInfo(Const.PHONE,user.getPhone());
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }

        serverResponse = this.checkInfo(Const.EMAIL,user.getEmail());
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }
//        if(user.getOrganizationId().equals(0)) user.setOrganizationId(0);  //普通用户的组织id为0
//        else{
//            Organization organization = organizationMapper.selectByPrimaryKey(user.getOrganizationId());
//            if(organization == null) return ServerResponse.createByErrorMessage("所选组织不存在");
//        }
        new PasswordUtil().encryptPassword(user);    //shiro密码加密
        user.setChecked(Const.UserChecked.CHECKED);      //管理员添加的，所以直接审核通过
        if(user.getRoleId() == null)
            user.setRoleId(Const.Role.COMMON_USER.getCode()); //未选择默认为普通用户
        else if(user.getRoleId() <1 || user.getRoleId()>5)
            return ServerResponse.createByErrorMessage("不存在该类型的角色");
        if(user.getRoleId().equals(1))
            return ServerResponse.createByErrorMessage("不能选择管理员权限");
        int resultCount = userMapper.insert(user);

        if(resultCount ==0 ){
            return ServerResponse.createByErrorMessage("用户添加失败");
        }
        return ServerResponse.createBySuccessMessage("用户添加成功");
    }


    public ServerResponse modifyPassword(Integer userId, String newPassword, String oldPassword){
        log.info("修改用户密码");
        User user = userMapper.selectByPrimaryKey(userId);

        User oldUser = new User();
        oldUser.setUsername(user.getUsername());
        oldUser.setPassword(oldPassword);
        new PasswordUtil().encryptPassword(oldUser);        //得出验证的密码
        if(!user.getPassword().equals(oldUser.getPassword())){
            return ServerResponse.createByErrorMessage("原密码输入错误");
        }

        User newUser = new User();
        newUser.setPassword(newPassword);
        newUser.setId(userId);
        newUser.setUsername(user.getUsername());
        new PasswordUtil().encryptPassword(newUser);        //得出加密后的密码

        int resultCount = userMapper.updateByPrimaryKeySelective(newUser);
        if(resultCount > 0)
            return ServerResponse.createBySuccessMessage("密码更新成功");
        else
            return ServerResponse.createByErrorMessage("密码更新失败");
    }


    public boolean isChecked(Integer userId){
        logger.info("判断用户审核状态");
        User user = userMapper.selectByPrimaryKey(userId);
        if(user.getChecked().equals(1))
            return true;
        else
            return false;
    }


    public ServerResponse searchCallDetail(String serviceName,Integer pageNum, Integer pageSize){

        return null;
    }
}
