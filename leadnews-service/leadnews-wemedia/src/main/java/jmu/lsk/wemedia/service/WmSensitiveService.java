package jmu.lsk.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.wemedia.dtos.SensitiveDto;
import jmu.lsk.model.wemedia.pojos.WmSensitive;


public interface WmSensitiveService extends IService<WmSensitive> {

    ResponseResult list(SensitiveDto dto);

    ResponseResult insert(WmSensitive wmSensitive);

    ResponseResult update(WmSensitive wmSensitive);

    ResponseResult delete(Integer id);
}
