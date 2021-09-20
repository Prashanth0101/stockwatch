package com.stock.price.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.StringVendorExtension;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableWebMvc
@EnableSwagger2
@ComponentScan("com.stock.price.*")
public class SwaggerConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}

	@Bean
	public Docket api() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).pathMapping("/")
				.apiInfo(ApiInfo.DEFAULT).forCodeGeneration(true).useDefaultResponseMessages(false);
		docket = docket.select().apis(RequestHandlerSelectors.basePackage("com.stock.price.controller"))
				.paths(PathSelectors.any()).build().apiInfo(apiInfo()).enable(true);
		return docket;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

	}

	private ApiInfo apiInfo() {
		VendorExtension vendorExtension = new StringVendorExtension("", "");
		ApiInfo apiInfo = new ApiInfo("Organization Rlease Information", "Organization Rlease Application", "v1", null,
				new Contact("Test", "", "test@gmail.com"), "API License", "API License URL",
				Arrays.asList(vendorExtension));
		return apiInfo;
	}
}
