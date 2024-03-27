package jmu.lsk.common.springdoc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringdocConfig {

    @Bean
    public OpenAPI createApi(){
        return new OpenAPI().info(getInfo());
    }

    // 创建元数据
    private Info getInfo(){
        return new Info()
                // 标题
                .title("openApi test")
                // 简短描述
                .description("a test openApi")
                // 指向服务条款的URL地址，必须是URL地址格式
                .termsOfService("http://localhost:51801")
                // 公开的API的联系人信息
                .contact(createCon())
                // API文档的版本信息
                .version("0.1");
    }

    //http://localhost:51801/swagger-ui/index.html
    // 创建公开的API的联系人信息
    private Contact createCon(){
        return new Contact()
                .email("2287093864@qq.com")
                .name("LinSK")
                .url("https://github.com/Linshuk");
    }

}
