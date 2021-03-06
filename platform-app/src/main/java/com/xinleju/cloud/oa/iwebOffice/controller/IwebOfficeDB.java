package com.xinleju.cloud.oa.iwebOffice.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class IwebOfficeDB {
	  public String ClassString=null;
	  public String ConnectionString=null;
	  public String UserName=null;
	  public String PassWord=null;

	  public Connection Conn;
	  public Statement Stmt;


	  public IwebOfficeDB() {
	    ClassString = "org.gjt.mm.mysql.Driver";
	    ConnectionString = "jdbc:mysql://localhost:3306/xyoatest?relaxAutoCommit=true";
	    UserName="root";
	    PassWord="123456";
	  }
	  public boolean OpenConnection()
	  {
	   boolean mResult=true;
	   try
	   {
	     Class.forName(ClassString);
	     if ((UserName==null) && (PassWord==null))
	     {
	       Conn= DriverManager.getConnection(ConnectionString);
	     }
	     else
	     {
	       Conn= DriverManager.getConnection(ConnectionString,UserName,PassWord);
	     }

	     Stmt=Conn.createStatement();
	     mResult=true;
	   }
	   catch(Exception e)
	   {
	     System.out.println(e.toString());
	     mResult=false;
	   }
	   return (mResult);
	  }

	  //关闭数据库连接
	  public void CloseConnection()
	  {
	   try
	   {
	     Stmt.close();
	     Conn.close();
	   }
	   catch(Exception e)
	   {
	     System.out.println(e.toString());
	   }
	  }
	  public String GetDateTime()
	  {
	   Calendar cal  = Calendar.getInstance();
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String mDateTime=formatter.format(cal.getTime());
	   return (mDateTime);
	  }

	  public  java.sql.Date  GetDate()
	  {
	    java.sql.Date mDate;
	    Calendar cal  = Calendar.getInstance();
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    String mDateTime=formatter.format(cal.getTime());
	    return (java.sql.Date.valueOf(mDateTime));
	  }

	  public int GetMaxID(String vTableName,String vFieldName)
	  {
	   int mResult=0;
	   boolean mConn=true;
	   String mSql=new String();
	   mSql = "select max("+vFieldName+")+1 as MaxID from " + vTableName;
	   try
	   {
	       if (Conn!=null){
	           mConn=Conn.isClosed();
	       }
	       if (mConn){
	         OpenConnection();
	       }

	       ResultSet result=ExecuteQuery(mSql);
	       if (result.next())
	       {
	         mResult=result.getInt("MaxID");
	       }
	       result.close();

	       if (mConn)
	       {
	         CloseConnection();
	       }

	     }
	     catch(Exception e)
	     {
	       System.out.println(e.toString());
	   }
	   return (mResult);
	 }

	  public ResultSet ExecuteQuery(String SqlString)
	  {
	    ResultSet result=null;
	    try
	    {
	      result=Stmt.executeQuery(SqlString);
	    }
	    catch(Exception e)
	    {
	      System.out.println(e.toString());
	    }
	    return (result);
	  }

	  public int ExecuteUpdate(String SqlString)
	  {
	    int result=0;
	    try
	    {
	      result=Stmt.executeUpdate(SqlString);
	    }
	    catch(Exception e)
	    {
	      System.out.println(e.toString());
	    }
	    return (result);
	  }

	  /**
	   * 功能或作用：获取当前服务器日期（字符型）
	   * @return 当前日期（字符型）
	   */
	  public String GetDateAsStr()
	  {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    String mDateTime = formatter.format(cal.getTime());
	    return (mDateTime);
	  }
}
