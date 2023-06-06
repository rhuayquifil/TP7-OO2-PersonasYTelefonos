package ar.unrn.domain.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ar.unrn.domain.portsout.Propiedades;

public class ProxyArrayList implements Set {

	private int id;
	private Set<Telefono> telefonos;
	private Propiedades properties;

	public ProxyArrayList(int id, Propiedades properties) {
		this.id = id;
		this.telefonos = null;
		this.properties = properties;
	}

	@Override
	public int size() {
		if (this.telefonos == null) {
			return obtenerCantidadTelefonos(id);
		}

		return telefonos.size();
	}

	@Override
	public String toString() {
		return "ProxyArrayList [id=" + id + ", telefonos=" + telefonos + "]";
	}

	private int obtenerCantidadTelefonos(int id) {

		String sql = "SELECT idPersona, COUNT(idPersona) AS repeticiones FROM telefonos WHERE idPersona = ? GROUP BY idPersona HAVING COUNT(idPersona);";

		try (Connection conn = obtenerConexion(); PreparedStatement statement = conn.prepareStatement(sql);) {

			statement.setInt(1, id);

			ResultSet result = statement.executeQuery();

			return cantidadDeTelefonos(result);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private int cantidadDeTelefonos(ResultSet result) throws SQLException {
		int resultado = 0;

		while (result.next()) {
			resultado = result.getInt("repeticiones");
		}

		return resultado;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray(Object[] array) {

		String sql = "select p.nombre,t.numero " + "from personas p, telefonos t "
				+ "where p.id = t.idpersona and p.id = ?";

		try (Connection conn = obtenerConexion(); PreparedStatement statement = conn.prepareStatement(sql);) {

			telefonos = new HashSet<Telefono>();

			statement.setInt(1, id); // seteo el valor en el sql

			ResultSet result = statement.executeQuery();

			obtenerTelefonos(result);

			completar(array);

			return array;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void completar(Object[] a) {
		int i = 0;
		for (Object object : telefonos) {
			a[i] = object;
			i++;
		}
	}

	private void obtenerTelefonos(ResultSet result) throws SQLException {
		String nombrePersona = null;

		while (result.next()) {
			nombrePersona = result.getString(1);

			telefonos.add(new Telefono(result.getString(2)));
		}
	}

	private Connection obtenerConexion() throws SQLException {
		return DriverManager.getConnection(properties.get("url"), properties.get("usuario"),
				properties.get("contrasena"));
	}

	@Override
	public boolean add(Object e) {
		// TODO Auto-generated method stub

		return false;
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(Collection c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(Collection c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

}
