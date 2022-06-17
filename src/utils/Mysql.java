/*
	Hecho por: Iván Márquez García
		
	2° D.A.M.
	
	Proyecto final - iChat


	------------------------------- DESCRIPCIÓN -------------------------------
	
	Clases para el acceso a la base de datos.
*/

package utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static utils.Constants.*;


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


	public static HashMap<String, String> login(Connection c, HashMap<String, String> data) {
		HashMap<String, String> response = new HashMap<String, String>();
		String query = "SELECT id, username, password, language FROM user WHERE username = ?";
		try {
			PreparedStatement ps = c.prepareStatement(query);
			ps.setString(1, data.get("username"));
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				if (!rs.getString(3).equals(data.get("password")))
					response.put("code", RESPONSE_INCORRECT_PASSWORD);
				else {
					response.put("code", RESPONSE_OK);
					response.put("id", rs.getString(1));
					response.put("username", rs.getString(2));
					response.put("language", rs.getString(4));
				}
			}
			else
				response.put("code", RESPONSE_UNREGISTERED);
		} catch (SQLException e) {
			e.printStackTrace();
			response.put("code", RESPONSE_ERROR);
		}
		return response;
	}


	public static String logup(Connection c, HashMap<String, String> data) {
		try {
			String insert = "INSERT INTO user (username, password, language) VALUES (?, ?, ?)";
			PreparedStatement ps = c.prepareStatement(insert);
			ps.setString(1, data.get("username"));
			ps.setString(2, data.get("password"));
			ps.setString(3, data.get("language"));
			if (ps.executeUpdate() >= 0)
				return RESPONSE_OK;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return RESPONSE_ERROR;
	}
	
	
	public static ResultSet searchUserByUsername(Connection c, String username) {
		String query = "SELECT * FROM user WHERE username=?";
		try {
			PreparedStatement ps = c.prepareStatement(query);
			ps.setString(1, username);
			return ps.executeQuery();
		} catch(SQLException sqle) {
			return null;
		}
	}
	
	
	public static String userExistsByUsername(Connection c, String username) {
		ResultSet rs = searchUserByUsername(c, username);
		try {
			if (rs == null)
				return RESPONSE_ERROR;
			else if (rs.next())
				return ALREADY_EXISTS;
			return NO_RESULTS_FOUND;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return RESPONSE_ERROR;
		}
	}

}
