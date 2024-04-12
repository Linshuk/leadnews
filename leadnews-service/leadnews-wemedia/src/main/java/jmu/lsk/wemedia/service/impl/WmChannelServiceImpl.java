package jmu.lsk.wemedia.service.impl;
 
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.net.httpserver.Authenticator;
import jmu.lsk.model.common.dtos.PageResponseResult;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.common.enums.AppHttpCodeEnum;
import jmu.lsk.model.common.wemedia.dtos.ChannelDto;
import jmu.lsk.model.common.wemedia.pojos.WmChannel;
import jmu.lsk.model.common.wemedia.pojos.WmSensitive;
import jmu.lsk.wemedia.mapper.WmChannelMapper;
import jmu.lsk.wemedia.service.WmChannelService;
import jmu.lsk.wemedia.service.WmMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class WmChannelServiceImpl extends ServiceImpl<WmChannelMapper, WmChannel> implements WmChannelService {
    /**
     * 查询所有频道
     * @return
     */
    @Override
    public ResponseResult findAll() {
        return ResponseResult.okResult(list());
    }

    @Override
    public ResponseResult findByNameAndPage(ChannelDto dto) {
        dto.checkParam();
        IPage page = new Page(dto.getPage(),dto.getSize());
        LambdaQueryWrapper<WmChannel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(WmChannel::getName,dto.getName());
        page = page(page,lambdaQueryWrapper);
        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }

    @Autowired
    private WmChannelMapper wmChannelMapper;
    @Override
    public ResponseResult insert(WmChannel adChannel) {
        Long count = wmChannelMapper.selectCount(Wrappers.<WmChannel>lambdaQuery().eq(WmChannel::getName,adChannel.getName()));
        if(count > 0){
            return ResponseResult.errorResult(AppHttpCodeEnum.CREATED);
        }else{
            wmChannelMapper.insert(adChannel);
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult update(WmChannel adChannel) {
        wmChannelMapper.updateById(adChannel);
        return  ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult delete(Integer id) {
        wmChannelMapper.delete(Wrappers.<WmChannel>lambdaQuery().eq(WmChannel::getId,id));
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}