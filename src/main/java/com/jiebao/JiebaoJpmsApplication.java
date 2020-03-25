/**
 * jpms-v1.0
 * 2019年06月28日
 * @copyright 2019 www.jbx.cn 湖南捷报信息技术有限公司 All rights reserved.
 * @author Kunkka
 */
package com.jiebao;

import com.central.common.annotation.EnableLoginArgResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableLoginArgResolver
@EnableDiscoveryClient
@EnableFeignClients
@EnableTransactionManagement
@SpringBootApplication
public class JiebaoJpmsApplication extends SpringBootServletInitializer {

	//重写配置方法
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(JiebaoJpmsApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(JiebaoJpmsApplication.class);
	}
}
