package edu.zju.ccnt.service.service;

import edu.zju.ccnt.service.common.ServerResponse;
import edu.zju.ccnt.service.model.ServiceCategory;

import java.util.List;

public interface IServiceCategoryService {

    ServerResponse<String> addCategory(Integer parentId, String categoryName);

    ServerResponse<List<ServiceCategory>> list();

    //由Id获得Category对象
    ServerResponse<ServiceCategory> getCategorybyId(Integer categoryId);

    boolean containsCategory(Integer categoryId);
}
