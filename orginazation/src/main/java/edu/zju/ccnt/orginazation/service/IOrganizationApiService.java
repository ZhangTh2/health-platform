package edu.zju.ccnt.orginazation.service;

import edu.zju.ccnt.orginazation.common.ServerResponse;

/**
 * @author zth
 * 2019.1.3
 */
public interface IOrganizationApiService {
    //根据id判断有无此组织
    ServerResponse judgebyId(Integer id);

    //根据id获得组织名称
    ServerResponse getbyId(Integer id);
}
