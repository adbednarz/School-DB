package schoolProjectDB;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnector{
	
	private String user = "root";
	private String password = "--------"; //TODO insert your password
	private String query;
	private int i;
	
	public DatabaseConnector(String user, String password) throws Exception {
		this.user = user;
		this.password = password;
		Class.forName("com.mysql.cj.jdbc.Driver"); // sprawdza czy uzytkownik o podanych danych da rade nawiazac polaczenie
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/schoolDB", user, password);
		con.close();
	}
	
	public ArrayList<String> createConnection() {
		ArrayList<String> array = new ArrayList<String>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/schoolDB", user, password);
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(query);
			array = read(rs);
			con.close();
		}catch(Exception e) {
			System.out.println(e);
		}
		return array;
	}
	
	public String createConnectionPstmt(int number, ArrayList<String> values) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/schoolDB", user, password);
			PreparedStatement pstmt = con.prepareStatement(query);
			for(int i = 0; i < number; i++) {
				pstmt.setString(i + 1, values.get(i));
			}
			pstmt.executeUpdate();
			con.close();
			return "Zmieniono";
		}catch(Exception e) {
			System.out.println(e);
			return "Niepoprawna dana";
		}
	}
	
	public void createConnectionProc() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/schoolDB", user, password);
			String query = "{CALL endschoolyear()}";
			CallableStatement cs = con.prepareCall(query);
	        cs.execute();
			con.close();
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public ArrayList<String> createConnectionPstmtA(int number, ArrayList<String> values) {
		ArrayList<String> array = new ArrayList<String>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/schoolDB", user, password);
			PreparedStatement pstmt = con.prepareStatement(query);
			for(int i = 0; i < number; i++) {
				pstmt.setString(i + 1, values.get(i));
			}
			ResultSet rs = pstmt.executeQuery();
			array = read(rs);
			con.close();
		}catch(Exception e) {
			System.out.println(e);
		}
		return array;
	}
	
	public void createConnectionVoid() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/schoolDB", user, password);
			Statement stmt=con.createStatement();
			stmt.executeUpdate(query);
			con.close();
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public ArrayList<String> read(ResultSet rs) throws Exception {
		ArrayList<String> array = new ArrayList<String>();
		switch(i) {
			case 1:
				while(rs.next())  
					array.add(rs.getString(1));
				break;
			case 2:
				while(rs.next()) {
					array.add(rs.getString(1));
					array.add(rs.getString(2));
				}
				break;	
			case 3:
				while(rs.next()) {
					array.add(rs.getString(1));
					array.add(rs.getString(2));
					array.add(rs.getString(3));
				}
				break;
			case 4:
				while(rs.next()) {
					array.add(rs.getString(1));
					array.add(rs.getString(2));
					array.add(rs.getString(3));
					array.add(rs.getString(4));
				}
				break;
			case 5:
				while(rs.next()) {
					array.add(rs.getString(1));
					array.add(rs.getString(2));
					array.add(rs.getString(3));
					array.add(rs.getString(4));
					array.add(rs.getString(5));
				}
				break;
			case 12:
				while(rs.next()) {
					array.add(rs.getString(1));
					array.add(rs.getString(2));
					array.add(rs.getString(3));
					array.add(rs.getString(4));
					array.add(rs.getString(5));
					array.add(rs.getString(6));
					array.add(rs.getString(7));
					array.add(rs.getString(8));
					array.add(rs.getString(9));
					array.add(rs.getString(10));
					array.add(rs.getString(11));
					array.add(rs.getString(12));
				}
		}
		return array;	
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	public void setNumber(int i) {
		this.i = i;
	}
}  
