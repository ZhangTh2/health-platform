package edu.zju.ccnt.orginazation.service.impl;


import edu.zju.ccnt.orginazation.common.Const;
import edu.zju.ccnt.orginazation.common.ServerResponse;
import edu.zju.ccnt.orginazation.dao.OrginazationMapper;
import edu.zju.ccnt.orginazation.model.Orginazation;
import edu.zju.ccnt.orginazation.service.IOrginazationService;
import edu.zju.ccnt.orginazation.utils.DateTimeUtil;
import edu.zju.ccnt.orginazation.vo.OrginazationVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@Service("iOrginazationService")
@Slf4j
public class OrginazationServiceImpl implements IOrginazationService {

    @Autowired
    private OrginazationMapper orginazationMapper;

    public ServerResponse add(Integer parentId, Orginazation orginazation){
        log.info("添加组织信息");
        if(parentId == null){
            orginazation.setParentId(0);
        }else
            orginazation.setParentId(parentId);
        Orginazation parentOrg = orginazationMapper.selectByPrimaryKey(orginazation.getParentId());
        if(parentOrg == null && !parentId.equals(0))            //为空且不为0
            return ServerResponse.createByErrorMessage("不存在该父组织");
        //判断是否已经存在该组织了
        int resultCount = orginazationMapper.selectByOrginazationName(orginazation.getOrginazationName());
        if(resultCount > 0)
            return ServerResponse.createByErrorMessage("该组织已经存在");
        orginazation.setChecked(Const.CheckStatus.WAIT_CHECK);
        resultCount = orginazationMapper.insert(orginazation);
        if(resultCount > 0)
            return ServerResponse.createBySuccessMessage("组织信息添加成功");
        return ServerResponse.createByErrorMessage("组织信息添加失败");
    }

    public ServerResponse update(Integer userId,Orginazation orginazation){
        log.info("更新组织信息");
        Orginazation oldOrganization = orginazationMapper.selectByPrimaryKey(orginazation.getId());
        if(oldOrganization == null)
            return ServerResponse.createByErrorMessage("不存在该组织");
        Orginazation parentOrg = orginazationMapper.selectByPrimaryKey(orginazation.getParentId());
        if(parentOrg == null && !orginazation.getParentId().equals(0))
            return ServerResponse.createByErrorMessage("不存在该父组织");

        /**
         * 要在user服务中添加一个接口：根据userid查询orginazationid和role
         */

//        User user = userMapper.selectByPrimaryKey(userId);
//        if(!  ((/**user.getRoleId().equals(Const.Role.SERVICE_ADMIN.getCode())&&*/ user.getOrganizationId().equals(organization.getId()))
//                || (user.getRoleId().equals(Const.Role.SUPER_ADMIN.getCode()))
//        ))
//            return ServerResponse.createByErrorMessage("只有本组织人员或超级管理员才能修改组织信息");
        int resultCount = orginazationMapper.selectByOrginazationNameAndId(orginazation.getOrginazationName(),orginazation.getId());
        if(resultCount >0)
            return ServerResponse.createByErrorMessage("已经存在该组织名");

        Orginazation newOrginazation = new Orginazation();      //只能更新某些特定信息
        newOrginazation.setId(orginazation.getId());
        newOrginazation.setOrginazationName(orginazation.getOrginazationName());
        newOrginazation.setDescription(orginazation.getDescription());
        newOrginazation.setParentId(orginazation.getParentId());
        newOrginazation.setOrginazationAddress(orginazation.getOrginazationAddress());
        newOrginazation.setPhone(orginazation.getPhone());
        newOrginazation.setEmail(orginazation.getEmail());
//        if(user.getRoleId().equals(Const.Role.SUPER_ADMIN.getCode()))
//            newOrganization.setChecked(Const.CheckStatus.CHECKED);       //因为是superAdmin添加，这里直接变为审核通过。。
//        else
//            newOrganization.setChecked(Const.CheckStatus.WAIT_CHECK);    //需要重新审核
//        resultCount = organizationMapper.updateByPrimaryKeySelective(newOrganization);
        if(resultCount > 0)
            return ServerResponse.createBySuccessMessage("组织信息更新成功");
        return null;
    }

    public ServerResponse delete(Integer orginazationId){
        log.info("删除组织信息");
        if(orginazationId == null)
            return ServerResponse.createByErrorMessage("id不能为空");
        Orginazation orginazation = orginazationMapper.selectByPrimaryKey(orginazationId);
        if(orginazation == null)
            return ServerResponse.createByErrorMessage("该组织已经不存在");
        int resultCount = orginazationMapper.deleteByPrimaryKey(orginazationId);
        if(resultCount > 0)
            return ServerResponse.createBySuccessMessage("删除成功");
        return null;
    }

    public ServerResponse list(Integer checked){
        log.info("获取组织列表");
        List<Orginazation> orginazations = orginazationMapper.selectBySuperAdmin(checked);
        List<OrginazationVo> orginazationVoList = new ArrayList<>();
        for(Orginazation orginazation : orginazations){
            orginazationVoList.add(assembleOrganizationVo(orginazation));
        }
        return ServerResponse.createBySuccess("查询成功",orginazationVoList);
    }

    private OrginazationVo assembleOrganizationVo(Orginazation orginazation){
        OrginazationVo orginazationVo = new OrginazationVo();
        orginazationVo.setId(orginazation.getId());
        orginazationVo.setOrginazationName(orginazation.getOrginazationName());
        orginazationVo.setOrginazationAddress(orginazation.getOrginazationAddress());
        orginazationVo.setParentId(orginazation.getParentId());
        orginazationVo.setRemarks(orginazation.getRemarks());
        orginazationVo.setPhone(orginazation.getPhone());
        orginazationVo.setEmail(orginazation.getEmail());
        if(orginazation.getParentId() != 0){
            Orginazation parent = orginazationMapper.selectByPrimaryKey(orginazation.getParentId());
            if(parent !=null)
                orginazationVo.setPatentName(parent.getOrginazationName());
        }
        orginazationVo.setCreateTime(DateTimeUtil.dateToStr(orginazation.getCreateTime()));
        orginazationVo.setChecked(orginazation.getChecked());
        orginazationVo.setDescription(orginazation.getDescription());
        return orginazationVo;
    }

    public ServerResponse check(Integer orginazationId,boolean checked,String uncheckedCause){
        log.info("审核组织信息");
        if(orginazationId == null) return ServerResponse.createByErrorMessage("id不能为空");
        Orginazation orginazation = orginazationMapper.selectByPrimaryKey(orginazationId);
        if(orginazation == null)
            return ServerResponse.createByErrorMessage("该组织不存在");
        Integer check = checked?Const.CheckStatus.CHECKED:Const.CheckStatus.FAILED_CHECKED;
        if(check.equals(Const.CheckStatus.FAILED_CHECKED)&& StringUtils.isEmpty(uncheckedCause))
            return ServerResponse.createByErrorMessage("审核失败需要给出失败原因");
        if(check.equals(orginazation.getChecked()))
            return ServerResponse.createByErrorMessage("不能重复相同审核");
        Orginazation newOrginazaton = new Orginazation();
        newOrginazaton.setId(orginazationId);
        newOrginazaton.setChecked(check);
        if(check.equals(Const.CheckStatus.FAILED_CHECKED)) newOrginazaton.setRemarks(uncheckedCause);
        else newOrginazaton.setRemarks(StringUtils.EMPTY);
        int resultCount = orginazationMapper.updateByPrimaryKeySelective(newOrginazaton);
        if(resultCount > 0)
            return ServerResponse.createBySuccessMessage("审核成功");
        return ServerResponse.createByErrorMessage("审核失败");
    }

    public ServerResponse getOrginazationInfo(Integer userId){
        log.info("查询组织信息");
        /**
        这部分待实现user接口
        User user = userMapper.selectByPrimaryKey(userId);
        Organization organization = organizationMapper.selectByPrimaryKey(user.getOrganizationId());
        if(organization == null)
            return ServerResponse.createByErrorMessage("不属于任何组织");
        OrganizationVo organizationVo = assembleOrganizationVo(organization);

        return ServerResponse.createBySuccess("查询成功",orginazationVo);
         */
        return ServerResponse.createBySuccess("查询成功");
    }


    public ServerResponse judgebyId(Integer id) {
        log.info("根据id判断有无此组织");

        String exist = "true";
        Orginazation  orginazation = orginazationMapper.selectByPrimaryKey(id);
        if(orginazation==null) exist = "false";
        return ServerResponse.createBySuccessMessage(exist);
    }

    //根据id获得组织名称
    public ServerResponse getbyId(Integer id) {
        log.info("根据id获得组织名称");

        String name = orginazationMapper.selectNameById(id);
        return ServerResponse.createBySuccessMessage(name);
    }
}

