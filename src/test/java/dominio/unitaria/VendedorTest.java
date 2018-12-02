package dominio.unitaria;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import dominio.Producto;
import dominio.Vendedor;
import dominio.repositorio.RepositorioGarantiaExtendida;
import dominio.repositorio.RepositorioProducto;
import infraestructura.GeneradorFecha;
import testdatabuilder.ProductoTestDataBuilder;

public class VendedorTest {

	@Test
	public void productoYaTieneGarantiaTest() {

		// arrange
		ProductoTestDataBuilder productoTestDataBuilder = new ProductoTestDataBuilder();

		Producto producto = productoTestDataBuilder.build();

		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		GeneradorFecha generadorFecha = mock(GeneradorFecha.class);

		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(producto);

		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia, generadorFecha);

		// act
		boolean existeProducto = vendedor.tieneGarantia(producto.getCodigo());

		//assert
		assertTrue(existeProducto);
	}

	@Test
	public void productoNoTieneGarantiaTest() {

		// arrange
		ProductoTestDataBuilder productoestDataBuilder = new ProductoTestDataBuilder();

		Producto producto = productoestDataBuilder.build();

		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		GeneradorFecha generadorFecha = mock(GeneradorFecha.class);

		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(null);

		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia, generadorFecha);

		// act
		boolean existeProducto =  vendedor.tieneGarantia(producto.getCodigo());

		//assert
		assertFalse(existeProducto);
	}
}
