package edu.zju.ccnt.service.model;

import lombok.Data;

/**
 * Created by twb on 2017/10/31.
 */
@Data
public class ApiCoreInfo {

    private Integer id;

    private String path;

    private int serviceId;

    private String apiAddress;

    private boolean stripPrefix;

    private String apiName;

    private boolean status;

    private boolean checked;

    private String appKey;

    private String result;

}
