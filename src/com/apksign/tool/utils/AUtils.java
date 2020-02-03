package com.apksign.tool.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

/**
 * Created by chenzhi on 2019年12月20日
 */
public class AUtils {

	public static String getsignApkFileName(String fileName) {
		if (isEmpty(fileName))
			return "";
		
		int point = fileName.lastIndexOf('.');
		int ponit2 = fileName.lastIndexOf(File.separator);
		return fileName.substring(ponit2 + 1, point);
	}
	
	public static String getSignApkPath(String filePath) {
		if (isEmpty(filePath))
			return "";

		int point = filePath.lastIndexOf(File.separator);
		return filePath.substring(0, point + 1);
	}
	
	public static boolean isEmpty(String string) {
		return string == null || string.length() <= 0;
	}
	
	public static String getFileName4Path(String filePath) {
		if (isEmpty(filePath)) {
			return "";
		}
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1, filePath.length());
	}
	
	public static String getFileFormat(String fileName) {
		if (fileName == null || fileName.length() <= 0)
			return "";

		int point = fileName.lastIndexOf('.');
		return fileName.substring(point + 1);
	}
	
	public static boolean deleteFile(File newPath) {
		boolean status;

		if (newPath.isFile() && newPath.exists()) {
			try {
				newPath.delete();
				status = true;
			} catch (SecurityException se) {
				se.printStackTrace();
				status = false;
			}
		} else
			status = false;
		
		return status;
	}
	
	public static void showTipDialog(String msg, String title) {
		JOptionPane.showMessageDialog(null, msg, title, JOptionPane.WARNING_MESSAGE);
	}
	
	public static void showOkDialog(String msg, String title) {
		JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static String getCurrentDetailsData() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(new Date());
	}
}
