package com.apksign.tool.utils;

/**
 * Created by chenzhi on 2019年12月19日
 */
public class ALog {

	public static void error(String msg) {
		System.err.println("------" + msg + "------");
	}

	public static void error(String msg, Throwable throwable) {
		System.err.println("------" + msg + "------");
		throwable.printStackTrace();
	}

	public static void debug(String msg) {
		System.out.println("------" + msg + "------");
	}

	public static void debug(String msg, Throwable throwable) {
		System.out.println("------" + msg + "------");
		throwable.printStackTrace();
	}
	
}
