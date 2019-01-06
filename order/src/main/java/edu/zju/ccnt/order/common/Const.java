package edu.zju.ccnt.order.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * Created by LXY on 2017/10/25.
 */
//常量类
public class Const {

    public static final String CURRENT_USER="currentUser";

    public static final String EMAIL="email";

    public static final String USERNAME="username";

    public static final String PHONE="phone";

    public static final String SERVER_ADDRESS = "183.129.218.51:8077";

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public enum Role{          //用户角色
        SUPER_ADMIN ("超级管理员",1),     //超级管理员
        COMMON_USER ("普通用户",2),     //普通用户
        SERVICE_PROVIDER("服务提供者",3),      //服务提供方，服务提交者，一般指开发人员
        SERVICE_ADMIN ("服务提供方管理员",4) ,  //服务提供方管理员
        SERVICE_LEADER("服务提供方领导",5); //服务提供方的领导

        private  final String desc;
        private final int code;
        Role(String desc, int code) {
            this.desc = desc;
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public int getCode() {
            return code;
        }
    }

    public interface UserChecked{
        int WAIT_CHECK = 0;      //待审核
        int CHECKED = 1;      //已审核
        int FAILED_CHECKED = 2;      //审核失败
    }

    public interface CheckStatus{
        int WAIT_CHECK = 0;      //待审核
        int CHECKED = 1;      //已审核
        int FAILED_CHECKED = 2;      //审核失败
    }

    public interface Status{
        int onSell = 1;
        int undercarriage = 2;
    }

    public enum SysResponseCode {      //api调用时，系统调用级别返回码
        SUCCESS(10000,"查询成功"),
        ERROR_KEY(10001,"错误的请求apikey"),
        KEY_NO_AUTHORITY(10002,"该apikey无请求权限"),
        KEY_OVERDUE(10003,"apikey过期"),
        API_BLOCK_UP(10004,"API接口停用"),
        NO_API_KEY(10005,"URL上apikey参数不能为空"),
        NO_CALL_NUM(10006,"请求次数用完"),
        API_KEY_NO_CALL(10010,"请求次数超过限制，请重新购买"),
        SYSTEM_BUSY(10020,"系统繁忙，请稍后再试"),
        SYSTEM_TRANSMIT_FAILED(10030,"调用系统网关失败， 请与系统管理员联系"),
        LIMITED_IP(10040,"该ip调用超过每天限量，请明天继续"),
        LIMITED_APIKEY(10050,"该apikey调用超过每天限量，请明天继续"),
        FILE_LIMITED(10090,"文件大小超限，请上传小于1M的文件"),
        ENE_EXCEPTION(11010,"商家接口调用异常，请稍后再试"),
          ENE_RETURN_STYLE_ERROR(11030,"商家接口返回格式有误"),
        ;
        private final int code;
        private  final String desc;

        SysResponseCode(int code,String desc){
            this.code =code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

    public static List<SystemErrorCode> getCodeList(){     //获取系统错误码的list
        List<SystemErrorCode> list = Lists.newArrayList();
        for (SysResponseCode sysResponseCode: SysResponseCode.values()) {
            SystemErrorCode systemErrorCode = new SystemErrorCode();
            systemErrorCode.setCode(sysResponseCode.getCode());
            systemErrorCode.setDesc(sysResponseCode.getDesc());
            list.add(systemErrorCode);
        }
        return list;
    }

    @Data
    public static class SystemErrorCode{
        private int code;
        private String desc;
    }

    public interface ServiceStatus{
        int onSell = 1;     //正常
        int undercarriage = 2;  //已下架
    }

    public interface ServiceChecked{
        int waitingCheck = 0;     //待审核
        int checked = 1;  //审核通过
        int checkRefuse = 2;    //审核未通过
    }


    @Data
    public class SystemRequestParam{
        String name = "apiKey";
        String type="String";
        String isNecessary = "是";
        String example = "-";
        String desc = "购买服务时所分配的key";

        public SystemRequestParam() {
            name = "apiKey";
            type="String";
            isNecessary = "是";
            example = "-";
            desc = "购买服务时所分配的key";
        }
    }


    public interface EnterpriseChecked{
        int waitingCheck = 0;     //待审核
        int checked = 1;  //审核通过
        int checkRefuse = 2;    //审核未通过
    }


    public interface ServiceOrderBy{
        Set<String> SERVICE_ORDER_BY  = Sets.newHashSet("create_time_desc","create_time_asc","score_asc","score_desc");
    }

    public enum OrderStatusEnum{            //现在订单默认为已完成
//        CANCELED(0,"已取消"),
//        NO_PAY(10,"未支付"),
//        PAID(20,"已付款"),
//        SHIPPED(40,"已发货"),
        ORDER_SUCCESS(50,"订单完成");
//        ORDER_CLOSE(60,"订单关闭");

        private int code;
        private String value;

        OrderStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        public static OrderStatusEnum codeOf(int code){
            for(OrderStatusEnum orderStatusEnum : values()){
                if(orderStatusEnum.getCode() == code){
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }
    }


    public enum RolePermission{  //角色权限几个大类

        USER_PERMISSION(1,"用户管理权限","用户账户操作相关权限，如信息更新，查看信息等"),
        ORGNAZATION_PERMISSION(2,"组织管理权限","组织管理权限，主要有更新组织信息"),
        SERVICE_PERMISSION(3,"服务管理权限","服务管理权限，主要有服务添加、删除、审核等"),
        API_PERMISSION(4,"API管理权限","API管理权限，主要有api添加、删除、审核等"),
        ORDER_PERMISSION(5,"订单管理权限","订单管理权限，主要订单添加、查看等"),
        API_CALL_PERMISSION(6,"api调用管理权限","api调用管理权限，api调用查看"),
        SERVICE_CATEGORY_PERMISSION(7,"服务类别管理权限","服务类别管理，服务类别添加"),
        LOG_PERMISSION(8,"日志管理","日志查看");
        private int permissionCategory;

        private String name;

        private String description;

        RolePermission(int permissionCategory, String name, String description) {
            this.permissionCategory = permissionCategory;
            this.name = name;
            this.description = description;
        }

        public int getPermissionCategory() {
            return permissionCategory;
        }

        public void setPermissionCategory(int permissionCategory) {
            this.permissionCategory = permissionCategory;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static final String[] provenceDesc = new String[]{"香港","海南省","云南省","北京市","天津市","新疆","西藏","青海省","甘肃省","内蒙古","宁夏省","山西省","辽宁省","吉林省","黑龙江省","河北省","山东省","河南省","陕西省","四川省","重庆市","湖北省","安徽省","江苏省","上海市","浙江省","福建省","台湾","江西省","湖南省","贵州省","广西省","广东省","澳门"};


    public static final String[] provenceCode = new String[]{"HKG","HAI","YUN","BEJ","TAJ","XIN",
            "TIB","QIH","GAN","NMG","NXA","SHX","LIA","JIL","HLJ","HEB","SHD","HEN","SHA","SCH","CHQ","HUB","ANH","JSU","SHH","ZHJ","FUJ","TAI","JXI","HUN","GUI","GXI","GUD","MAC"};


    public interface SdkCategory{
        Set<String> SDK_CATEGORY  = Sets.newHashSet("Java","C#","GO","PHP");
    }

    public enum WebControllersEnum{
        USER("UserController","用户管理"),
        SERVICE("ServiceController","服务管理"),
        SERVICE_CATEGORY("ServcieCategoryController","服务类别管理"),
        ORGANIZATION("OrganizationController","组织管理"),
        ORDER("OrderController","订单管理"),
        API("ApiController","API管理"),
        API_CALL("ApiCallController","API调用记录管理"),
        USER_MANAGE("UserManageController","超级管理员用户管理"),
        SERVICE_MANAGE("ServiceManageController","超级管理员服务管理"),
        SERVICE_CATEGORY_MANAGE("ServiceCategoryController","超级管理员服务类别管理"),
        SDK_MANAGE("SdkManageController","超级管理员sdk管理"),
        PERMISSION_MANAGE("PermissionManageController","超级管理员权限管理"),
        ORGANIZATION_MANAGE("OrganizationManageController","超级管理员组织管理"),
        ORDER_MANAGE("OrderManageController","超级管理员订单管理"),
        LOG_MANAGE("GridLogController","超级管理员日志管理"),
        API_MANAGE("ApiManageController","超级管理员API管理"),
        API_CALL_MANAGE("ApiCallManageController","超级管理员API调用记录管理");
        private String code;
        private String value;
        WebControllersEnum(String code, String value) {
            this.code = code;
            this.value = value;
        }
        public String getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
        public static String codeOf(String code){
            for(WebControllersEnum webControllersEnum : values()){
                if(webControllersEnum.getCode().equals(code)){
                    return webControllersEnum.getValue();
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }
    }

    public interface isOtherService{
        int noThirdParty = 0;
        int isThirdParty = 1;
    }
}
