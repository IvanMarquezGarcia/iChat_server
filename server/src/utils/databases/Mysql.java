package utils.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

	// Cerrar conexi�n
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
}