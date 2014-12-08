package com.qunar.liwei.weibo_crawler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;

import com.qunar.liwei.weibo_crawler.util.FileUtils;

public class FileUtilsTest {
	@Test
	public void testFileUtils(){
		List<File> fileList = new ArrayList<>();
		FileUtils.listAllFile(new File("./src/resource/data"), fileList);
		assertThat(fileList.size(), is(2));
	}
	@Test
	public void storeImage() {
		
	}
}
