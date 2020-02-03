package com.apksign.tool.sign;

import java.io.File;

import com.apksign.tool.AConstant;
import com.apksign.tool.utils.ALog;
import com.apksign.tool.utils.AUtils;
import com.apksign.tool.utils.CmdUtil;

/**
 * Created by chenzhi on 2020年1月6日
 */
public class SignV2 extends Sign {

	public SignV2(String keyStoreName, String keyStoreAlais, String keyStorePwd) {
		super(keyStoreName, keyStoreAlais, keyStorePwd);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(boolean isZipAlign, String unV2SignApkPath) {
		String cmd = CmdUtil.CMD_SIGN_V2_PACKAGE;
		
		String libsPath = AConstant.LIBS + File.separator;
		String v2SignApkName = AUtils.getsignApkFileName(unV2SignApkPath) + "_v2_sign.apk";
		String v2SignApkPath = AUtils.getSignApkPath(unV2SignApkPath) + v2SignApkName;
		
		String tmpZipApkName = AUtils.getsignApkFileName(v2SignApkPath) + "_align.apk";
		String tmpZipApkPath = AUtils.getSignApkPath(v2SignApkPath) + tmpZipApkName;
		
		try {
			// 在这里加入对齐操作
			if (isZipAlign) {
				// 对齐
				zipAlign(unV2SignApkPath, tmpZipApkPath);
				// 重新赋值
				unV2SignApkPath = tmpZipApkPath;
				v2SignApkName = AUtils.getsignApkFileName(v2SignApkPath) + "_align.apk";
				v2SignApkPath = AUtils.getSignApkPath(unV2SignApkPath) + v2SignApkName;
			}
			
			String v2SignCmd = String.format(cmd, libsPath, keyStoreName, keyStoreAlais, keyStorePwd, v2SignApkPath, unV2SignApkPath);
			
			printLog("开始签名：" + v2SignApkName + ", 签名类型：V2");
			CmdUtil.runCmd(v2SignCmd);
			printLog("签名结束：" + v2SignApkName + ", 签名类型：V2");

			if (new File(v2SignApkPath).exists()) {
				// 回调成功
				onSuccess();
			} else {
				onFailed("签名失败~(>_<)~ 请查看签名日志");
			}
		} catch (Exception e) {
			ALog.error(e.getMessage(), e);
			onFailed("签名失败~(>_<)~ " + e.getMessage());
		}
	}

	@Override
	public void setListener(SignListener listener) {
		cmdListener = listener;
	}
}
