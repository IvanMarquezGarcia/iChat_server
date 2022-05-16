package utils.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Mysql {	

	// Conectar con base de datos MySQL
	public static Connection connect(String host, String user, String password) {
		Connection c = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mysql://" + host + "/ichat", user, password);
		} catch (Exception e) {
			System.out.println("Error al conectar");
		}

		return c;
	}

	
	// Cerrar conexión
	public static void disconnect(Connection c) {
		try {
			if (c != null && c.isClosed() == false) {
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			else
				System.out.println("Conexión inválida");
		} catch (SQLException e) {
			System.out.println("Conexión inválida");
		}
	}
	
	
	public static String login(Connection c, HashMap<String, String> data) {
		String query = "SELECT username, password FROM user WHERE username = ?";
		try {
			PreparedStatement ps = c.prepareStatement(query);
			ps.setString(1, data.get("username"));
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				if (rs.getString(2).equals(data.get("password")))
					return "------#//u_o/k_#";
				else
					return "------***#pass/*word**#";
			}
			else
				return "------un##/r//_/_";
		} catch (SQLException e) {
			e.printStackTrace();
			return "------e#rr//_";
		}
	}
	
	
	public static String logup(Connection c, HashMap<String, String> data) {
		try {
			String instert = "INSERT INTO user (username, password, language) VALUES (?, ?, ?)";
			PreparedStatement ps = c.prepareStatement(instert);
			ps.setString(1, data.get("username"));
			ps.setString(2, data.get("password"));
			ps.setString(3, data.get("language"));
			if (ps.executeUpdate() >= 0)
				return "------#//u_o/k_#";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "------e#rr//_";
	}
	
}