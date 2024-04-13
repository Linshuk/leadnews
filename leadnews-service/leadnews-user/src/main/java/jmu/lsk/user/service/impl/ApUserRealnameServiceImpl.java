package jmu.lsk.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jmu.lsk.common.constants.UserConstants;
import jmu.lsk.model.common.dtos.PageResponseResult;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.common.enums.AppHttpCodeEnum;
import jmu.lsk.model.common.user.dtos.AuthDto;
import jmu.lsk.model.common.user.pojos.ApUserRealname;
import jmu.lsk.model.common.wemedia.pojos.WmChannel;
import jmu.lsk.user.mapper.ApUserRealnameMapper;
import jmu.lsk.user.service.ApUserRealnameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApUserRealnameServiceImpl extends ServiceImpl<ApUserRealnameMapper, ApUserRealname> implements ApUserRealnameService {
    @Override
    public ResponseResult loadListByStatus(AuthDto dto) {
        dto.checkParam();
        IPage page = new Page(dto.getPage(),dto.getSize());
        LambdaQueryWrapper<ApUserRealname> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(dto.getStatus()==null){
            lambdaQueryWrapper.select();
        }else{
            lambdaQueryWrapper.eq(ApUserRealname::getStatus,dto.getStatus());
        }
        page = page(page,lambdaQueryWrapper);
        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }

    @Autowired
    private ApUserRealnameMapper apUserRealnameMapper;

    @Override
    public ResponseResult updateStatus(AuthDto dto, Short operation) {
        dto.checkParam();
        IPage page = new Page(dto.getPage(),dto.getSize());
        LambdaQueryWrapper<ApUserRealname> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        ApUserRealname apUserRealname = apUserRealnameMapper.selectOne(Wrappers.<ApUserRealname>lambdaQuery().eq(ApUserRealname::getId,dto.getId()));
        if(operation.equals(UserConstants.PASS_AUTH)){
            apUserRealname.setStatus(UserConstants.PASS);
            apUserRealnameMapper.updateById(apUserRealname);
        }else{
            apUserRealname.setStatus(UserConstants.FAIL);
            apUserRealnameMapper.updateById(apUserRealname);
        }
        page = page(page,lambdaQueryWrapper);
        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
