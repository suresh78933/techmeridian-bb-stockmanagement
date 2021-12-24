package com.techmeridian.stockmanagement.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan({ "com.techmeridian.stockmanagement" })
@EnableAutoConfiguration
@EnableJpaRepositories({ "com.techmeridian.stockmanagement.warehouse", "com.techmeridian.stockmanagement.journal",
		"com.techmeridian.stockmanagement.user", "com.techmeridian.stockmanagement.role",
		"com.techmeridian.stockmanagement.common", "com.techmeridian.stockmanagement.item",
		"com.techmeridian.stockmanagement.purchaseorder", "com.techmeridian.stockmanagement.stockissue",
		"com.techmeridian.stockmanagement.uom", "com.techmeridian.stockmanagement.vendor",
		"com.techmeridian.stockmanagement.company" })
@EntityScan({ "com.techmeridian.stockmanagement.warehouse", "com.techmeridian.stockmanagement.journal",
		"com.techmeridian.stockmanagement.user", "com.techmeridian.stockmanagement.role",
		"com.techmeridian.stockmanagement.common", "com.techmeridian.stockmanagement.item",
		"com.techmeridian.stockmanagement.purchaseorder", "com.techmeridian.stockmanagement.stockissue",
		"com.techmeridian.stockmanagement.uom", "com.techmeridian.stockmanagement.nav",
		"com.techmeridian.stockmanagement.vendor", "com.techmeridian.stockmanagement.company" })
@Configuration
@PropertySource("classpath:nav.properties")
@EnableSwagger2
public class StarterApp extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(StarterApp.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(StarterApp.class, args);
	}

	@Bean
	public Docket postsApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("public-api").apiInfo(apiInfo()).select()
				.paths(postPaths()).build();
	}

	@SuppressWarnings("unchecked")
	private Predicate<String> postPaths() {
		return Predicates.or(PathSelectors.regex("/wr.*"), PathSelectors.regex("/journal.*"),
				PathSelectors.regex("/user.*"), PathSelectors.regex("/item.*"), PathSelectors.regex("/nav.*"),
				PathSelectors.regex("/po.*"), PathSelectors.regex("/si.*"), PathSelectors.regex("/company.*"));
	}

	@SuppressWarnings("deprecation")
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Stock Management API (2019-02-15)")
				.description("Stock Managment API services. This is process layer between view and NAV services.")
				.contact("info@techmeridian.in").license("TechMeridian Solutions Pvt Ltd")
				.licenseUrl("info@techmeridian.in").version("1.0").build();
	}
}
