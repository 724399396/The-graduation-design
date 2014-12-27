package com.qunar.liwei.graduation.forwarding_analyze;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.junit.Test;

public class HtmlGetTest {
	@Test
	public void testSearch() throws FileNotFoundException{
		System.setOut(new PrintStream(new File("outPrint.txt")));
		System.out.println(HtmlGet.search("兵马俑", null, "hot"));
	}
}
