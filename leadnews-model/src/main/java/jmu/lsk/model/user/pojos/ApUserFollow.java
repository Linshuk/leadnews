package jmu.lsk.model.user.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jmu.lsk.model.common.annotation.IdEncrypt;
import lombok.Data;

import java.util.Date;

@Data
@TableName("ap_user_follow")
public class ApUserFollow {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Integer userId;

    private Integer followId;

    private String followName;

    private Short level;

    private Boolean isNotice;

    private Date createdTime;

}
