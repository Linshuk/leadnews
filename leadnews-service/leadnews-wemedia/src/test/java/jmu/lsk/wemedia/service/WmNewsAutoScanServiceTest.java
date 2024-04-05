package jmu.lsk.wemedia.service;

import jmu.lsk.wemedia.WeMediaApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
 
import static org.junit.Assert.*;
 
 
@SpringBootTest(classes = WeMediaApplication.class)
@RunWith(SpringRunner.class)
public class WmNewsAutoScanServiceTest {
 
    @Autowired
    private WmNewsAutoScanService wmNewsAutoScanService;
 
    @Test
    public void autoScanWmNews() {
 
        wmNewsAutoScanService.autoScanWmNews(6236);
    }
}