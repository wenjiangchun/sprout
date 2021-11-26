package com.sprout;

import com.sprout.core.jpa.repository.SimpleBaseRepository;
import com.ververica.cdc.connectors.mysql.MySqlSource;
import com.ververica.cdc.connectors.postgres.PostgreSQLSource;
import com.ververica.cdc.debezium.DebeziumSourceFunction;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.core.fs.Path;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.runtime.state.filesystem.FsStateBackendFactory;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EnableCaching
@EnableScheduling
@EnableJpaRepositories(repositoryBaseClass = SimpleBaseRepository.class)
@EnableTransactionManagement(proxyTargetClass = true)
@SpringBootApplication
public class DlyyApplication {

	public static void main(String[] args) throws Exception {
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
		//SpringContextUtils.getBean(PaperService.class).generatePaper(60, 40, false, "d:\\口算不带答案.doc");tableList("mydb.products")

		/*ExecutorService threadPool = Executors.newFixedThreadPool(20);
		Runnable task1 = () -> {
			DebeziumSourceFunction<String> mySqlSource = MySqlSource.<String>builder().hostname("10.18.22.101")
					.port(3306).databaseList("mydb").tableList("mydb.*").username("root").password("123456").deserializer(new JsonDebeziumDeserializationSchema()).build();
			StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
			env.setParallelism(1);
			env.enableCheckpointing(5000L);
			env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, 2000L));
			//env.getCheckpointConfig().setCheckpointStorage("file:///D://aaa");
			env.setStateBackend(new FsStateBackend("file:///D://aaa"));
			env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
			//env.setDefaultSavepointDirectory("file:///D://aaa");
			env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
			env.addSource(mySqlSource).print();
			//env.fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), "Mysql Source").setParallelism(4).print().setParallelism(1);
			try {
				env.execute("Print Mysql + binlog");
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		Runnable task2 = () -> {
			DebeziumSourceFunction<String> mySqlSource = PostgreSQLSource.<String>builder().hostname("10.18.22.170")
					.database("dlyy").tableList("public.*").username("postgres").password("1234").deserializer(new JsonDebeziumDeserializationSchema()).build();
			StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
			env.setParallelism(1);
			env.enableCheckpointing(5000L);
			env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, 2000L));
			//env.getCheckpointConfig().setCheckpointStorage("file:///D://aaa");
			env.setStateBackend(new FsStateBackend("file:///D://bbb"));
			env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
			//env.setDefaultSavepointDirectory("file:///D://aaa");
			env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
			env.addSource(mySqlSource).print();
			//env.fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), "Mysql Source").setParallelism(4).print().setParallelism(1);
			try {
				env.execute("Print Postgres + binlog");
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		threadPool.submit(task1);
		threadPool.submit(task2);*/
	}

	@Bean
	MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name}") String applicationName) {
		return (registry) -> registry.config().commonTags("application", applicationName);
	}
}
