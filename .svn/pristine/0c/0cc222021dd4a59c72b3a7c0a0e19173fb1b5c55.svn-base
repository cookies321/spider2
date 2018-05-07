package cn.jj.controller;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

import cn.jj.quartz.TestJob;
import cn.jj.quartz.TestJob2;

/**
 * @Description: 调度器
 * @author 徐仁杰
 * @date 2017年11月30日 上午9:54:21
 */
public class SpiderScheduler {

	public static void main(String[] args) throws Exception {
		// 获取默认调度器
		Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
		// 开启调度器
		defaultScheduler.start();
		/********************添加第一个任务*******************/
		// 被调度的任务
		JobDetail jobDetail = new JobDetail("test-job", Scheduler.DEFAULT_GROUP, TestJob.class);
		// 定时执行任务
		CronTrigger cronTrigger = new CronTrigger("test-job", Scheduler.DEFAULT_GROUP, "0 03 10 * * ?");
		// 添加调度任务
		defaultScheduler.scheduleJob(jobDetail, cronTrigger);
		/********************添加第一个任务*******************/
		
		/********************添加第二个任务*******************/
		JobDetail jobDetail2 = new JobDetail("test-job2", Scheduler.DEFAULT_GROUP, TestJob2.class);
		CronTrigger cronTrigger2 = new CronTrigger("test-job2", Scheduler.DEFAULT_GROUP, "0 04 10 * * ?");
		defaultScheduler.scheduleJob(jobDetail2, cronTrigger2);
		/********************添加第二个任务*******************/
	}
}
