package com.apksign.tool.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

/**
 * Created by chenzhi on 2018年3月21日
 */
public class ZipUtils {
	
	/** 复制文件 */
	public static void copyFileUsingJava7(String sourcePath, String destPath) throws IOException {
		File source = new File(sourcePath);
		File dest = new File(destPath);
		Files.deleteIfExists(dest.toPath());
		Files.copy(source.toPath(), dest.toPath());
	}

	public static boolean removeDirFromZipArchive(String file, String removeDir) throws ZipException {
		try {
			// 创建ZipFile并设置编码
			net.lingala.zip4j.core.ZipFile zipFile = new net.lingala.zip4j.core.ZipFile(file);
			zipFile.setFileNameCharset("utf-8");// 字符集根据环境更换
			removeDir += "/"; // !!!!!!!!!!!!!!!!!!!!!!
			// 遍历压缩文件中所有的FileHeader, 将指定删除目录下的子文件名保存起来
			@SuppressWarnings("unchecked")
			List<FileHeader> headersList = zipFile.getFileHeaders();
			ArrayList<String> removeHeaderNames = new ArrayList<String>();
			for (FileHeader subHeader : headersList) {
				String subHeaderName = subHeader.getFileName();
				if (subHeaderName.startsWith(removeDir) && !subHeaderName.equals(removeDir)) {
					removeHeaderNames.add(subHeaderName);
				}
			}
			// 遍历删除指定目录下的所有子文件(所有子文件删除完毕，该目录自动删除)
			for (String headerNameString : removeHeaderNames) {
				zipFile.removeFile(headerNameString);
			}
			return true;
		} catch (Exception e) {
			ALog.error(e.getMessage(), e);
		}
		return false;
	}
}
