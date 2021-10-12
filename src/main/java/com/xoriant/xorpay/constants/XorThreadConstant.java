package com.xoriant.xorpay.constants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class XorThreadConstant {

	public static ExecutorService batchPymentProcess = Executors.newFixedThreadPool(30);
	public static ExecutorService batchExecutor = Executors.newFixedThreadPool(10);
	public static ExecutorService PymentProcess = Executors.newFixedThreadPool(1);
	public static ExecutorService paymentExecutor = Executors.newFixedThreadPool(30);
	public static ExecutorService staticsExecutor = Executors.newFixedThreadPool(100);
	public static ScheduledExecutorService updateStatusExecutor = Executors.newScheduledThreadPool(1);
	public static ScheduledExecutorService dashBoardexecutor = Executors.newScheduledThreadPool(1);

}
