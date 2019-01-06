package edu.zju.ccnt.service.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import edu.zju.ccnt.service.common.Const;
import edu.zju.ccnt.service.common.ServerResponse;
import edu.zju.ccnt.service.dao.ServiceMapper;
import edu.zju.ccnt.service.model.Api;
import edu.zju.ccnt.service.model.ServiceCallDetail;
import edu.zju.ccnt.service.model.ServiceCategory;
import edu.zju.ccnt.service.service.IOrganizationFeign;
import edu.zju.ccnt.service.service.IServiceCategoryService;
import edu.zju.ccnt.service.service.IServiceService;
import edu.zju.ccnt.service.service.IUserFeign;
import edu.zju.ccnt.service.vo.*;
import edu.zju.ccnt.service.dao.ApiMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zth
 * 2019.1.2
 */
@org.springframework.stereotype.Service("iServiceService")
@Slf4j
public class ServiceServiceImpl implements IServiceService {

    @Autowired
    private ServiceMapper serviceMapper;

    @Autowired
    private IUserFeign iUserFeign;

    @Autowired
    private IOrganizationFeign iOrganizationFeign;


    @Autowired
    private ApiMapper apiMapper;

    @Autowired
    private IServiceCategoryService iServiceCategoryService;

    @Transactional
    public ServerResponse<String> add(edu.zju.ccnt.service.model.Service service, Integer userId){
        log.info("添加服务");
        //1、验证服务名，不能重复
        int resultCount = serviceMapper.selectByServiceName(service.getServiceName());
        if(resultCount > 0)
            return ServerResponse.createByErrorMessage("该服务已经存在");
        /**
         * UserFeign
         */
        Integer roleId = iUserFeign.getRole(userId).getData();
        String judgeStatus = iUserFeign.judgeChecked(userId).getMsg();


        if(roleId==Const.Role.SUPER_ADMIN.getCode()){        //管理员只能添加第三方服务
            service.setIsOther(Const.isOtherService.isThirdParty);
            service.setChecked(Const.CheckStatus.CHECKED);   //已审核
        }else{
            if(judgeStatus.equals("false")){       //需要审核通过的用户才能添加服务
                return ServerResponse.createByErrorMessage("需要审核通过之后才能添加服务");
            }
            service.setIsOther(Const.isOtherService.noThirdParty);
            service.setChecked(Const.CheckStatus.WAIT_CHECK);   //未审核
       }

        //2、插入服务
        /**
         * mark
         * 图片的插入搁置了
         */
        service.setStatus(Const.Status.onSell);       //状态正常，显示再架上
        service.setUserCount(0);    //使用次数为0
        service.setCommentCount(0);     //默认评价人数为0
        service.setScore(new BigDecimal(5).longValue());        //分数默认为5分
        service.setRemarks(StringUtils.EMPTY);
        resultCount = serviceMapper.insert(service);
        if(resultCount > 0) {
            if(roleId==Const.Role.SUPER_ADMIN.getCode())
                return ServerResponse.createBySuccessMessage("服务已经添加");
            else
                return ServerResponse.createBySuccessMessage("服务已经添加，待管理员审核");
        }
        return ServerResponse.createByErrorMessage("服务添加失败");
    }

    //查看服务信息，普通用户和游客查看服务信息，只能查找在架上，已审核的服务和api
    public ServerResponse search(Integer serviceId){
        log.info("查看服务信息");
        edu.zju.ccnt.service.model.Service service = serviceMapper.selectByPrimaryKey(serviceId);
        if(service == null)
            return ServerResponse.createByErrorMessage("服务不存在");
        if(service.getChecked()!=Const.CheckStatus.CHECKED)
            return ServerResponse.createByErrorMessage("服务未通过审核");
        if(service.getStatus() != Const.Status.onSell)
            return ServerResponse.createByErrorMessage("服务已下架");
        List<Api> apiList = Lists.newArrayList();
        ServiceInfoVo serviceInfoVo = new ServiceInfoVo();
        apiList = apiMapper.selectByServiceIdAndStatus(serviceId, Const.ServiceStatus.onSell,Const.ServiceChecked.checked);
        serviceInfoVo = assembleServiceInfoVo(service,apiList,false);
        return ServerResponse.createBySuccess("查询成功",serviceInfoVo);
    }

    private ServiceInfoVo assembleServiceInfoVo(edu.zju.ccnt.service.model.Service service, List<Api> apiList, boolean self){     //产生serviceInfo对象
        ServiceInfoVo serviceInfoVo = new ServiceInfoVo();
        serviceInfoVo.setId(service.getId());
        serviceInfoVo.setServiceName(service.getServiceName());
        serviceInfoVo.setCategoryId(service.getCategoryId());
        ServiceCategory serviceCategory = iServiceCategoryService.getCategorybyId(service.getCategoryId()).getData();
        serviceInfoVo.setCategoryName(serviceCategory.getCategoryName());
        serviceInfoVo.setUserId(service.getUserId());
        /**
        UserFeign and OrganizationFeign
         */
        Integer organizationId=iUserFeign.getOrganization(service.getUserId()).getData();
        String organizationName=iOrganizationFeign.getbyId(organizationId).getData();
        /**
         * dezign
         * 将高分中心和独立开发者都加入组织,就不用判断了
         */
        serviceInfoVo.setEnterpriseName(organizationName);
//        Organization organization = organizationMapper.selectByPrimaryKey(user.getOrganizationId());
//        if(organization == null){
//            if(user.getRoleId().equals(Const.Role.SUPER_ADMIN.getCode())){
//                serviceInfoVo.setEnterpriseName("高分中心");
//            }else{
//                serviceInfoVo.setEnterpriseName("独立开发者");
//            }
//        }else {
//            serviceInfoVo.setEnterpriseName(organization.getOrganizationName());
//        }

        serviceInfoVo.setOrganizationId(organizationId);
        serviceInfoVo.setFormat(service.getFormat());
        /**
         * test
         * long转bigDecimal 待测试
         */
        serviceInfoVo.setPrice(new BigDecimal(service.getPrice()));
        serviceInfoVo.setIntroduction(service.getIntroduction());
        serviceInfoVo.setDetailIntroduction(service.getDetailIntroduction());
        serviceInfoVo.setCommentCount(service.getCommentCount());
        serviceInfoVo.setUserCount(service.getUserCount());
        serviceInfoVo.setScore(new BigDecimal(service.getScore()));
//        //拼装返回地址
//        serviceInfoVo.setServiceImg(PropertiesUtil.getProperty("ftp.server.http.prefix")+PropertiesUtil.getProperty("ftp.server.img")+"/"+service.getServiceImg());
        List<ApiInfoVo> serviceInfoVoList = assembleApiInfoList(apiList,self);
//
//        List<SdkVo> sdkVoList = Lists.newArrayList();
//        List<ServiceSDK> sdkList = serviceSDKMapper.selectByServiceId(service.getId());
//        for(ServiceSDK serviceSDK:sdkList){
//            SdkVo sdkVo = new SdkVo();
//            sdkVo = assembleSdkVo(serviceSDK);
//            sdkVoList.add(sdkVo);
//        }
//        serviceInfoVo.setSdkVoList(sdkVoList);
//
        serviceInfoVo.setApiInfoList(serviceInfoVoList);
        Const con = new Const();
        serviceInfoVo.setSystemRequestParam(con.new SystemRequestParam());
        serviceInfoVo.setSystemErrorCode(Const.getCodeList());
        return serviceInfoVo;
    }

    private List<ApiInfoVo> assembleApiInfoList(List<Api> apiList, boolean self){
        List<ApiInfoVo> apiInfoVoList = Lists.newArrayList();
        /**
         * mark
         * 获得Api列表对象
         */
//        for(Api api : apiList){
//            ApiInfoVo apiInfoVo = new ApiInfoVo();
//            apiInfoVo.setId(api.getId());
//            apiInfoVo.setName(api.getName());
//            apiInfoVo.setMark(api.getMark());
//            if(self == true) {
//                apiInfoVo.setApiAddress(api.getApiAddress());
//                apiInfoVo.setPath(api.getPath());
//                apiInfoVo.setStripPrefix(api.getStripPrefix());
//            } else{
//                apiInfoVo.setApiAddress(null);
//                apiInfoVo.setPath(null);
//                apiInfoVo.setStripPrefix(null);
//            }
//            apiInfoVo.setApiUrl(PropertiesUtil.getProperty("server.url")+api.getPath());
//            apiInfoVo.setDescription(api.getDescription());
//            apiInfoVo.setApiCallWay(api.getApiCallWay());
//            apiInfoVo.setApiIntroduction(api.getApiIntroduction());
//            apiInfoVo.setStatus(api.getStatus());
//            apiInfoVo.setServiceId(api.getServiceId());
//            apiInfoVo.setChecked(api.getChecked());
//            apiInfoVo.setArguments(api.getArguments());
//            apiInfoVo.setCallLimit(api.getCallLimit());
//            apiInfoVo.setCallIpLimit(api.getCallIpLimit());
//            apiInfoVo.setResultArguments(api.getResultArguments());
//            apiInfoVo.setErrorCode(api.getErrorCode());
//            apiInfoVo.setResult(api.getResult());
//            if(api.getChecked().equals(Const.CheckStatus.WAIT_CHECK)){      //未审核返回本组织管理员电话
//                Service service = serviceMapper.selectByPrimaryKey(api.getServiceId());
//                User  user = userMapper.selectByPrimaryKey(service.getUserId());
//                Organization organization = organizationMapper.selectByPrimaryKey(user.getOrganizationId());
//                if(organization == null){       //无组织则返回超级管理员联系方式
//                    List<User> userList = userMapper.selectAdmins(Const.Role.SUPER_ADMIN.getCode());
//                    User adminUser = userList.get(0);
//                    String res = "电话：" + adminUser.getPhone() + " ;邮箱：" +  adminUser.getEmail() ;
//                    apiInfoVo.setRemarks(res);
//                }else{
//                    List<User> userList = userMapper.selectByOrganizationAndRoleId(user.getOrganizationId(),Const.Role.SERVICE_ADMIN.getCode(),Const.CheckStatus.CHECKED);
//                    User adminUser = userList.get(0);
//                    String res = "电话：" + adminUser.getPhone() + " ;邮箱：" +  adminUser.getEmail() ;
//                    apiInfoVo.setRemarks(res);
//                }
//            }else
//                apiInfoVo.setRemarks(api.getRemarks());
//            apiInfoVo.setReturnStyle(api.getReturnStyle());
//            String callExample = api.getCallExample();
//            //对于请求示例这里需要对原始地址进行封装，把原连接替换掉
//            String newCallExample;
//            if(callExample.contains("?")){      //如果是get或delete类的请求
//                newCallExample = new StringBuilder().append(PropertiesUtil.getProperty("server.url")+api.getPath()).append(callExample.substring(callExample.indexOf('?'))).toString();       //替换掉？之前的链接
//            }else
//                newCallExample = PropertiesUtil.getProperty("server.url")+api.getPath();
//            apiInfoVo.setCallExample(newCallExample);
//            apiInfoVoList.add(apiInfoVo);
//
//        }
        return apiInfoVoList;
    }

    //查询服务列表只包含服务信息，用于展示
    @Cacheable("selectServiceList")
    public ServerResponse list(String serviceName,String orderBy,Integer categoryId,Integer pageNum,Integer pageSize){
        if(!StringUtils.isEmpty(serviceName)){
            serviceName = new StringBuilder().append("%").append(serviceName).append("%").toString();
        }else
            serviceName = new StringBuilder().append("%").append("%").toString();
        StringBuilder newOrderBy ;      //排序规则，一次只能使用一个排序规则
        if(Const.ServiceOrderBy.SERVICE_ORDER_BY.contains(orderBy)){
            //得到行如 create_time asc 的字符串
            newOrderBy = new StringBuilder(orderBy);
            int index = orderBy.lastIndexOf('_');
            newOrderBy.replace(index,index+1," ");
        }else{
            newOrderBy = null;
        }
        //分页
        PageHelper.startPage(pageNum,pageSize);
        List<edu.zju.ccnt.service.model.Service> serviceList = serviceMapper.selectServiceList(serviceName,newOrderBy==null?null:newOrderBy.toString(),categoryId);
        PageInfo pageInfo = new PageInfo(serviceList);
        List<ServiceVo> serviceVoList = assembleServiceVoList(serviceList);
//        System.out.println(JSONObject.toJSON(serviceVoList.get(0)).toString());
        pageInfo.setList(serviceVoList);
        return  ServerResponse.createBySuccess("查询成功",pageInfo);
    }

    private List<ServiceVo> assembleServiceVoList(List<edu.zju.ccnt.service.model.Service> serviceList){           //产生serviceVo list
        List<ServiceVo> serviceVoList = Lists.newArrayList();
        for(edu.zju.ccnt.service.model.Service service:serviceList){
            ServiceVo serviceVo = new ServiceVo();
            serviceVo = assembleServiceVo(service);
            serviceVoList.add(serviceVo);
        }
        return serviceVoList;
    }

    private ServiceVo assembleServiceVo(edu.zju.ccnt.service.model.Service service){       //根据service对象产生serviceVo对象
        ServiceVo serviceVo = new ServiceVo();
        serviceVo.setServiceId(service.getId());
        serviceVo.setCategoryId(service.getCategoryId());
        serviceVo.setServiceName(service.getServiceName());
        /**
         * mark
         * 图片
         */
      //  serviceVo.setServiceImg(PropertiesUtil.getProperty("ftp.server.http.prefix")+PropertiesUtil.getProperty("ftp.server.img")+"/"+service.getServiceImg());
        serviceVo.setUserCount(service.getUserCount());
        serviceVo.setIntroduction(service.getIntroduction());
        serviceVo.setCommentCount(service.getCommentCount());
        /**
         * mark
         * 两个属性需要转换格式
         */
       // serviceVo.setScore(service.getScore());
       // serviceVo.setCreateTime(DateTimeUtil.dateToStr(service.getCreateTime()));
        return serviceVo;
    }


    public List<ServiceVo> listOfAll(String serviceName){
        if(!StringUtils.isEmpty(serviceName)){
            serviceName = new StringBuilder().append("%").append(serviceName).append("%").toString();
        }else
            serviceName = new StringBuilder().append("%").append("%").toString();

        List<edu.zju.ccnt.service.model.Service> serviceList = serviceMapper.selectServiceList(serviceName,null, null);
        List<ServiceVo> serviceVoList = assembleServiceVoList(serviceList);
//        System.out.println(JSONObject.toJSON(serviceVoList.get(0)).toString());
        return serviceVoList;
    }

    //服务下架
    public ServerResponse undercarriage(Integer userId,Integer serviceId){
        log.info("服务下架");
        //需要先用userId验证该service是否属于该用户
        edu.zju.ccnt.service.model.Service service  = serviceMapper.selectByPrimaryKey(serviceId);
        if(service == null)
            return ServerResponse.createByErrorMessage("不存在该服务");
        int id = service.getUserId();
        if(id != userId)
            return ServerResponse.createByErrorMessage("不拥有该服务，无下架权限");
        if(service.getStatus()!=Const.Status.onSell)
            return ServerResponse.createByErrorMessage("服务已经下架，不能重复下架");
       edu.zju.ccnt.service.model.Service newService = new edu.zju.ccnt.service.model.Service();
        newService.setId(service.getId());
        newService.setStatus(Const.Status.undercarriage);
        int resultCount = serviceMapper.updateByPrimaryKeySelective(newService);
        if(resultCount>0)
            return ServerResponse.createBySuccessMessage("下架成功");
        return ServerResponse.createByErrorMessage("下架失败");
    }

    //服务上架
    public ServerResponse grounding(Integer userId,Integer serviceId){
        log.info("服务上架");
        //需要先用userId验证该service是否属于该用户
        edu.zju.ccnt.service.model.Service service  = serviceMapper.selectByPrimaryKey(serviceId);
        if(service == null)
            return ServerResponse.createByErrorMessage("不存在该服务");
        int id = service.getUserId();
        if(id != userId)
            return ServerResponse.createByErrorMessage("不拥有该服务，无上架权限");
        if(service.getStatus()!=Const.Status.undercarriage)
            return ServerResponse.createByErrorMessage("服务已经上架，不能重复上架");
        edu.zju.ccnt.service.model.Service newService = new edu.zju.ccnt.service.model.Service();
        newService.setId(service.getId());
        newService.setStatus(Const.Status.onSell);
        int resultCount = serviceMapper.updateByPrimaryKeySelective(newService);
        if(resultCount>0)
            return ServerResponse.createBySuccessMessage("上架成功");
        return ServerResponse.createByErrorMessage("上架失败");
    }

    //删除服务
    @Transactional
    public ServerResponse delete(Integer userId,Integer serviceId){
        log.info("删除服务");
        //需要先用userId验证该service是否属于该用户
        edu.zju.ccnt.service.model.Service service  = serviceMapper.selectByPrimaryKey(serviceId);

        /**
         * UserFeign
         * 用到了User提供的用户是否审核
         */
        if(iUserFeign.judgeChecked(userId).getData().equals("false"))
            return ServerResponse.createByErrorMessage("用户审核通过才能进行删除服务");
        if(service == null)
            return ServerResponse.createByErrorMessage("不存在该服务，无法删除");
        int id = service.getUserId();
        if(id != userId)
            return ServerResponse.createByErrorMessage("不拥有该服务，无删除权限");

        int resultCount = serviceMapper.deleteByPrimaryKey(serviceId);
        if(resultCount > 0) {
            //删除服务所属的api
            List<Api> apiList = apiMapper.selectByServiceIdAndStatus(serviceId,null,null);
            for(Api api:apiList)
                apiMapper.deleteByPrimaryKey(api.getId());

            /**
             * mark SDK
             */
//            List<ServiceSDK> sdkList = serviceSDKMapper.selectByServiceId(serviceId);
//            for(ServiceSDK sdk : sdkList)
//                serviceSDKMapper.deleteByPrimaryKey(sdk.getId());

            /**
             * mark
             * 删除服务的调用情况感觉没有必要
             */
            //删除该服务的服务调用情况
//            int res = apiCallMapper.deleteByServiceId(serviceId);
//            ServerResponse serverResponse = iFileService.deleteFile(service.getServiceImg());
            return ServerResponse.createBySuccessMessage("删除成功");
        }
        return ServerResponse.createByErrorMessage("删除失败");
    }


    //更新服务
    public ServerResponse update(Integer userId, edu.zju.ccnt.service.model.Service service){
        log.info("更新服务");
        /**
         * UserFeign
         */
        String userStatus = iUserFeign.judgeChecked(userId).getData();
        Integer roleId = iUserFeign.getRole(userId).getData();

        if(userStatus.equals("fasle"))
            return ServerResponse.createByErrorMessage("用户需通过审核之后才能更新服务");
        boolean categoryTrue = iServiceCategoryService.containsCategory(service.getCategoryId());
        if(!categoryTrue)
            return ServerResponse.createByErrorMessage("不存在该服务类别，无法更新");
        edu.zju.ccnt.service.model.Service service1 = serviceMapper.selectByPrimaryKey(service.getId());
        if(!service1.getUserId().equals(userId))
            return ServerResponse.createByErrorMessage("没有更新该服务的权限");
        service.setUserId(userId); //注入userId,防止伪造
        edu.zju.ccnt.service.model.Service newService = new edu.zju.ccnt.service.model.Service();
        newService.setId(service.getId());
        //判断服务是否重名,除了当前服务
        int resultCount = serviceMapper.selectByServiceNameAndServiceId(service.getServiceName(),service.getId());
        if(resultCount > 0)
            return ServerResponse.createByErrorMessage("已经存在该服务名称");
        //添加可以修改信息
        newService.setServiceName(service.getServiceName());
        newService.setServiceImg(service.getServiceImg());
        newService.setCategoryId(service.getCategoryId());
        newService.setDetailIntroduction(service.getDetailIntroduction());
        newService.setIntroduction(service.getIntroduction());
        newService.setPrice(service.getPrice());
        newService.setFormat(service.getFormat());
        if(roleId.equals(Const.Role.SUPER_ADMIN.getCode()))
            newService.setChecked(Const.CheckStatus.CHECKED);
        else newService.setChecked(Const.CheckStatus.WAIT_CHECK);    //重置为待审核

        resultCount = serviceMapper.updateByPrimaryKeySelective(newService);
        if(resultCount > 0 ) {
            if(roleId.equals(Const.Role.SUPER_ADMIN.getCode()))
                return ServerResponse.createBySuccessMessage("服务更新成功");
            else
                return ServerResponse.createBySuccessMessage("服务更新成功,等待管理员审核");
        }
        return ServerResponse.createByErrorMessage("服务更新失败");
    }


    //审核服务
    public ServerResponse check(Integer userId,Integer serviceId,boolean checkStatus,String uncheckedCause){
        log.info("审核服务");
        edu.zju.ccnt.service.model.Service service  = serviceMapper.selectByPrimaryKey(serviceId);
        if(service == null)
            return ServerResponse.createByErrorMessage("不存在该服务");
//        if(service.getChecked() != Const.CheckStatus.WAIT_CHECK)
//            return ServerResponse.createByErrorMessage("服务已经审核过,不能重复审核");
        /**
         * UserFeign
         */
        String userStatus = iUserFeign.judgeChecked(userId).getData();
        if(userStatus.equals("false"))
            return ServerResponse.createByErrorMessage("只有已通过审核用户才能进行操作");
        //判断是否有审核该服务的权限
        /**
         * mark
         * 这里需要user的服务  判断当前登录的用户是否有审核这个服务的权限
         */
     //   List<Integer> userList = convertIntegerList(userMapper.selectByOrganizationIdAndRole(Const.Role.SERVICE_PROVIDER.getCode(),Lists.newArrayList(user.getOrganizationId())));
//        if(!userList.contains(service.getUserId())){
//            return ServerResponse.createByErrorMessage("没有该服务的权限");
//        }
        Integer check = checkStatus? Const.CheckStatus.CHECKED : Const.CheckStatus.FAILED_CHECKED;
        //审核
        if(check.equals(Const.CheckStatus.FAILED_CHECKED) && StringUtils.isEmpty(uncheckedCause))
            return ServerResponse.createByErrorMessage("当审核不通过时必须给出原因");
        service.setChecked(check);
        if(check.equals(Const.CheckStatus.FAILED_CHECKED))
            service.setRemarks(uncheckedCause);
        else
            service.setRemarks(StringUtils.EMPTY);
        int resultCount =  serviceMapper.updateByPrimaryKeySelective(service);
        if(resultCount > 0)
            return ServerResponse.createBySuccessMessage("审核成功");
        else
            return ServerResponse.createByErrorMessage("审核失败");
    }


    //服务提供方查看自身服务列表
    public ServerResponse listSelf(Integer userId,String serviceName,Integer checked, Integer pageNum,Integer pageSize){
        log.info("查看本组织下的服务列表");
        if(!StringUtils.isEmpty(serviceName)){
            serviceName = new StringBuilder().append("%").append(serviceName).append("%").toString();
        }else
            serviceName = new StringBuilder().append("%").append("%").toString();
        /**
         * mark
         * organization要提供对用户身份的验证
         */
//        User user = userMapper.selectByPrimaryKey(userId);
//        List<Integer> userList = convertIntegerList(userMapper.selectByOrganizationIdAndRole(Const.Role.SERVICE_PROVIDER.getCode(),Lists.newArrayList(user.getOrganizationId())));        //登录用户所能看到的服务提供者
//        PageHelper.startPage(pageNum,pageSize);
//        List<Service> serviceList = serviceMapper.selectByServiceNameAndUserList(serviceName,userList,checked);
//        PageInfo pageInfo = new PageInfo(serviceList);
//        List<ServiceSelfVo> serviceSelfVoList = assembleServiceSelfVoList(serviceList) ;
//        pageInfo.setList(serviceSelfVoList);
//        return ServerResponse.createBySuccess("查询成功",pageInfo);
        //先瞎写一个
        return ServerResponse.createByError();
    }

    private List<ServiceSelfVo> assembleServiceSelfVoList(List<edu.zju.ccnt.service.model.Service> serviceList ){      //生成返回对象
        List<ServiceSelfVo> serviceSelfVoList = Lists.newArrayList();
        for(edu.zju.ccnt.service.model.Service service : serviceList){
            ServiceSelfVo serviceSelfVo = assembleServiceSelfVo(service);
            serviceSelfVoList.add(serviceSelfVo);
        }
        return serviceSelfVoList;
    }

    private ServiceSelfVo assembleServiceSelfVo(edu.zju.ccnt.service.model.Service service){       //生成serviceSelf返回对象
        ServiceSelfVo serviceSelfVo = new ServiceSelfVo();
        serviceSelfVo.setServiceId(service.getId());
        serviceSelfVo.setServiceName(service.getServiceName());
        /**
         * mark
         * 图片
         */
        //serviceSelfVo.setServiceImg(PropertiesUtil.getProperty("ftp.server.http.prefix")+PropertiesUtil.getProperty("ftp.server.img")+"/"+service.getServiceImg());
        serviceSelfVo.setUserCount(service.getUserCount());
        serviceSelfVo.setCommentCount(service.getCommentCount());
        serviceSelfVo.setIntroduction(service.getIntroduction());
        /**
         * mark
         * score属性
         */
        //serviceSelfVo.setScore(service.getScore());
        serviceSelfVo.setCategoryId(service.getCategoryId());
        ServiceCategory serviceCategory = iServiceCategoryService.getCategorybyId(service.getCategoryId()).getData();
        serviceSelfVo.setCategoryName(serviceCategory.getCategoryName());
        serviceSelfVo.setChecked(service.getChecked());
        if(service.getChecked().equals(Const.CheckStatus.WAIT_CHECK)){      //未审核返回本组织管理员电话
            /**
             * mark
             * 要根据用户id返回组织管理员的电话  接口待设计  另外感觉返回邮件可能更好
             */
//            User  user = userMapper.selectByPrimaryKey(service.getUserId());
//            Organization organization = organizationMapper.selectByPrimaryKey(user.getOrganizationId());
//            if(organization == null){       //无组织则返回超级管理员联系方式
//                List<User> userList = userMapper.selectAdmins(Const.Role.SUPER_ADMIN.getCode());
//                User adminUser = userList.get(0);
//                String res = "电话：" + adminUser.getPhone() + " ;邮箱：" +  adminUser.getEmail() ;
//                serviceSelfVo.setRemarks(res);
//            }else{
//                List<User> userList = userMapper.selectByOrganizationAndRoleId(user.getOrganizationId(),Const.Role.SERVICE_ADMIN.getCode(),Const.CheckStatus.CHECKED);
//                User adminUser = userList.get(0);
//                String res = "电话：" + adminUser.getPhone() + " ;邮箱：" +  adminUser.getEmail() ;
//                serviceSelfVo.setRemarks(res);
//            }
        }else
            serviceSelfVo.setRemarks(service.getRemarks());
        serviceSelfVo.setStatus(service.getStatus());
        /**
         * mark
         * 要转换日期格式
         */
        //serviceSelfVo.setCreateTime(DateTimeUtil.dateToStr(service.getCreateTime()));
        serviceSelfVo.setUserId(service.getUserId());
        /**
         * mark
         * 需要user提供根据userid返回name的服务
         */
//        User user = userMapper.selectByPrimaryKey(service.getUserId());
//        serviceSelfVo.setUserName(user.getUsername());
        return serviceSelfVo;
    }

    //服务提供方查看自身服务详细信息
    public ServerResponse searchSelf(Integer userId,Integer serviceId){
        log.info("服务提供方查看服务详细信息，包含所有api信息");
        ServiceSelfInfoVo serviceSelfInfoVo = new ServiceSelfInfoVo();
        edu.zju.ccnt.service.model.Service service = serviceMapper.selectByPrimaryKey(serviceId);
        if(service == null)
            return ServerResponse.createByErrorMessage("不存在该服务");
        /**
         * mark
         * 用户是否有审核权限的问题
         */
//        User user = userMapper.selectByPrimaryKey(userId);
//        //判断是否有审核该服务的权限
//        List<Integer> userList = convertIntegerList(userMapper.selectByOrganizationIdAndRole(Const.Role.SERVICE_PROVIDER.getCode(),Lists.newArrayList(user.getOrganizationId())));
//        Integer serviceUserId = service.getUserId();
//        if((!userList.contains(serviceUserId))&&(!userId.equals(serviceUserId))){
//            return ServerResponse.createByErrorMessage("没有该服务的权限");
//        }
        List<Api> apiList = apiMapper.selectByServiceIdAndStatus(serviceId,null,null);
        serviceSelfInfoVo = assembleServiceSelfInfoVo(service,apiList);
        return ServerResponse.createBySuccess("查询成功",serviceSelfInfoVo);
    }

    private ServiceSelfInfoVo assembleServiceSelfInfoVo(edu.zju.ccnt.service.model.Service service , List<Api> apiList){       //产生返回对象，服务提供方视角
        ServiceSelfInfoVo serviceSelfInfoVo = new ServiceSelfInfoVo();
        serviceSelfInfoVo.setServiceId(service.getId());
        serviceSelfInfoVo.setServiceName(service.getServiceName());
        serviceSelfInfoVo.setFormat(service.getFormat());
        /**
         * mark
         * price转换格式
         */
        //serviceSelfInfoVo.setPrice(service.getPrice());
        serviceSelfInfoVo.setIntroduction(service.getIntroduction());
        serviceSelfInfoVo.setDetailIntroduction(service.getDetailIntroduction());
        serviceSelfInfoVo.setCommentCount(service.getCommentCount());
        serviceSelfInfoVo.setUserCount(service.getUserCount());
        /**
         * mark
         * score转换格式
         */
       // serviceSelfInfoVo.setScore(service.getScore());
        serviceSelfInfoVo.setServiceImg(service.getServiceImg());
        /**
         * mark
         * 图片
         */
        //serviceSelfInfoVo.setImgUrl( PropertiesUtil.getProperty("ftp.server.http.prefix")+PropertiesUtil.getProperty("ftp.server.img")+"/" + service.getServiceImg());
        serviceSelfInfoVo.setCategoryId(service.getCategoryId());
        ServiceCategory serviceCategory = iServiceCategoryService.getCategorybyId(service.getCategoryId()).getData();
        if(serviceCategory != null)
            serviceSelfInfoVo.setCategoryName(serviceCategory.getCategoryName());
        serviceSelfInfoVo.setStatus(service.getStatus());
        serviceSelfInfoVo.setChecked(service.getChecked());
        serviceSelfInfoVo.setRemarks(service.getRemarks());
        /**
         * 日期格式转换
         */
       // serviceSelfInfoVo.setCreateTime(DateTimeUtil.dateToStr(service.getCreateTime()));
        //serviceSelfInfoVo.setUpdateTime(DateTimeUtil.dateToStr(service.getUpdateTime()));
        serviceSelfInfoVo.setUserId(service.getUserId());
        /**
         * userid获取姓名
         */
//        User user = userMapper.selectByPrimaryKey(service.getUserId());
//        serviceSelfInfoVo.setUserName(user.getUsername());
        List<ApiInfoVo> apiInfoVoList = assembleApiInfoList(apiList,true);
        serviceSelfInfoVo.setApiInfoList(apiInfoVoList);
        return serviceSelfInfoVo;
    }


    /**
     * mark
     * convertIntegerList 和User有关
     */
//    private List<Integer> convertIntegerList(List<User> userList){
//        List<Integer> integerList = new ArrayList<>();
//        for(User user : userList){
//            integerList.add(user.getId());
//        }
//        return integerList;
//    }

    //首页获取所有服务大类的信息
    public List<ServiceInfoByCategory> getAllServiceByCategory(){
        log.info("获取服务类别");
        List<ServiceCategory> serviceCategoryList = iServiceCategoryService.list().getData();
        List<edu.zju.ccnt.service.model.Service> serviceList = serviceMapper.selectServiceList("%%", null, null);
        List<ServiceVo> serviceVoList = assembleServiceVoList(serviceList);

        List<ServiceInfoByCategory> categories = new ArrayList<>();
        for(ServiceCategory serviceCategory : serviceCategoryList){
            ServiceInfoByCategory serviceInfoByCategory = new ServiceInfoByCategory();
            serviceInfoByCategory.setCategoryId(serviceCategory.getId());
            serviceInfoByCategory.setCategoryName(serviceCategory.getCategoryName());
            serviceInfoByCategory.setServiceVos(new ArrayList<>());
            categories.add(serviceInfoByCategory);
        }

        for(ServiceVo serviceVo : serviceVoList){
            for(ServiceInfoByCategory serviceInfoByCategory : categories){
                if(serviceVo.getCategoryId() == serviceInfoByCategory.getCategoryId()){
                    serviceInfoByCategory.getServiceVos().add(serviceVo);
                }
            }
        }
        return categories;
    }
    /**
     * mark  和order有关
     */


    //给webIde那边提供的接口
//    public List<ServiceInfoByCategoryAndOrder> getAllServiceByOrderInfo(int userId){
//        log.info("根据用户的订单获取所有服务信息");
//        /**
//         * mark
//         * 和order有关
//         */
//        List<ServiceCategory> serviceCategoryList = serviceCategoryMapper.list();
//        List<Order> orderList = orderMapper.selectByUserId(userId);
//
//        List<Integer> serviceIds = new ArrayList<>();
//        orderList.forEach(order -> {
//            serviceIds.add(order.getServiceId());
//        });
//        List<Service> serviceList = new ArrayList<>();
//        serviceList = serviceMapper.selectByServiceIdList(serviceIds);
//        Map<Integer, List<Service>> serviceMap = new HashMap<>();
//        for(Service service : serviceList){
//            if(serviceMap.get(service.getCategoryId()) != null){
//                serviceMap.get(service.getCategoryId()).add(service);
//            }else{
//                serviceMap.put(service.getCategoryId(), new ArrayList<>());
//                serviceMap.get(service.getCategoryId()).add(service);
//            }
//        }
//
//        for(ServiceCategory serviceCategory : serviceCategoryList){
//            if(!serviceMap.containsKey(serviceCategory.getId())){
//                serviceMap.put(serviceCategory.getId(), new ArrayList<>());
//            }
//        }
//        Map<Integer, List<Api>> map = new HashMap<>();
//        List<Api> apiList = new ArrayList<>();
//        apiList = apiMapper.selectByServiceIdsAndStatus(serviceIds, 1, 1);
//        for(Api api : apiList){
//            if(map.get(api.getServiceId()) != null){
//                map.get(api.getServiceId()).add(api);
//            }else{
//                map.put(api.getServiceId(), new ArrayList<>());
//                map.get(api.getServiceId()).add(api);
//            }
//        }


      //  List<ServiceInfoByCategoryAndOrder> serviceInfoByCategories = new ArrayList<>();
//        for(ServiceCategory serviceCategory : serviceCategoryList){
//            ServiceInfoByCategoryAndOrder serviceInfoByCategory = new ServiceInfoByCategoryAndOrder();
//            serviceInfoByCategory.setCategoryId(serviceCategory.getId());
//            serviceInfoByCategory.setCategoryName(serviceCategory.getCategoryName());
//            serviceInfoByCategory.setServiceVoByOrders(transServiceToServiceVo(serviceMap.get(serviceCategory.getId()), map, orderList));
//            serviceInfoByCategories.add(serviceInfoByCategory);
//        }
 //       return serviceInfoByCategories;
   // }
//
//    private List<ServiceVoByOrder> transServiceToServiceVo(List<Service> services, Map<Integer, List<Api>> apiMap, List<Order> orderList){
//
//        List<ServiceVoByOrder> serviceVos = new ArrayList<>();
//        for(Service service : services){
//
//            ServiceVoByOrder serviceVo = new ServiceVoByOrder();
//            serviceVo.setCategoryId(service.getCategoryId());
//            serviceVo.setServiceName(service.getServiceName());
//            serviceVo.setScore(service.getScore());
//            serviceVo.setServiceId(service.getId());
//            serviceVo.setUserCount(service.getUserCount());
//            serviceVo.setIntroduction(service.getIntroduction());
//            serviceVo.setApiKey(orderList.stream().filter(order -> {
//                return order.getServiceId() == service.getId();
//            }).map(Order::getApikey).collect(Collectors.toList()).get(0));
//
//            if(apiMap.containsKey(service.getId())){
//                List<ApiInfoVo> apiInfoVos = new ArrayList<>();
//                apiInfoVos = apiMap.get(service.getId()).stream().map(api -> {
//                    ApiInfoVo apiInfoVo = new ApiInfoVo();
//                    apiInfoVo.setApiCallWay(api.getApiCallWay());
//                    apiInfoVo.setApiAddress(api.getApiAddress());
//                    apiInfoVo.setApiIntroduction(api.getApiIntroduction());
//                    apiInfoVo.setApiUrl(SERVER_ADDRESS + api.getPath());
//                    apiInfoVo.setServiceId(api.getServiceId());
//                    apiInfoVo.setArguments(api.getArguments());
//                    apiInfoVo.setCallExample(api.getCallExample());
//                    apiInfoVo.setDescription(api.getDescription());
//                    apiInfoVo.setId(api.getId());
//                    return apiInfoVo;
//                }).collect(Collectors.toList());
//                serviceVo.setApiInfoVoList(apiInfoVos);
//            }
//            serviceVos.add(serviceVo);
//        }
//        return serviceVos;
//    }


    //管理员查看服务列表
    public ServerResponse manageList(String serviceName,Integer checked,Integer organizationId, Integer pageNum, Integer pageSize){
        log.info("superAdmin查看服务列表");
        if(serviceName != null){
            serviceName = new StringBuilder().append("%").append(serviceName).append("%").toString();
        }else
            serviceName = new StringBuilder().append("%").append("%").toString();
        List<Integer> userList = null;
        if(organizationId !=null && organizationId.equals(0))
            organizationId = null;
        if(organizationId != null) {
            /**
             * 和user有关
             */
            //userList = convertIntegerList(userMapper.selectByOrganizationIdAndRole(Const.Role.SERVICE_PROVIDER.getCode(), Lists.newArrayList(organizationId)));
            if(userList.size() == 0)
                return ServerResponse.createBySuccess(new PageInfo<>());
        }
        PageHelper.startPage(pageNum,pageSize);
        List<edu.zju.ccnt.service.model.Service> serviceList = serviceMapper.selectBySuperAdmin(serviceName,checked,userList);
        PageInfo pageInfo = new PageInfo(serviceList);
        List<ServiceSelfVo> serviceSelfVoList = assembleServiceSelfVoList(serviceList) ;
        pageInfo.setList(serviceSelfVoList);
        return ServerResponse.createBySuccess("查询成功",pageInfo);
    }

    //管理员查看服务详情
    public ServerResponse manageServiceDetail(Integer serviceId){
        log.info("superAdmin查看服务详情");
        edu.zju.ccnt.service.model.Service service = serviceMapper.selectByPrimaryKey(serviceId);
        if(service == null)
            return ServerResponse.createByErrorMessage("不存在该服务");
        List<Api> apiList = apiMapper.selectByServiceIdAndStatus(serviceId,null,null);
        ServiceSelfInfoVo serviceSelfInfoVo = new ServiceSelfInfoVo();
        serviceSelfInfoVo = assembleServiceSelfInfoVo(service,apiList);
        return ServerResponse.createBySuccess("查询成功",serviceSelfInfoVo);
    }

    public ServerResponse superAdmincheck(Integer serviceId,boolean checked,String uncheckedCause){
        log.info("superAdmin审核");
        if(serviceId == null)
            return ServerResponse.createByErrorMessage("id不能为空");
        edu.zju.ccnt.service.model.Service service = serviceMapper.selectByPrimaryKey(serviceId);
        if(service == null)
            return ServerResponse.createByErrorMessage("不存在该服务");
        Integer check = checked?Const.CheckStatus.CHECKED:Const.CheckStatus.FAILED_CHECKED;
        if(check.equals(service.getChecked()))
            return ServerResponse.createByErrorMessage("不能重复审核");
        if(check.equals(Const.CheckStatus.FAILED_CHECKED) && StringUtils.isEmpty(uncheckedCause))
            return ServerResponse.createByErrorMessage("审核失败时必须给出原因");
        edu.zju.ccnt.service.model.Service newService = new edu.zju.ccnt.service.model.Service();
        newService.setId(service.getId());
        newService.setChecked(check);
        if(check.equals(Const.CheckStatus.FAILED_CHECKED))
            newService.setRemarks(uncheckedCause);
        else
            service.setRemarks(StringUtils.EMPTY);
        int resultCount = serviceMapper.updateByPrimaryKeySelective(newService);
        if(resultCount > 0)
            return ServerResponse.createBySuccessMessage("审核成功");
        return ServerResponse.createByErrorMessage("审核失败");
    }

    public ServerResponse listThirdService(String serviceName, Integer pageNum,Integer pageSize){
        log.info("查询所有第三方服务");
        PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isEmpty(serviceName)){
            serviceName = new StringBuilder().append("%").append("%").toString();
        }else{
            serviceName = new StringBuilder().append("%").append(serviceName).append("%").toString();
        }
        List<edu.zju.ccnt.service.model.Service> serviceList = serviceMapper.selectThirdByServiceName(serviceName,Const.isOtherService.isThirdParty);
        PageInfo pageInfo = new PageInfo(serviceList);
        List<ServiceSelfVo> serviceSelfVoList = assembleServiceSelfVoList(serviceList) ;
        pageInfo.setList(serviceSelfVoList);
        return ServerResponse.createBySuccess("查询成功",pageInfo);
    }



}
