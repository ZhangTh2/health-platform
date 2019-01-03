package edu.zju.ccnt.service.service;

import edu.zju.ccnt.service.common.ServerResponse;
import edu.zju.ccnt.service.model.Service;
import edu.zju.ccnt.service.vo.ServiceInfoByCategory;
import edu.zju.ccnt.service.vo.ServiceInfoByCategoryAndOrder;
import edu.zju.ccnt.service.vo.ServiceVo;

import java.util.List;

/**
 * Created by LXY on 2017/10/26.
 */
public interface IServiceService {

    ServerResponse<String> add(Service service, Integer userId);

    ServerResponse search(Integer serviceId);


    ServerResponse list(String serviceName, String orderBy, Integer categoryId, Integer pageNum, Integer pageSize);

    ServerResponse undercarriage(Integer userId, Integer serviceId);

    ServerResponse grounding(Integer userId, Integer serviceId);

    ServerResponse delete(Integer userId, Integer serviceId);

    ServerResponse update(Integer userId, Service service);

    ServerResponse check(Integer userId, Integer serviceId, boolean checkStatus, String uncheckedCause);

    ServerResponse listSelf(Integer userId, String serviceName, Integer checked, Integer pageNum, Integer pageSize);

    ServerResponse searchSelf(Integer userId, Integer serviceId);

    List<ServiceInfoByCategory> getAllServiceByCategory();

    List<ServiceInfoByCategoryAndOrder> getAllServiceByOrderInfo(int userId);

    ServerResponse manageList(String serviceName, Integer checked, Integer organizationId, Integer pageNum, Integer pageSize);

    ServerResponse manageServiceDetail(Integer serviceId);

    ServerResponse superAdmincheck(Integer serviceId, boolean checked, String uncheckedCause);

    //ServerResponse elasticsearch(String keyword);
    public List<ServiceVo> listOfAll(String serviceName);

    List<ServiceVo> searchBykeyword(String keyword);

    ServerResponse sdkList(Integer serviceId);

    ServerResponse listThirdService(String serviceName, Integer pageNum, Integer pageSize);


    ServerResponse searchOperationLogs(String userName, Integer pageNum, Integer pageSize);

    ServerResponse searchTopFive();

    ServerResponse searchCallDetail(String serviceName, Integer pageNum, Integer pageSize);
}
