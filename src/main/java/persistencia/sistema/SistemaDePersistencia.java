package persistencia.sistema;

import javax.persistence.EntityManager;

import dominio.repositorio.RepositorioProducto;
import dominio.repositorio.RepositorioGarantiaExtendida;
import persistencia.conexion.ConexionJPA;
import persistencia.repositorio.RepositorioProductoPersistente;
import persistencia.repositorio.RepositorioGarantiaPersistente;

public class SistemaDePersistencia {

	private EntityManager entityManager;

	public SistemaDePersistencia() {
		this.entityManager = new ConexionJPA().createEntityManager();
	}

	public RepositorioProducto obtenerRepositorioProductos() {
		return new RepositorioProductoPersistente(entityManager);
	}
	
	public RepositorioGarantiaExtendida obtenerRepositorioGarantia() {
		return new RepositorioGarantiaPersistente(entityManager, this.obtenerRepositorioProductos());
	}

	public void iniciar() {
		entityManager.getTransaction().begin();
	}

	public void terminar() {
		entityManager.getTransaction().commit();
	}
}
