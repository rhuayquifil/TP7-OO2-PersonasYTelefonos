package ar.unrn.infrastructure.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import ar.unrn.domain.model.Persona;
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
		String sql = "select p.nombre,t.numero " + "from personas p, telefonos t "
				+ "where p.id = t.idpersona and p.id = ?";

		try (Connection conn = obtenerConexion(); PreparedStatement statement = conn.prepareStatement(sql);) {

			statement.setInt(1, id);

			ResultSet result = statement.executeQuery();

			Set<Telefono> telefonos = new HashSet<Telefono>();

			String nombrePersona = null;
			while (result.next()) {
				nombrePersona = result.getString(1);
				telefonos.add(new Telefono(result.getString(2)));
			}
			return new Persona(id, nombrePersona, telefonos);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
