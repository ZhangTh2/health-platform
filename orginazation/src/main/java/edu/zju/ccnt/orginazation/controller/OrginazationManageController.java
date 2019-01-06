package edu.zju.ccnt.orginazation.controller;


import com.mysql.cj.Session;
import edu.zju.ccnt.orginazation.common.ServerResponse;
import edu.zju.ccnt.orginazation.model.Orginazation;
import edu.zju.ccnt.orginazation.service.IOrginazationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manage/organization/")
@Api(value = "superAdmin组织管理")

/**
 * 要加入鉴权,待实现
 */
public class OrginazationManageController  {

    @Autowired
    private IOrginazationService iOrganizationService;



    @RequestMapping(value = "add.do",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "添加组织",notes = "管理员添加组织信息")
    //"添加组织"
    public ServerResponse add(@RequestBody Orginazation organization){
        if(StringUtils.isEmpty(organization.getOrginazationName()))
            return ServerResponse.createByErrorMessage("组织名不能为空");

        if(StringUtils.isEmpty(organization.getOrginazationAddress()))
            return ServerResponse.createByErrorMessage("组织地址不能为空");

        if(StringUtils.isEmpty(organization.getDescription()))
            return ServerResponse.createByErrorMessage("描述不能为空");

        if(StringUtils.isEmpty(organization.getPhone()))
            return ServerResponse.createByErrorMessage("电话不能为空");

        if(StringUtils.isEmpty(organization.getEmail()))
            return ServerResponse.createByErrorMessage("邮箱不能为空");


        return iOrganizationService.add(organization.getParentId(),organization);
    }


    /**
     * 2018.12.22
     * 涉及到和user的交互  需要再设计
     * @param organizationId
     * @return
     */

//    //修改组织
//    @RequestMapping(value = "update.do",method = RequestMethod.POST)
//    @ResponseBody
//    @ApiOperation(value = "更新组织",notes = "超级管理员或者本组织人员更新组织信息，本组织人员提交的信息需要重新审核")
//    public ServerResponse update( @RequestBody Orginazation organization){
//        Session session =  SecurityUtils.getSubject().getSession();
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
//        return iOrganizationService.update(user.getId(),organization);
//    }


    //删除组织
    @RequestMapping(value = "delete.do",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "删除组织",notes = "管理员删除组织信息")
    public ServerResponse delete(Integer organizationId){
        return iOrganizationService.delete(organizationId);
    }


    //查询组织列表
    @RequestMapping(value = "list.do",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "组织列表",notes = "可以根据审核状态查看，管理员查询组织列表")
    public ServerResponse list(Integer checked){
        return iOrganizationService.list(checked);
    }


    //审核组织信息
    @RequestMapping(value = "check.do",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "审核组织信息",notes = "管理员审核组织信息")
    public ServerResponse check(Integer organizationId,boolean checked,String uncheckedCause){
        return iOrganizationService.check(organizationId,checked,uncheckedCause);
    }

}
