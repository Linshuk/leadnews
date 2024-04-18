package jmu.lsk.wemedia.service.impl;
import jmu.lsk.apis.schedule.IScheduleClient;
import jmu.lsk.model.common.enums.TaskTypeEnum;
import jmu.lsk.model.schedule.dtos.Task;
import jmu.lsk.model.wemedia.pojos.WmNews;
import jmu.lsk.utils.common.ProtostuffUtil;
import jmu.lsk.wemedia.service.WmNewsTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class WmNewsTaskServiceImpl  implements WmNewsTaskService {
 
 
    @Autowired
    private IScheduleClient scheduleClient;
 
    /**
     * 添加任务到延迟队列中
     * @param id          文章的id
     * @param publishTime 发布的时间  可以做为任务的执行时间
     */
    @Override
    @Async
    public void addNewsToTask(Integer id, Date publishTime) {
 
        log.info("添加任务到延迟服务中----begin");
 
        Task task = new Task();
        task.setExecuteTime(publishTime.getTime());
        task.setTaskType(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType());
        task.setPriority(TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        WmNews wmNews = new WmNews();
        wmNews.setId(id);
        task.setParameters(ProtostuffUtil.serialize(wmNews));
 
        scheduleClient.addTask(task);
 
        log.info("添加任务到延迟服务中----end");
 
    }
    
}