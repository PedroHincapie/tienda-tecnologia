package testdatabuilder;



import dominio.Cliente;
public class ClienteTestDataBuilder {

	private static final String NOMBRE = "PEDRO JESUS HINCAPIE GARCIA";

	private String nombre;

	public ClienteTestDataBuilder() {
		this.nombre = NOMBRE;		
	}

	public ClienteTestDataBuilder conNombre(String nombre) {
		this.nombre=nombre;
		return this;
	}

	public Cliente build() {
		return new Cliente(this.nombre);
	}
}
