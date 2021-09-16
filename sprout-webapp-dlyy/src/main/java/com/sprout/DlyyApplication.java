package com.sprout;

import com.sprout.core.jpa.repository.SimpleBaseRepository;
import com.sprout.core.spring.SpringContextUtils;
import com.sprout.dlyy.student.QuestionType;
import com.sprout.dlyy.student.service.PaperService;
import com.sprout.dlyy.student.service.QuestionService;
import com.sprout.system.entity.User;
import com.sprout.system.service.UserService;
import com.sprout.system.utils.Status;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
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
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

@EnableCaching
@EnableScheduling
@EnableJpaRepositories(repositoryBaseClass = SimpleBaseRepository.class)
@EnableTransactionManagement(proxyTargetClass = true)
@SpringBootApplication
public class DlyyApplication {

	public static void main(String[] args) {
		SpringApplication.run(DlyyApplication.class, args);
		/*try {
			WebScrawUtils.batchAdd();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		/*UserService userService = SpringContextUtils.getBean(UserService.class);
		User u = new User();
		u.setLoginName("admin");
		u.setPassword(User.DEFAULT_PASSWORD);
		u.setName("系统管理员");
		u.setStatus(Status.ENABLE);
		try {
			userService.saveOrUpdate(u);
		} catch (Exception e) {
			e.printStackTrace();
		}*/

		//SpringContextUtils.getBean(QuestionService.class).generateMathQuestion(5000, QuestionType.Minus);
		//SpringContextUtils.getBean(PaperService.class).generatePaper(60, 40, true, "d:\\口算带答案.doc");
		//SpringContextUtils.getBean(PaperService.class).generatePaper(60, 40, false, "d:\\口算不带答案.doc");
	}

	@Bean
	MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name}") String applicationName) {
		return (registry) -> registry.config().commonTags("application", applicationName);
	}
}
