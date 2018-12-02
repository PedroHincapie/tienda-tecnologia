package infraestructura;

import java.time.LocalDate;

public interface GeneradorFecha {

	/**
	 * Permite obtener la fecha de incio de la garantia
	 *
	 * @return
	 */
	LocalDate obtenerFechaActual();

	/**
	 * Permite obtener la fecha de fin de la garantia
	 *
	 * @return
	 */
	LocalDate obtenerFechaFin();

}
