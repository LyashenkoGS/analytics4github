package com.rhcloud.analytics4github.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * @author Grigoriy Lyashenko (Grog).
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //a workaround to correctly display LocalDate type
                .directModelSubstitute(LocalDate.class, java.sql.Date.class)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(analyticsPaths())
                .build();
    }

    private Predicate<String> analyticsPaths() {
        return or(
                regex(".*/commits.*"),
                regex(".*/stargazers.*"),
                regex(".*/uniqueContributors.*"),
                regex("/statistics.*")
        );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("analytics4github API")
                .description("A public REST API to perform analytics on GitHub repositories")
                .license("MIT")
                .licenseUrl("https://github.com/LyashenkoGS/analytics4github/blob/master/LICENSE")
                .version("1.0")
                .build();
    }

    @Bean
    UiConfiguration uiConfig() {
        return new UiConfiguration(
                "",
                "list",
                "alpha",
                "schema",
                true,
                true
        );
    }
}