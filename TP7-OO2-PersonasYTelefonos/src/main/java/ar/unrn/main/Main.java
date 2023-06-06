package ar.unrn.main;

import ar.unrn.domain.model.Persona;
import ar.unrn.domain.model.Telefono;
import ar.unrn.domain.portsout.InfrastructureExceptions;
import ar.unrn.infrastructure.data.BaseDeDatosPropiedades;
import ar.unrn.infrastructure.data.PersonaDao;

public class Main {

	public static void main(String[] args) {

		try {
			PersonaDao dao = new PersonaDao(
					new BaseDeDatosPropiedades("jdbc:mysql://127.0.0.1/tp7_personas_y_telefonos", "root", ""));
			Persona p = dao.personaPorId(41321062);
			System.out.println("Antes de p.telefonos(): " + p);

			System.out.println("llamada al proxy");
			for (Telefono telefono : p.telefonos()) {
				System.out.println(telefono);
			}
			System.out.println("despues de p.telefonos(): " + p);
		} catch (InfrastructureExceptions e) {
			System.out.println(e.getMessage());
		}

	}

}
