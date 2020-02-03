package com.apksign.tool.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CmdUtil {
	
	// 签名校验网址
	private static String TSA = "-tsa http://timestamp.digicert.com/"; // -tsa http://timestamp.digicert.com/
	
	// 对APP包进行签名的CMD命令
	public static String CMD_SIGN_PACKAGE = "cmd.exe /C jarsigner -digestalg SHA1 -sigalg MD5withRSA " + TSA
			+ " -verbose -keystore " + "%s" + " -signedjar %s %s " + "%s" + " -storepass "
			+ "%s";

	// 对APP包进行签名的CMD命令
	public static String CMD_SIGN_V2_PACKAGE = "cmd.exe /C java -jar " + "%sapksigner.jar sign --ks "
			+ "%s" + " --ks-key-alias " + "%s" + " --ks-pass pass:" + "%s" + " --out %s %s";
	
	// 对APP包进行压缩对齐的CMD命令
	public static String CMD_ZIPALIGN_PACKAGE = "cmd.exe /C " + "%szipalign -v 4 %s %s";
	
	public static void runCmd(String cmd) {
		runCmd(cmd, "UTF-8", null);
	}

	public static void runCmd(String cmd, String format, ICmdExecCallback callback) {
		// ALog.debug(cmd);
		Runtime rt = Runtime.getRuntime();
		BufferedReader bufferedReader = null;
		InputStreamReader isr = null;
		
		try {
			Process p = rt.exec(cmd);
			// p.waitFor();
			isr = new InputStreamReader(p.getInputStream(), format);
			bufferedReader = new BufferedReader(isr);
			String msg = null;
			while ((msg = bufferedReader.readLine()) != null) {
				if (callback != null) {
					callback.onCallback(msg);
				}
			}
		} catch (Exception e) {
			ALog.error("执行cmd命令出错" + e.getMessage(), e);
		}
	}
	
	public interface ICmdExecCallback {
		void onCallback(String line);
	}
}