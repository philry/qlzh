package com.sy.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyQuartzConfig {

	@Bean
	public JobDetail nettyjobDetail() {
		//指定job的名称和持久化保存任务
		return JobBuilder
				.newJob(NettyQuartz.class)
				.withIdentity("nettyQuartz")
				.storeDurably()
				.build();
	}
	
	@Bean
	public Trigger nettyTrigger() {
		/*SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule()
				.withIntervalInMinutes(1)	//定义时间周期
				.repeatForever();*/
		CronScheduleBuilder scheduleBuilder 
			= CronScheduleBuilder.cronSchedule("0 */1 * * * ?");
		return TriggerBuilder
				.newTrigger()
				.forJob(nettyjobDetail())
				.withIdentity("nettyQuartz")
				.withSchedule(scheduleBuilder).build();
	}
}
