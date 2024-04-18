package jmu.lsk.wemedia.feign;

import jmu.lsk.apis.wemedia.IWemediaClient;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.wemedia.service.WmChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WemediaClient implements IWemediaClient {

    @Autowired
    WmChannelService wmChannelService;

    @Override
    @GetMapping("/api/v1/channel/list")
    public ResponseResult getChannels() {
        return wmChannelService.findAll();
    }
}
