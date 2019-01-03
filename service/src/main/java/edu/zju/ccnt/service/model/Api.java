package edu.zju.ccnt.service.model;

import java.util.Date;

public class Api {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api.mark
     *
     * @mbg.generated
     */
    private String mark;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api.api_address
     *
     * @mbg.generated
     */
    private String apiAddress;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api.path
     *
     * @mbg.generated
     */
    private String path;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api.strip_prefix
     *
     * @mbg.generated
     */
    private Integer stripPrefix;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api.description
     *
     * @mbg.generated
     */
    private String description;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api.api_call_way
     *
     * @mbg.generated
     */
    private String apiCallWay;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api.api_introduction
     *
     * @mbg.generated
     */
    private String apiIntroduction;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api.status
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api.service_id
     *
     * @mbg.generated
     */
    private Integer serviceId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api.checked
     *
     * @mbg.generated
     */
    private Integer checked;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api.remarks
     *
     * @mbg.generated
     */
    private String remarks;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api.arguments
     *
     * @mbg.generated
     */
    private String arguments;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api.call_limit
     *
     * @mbg.generated
     */
    private Integer callLimit;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api.call_ip_limit
     *
     * @mbg.generated
     */
    private Integer callIpLimit;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api.result_arguments
     *
     * @mbg.generated
     */
    private String resultArguments;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api.error_code
     *
     * @mbg.generated
     */
    private String errorCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api.result
     *
     * @mbg.generated
     */
    private String result;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api.call_example
     *
     * @mbg.generated
     */
    private String callExample;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api.app_key
     *
     * @mbg.generated
     */
    private String appKey;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api.id
     *
     * @return the value of api.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api.id
     *
     * @param id the value for api.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api.name
     *
     * @return the value of api.name
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api.name
     *
     * @param name the value for api.name
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api.mark
     *
     * @return the value of api.mark
     *
     * @mbg.generated
     */
    public String getMark() {
        return mark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api.mark
     *
     * @param mark the value for api.mark
     *
     * @mbg.generated
     */
    public void setMark(String mark) {
        this.mark = mark == null ? null : mark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api.api_address
     *
     * @return the value of api.api_address
     *
     * @mbg.generated
     */
    public String getApiAddress() {
        return apiAddress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api.api_address
     *
     * @param apiAddress the value for api.api_address
     *
     * @mbg.generated
     */
    public void setApiAddress(String apiAddress) {
        this.apiAddress = apiAddress == null ? null : apiAddress.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api.path
     *
     * @return the value of api.path
     *
     * @mbg.generated
     */
    public String getPath() {
        return path;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api.path
     *
     * @param path the value for api.path
     *
     * @mbg.generated
     */
    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api.strip_prefix
     *
     * @return the value of api.strip_prefix
     *
     * @mbg.generated
     */
    public Integer getStripPrefix() {
        return stripPrefix;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api.strip_prefix
     *
     * @param stripPrefix the value for api.strip_prefix
     *
     * @mbg.generated
     */
    public void setStripPrefix(Integer stripPrefix) {
        this.stripPrefix = stripPrefix;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api.description
     *
     * @return the value of api.description
     *
     * @mbg.generated
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api.description
     *
     * @param description the value for api.description
     *
     * @mbg.generated
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api.api_call_way
     *
     * @return the value of api.api_call_way
     *
     * @mbg.generated
     */
    public String getApiCallWay() {
        return apiCallWay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api.api_call_way
     *
     * @param apiCallWay the value for api.api_call_way
     *
     * @mbg.generated
     */
    public void setApiCallWay(String apiCallWay) {
        this.apiCallWay = apiCallWay == null ? null : apiCallWay.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api.api_introduction
     *
     * @return the value of api.api_introduction
     *
     * @mbg.generated
     */
    public String getApiIntroduction() {
        return apiIntroduction;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api.api_introduction
     *
     * @param apiIntroduction the value for api.api_introduction
     *
     * @mbg.generated
     */
    public void setApiIntroduction(String apiIntroduction) {
        this.apiIntroduction = apiIntroduction == null ? null : apiIntroduction.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api.status
     *
     * @return the value of api.status
     *
     * @mbg.generated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api.status
     *
     * @param status the value for api.status
     *
     * @mbg.generated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api.service_id
     *
     * @return the value of api.service_id
     *
     * @mbg.generated
     */
    public Integer getServiceId() {
        return serviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api.service_id
     *
     * @param serviceId the value for api.service_id
     *
     * @mbg.generated
     */
    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api.checked
     *
     * @return the value of api.checked
     *
     * @mbg.generated
     */
    public Integer getChecked() {
        return checked;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api.checked
     *
     * @param checked the value for api.checked
     *
     * @mbg.generated
     */
    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api.remarks
     *
     * @return the value of api.remarks
     *
     * @mbg.generated
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api.remarks
     *
     * @param remarks the value for api.remarks
     *
     * @mbg.generated
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api.arguments
     *
     * @return the value of api.arguments
     *
     * @mbg.generated
     */
    public String getArguments() {
        return arguments;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api.arguments
     *
     * @param arguments the value for api.arguments
     *
     * @mbg.generated
     */
    public void setArguments(String arguments) {
        this.arguments = arguments == null ? null : arguments.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api.call_limit
     *
     * @return the value of api.call_limit
     *
     * @mbg.generated
     */
    public Integer getCallLimit() {
        return callLimit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api.call_limit
     *
     * @param callLimit the value for api.call_limit
     *
     * @mbg.generated
     */
    public void setCallLimit(Integer callLimit) {
        this.callLimit = callLimit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api.call_ip_limit
     *
     * @return the value of api.call_ip_limit
     *
     * @mbg.generated
     */
    public Integer getCallIpLimit() {
        return callIpLimit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api.call_ip_limit
     *
     * @param callIpLimit the value for api.call_ip_limit
     *
     * @mbg.generated
     */
    public void setCallIpLimit(Integer callIpLimit) {
        this.callIpLimit = callIpLimit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api.result_arguments
     *
     * @return the value of api.result_arguments
     *
     * @mbg.generated
     */
    public String getResultArguments() {
        return resultArguments;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api.result_arguments
     *
     * @param resultArguments the value for api.result_arguments
     *
     * @mbg.generated
     */
    public void setResultArguments(String resultArguments) {
        this.resultArguments = resultArguments == null ? null : resultArguments.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api.error_code
     *
     * @return the value of api.error_code
     *
     * @mbg.generated
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api.error_code
     *
     * @param errorCode the value for api.error_code
     *
     * @mbg.generated
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode == null ? null : errorCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api.result
     *
     * @return the value of api.result
     *
     * @mbg.generated
     */
    public String getResult() {
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api.result
     *
     * @param result the value for api.result
     *
     * @mbg.generated
     */
    public void setResult(String result) {
        this.result = result == null ? null : result.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api.call_example
     *
     * @return the value of api.call_example
     *
     * @mbg.generated
     */
    public String getCallExample() {
        return callExample;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api.call_example
     *
     * @param callExample the value for api.call_example
     *
     * @mbg.generated
     */
    public void setCallExample(String callExample) {
        this.callExample = callExample == null ? null : callExample.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api.app_key
     *
     * @return the value of api.app_key
     *
     * @mbg.generated
     */
    public String getAppKey() {
        return appKey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api.app_key
     *
     * @param appKey the value for api.app_key
     *
     * @mbg.generated
     */
    public void setAppKey(String appKey) {
        this.appKey = appKey == null ? null : appKey.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api.create_time
     *
     * @return the value of api.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api.create_time
     *
     * @param createTime the value for api.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api.update_time
     *
     * @return the value of api.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api.update_time
     *
     * @param updateTime the value for api.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}