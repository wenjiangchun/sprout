package com.sprout;

import com.sprout.core.jpa.repository.SimpleBaseRepository;
import com.sprout.core.spring.SpringContextUtils;
import com.sprout.devops.util.ControlType;
import com.sprout.devops.util.OSType;
import com.sprout.dlyy.config.PlatformConfig;
import com.sprout.devops.server.entity.ServerHost;
import com.sprout.devops.server.service.ServerHostService;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableCaching
@EnableScheduling
@EnableJpaRepositories(repositoryBaseClass = SimpleBaseRepository.class)
@EnableTransactionManagement(proxyTargetClass = true)
@SpringBootApplication
public class DlyyUpsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DlyyUpsApplication.class, args);
		PlatformConfig platformConfig = SpringContextUtils.getBean(PlatformConfig.class);
		if (platformConfig.isGenExampleHost()) {
			int[] host = {109, 110, 111, 112, 113, 114, 116, 117, 118, 119, 120};
			for (int ht : host) {
				ServerHost serverHost = new ServerHost();
				serverHost.setControlType(ControlType.IPMI);
				serverHost.setIp("10.18.21." + ht);
				serverHost.setName("10.18.21." + ht);
				serverHost.setOs(OSType.centos);
				serverHost.setUserName("root");
				serverHost.setPassword("youedata.com");
				try {
					SpringContextUtils.getBean(ServerHostService.class).save(serverHost);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Bean
	MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name}") String applicationName) {
		return (registry) -> registry.config().commonTags("application", applicationName);
	}
}
