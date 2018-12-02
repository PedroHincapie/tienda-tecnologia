package dominio.unitaria;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dominio.Cliente;
import testdatabuilder.ClienteTestDataBuilder;

public class ClienteTest {

	private static final String NOMBRE_CLIENTE = "JESUS MARIA HINCAPIE HERNANDEZ";

	@Test
	public void crearClienteTest() {

		// arrange
		ClienteTestDataBuilder clienteTestDataBuilder = new ClienteTestDataBuilder().
				conNombre(NOMBRE_CLIENTE);
		// act
		Cliente cliente = clienteTestDataBuilder.build();

		// assert
		assertEquals(NOMBRE_CLIENTE, cliente.getNombre());	

	}

}
