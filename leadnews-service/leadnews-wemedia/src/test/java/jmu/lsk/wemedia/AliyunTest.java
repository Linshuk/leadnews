package jmu.lsk.wemedia;

import jmu.lsk.common.aliyun.GreenImageScan;
import jmu.lsk.common.aliyun.GreenTextScan;
import jmu.lsk.file.service.FileStorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Map;
 
@SpringBootTest(classes = WeMediaApplication.class)
@RunWith(SpringRunner.class)
public class AliyunTest {
 
    @Autowired
    private GreenTextScan greenTextScan;
 
    @Autowired
    private GreenImageScan greenImageScan;
 
    @Autowired
    private FileStorageService fileStorageService;
 
    @Test
    public void testScanText() throws Exception {
        Map map = greenTextScan.greeTextScan("我是一个好人,冰毒");
        System.out.println(map);
    }
 
    @Test
    public void testScanImage() throws Exception {
        byte[] bytes = fileStorageService.downLoadFile("http://192.168.200.130:9000/leadnews/2021/04/26/ef3cbe458db249f7bd6fb4339e593e55.jpg");
        Map map = greenImageScan.imageScan(Arrays.asList(bytes));
        System.out.println(map);
    }
}