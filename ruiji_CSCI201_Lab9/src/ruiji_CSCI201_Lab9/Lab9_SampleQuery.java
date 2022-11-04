package ruiji_CSCI201_Lab9;

import java.sql.*;
import java.util.ArrayList;

public class Lab9_SampleQuery {
	public static void main(String args[]) {
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/sql_lab9?user=root&password=root");
			st = conn.createStatement();
			rs = st.executeQuery("select g.ClassName, COUNT(*) as NUMBER_OF_STUDENTS from grades as g Group By ClassName order by NUMBER_OF_STUDENTS;");
			ArrayList<String> ClassName = new ArrayList<String>();
			ArrayList<Integer> N_OF_Students = new ArrayList<Integer>();
		
			while(rs.next()) {
				String Classname = rs.getString("ClassName");
				int N_of_s = Integer.parseInt(rs.getString("NUMBER_OF_STUDENTS"));   
				System.out.println(Classname + " " + N_of_s);
				ClassName.add(Classname);
				N_OF_Students.add(N_of_s);
			}
			
			ArrayList<String> ClassName2 = new ArrayList<String>();
			ArrayList<String> Name2 = new ArrayList<String>();
			ArrayList<String> grade2 = new ArrayList<String>();
			rs2 = st.executeQuery("Select g.ClassName, si.Name, g.grade as Grade from grades as g, studentinfo as si where si.sid = g.sid order by Name");
			while(rs2.next()) {
				String Classname = rs2.getString("ClassName");
				String N_of_s = rs2.getString("Name");
				String g_of_s = rs2.getString("grade");
				
				System.out.println(Classname + " " + N_of_s + " " + g_of_s);
				ClassName2.add(Classname);
				Name2.add(N_of_s);
				grade2.add(g_of_s);
			}
			
			
			
			
		}catch (SQLException sqle) {
			System.out.println (sqle.getMessage());
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if(rs2 != null) {
					rs2.close();
				}
				if(st != null) {
					st.close();
				}
				if(conn != null) {
					conn.close();
				}
			}catch (SQLException sqle) {
				 System.out.println(sqle.getMessage());
			}	
		}
	}
}
