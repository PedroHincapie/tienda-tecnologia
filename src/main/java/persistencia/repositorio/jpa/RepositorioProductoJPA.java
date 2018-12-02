package persistencia.repositorio.jpa;

import persistencia.entitad.ProductoEntity;

public interface RepositorioProductoJPA {

	/**
	 * Permite obtener un producto entity por un codigo
	 * @param codigo
	 * @return
	 */
	ProductoEntity obtenerProductoEntityPorCodigo(String codigo);

}
