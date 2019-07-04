package fr.epita.quiz.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestDatabaseConnection {

public static void main(String[] args) throws SQLException {
		
		/* Operations we would like to test : 
		create table QUIZ(id bigint auto_increment, name varchar(255));

		select ID, NAME from QUIZ

		insert into QUIZ (name) values ('Test Quiz')
		UPDATE QUIZ SET NAME='Quiz Test 2' WHERE ID = 1;
		DELETE QUIZ  WHERE ID = 1;
		
		*/
		Connection connection = getConnection();
		
		
		//search(connection);
		
		//
		

	
		
		PreparedStatement pstmt = connection.prepareStatement("update QUIZ set NAME = ? where name=?");
		pstmt.setString(1,"Test");
		pstmt.setString(2, "Test Quiz");
		pstmt.execute();
		// TODO get the generated ID
//		ResultSet rs = pstmt.getGeneratedKeys();
//		int id = rs.getInt(1);
//		System.out.println("generated ID : " + id);
//		
		
//		rs.close();
		pstmt.close();
		connection.close();
		
		
		

	}

	private static void search(Connection connection) throws SQLException {
		String query = "select ID, NAME from QUIZ";
		
		PreparedStatement pstmt = connection.prepareStatement(query);
	
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			int id = rs.getInt("ID");
			String topic = rs.getString("NAME");
			System.out.println("id : " + id + " topic:" +  topic);
		}
		
		pstmt.close();
		rs.close();
		connection.close();
	}

	private static Connection getConnection() throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/./test2","test","");
		return connection;
	}

}
