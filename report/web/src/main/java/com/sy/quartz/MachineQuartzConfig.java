package com.sy.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MachineQuartzConfig {

	@Bean
	public JobDetail machinejobDetail() {

		return JobBuilder
				.newJob(MachineQuartz.class)
				.withIdentity("machineQuartz")
				.storeDurably()
				.build();
	}
	
	@Bean
	public Trigger machineTrigger() {
		/*SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule()
				.withIntervalInMinutes(1)
				.repeatForever();*/
		CronScheduleBuilder scheduleBuilder 

			= CronScheduleBuilder.cronSchedule("0 0 1 * * ?");
			//= CronScheduleBuilder.cronSchedule("0 */1 * * * ?");
		return TriggerBuilder
				.newTrigger()
				.forJob(machinejobDetail())
				.withIdentity("machineQuartz")
				.withSchedule(scheduleBuilder).build();
	}
}
