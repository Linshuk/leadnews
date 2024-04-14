package jmu.lsk.wemedia.controller.v1;

import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.common.enums.AppHttpCodeEnum;
import jmu.lsk.model.wemedia.dtos.WmMaterialDto;
import jmu.lsk.model.wemedia.pojos.WmMaterial;
import jmu.lsk.wemedia.service.WmMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/material")
public class WmMaterialController {

    @Autowired
    private WmMaterialService wmMaterialService;
 
    @PostMapping("/upload_picture")
    public ResponseResult uploadPicture(MultipartFile multipartFile){
        return wmMaterialService.uploadPicture(multipartFile);
    }


    @PostMapping("/list")
    public ResponseResult findList(@RequestBody WmMaterialDto dto){
        return wmMaterialService.findList(dto);
    }


    @GetMapping("/del_picture/{id}")
    public ResponseResult delPicture(@PathVariable Integer   id){
        if(id==null)
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        WmMaterial wmMaterial = wmMaterialService.getById(id);
        if(wmMaterial==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        boolean b = wmMaterialService.removeById(id);
        if(b){
            return ResponseResult.okResult(wmMaterial);
        }else{
            return ResponseResult.errorResult(501,"文件删除失败");
        }
    }

    @GetMapping("/collect/{id}")
    public ResponseResult collect(@PathVariable Integer id){
        if(id==null)
            return ResponseResult.errorResult(0,"传输的收藏id为空");
        WmMaterial wmMaterial = wmMaterialService.getById(id);
//        System.out.println(wmMaterial);
        wmMaterial.setIsCollection((short) 1);
        wmMaterialService.updateById(wmMaterial);

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @GetMapping("/cancel_collect/{id}")
    public ResponseResult cancelCollect(@PathVariable Integer id){
        if(id==null)
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        WmMaterial wmMaterial = wmMaterialService.getById(id);
//        System.out.println(wmMaterial);
        wmMaterial.setIsCollection((short) 0);
        wmMaterialService.updateById(wmMaterial);

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }





}