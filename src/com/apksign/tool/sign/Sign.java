package com.apksign.tool.sign;

import java.io.File;

import com.apksign.tool.AConstant;
import com.apksign.tool.utils.CmdUtil;

/**
 * Created by chenzhi on 2020年1月6日
 */
public abstract class Sign {
	
	public SignListener cmdListener;
	public String keyStoreName = "";
	public String keyStoreAlais = "";
	public String keyStorePwd = "";
	
	public Sign(String keyStoreName, String keyStoreAlais, String keyStorePwd) {
		this.keyStoreName = keyStoreName;
		this.keyStoreAlais = keyStoreAlais;
		this.keyStorePwd = keyStorePwd;
	}

	public abstract void execute(boolean isZipAlign, String unSignApkPath);
	
	public abstract void setListener(SignListener listener);
	
	public void zipAlign(String unAlignApkPath, String alignApkPath) {
		String cmd = CmdUtil.CMD_ZIPALIGN_PACKAGE;
		
		String libsPath = AConstant.LIBS + File.separator;
		String zipAlignCmd = String.format(cmd, libsPath, unAlignApkPath, alignApkPath);
		
		printLog("开始对齐操作");
		CmdUtil.runCmd(zipAlignCmd, "GBK", new CmdUtil.ICmdExecCallback() {

			@Override
			public void onCallback(String line) {
				printLog(line);
			}
		});
		printLog("对齐操作结束");
	}
	
	public void printLog(String line) {
		if (cmdListener != null) {
			cmdListener.printSignLog(line);
		}
	}
	
	public void onSuccess(){
		if (cmdListener != null) {
			cmdListener.onSuccess();
		}
	}
	
	public void onFailed(String msg) {
		if (cmdListener != null) {
			cmdListener.onFialed(msg);
		}
	}
	
	// /////////////////////////////////////
	public static interface SignListener {
		void printSignLog(String line);
		
		void onSuccess();
		
		void onFialed(String msg);
	}
}
