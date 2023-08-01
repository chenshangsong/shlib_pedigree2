// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SQLDriver.java

package cn.sh.library.pedigree.fullContentLink;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class ZyddSQLDriver {

	public Connection conActive;
	private Statement stmtActive;
	public ResultSet rsQuery;
	public boolean bHasResult;
	private String url;
	private String classforname;

	public ZyddSQLDriver() {
		url = "";
		classforname = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		try {
			//https://docs.microsoft.com/zh-cn/sql/connect/jdbc/connecting-with-ssl-encryption?view=sql-server-2017
			//当 encrypt 属性设置为 true 且 trustServerCertificate 属性设置为 true 时，Microsoft JDBC Driver for SQL Server 将不验证SQL Server TLS 证书。 此设置常用于允许在测试环境中建立连接，如 SQL Server 实例只有自签名证书的情况
			
			Class.forName(classforname);
			url = (new StringBuilder("jdbc:sqlserver://")).append(
					"10.1.20.60:1433").append(";databaseName=").append(
					"zydd").append(";user=").append(
					"zydd").append(";password=").append(
					"sysnetzydd").append(";encrypt=true;trustServerCertificate=true").toString();
			conActive = DriverManager.getConnection(url);
			//System.out.println("OK");
			bHasResult = false;
		} catch (ClassNotFoundException E) {
			System.out.println("zydd获取全文错误："+E.getMessage());
			
		} catch (SQLException E) {
			System.out.println("zydd获取全文错误："+E.getMessage());
			
		}
	}

	public void Disconnect() {
		try {
			conActive.close();
		} catch (SQLException E) {
			return;
		}
	}

	public void OpenStatement() {
		try {
			stmtActive = conActive.createStatement();
			bHasResult = false;
		} catch (SQLException E) {
			bHasResult = false;
		}
	}

	public void CloseStatement() {
		try {
			stmtActive.close();
			bHasResult = false;
		} catch (SQLException E) {
			bHasResult = false;
		}
	}

	public void ExecuteSQL(String sSQL, int iInfo) {
		try {
			if (iInfo == 0) {
				rsQuery = stmtActive.executeQuery(sSQL);
				if (rsQuery.next())
					bHasResult = true;
				else
					bHasResult = false;
			}
			if (iInfo == 1) {
				stmtActive.executeUpdate(sSQL);
				bHasResult = false;
			}
		} catch (SQLException E) {
			System.out.println("获取全文错误："+E.getMessage());
			E.printStackTrace();
			System.out.println((new StringBuilder("异常")).append(sSQL)
					.toString());
			bHasResult = false;
		}
	}

	public boolean ExecuteUpdateSQL(String sSQL) {
		boolean flag = true;
		try {
			stmtActive.executeUpdate(sSQL);
		} catch (SQLException E) {
			System.out.println("获取全文错误："+E.getMessage());
			flag = false;
		}
		return flag;
	}

	public String FieldByName(String sFieldName) {
		if (rsQuery == null)
			return "";
		String sValue;
		try {
			sValue = rsQuery.getString(sFieldName);
		} catch (SQLException E) {
			E.printStackTrace();
			System.out.println("获取全文错误："+E.getMessage());
			sValue = "";
		}
		if (sValue == null)
			sValue = "";
		sValue = sValue.trim();
		return sValue;
	}

	public String FieldByIndex(int iIndex) {
		if (rsQuery == null)
			return "";
		String sValue;
		try {
			sValue = rsQuery.getString(iIndex);
		} catch (SQLException E) {
			System.out.println("获取全文错误："+E.getMessage());
			sValue = "";
		}
		if (sValue == null)
			sValue = "";
		sValue = sValue.trim();
		return sValue;
	}

	public int IntegerByName(String sFieldName) {
		String sValue = FieldByName(sFieldName);
		if (sValue.equals(""))
			return 0;
		else
			return Integer.parseInt(sValue);
	}

	public int IntegerByIndex(int iIndex) {
		String sValue = FieldByIndex(iIndex);
		if (sValue.equals(""))
			return 0;
		else
			return Integer.parseInt(sValue);
	}

	public Timestamp DateTimeByName(String sFieldName) {
		if (rsQuery == null)
			return null;
		Timestamp tsValue;
		try {
			tsValue = rsQuery.getTimestamp(sFieldName);
		} catch (SQLException E) {
			System.out.println("获取全文错误："+E.getMessage());
			tsValue = null;
		}
		return tsValue;
	}

	public Timestamp DateTimeByIndex(int iIndex) {
		if (rsQuery == null)
			return null;
		Timestamp tsValue;
		try {
			tsValue = rsQuery.getTimestamp(iIndex);
		} catch (SQLException E) {
			tsValue = null;
		}
		return tsValue;
	}

	public boolean Next() {
		try {
			bHasResult = rsQuery.next();
			return bHasResult;
		} catch (SQLException E) {
			return false;
		}
	}

	public String ImageByName(String sFieldName) {
		if (rsQuery == null)
			return null;
		String ImValue;
		try {
			ImValue = new String(rsQuery.getBytes(sFieldName), "UTF-8");
		} catch (Exception E) {
			ImValue = null;
		}
		return ImValue;
	}

	public int getResultNum() throws SQLException {
		if (!bHasResult)
			return 0;
		int i;
		for (i = 1; rsQuery.next(); i++)
			;
		return i;
	}
	//ִ��sql��䣬����ֵ������----by guzhidi----------------
	public List<Map<String, String>> ExecuteSQL(String sSQL, List<String> keys) {
		try {
			
			Statement stmtActiveTmp = conActive.createStatement();
			ResultSet resultSet = stmtActiveTmp.executeQuery(sSQL);
			List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
			while (resultSet.next()) {
				Map<String, String> keyValues = new HashMap<String, String>();
				for (String key : keys) {
					String value = FieldByName(key, resultSet);
					keyValues.put(key, value);
				}
				datas.add(keyValues);
			}
			
			stmtActiveTmp.close();
			return datas;
		} catch (SQLException E) {
			System.out.println("获取全文错误："+E.getMessage());
			System.out.println((new StringBuilder("error sql")).append(sSQL)
					.toString());
			return null;
		}
	}
	public String FieldByName(String sFieldName, ResultSet rsQuery2) {
		String sValue;
		try {
			sValue = rsQuery2.getString(sFieldName);
		} catch (SQLException E) {
			sValue = "";
		}
		if (sValue == null)
			sValue = "";
		sValue = sValue.trim();
		return sValue;
	}
}
