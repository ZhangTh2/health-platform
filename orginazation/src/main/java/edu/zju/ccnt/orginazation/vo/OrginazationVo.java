package edu.zju.ccnt.orginazation.vo;

import lombok.Data;

/**
 * @author zth
 */
@Data
public class OrginazationVo {
    private Integer id;
    private String orginazationName;
    private String orginazationAddress;
    private String description;
    private Integer parentId;
    private String phone;
    private String email;
    private String patentName;
    private Integer checked;
    private String remarks;
    private String createTime;

}
