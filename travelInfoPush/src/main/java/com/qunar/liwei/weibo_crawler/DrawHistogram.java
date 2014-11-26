package com.qunar.liwei.weibo_crawler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;


public class DrawHistogram {
	
	public static <T extends Number> void createHistogram(
			 List<List<String>> words, List<List<T>> nums, List<String> title) {
        int size = title.size();
        for (int titleIndex = 0;titleIndex < size; titleIndex++) {
        	DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        	String titleStr = title.get(titleIndex);
        	List<String> word = words.get(titleIndex);
        	List<T> num = nums.get(titleIndex);
        	for (int wordIndex = 0; wordIndex < word.size(); wordIndex++) {
        		dataset.addValue(num.get(wordIndex), "", word.get(wordIndex));    
        	}
        	JFreeChart chart = ChartFactory.createBarChart(titleStr,// 标题
                     "(关键词) ",// x轴
                     "(词频/词数)",// y轴
                     dataset,// 数据
                     PlotOrientation.VERTICAL,// 定位，VERTICAL：垂直
                     false,// 是否显示图例注释(对于简单的柱状图必须是false)
                     false,// 是否生成工具//没用过
                     false);// 是否生成URL链接
            CategoryPlot plot = (CategoryPlot) chart.getPlot();
            CategoryItemRenderer renderer = plot.getRenderer();
            renderer
                  .setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
            // renderer.setDrawOutlines(true);//是否折线数据点根据不同数据使用不同的形状
            // renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesItemLabelsVisible(0, Boolean.TRUE);
            CategoryAxis domainAxis = plot.getDomainAxis();
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);// 倾斜45度
            BarRenderer renderer1 = new BarRenderer();// 设置柱子的相关属性
            // 设置柱子宽度
            // renderer1.setMaximumBarWidth(0.9);
            // renderer1.setMaximumBarWidth(0.10000000000000001D); //宽度
            // 设置柱子高度
            renderer1.setMinimumBarLength(0.5);
            // 设置柱子边框颜色
            // renderer1.setBaseOutlinePaint(Color.BLACK);
            // 设置柱子边框可见
            // renderer1.setDrawBarOutline(true);
            // 设置每个地区所包含的平行柱的之间距离，数值越大则间隔越大，图片大小一定的情况下会影响柱子的宽度，可以为负数
            renderer1.setItemMargin(0.1);
            // 是否显示阴影
           // renderer1.setShadowVisible(false);
            // 阴影颜色
            // renderer1.setShadowPaint(Color.white);
            plot.setRenderer(renderer1);
            plot.setBackgroundAlpha((float) 0.5); // 数据区的背景透明度（0.0～1.0）
            // 设置柱的透明度
            // plot.setForegroundAlpha(1.0f);
            // 设置图形的宽度
          //  CategoryAxis caxis = plot.getDomainAxis();
            //输出文件  
          FileOutputStream fos;
	      String graphURL = "/home/liwei/javaCode/The-graduation-design/travelInfoPush/image/"+titleStr+".jpg";
	      try {
	       fos = new FileOutputStream(graphURL);
	             //用ChartUtilities工具输出  
	       ChartUtilities.writeChartAsJPEG(fos, chart, 800, 400);
	             fos.close();  
	      } catch (FileNotFoundException e) {
	       e.printStackTrace();
	      }   catch (IOException e) {
	       e.printStackTrace();
	      }
        }       
	}

 }

