package com.qunar.liwei.weibo_crawler;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataAbout {
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String url = "jdbc:mysql://127.0.0.1:3306/sinaweibo?useUnicode=true&characterEncoding=UTF-8";
	private static final String user = "root";
	private static final String password = "ckart001753983";
	private static String lastedTime;
	
	private static Connection conn;
	
	static {
		  try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			System.err.println("数据库驱动不存在");
			e.printStackTrace();
		} catch (SQLException e) {
			System.err.println("建立连接失败");
			e.printStackTrace();
		}
	      
	}
	
	public static void insertMain(String name,String text,String type,
			String from, String time) {
        try {     
        	
         String sql ="insert into sinaweibo_main_user values(" 
        		 + "null , ?, ?, ?, ?, ?)";
         PreparedStatement preStm = conn.prepareStatement(sql);
         preStm.setString(1, name);
         preStm.setString(2, text);
         preStm.setString(3, type);
         preStm.setString(4, from);
         preStm.setString(5, time);
         preStm.executeUpdate();
         
        }catch(SQLException e) {
         e.printStackTrace();
        } catch(Exception e) {
         e.printStackTrace();
        } 
	}
	public static void insertSub(String name,String text,String type,
			String from, String time, String mainName) {
        try {     
        	
         String sql ="insert into sinaweibo_sub_user values(" 
        		 + "null , ?, ?, ?, ?, ?, ?)";
         PreparedStatement preStm = conn.prepareStatement(sql);
         preStm.setString(1, name);
         preStm.setString(2, text);
         preStm.setString(3, type);
         preStm.setString(4, from);
         preStm.setString(5, time);
         preStm.setString(6, mainName);
         preStm.executeUpdate();
         
        }catch(SQLException e) {
         e.printStackTrace();
        } catch(Exception e) {
         e.printStackTrace();
        } 
	}
	
	public static void saveImageUrl(String url) {
		try {
			String sql = "insert into sinaweibo_image values(null, ?)";
			PreparedStatement stm = conn.prepareStatement(sql);
			stm.setString(1, url);
			stm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static String getMaxTime() {
		Statement stam = null;
		ResultSet rs = null;
		try {
			if (lastedTime != null)
				return lastedTime;
			stam = conn.createStatement();
			rs = stam.executeQuery("SELECT MAX(time) FROM sinaweibo.sinaweibo_main_user");
			if(rs.next()) {				
				lastedTime = rs.getString(1);
				return lastedTime;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {		
			try {
				if (rs != null)
					rs.close();
				if (stam != null)
					stam.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}
		throw new RuntimeException("Can't get lasted time");
	}
	
	public static String getMinTime() {
		Statement stam = null;
		ResultSet rs = null;
		try {
			if (lastedTime != null)
				return lastedTime;
			stam = conn.createStatement();
			rs = stam.executeQuery("SELECT MIN(time) FROM sinaweibo.sinaweibo_main_user");
			if(rs.next()) {				
				lastedTime = rs.getString(1);
				return lastedTime;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {		
			try {
				if (rs != null)
					rs.close();
				if (stam != null)
					stam.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}
		throw new RuntimeException("Can't get lasted time");
	}
	
	public static void main(String[] args) {
		
	}
}
