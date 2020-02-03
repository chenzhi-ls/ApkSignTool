package com.apksign.tool.sign;

import java.io.File;

import com.apksign.tool.utils.ALog;
import com.apksign.tool.utils.AUtils;
import com.apksign.tool.utils.CmdUtil;

/**
 * Created by chenzhi on 2020年1月6日
 */
public class SignV1 extends Sign{

	public SignV1(String keyStoreName, String keyStoreAlais, String keyStorePwd) {
		super(keyStoreName, keyStoreAlais, keyStorePwd);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(boolean isZipAlign, String unSignApkPath) {
		String cmd = CmdUtil.CMD_SIGN_PACKAGE;

		String signApkName = AUtils.getsignApkFileName(unSignApkPath) + "_v1_sign.apk";
		String signApkPath = AUtils.getSignApkPath(unSignApkPath) + signApkName;

		String tmpZipApkName = AUtils.getsignApkFileName(signApkPath) + "_align.apk";
		String tmpZipApkPath = AUtils.getSignApkPath(signApkPath) + tmpZipApkName;
		
		String signCmd = String.format(cmd, keyStoreName, signApkPath, unSignApkPath, keyStoreAlais, keyStorePwd);
		
		try {
			printLog("开始签名：" + signApkName + ", 签名类型：V1");
			//
			CmdUtil.runCmd(signCmd, "GBK", new CmdUtil.ICmdExecCallback() {

				@Override
				public void onCallback(String line) {
					printLog(line);
				}
			});
			printLog("签名结束：" + signApkName + ", 签名类型：V1");
			
			if (!new File(signApkPath).exists()) {
				onFailed("签名失败~(>_<)~ 请查看签名日志");
				return;
			}
			
			// 在这里加入对齐操作
			if (isZipAlign) {
				// 签名对齐
				zipAlign(signApkPath, tmpZipApkPath);
				// 删除原来没有对齐的apk
				AUtils.deleteFile(new File(signApkPath));
			}
			
			// 回调成功
			onSuccess();
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
