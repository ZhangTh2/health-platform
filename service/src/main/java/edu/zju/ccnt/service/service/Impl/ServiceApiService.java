package edu.zju.ccnt.service.service.Impl;

import edu.zju.ccnt.service.common.Const;
import edu.zju.ccnt.service.common.ServerResponse;
import edu.zju.ccnt.service.dao.ServiceMapper;
import edu.zju.ccnt.service.service.IServiceApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zth
 */
@Slf4j
@Service("iServiceApiService")
public class ServiceApiService implements IServiceApiService {

    @Autowired
    private ServiceMapper serviceMapper;

    /**
     *
     * @param serviceId
     * @return 服务验证失败返回失败代码和信息，成功则返回成功代码和服务名
     */
    public ServerResponse<String> validateService(Integer serviceId) {
        log.info("Service-service验证服务");

        edu.zju.ccnt.service.model.Service service = serviceMapper.selectByPrimaryKey(serviceId);
        if(service.getChecked()!= Const.CheckStatus.CHECKED)
            return ServerResponse.createByErrorMessage("服务未审核，不能下单");
        else if(service.getStatus() != Const.Status.onSell)
            return ServerResponse.createByErrorMessage("服务未上架，不能下单");
        else
            return ServerResponse.createBySuccessMessage(service.getServiceName());
    }

}
