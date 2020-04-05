package com.jt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;

@SpringBootApplication
@MapperScan("com.jt.mapper") //为mapper接口创建代理对象
//@DubboComponentScan("com.jt") 和yml中scan:
//basePackages: com.jt  #包扫描dubbo的service注解  作用一样只用其一
public class SpringBootRun {
	
	
	public static void main(String[] args) {
		
		SpringApplication.run(SpringBootRun.class, args);
	}

}