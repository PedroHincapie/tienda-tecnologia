package dominio;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioGarantiaExtendida;
import dominio.repositorio.RepositorioProducto;
import infraestructura.GeneradorFecha;

public class Vendedor {

	public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya cuenta con una garantia extendida";

	public static final String PRODUCTO_SIN_GARANTIA = "Este producto no cuenta con garantía extendida";

	public static final String CODIGO_PRODUCTO_NULL = "Codigo de producto NULL";

	public static final double LIMITE_PRECIO = 500000;

	public static final int DIAS_GARANTIA_MAYOR = 200;

	public static final int DIAS_GARANTIA_MENOR = 100;

	public static final double PORCENTAJE_MAYOR = 0.20;

	public static final double PORCENTAJE_MENOR = 0.10;

	public static final String REGEX = "([aeiouAEIOU])";

	private RepositorioProducto repositorioProducto;
	private RepositorioGarantiaExtendida repositorioGarantia;
	private GeneradorFecha generadorFecha;

	public Vendedor(RepositorioProducto repositorioProducto, RepositorioGarantiaExtendida repositorioGarantia,
			GeneradorFecha generadorFecha) {
		this.repositorioProducto = repositorioProducto;
		this.repositorioGarantia = repositorioGarantia;
		this.generadorFecha = generadorFecha;

	}

	public void generarGarantia(String codigo, String nombreCliente) {

		validacionesGenerarGarantia(codigo);

		Producto producto = this.repositorioProducto.obtenerPorCodigo(codigo);

		double precioGarantiaExt = calcularPrecioGarantia(producto);

		Date fechaFinGarantia = calcularFechaFinGarantia(producto);

		GarantiaExtendida garantiaExtendida = new GarantiaExtendida(producto, new Date(), fechaFinGarantia,
				precioGarantiaExt, nombreCliente);
		this.repositorioGarantia.agregar(garantiaExtendida);
	}

	public boolean tieneGarantia(String codigo) {

		Producto prodGarantia = this.repositorioGarantia.obtenerProductoConGarantiaPorCodigo(codigo);
		return prodGarantia != null;
	}

	private boolean tieneTresVocales(String codigo) {

		Pattern pat = Pattern.compile(REGEX);

		Matcher mat = pat.matcher(codigo);

		int cont = 0;

		while (mat.find()) {
			cont++;
		}

		return cont == 3;
	}

	private double calcularPrecioGarantia(Producto producto) {

		return LIMITE_PRECIO < producto.getPrecio() ? producto.getPrecio() * PORCENTAJE_MAYOR
				: producto.getPrecio() * PORCENTAJE_MENOR;
	}

	private Date calcularFechaFinGarantia(Producto producto) {

		if (LIMITE_PRECIO < producto.getPrecio()) {
			return calcularFechaFinGarantiaMayor();

		} else {
			return calcularFechaFinGarantiaMenor();
		}
	}

	private Date calcularFechaFinGarantiaMayor() {

		LocalDate fechaInicio = generadorFecha.obtenerFechaActual();
		LocalDate fechaFin = fechaInicio.plusDays(DIAS_GARANTIA_MAYOR);

		fechaFin = procesarFechasGarantias(fechaInicio, fechaFin);

		if (DayOfWeek.SUNDAY.equals(fechaFin.getDayOfWeek())) {
			fechaFin = fechaFin.plusDays(1);
		}

		return java.util.Date.from(fechaFin.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	private LocalDate procesarFechasGarantias(LocalDate fechaInicio, LocalDate fechaFin) {
		int iter = 2;

		while (iter > 0) {
			int lunes = 0;

			while (fechaFin.isAfter(fechaInicio) || fechaFin.equals(fechaInicio)) {
				if (DayOfWeek.MONDAY.equals(fechaInicio.getDayOfWeek())) {
					lunes++;
				}
				fechaInicio = fechaInicio.plusDays(1);
			}

			fechaFin = fechaFin.plusDays(lunes);

			--iter;
		}

		return fechaFin;
	}

	private Date calcularFechaFinGarantiaMenor() {
		LocalDate fechaInicio = generadorFecha.obtenerFechaActual();

		LocalDate fechaFin = fechaInicio.plusDays(DIAS_GARANTIA_MENOR);

		return java.util.Date.from(fechaFin.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	private void validacionesGenerarGarantia(String codigo) {
		if (null == codigo) {
			throw new GarantiaExtendidaException(Vendedor.CODIGO_PRODUCTO_NULL);
		}

		if (tieneGarantia(codigo)) {
			throw new GarantiaExtendidaException(Vendedor.EL_PRODUCTO_TIENE_GARANTIA);
		}

		if (tieneTresVocales(codigo)) {
			throw new GarantiaExtendidaException(Vendedor.PRODUCTO_SIN_GARANTIA);
		}
	}
}
