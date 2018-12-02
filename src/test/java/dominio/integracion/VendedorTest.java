package dominio.integracion;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.ZoneId;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dominio.Cliente;
import dominio.GarantiaExtendida;
import dominio.Producto;
import dominio.Vendedor;
import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioGarantiaExtendida;
import dominio.repositorio.RepositorioProducto;
import infraestructura.GeneradorFecha;
import persistencia.sistema.SistemaDePersistencia;
import testdatabuilder.ClienteTestDataBuilder;
import testdatabuilder.ProductoTestDataBuilder;

public class VendedorTest {

	private static final String COMPUTADOR_LENOVO = "Computador Lenovo";

	private static final double COSTO_PRODUCTO_MAYOR_BASE = 500000.1;
	private static final double COSTO_PRODUCTO_MENOR_BASE = 499999;
	private static final double COSTO_PRODUCTO_IGUAL_BASE = 500000;
	private static final double COSTO_PRODUCTO_EJEMPLO_CEIBA = 650000;

	private SistemaDePersistencia sistemaPersistencia;

	private RepositorioProducto repositorioProducto;
	private RepositorioGarantiaExtendida repositorioGarantia;
	private GeneradorFecha generadorFecha;

	@Before
	public void setUp() {

		sistemaPersistencia = new SistemaDePersistencia();

		repositorioProducto = sistemaPersistencia.obtenerRepositorioProductos();
		repositorioGarantia = sistemaPersistencia.obtenerRepositorioGarantia();
		generadorFecha = mock(GeneradorFecha.class);

		sistemaPersistencia.iniciar();
	}

	@After
	public void tearDown() {
		sistemaPersistencia.terminar();
	}

	@Test
	public void generarGarantiaCodigoProductoNullTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().conNombre(COMPUTADOR_LENOVO).conCodigo(null).build();
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia, generadorFecha);
		Cliente cliente = new ClienteTestDataBuilder().build();

		// act
		try {
			vendedor.generarGarantia(producto.getCodigo(), cliente.getNombre());
			fail();

		} catch (GarantiaExtendidaException e) {
			// assert
			Assert.assertEquals(Vendedor.CODIGO_PRODUCTO_NULL, e.getMessage());
		}

	}

	@Test
	public void productoYaTieneGarantiaTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().conNombre(COMPUTADOR_LENOVO).build();
		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia, generadorFecha);
		Cliente cliente = new ClienteTestDataBuilder().build();

		when(generadorFecha.obtenerFechaActual()).thenReturn(LocalDate.now());

		// act
		vendedor.generarGarantia(producto.getCodigo(), cliente.getNombre());
		try {

			vendedor.generarGarantia(producto.getCodigo(), cliente.getNombre());
			fail();

		} catch (GarantiaExtendidaException e) {
			// assert
			Assert.assertEquals(Vendedor.EL_PRODUCTO_TIENE_GARANTIA, e.getMessage());
		}
	}

	@Test
	public void generarGarantiaTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().conNombre(COMPUTADOR_LENOVO).build();
		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia, generadorFecha);
		Cliente cliente = new ClienteTestDataBuilder().build();

		when(generadorFecha.obtenerFechaActual()).thenReturn(LocalDate.now());

		// act
		vendedor.generarGarantia(producto.getCodigo(), cliente.getNombre());

		// assert
		Assert.assertTrue(vendedor.tieneGarantia(producto.getCodigo()));
		Assert.assertNotNull(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo()));

	}

	@Test
	public void generarGarantiaProductoConTresVocalesTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().conCodigo("FARNe1odf10").build();
		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia, generadorFecha);
		Cliente cliente = new ClienteTestDataBuilder().build();

		// act
		try {
			vendedor.generarGarantia(producto.getCodigo(), cliente.getNombre());
			fail();

		} catch (GarantiaExtendidaException e) {
			// assert
			Assert.assertEquals(Vendedor.PRODUCTO_SIN_GARANTIA, e.getMessage());
			Assert.assertNull(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo()));
		}
	}

	@Test
	public void generarGarantiaProductoConDosVocalesTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().conCodigo("FARNe18df10").build();
		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia, generadorFecha);
		Cliente cliente = new ClienteTestDataBuilder().build();

		when(generadorFecha.obtenerFechaActual()).thenReturn(LocalDate.now());

		// act
		vendedor.generarGarantia(producto.getCodigo(), cliente.getNombre());

		// assert
		Assert.assertNotNull(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo()));
	}

	@Test
	public void generarGarantiaCostoProdMayorBaseTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().conNombre(COMPUTADOR_LENOVO)
				.conPrecio(COSTO_PRODUCTO_MAYOR_BASE).build();
		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia, generadorFecha);
		Cliente cliente = new ClienteTestDataBuilder().build();

		when(generadorFecha.obtenerFechaActual()).thenReturn(LocalDate.now());

		// act
		vendedor.generarGarantia(producto.getCodigo(), cliente.getNombre());

		// assert
		Assert.assertTrue(vendedor.tieneGarantia(producto.getCodigo()));
		Assert.assertNotNull(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo()));
		Assert.assertEquals(100000.02, repositorioGarantia.obtener(producto.getCodigo()).getPrecioGarantia(), 1e-6);

	}

	@Test
	public void generarGarantiaCostoProdIgulBaseTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().conNombre(COMPUTADOR_LENOVO)
				.conPrecio(COSTO_PRODUCTO_IGUAL_BASE).build();
		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia, generadorFecha);
		Cliente cliente = new ClienteTestDataBuilder().build();

		when(generadorFecha.obtenerFechaActual()).thenReturn(LocalDate.now());

		// act
		vendedor.generarGarantia(producto.getCodigo(), cliente.getNombre());

		// assert
		Assert.assertTrue(vendedor.tieneGarantia(producto.getCodigo()));
		Assert.assertNotNull(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo()));
		Assert.assertEquals(50000.00, repositorioGarantia.obtener(producto.getCodigo()).getPrecioGarantia(), 1e-6);

	}

	@Test
	public void generarGarantiaCostoProdMenorBaseTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().conNombre(COMPUTADOR_LENOVO)
				.conPrecio(COSTO_PRODUCTO_MENOR_BASE).build();
		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia, generadorFecha);
		Cliente cliente = new ClienteTestDataBuilder().build();

		when(generadorFecha.obtenerFechaActual()).thenReturn(LocalDate.now());

		// act
		vendedor.generarGarantia(producto.getCodigo(), cliente.getNombre());

		// assert
		Assert.assertTrue(vendedor.tieneGarantia(producto.getCodigo()));
		Assert.assertNotNull(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo()));
		Assert.assertEquals(49999.9, repositorioGarantia.obtener(producto.getCodigo()).getPrecioGarantia(), 1e-6);

	}

	@Test
	public void generarGarantiaFechaFinGarantiaCostoMayorBaseTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().conNombre(COMPUTADOR_LENOVO)
				.conPrecio(COSTO_PRODUCTO_EJEMPLO_CEIBA).build();
		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia, generadorFecha);
		Cliente cliente = new ClienteTestDataBuilder().build();

		when(generadorFecha.obtenerFechaActual()).thenReturn(LocalDate.of(2018, 8, 16));
		when(generadorFecha.obtenerFechaFin()).thenReturn(LocalDate.of(2019, 4, 6));

		// act
		vendedor.generarGarantia(producto.getCodigo(), cliente.getNombre());

		// assert
		Assert.assertTrue(vendedor.tieneGarantia(producto.getCodigo()));
		Assert.assertNotNull(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo()));
		Assert.assertEquals(130000.0, repositorioGarantia.obtener(producto.getCodigo()).getPrecioGarantia(), 1e-6);
		Assert.assertEquals(
				java.util.Date.from(
						generadorFecha.obtenerFechaFin().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
				repositorioGarantia.obtener(producto.getCodigo()).getFechaFinGarantia());

	}

	@Test
	public void generarGarantiaFechaFinGarantiaCostoMayorBaseDomingoTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().conNombre(COMPUTADOR_LENOVO)
				.conPrecio(COSTO_PRODUCTO_MAYOR_BASE).build();
		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia, generadorFecha);
		Cliente cliente = new ClienteTestDataBuilder().build();

		when(generadorFecha.obtenerFechaActual()).thenReturn(LocalDate.of(2018, 8, 17));
		when(generadorFecha.obtenerFechaFin()).thenReturn(LocalDate.of(2019, 4, 8));

		// act
		vendedor.generarGarantia(producto.getCodigo(), cliente.getNombre());

		// assert
		Assert.assertTrue(vendedor.tieneGarantia(producto.getCodigo()));
		Assert.assertNotNull(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo()));
		Assert.assertEquals(100000.02, repositorioGarantia.obtener(producto.getCodigo()).getPrecioGarantia(), 1e-6);
		Assert.assertEquals(
				java.util.Date.from(
						generadorFecha.obtenerFechaFin().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
				repositorioGarantia.obtener(producto.getCodigo()).getFechaFinGarantia());

	}

	@Test
	public void generarGarantiaFechaFinGarantiaCostoMenorBaseTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().conNombre(COMPUTADOR_LENOVO)
				.conPrecio(COSTO_PRODUCTO_MENOR_BASE).build();
		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia, generadorFecha);
		Cliente cliente = new ClienteTestDataBuilder().build();

		when(generadorFecha.obtenerFechaActual()).thenReturn(LocalDate.of(2018, 8, 16));
		when(generadorFecha.obtenerFechaFin()).thenReturn(LocalDate.of(2018, 11, 24));

		// act
		vendedor.generarGarantia(producto.getCodigo(), cliente.getNombre());

		// assert
		Assert.assertTrue(vendedor.tieneGarantia(producto.getCodigo()));
		Assert.assertNotNull(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo()));
		Assert.assertEquals(49999.9, repositorioGarantia.obtener(producto.getCodigo()).getPrecioGarantia(), 1e-6);
		Assert.assertEquals(
				java.util.Date.from(
						generadorFecha.obtenerFechaFin().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
				repositorioGarantia.obtener(producto.getCodigo()).getFechaFinGarantia());

	}

	@Test
	public void validarGeneracionGarantiaTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().conNombre(COMPUTADOR_LENOVO).build();
		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia, generadorFecha);
		Cliente cliente = new ClienteTestDataBuilder().build();

		when(generadorFecha.obtenerFechaActual()).thenReturn(LocalDate.now());

		// act
		vendedor.generarGarantia(producto.getCodigo(), cliente.getNombre());

		// assert
		Assert.assertTrue(vendedor.tieneGarantia(producto.getCodigo()));
		Assert.assertNotNull(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo()));
		GarantiaExtendida garantia = repositorioGarantia.obtener(producto.getCodigo());
		Assert.assertNotNull(garantia);
		Assert.assertNotNull(garantia.getNombreCliente());
		Assert.assertNotNull(garantia.getFechaFinGarantia());
		Assert.assertNotNull(garantia.getFechaSolicitudGarantia());
		Assert.assertNotNull(garantia.getPrecioGarantia());
		Assert.assertNotNull(garantia.getProducto().getCodigo());

	}

}
