package edu.zju.ccnt.user.dao;

import edu.zju.ccnt.user.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated
     */
    int insert(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated
     */
    int insertSelective(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated
     */
    User selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(User record);

    int checkUserName(String username);

    int checkEmail(String email);

    int checkPhone(String phone);


    User login(@Param("username") String username, @Param("password") String password);

    int checkEmailAndUserid(@Param("email") String email, @Param("userid") Integer userid);

    int checkUsernameAndUserid(@Param("username") String username, @Param("userid") Integer userid);

    int checkPhoneAndUserid(@Param("phone") String phone, @Param("userid") Integer userid);


    List<User> selectByAdmin(@Param("username") String username, @Param("checked") Integer checked);     //管理员查看用户列表，可以根据条件查询：是否只要企业认证的用户、认证信息审核状态、用户名


    User selectByUsername(String userName);

    //根据组织和角色查询，下面所属的用户
    List<User> selectByUserOrginationAndRoleId(@Param("organizationId") Integer organizationId, @Param("roleList") List<Integer> roleList);

    //根据组织和角色查询，下面所属的用户id
    List<Integer> selectByOrginationAndRoleId(@Param("organizationId") Integer organizationId, @Param("roleList") List<Integer> roleList);

    List<User> selectByOrganizationIdAndRole(@Param("roleId") Integer roleId,@Param("organizations")List<Integer> organizations);

    List<User> selectAdmins(Integer roleId);

    List<User> selectByOrganizationAndRoleId(@Param("organizationId") Integer organizationId, @Param("roleId") Integer roleId,@Param("checked") Integer checked);

    int selectByOrganizationAndRole(@Param("organizationId") Integer organizationId, @Param("roleId") Integer roleId,@Param("checked") Integer checked);

}