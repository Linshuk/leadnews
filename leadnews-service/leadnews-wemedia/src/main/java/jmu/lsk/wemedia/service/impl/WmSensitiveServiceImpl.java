package jmu.lsk.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jmu.lsk.model.common.dtos.PageResponseResult;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.common.enums.AppHttpCodeEnum;
import jmu.lsk.model.common.wemedia.dtos.SensitiveDto;
import jmu.lsk.model.common.wemedia.pojos.WmChannel;
import jmu.lsk.model.common.wemedia.pojos.WmSensitive;
import jmu.lsk.wemedia.mapper.WmSensitiveMapper;
import jmu.lsk.wemedia.service.WmSensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WmSensitiveServiceImpl extends ServiceImpl<WmSensitiveMapper, WmSensitive> implements WmSensitiveService {

    @Autowired
    WmSensitiveMapper wmSensitiveMapper;

    @Override
    public ResponseResult list(SensitiveDto dto) {
        dto.checkParam();
        IPage page = new Page(dto.getPage(),dto.getSize());
        LambdaQueryWrapper<WmSensitive> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(WmSensitive::getSensitives,dto.getName());
        page = page(page,lambdaQueryWrapper);
        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }

    @Override
    public ResponseResult insert(WmSensitive wmSensitive) {
        Long count = wmSensitiveMapper.selectCount(Wrappers.<WmSensitive>lambdaQuery().eq(WmSensitive::getSensitives,wmSensitive.getSensitives()));
        if(count > 0){
            return ResponseResult.errorResult(AppHttpCodeEnum.CREATED);
        }else{
            wmSensitiveMapper.insert(wmSensitive);
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult update(WmSensitive wmSensitive) {
        if(wmSensitiveMapper.selectOne(Wrappers.<WmSensitive>lambdaQuery().eq(WmSensitive::getId,wmSensitive.getId()))==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NOT_FOUND);
        }
        wmSensitiveMapper.updateById(wmSensitive);
        return  ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult delete(Integer id) {
        if(wmSensitiveMapper.selectOne(Wrappers.<WmSensitive>lambdaQuery().eq(WmSensitive::getId,id))==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NOT_FOUND);
        }
        wmSensitiveMapper.delete(Wrappers.<WmSensitive>lambdaQuery().eq(WmSensitive::getId,id));
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
