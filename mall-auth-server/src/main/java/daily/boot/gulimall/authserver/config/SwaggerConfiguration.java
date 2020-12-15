package daily.boot.gulimall.authserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
//model使用了JSR-303验证，需要加这个注解，
//参考https://stackoverflow.com/questions/48850355/swagger-springfox-bean-validation-jsr-303-not-recognize
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {
    
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("daily.boot.gulimall"))
                .paths(PathSelectors.any())
                .build();
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("MEMBER-AUTH-API测试文档")
                .description("谷粒商城-AUTH授权认证项目的接口测试文档")
                .termsOfServiceUrl("http://xxx:16000")
                .version("1.0")
                .build();
    }
}
