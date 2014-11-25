package com.qunar.liwei.weibo_crawler;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 柱状图
 *
 * @author lazy_p
 * @date 2010-3-20
 */
public class DrawHistogram {
    private final int histogramWidth = 15;// 柱形图的宽度
    private final int histogramPitch = 10;// 柱形图的间距
    private float scaling = 1f;// 缩放的比例
    private int maxStrWidth = 0; // 字符串需要的最大宽度

    /**
     * <pre>
     *   参数b[i]和str[i]必须对应
     * </pre>

     */
    public BufferedImage paintPlaneHistogram(String title, double[] v,
            String[] str, Color[] color) {
        int width = str.length * histogramWidth+str.length*histogramPitch+100;
        int height = 500;
        scaling = calculateScale(v, height);//计算缩放比例
        
        BufferedImage bufferImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferImage.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        FontMetrics metrics = null;

        g.setFont(new Font(null, Font.BOLD, 30));
        g.setColor(Color.RED);

        g.drawString(title, (bufferImage.getWidth() - g.getFontMetrics()
                .stringWidth(title)) >> 1, 30);// 画标题

        g.setFont(new Font(null, Font.PLAIN, 12));

        metrics = g.getFontMetrics();

        g.setColor(Color.BLACK);

        g.drawLine(70, 0, 70, height - 15); // 画Y坐标
        g.drawLine(70, height - 15, width, height - 15);// 画X坐标
        
        int j = 0;
        int colorCount=color.length;
        
        for (int i = 0; i < v.length; ++i) {

            if (color != null){
                g.setColor(color[j]);// 设置前景色
                if(j+1<colorCount){
                    j++;
                }else{
                    j=0;
                }
            }else{
                g.setColor(Color.RED);
            }

            int x = 80 + i
                    * (histogramPitch + histogramWidth + (maxStrWidth >> 1));// 计算出X坐标
            int y = height - 16 - (int) (v[i] * scaling); // 计算出Y坐标

            // 画占的比例
//            g.drawString(v[i] + "", x
//                    - ((metrics.stringWidth(v[i] + "") - histogramWidth) >> 1),
//                    y);

            // 画平面的柱状图
            g.drawRect(x, y, histogramWidth, (int) (v[i] * scaling));
            g.fillRect(x, y, histogramWidth, (int) (v[i] * scaling));

            // 画每一项表示的东西
            g.drawString(str[i], x
                    - ((metrics.stringWidth(str[i]) - histogramWidth) >> 1),
                    height - 2);
        }
        for (int yIndex = 0; yIndex <= 500; yIndex += 50){
        	g.setFont(new Font(null, Font.LAYOUT_NO_LIMIT_CONTEXT, 10));
            g.drawString(new Float(yIndex/scaling).toString(), 0, 500-yIndex);
        }

        return bufferImage;
    }
    
    /**
     * 计算缩放比例
     * @param v
     * @param h 图片的高度
     * @return
     */
    public float calculateScale(double[] v , int h){
        float scale = 1f;
        double max = Float.MIN_VALUE;
        for(int i=0 , len=v.length ; i < len ;++i){
            if(v[i]>max){
                max=v[i];
            }
        }
        scale=((int)(h*1.0f/max*1000))*1.0f/1000;
        return scale * 0.8f;
    }
	    
    public static void main(String[] args)  {
//        DrawHistogram planeHistogram = new DrawHistogram();
//        
//        BufferedImage image = planeHistogram.paintPlaneHistogram("颜色直方图",
//                        new int[]{100,200,300}, new String[]{"a" , "b" , "c"} ,  new Color[] {Color.BLUE });
//        File output = new File("F:\\code\\The-graduation-design\\image\\1.jpg");
//        try {
//            ImageIO.write(image, "jpg", output);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
