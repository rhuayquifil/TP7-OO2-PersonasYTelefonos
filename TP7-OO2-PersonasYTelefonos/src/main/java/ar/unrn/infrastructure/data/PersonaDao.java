package ar.unrn.infrastructure.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import ar.unrn.domain.model.Persona;
import ar.unrn.domain.model.ProxyArrayList;
import ar.unrn.domain.model.Telefono;
import ar.unrn.domain.portsout.Propiedades;

public class PersonaDao {

	private Propiedades properties;

	public PersonaDao(Propiedades properties) {
		this.properties = properties;
	}

	private Connection obtenerConexion() throws SQLException {
		return DriverManager.getConnection(properties.get("url"), properties.get("usuario"),
				properties.get("contrasena"));
	}

	public Persona personaPorId(int id) {

		String sql = "SELECT p.nombre FROM personas p WHERE p.id = ?";

		try (Connection conn = obtenerConexion(); PreparedStatement statement = conn.prepareStatement(sql);) {

			statement.setInt(1, id); // seteo el valor en el sql

			ResultSet result = statement.executeQuery();

			return obtenerPersona(id, result);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private Persona obtenerPersona(int id, ResultSet result) throws SQLException {
		if (result.next()) {
			String nombrePersona = result.getString(1);

			Set<Telefono> proxyTelefonos = new ProxyArrayList(id, properties);

			return new Persona(id, nombrePersona, proxyTelefonos);
		}

		throw new RuntimeException("No se encontró ninguna persona con el ID: " + id);
	}
}
