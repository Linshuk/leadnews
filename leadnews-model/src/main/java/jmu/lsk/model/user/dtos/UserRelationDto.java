package jmu.lsk.model.user.dtos;

import jmu.lsk.model.common.annotation.IdEncrypt;
import lombok.Data;


@Data
public class UserRelationDto {

    @IdEncrypt
    private Long articleId;

    private Integer authorId;

    private Short operation;
}
