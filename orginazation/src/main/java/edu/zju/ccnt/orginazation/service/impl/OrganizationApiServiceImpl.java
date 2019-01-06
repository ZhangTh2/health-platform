package edu.zju.ccnt.orginazation.service.impl;

import edu.zju.ccnt.orginazation.common.ServerResponse;
import edu.zju.ccnt.orginazation.dao.OrginazationMapper;
import edu.zju.ccnt.orginazation.model.Orginazation;
import edu.zju.ccnt.orginazation.service.IOrganizationApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("iOrganizationApiService")
public class OrganizationApiServiceImpl implements IOrganizationApiService {

    @Autowired
    private OrginazationMapper orginazationMapper;
    //根据id判断有无此组织
    public ServerResponse judgebyId(Integer id) {
        log.info("根据id判断有无此组织");

        String exist = "true";
        Orginazation orginazation = orginazationMapper.selectByPrimaryKey(id);
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
