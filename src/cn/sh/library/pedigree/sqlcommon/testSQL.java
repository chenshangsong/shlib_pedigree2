package cn.sh.library.pedigree.sqlcommon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class testSQL {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Class.forName("virtuoso.jdbc4.Driver");
	     Connection conn = DriverManager.getConnection("jdbc:virtuoso://127.0.0.1:1111","dba","dba");
	       ResultSetMetaData meta;
	       Statement stmt;
	       ResultSet result;
	       int count;
	       stmt = conn.createStatement ();
	       result = stmt.executeQuery ("Select * from SYS_USERS");
	       meta = result.getMetaData ();
	       count = meta.getColumnCount ();
	       System.out.println(count);
	}

}
