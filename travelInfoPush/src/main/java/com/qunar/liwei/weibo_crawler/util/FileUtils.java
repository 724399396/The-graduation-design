package com.qunar.liwei.weibo_crawler.util;

import java.io.File;
import java.util.List;

public class FileUtils {
	public static void listAllFile(File file, List<File> list) {
		if (file.isDirectory()) {
			for (File f : file.listFiles())
				listAllFile(f, list);
		} else
			list.add(file);
	}
}
