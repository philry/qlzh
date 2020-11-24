package com.sy.quartz;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyQuartzConfig {

	/*@Bean
	public JobDetail nettyjobDetail() {

		return JobBuilder
				.newJob(NettyQuartz.class)
				.withIdentity("nettyQuartz")
				.storeDurably()
				.build();
	}

	@Bean
	public Trigger nettyTrigger() {
		*//*SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule()
				.withIntervalInMinutes(1)
				.repeatForever();*//*
		CronScheduleBuilder scheduleBuilder
			= CronScheduleBuilder.cronSchedule("45 * * * * ? ");
	//		= CronScheduleBuilder.cronSchedule("45 1/1 * * * ?");
		return TriggerBuilder
				.newTrigger()
				.forJob(nettyjobDetail())
				.withIdentity("nettyQuartz")
				.withSchedule(scheduleBuilder).build();
	}*/


	@Bean
	public JobDetail nettyjobDetail() {

		return JobBuilder
				.newJob(NettyNewQuartz.class)
				.withIdentity("nettyNewQuartz")
				.storeDurably()
				.build();
	}

	@Bean
	public Trigger nettyTrigger() {
		SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule()
				.withIntervalInMinutes(1)
				.repeatForever();
		CronScheduleBuilder scheduleBuilder
				= CronScheduleBuilder.cronSchedule("45 * * * * ? ");
		//		= CronScheduleBuilder.cronSchedule("45 1/1 * * * ?");
		return TriggerBuilder
				.newTrigger()
				.forJob(nettyjobDetail())
				.withIdentity("nettyNewQuartz")
				.withSchedule(scheduleBuilder).build();
	}



	/*@Bean
	public JobDetail nettyjobDetail1() {

		return JobBuilder
				.newJob(OnAndOffCheckQuartz.class)
				.withIdentity("OnAndOffCheckQuartz")
				.storeDurably()
				.build();
	}

	@Bean
	public Trigger nettyTrigger1() {
		*//*SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule()
				.withIntervalInMinutes(1)
				.repeatForever();*//*
		CronScheduleBuilder scheduleBuilder
				= CronScheduleBuilder.cronSchedule("45 * * * * ? ");
		//		= CronScheduleBuilder.cronSchedule("45 1/1 * * * ?");
		return TriggerBuilder
				.newTrigger()
				.forJob(nettyjobDetail1())
				.withIdentity("OnAndOffCheckQuartz")
				.withSchedule(scheduleBuilder).build();
	}*/
}
