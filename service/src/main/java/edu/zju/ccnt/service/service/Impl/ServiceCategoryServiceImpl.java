package edu.zju.ccnt.service.service.Impl;

import edu.zju.ccnt.service.common.ServerResponse;
import edu.zju.ccnt.service.dao.ServiceCategoryMapper;
import edu.zju.ccnt.service.model.ServiceCategory;
import edu.zju.ccnt.service.service.IServiceCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("iServiceCategoryService")
public class ServiceCategoryServiceImpl implements IServiceCategoryService {

    @Autowired
    private ServiceCategoryMapper  serviceCategoryMapper;

    public ServerResponse<String> addCategory(Integer parentId, String categoryName) {
        log.info("添加服务类别"+categoryName);
        if(parentId==null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("添加服务类别参数错误");
        }
        ServiceCategory serviceCategory = new ServiceCategory();
        serviceCategory.setCategoryName(categoryName);
        serviceCategory.setParentId(parentId==null?0:parentId);
        serviceCategory.setStatus(1);
        int resultCount = serviceCategoryMapper.insert(serviceCategory);
        if(resultCount > 0){
            return ServerResponse.createBySuccessMessage("服务类别添加成功");
        }
        return ServerResponse.createByErrorMessage("服务类别添加失败");
    }

    public ServerResponse<List<ServiceCategory>> list() {
        log.info("获取服务类别");
        List<ServiceCategory> serviceCategoryList = serviceCategoryMapper.list();
        return ServerResponse.createBySuccess("查询成功",serviceCategoryList);
    }

    //判断是否存在某个服务类别
    public boolean containsCategory(Integer categoryId) {
        log.info("获取服务类别");
        List<ServiceCategory> serviceCategoryList = serviceCategoryMapper.list();
        for(ServiceCategory serviceCategory : serviceCategoryList){
            if(serviceCategory.getId().equals(categoryId))
                return true;
        }
        return false;
    }

    //由Id获得Category对象
    public ServerResponse<ServiceCategory> getCategorybyId(Integer categoryId) {
        log.info("由Id获得Category对象");
        ServiceCategory serviceCategory = serviceCategoryMapper.selectByPrimaryKey(categoryId);
        if(serviceCategory==null)
            return ServerResponse.createByErrorMessage("该类别不存在");
        else
            return ServerResponse.createBySuccess("查询成功",serviceCategory);
    }
}
