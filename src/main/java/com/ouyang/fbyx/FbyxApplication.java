package com.ouyang.fbyx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan(basePackages = "com.ouyang.fbyx.mapper")
@ServletComponentScan   //启动器启动时，扫描本目录以及子目录带有的webservlet注解的
//public class FbyxApplication extends SpringBootServletInitializer{
public class FbyxApplication{

	public static void main(String[] args) {
		SpringApplication.run(FbyxApplication.class, args);
		//new SpringApplicationBuilder(FbyxApplication.class).run();
	}
	/*@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		//此处的Application.class为带有@SpringBootApplication注解的启动类
		return builder.sources(FbyxApplication.class);
	}*/

}
