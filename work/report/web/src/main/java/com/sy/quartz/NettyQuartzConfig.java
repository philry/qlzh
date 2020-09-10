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
			= CronScheduleBuilder.cronSchedule("45 * * * * ? ");//nettyQuartz 每分钟的第45秒执行一次,原来的
	//		= CronScheduleBuilder.cronSchedule("45 1/1 * * * ?");//nettyQuartz 从第1分钟0秒开始执行，每1分钟执行一次,我改的
		return TriggerBuilder                                //从第1分钟30秒开始执行是为了获得底表数据包(采集器开机1分钟后才发包过来)
				.newTrigger()
				.forJob(nettyjobDetail())
				.withIdentity("nettyQuartz")
				.withSchedule(scheduleBuilder).build();
	}
}
