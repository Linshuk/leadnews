package jmu.lsk.model.user.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jmu.lsk.model.common.annotation.IdEncrypt;
import lombok.Data;

import java.util.Date;

@Data
@TableName("ap_user_fan")
public class ApUserFan {

    @IdEncrypt
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Integer userId;

    private Integer fansId;

    private String fansName;

    private Short level;

    private Date createdTime;

    private Boolean isDisplay;

    private Boolean isShieldLetter;

    private Boolean isShieldComment;

}
