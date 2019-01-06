package edu.zju.ccnt.orginazation.service;


import edu.zju.ccnt.orginazation.common.ServerResponse;
import edu.zju.ccnt.orginazation.model.Orginazation;

/**
 * @author zth
 * 有注释部分是新增的
 */
public interface IOrginazationService {

    ServerResponse add(Integer parentId, Orginazation orginazation);

    ServerResponse update(Integer userId, Orginazation orginazation);

    ServerResponse delete(Integer orginazationId);

    ServerResponse list(Integer checked);

    ServerResponse check(Integer orginazationId, boolean checked, String uncheckedCause);

    ServerResponse getOrginazationInfo(Integer userId);



}
