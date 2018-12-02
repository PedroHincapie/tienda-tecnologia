package persistencia.entitad;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

@Entity(name = "GarantiaExtendida")
@NamedQuery(name = "GarantiaExtendida.findByCodigo", query = "SELECT garantia from GarantiaExtendida garantia where garantia.producto.codigo = :codigo")
public class GarantiaExtendidaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "ID_PRODUCTO", referencedColumnName = "id")
	private ProductoEntity producto;

	private Date fechaSolicitudGarantia;

	private Date fechaFinGarantia;

	private String nombreCliente;

	private double precio;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProductoEntity getProducto() {
		return producto;
	}

	public void setProducto(ProductoEntity producto) {
		this.producto = producto;
	}

	public Date getFechaSolicitudGarantia() {
		return fechaSolicitudGarantia;
	}

	public void setFechaSolicitudGarantia(Date fechaSolicitudGarantia) {
		this.fechaSolicitudGarantia = fechaSolicitudGarantia;
	}

	public Date getFechaFinGarantia() {
		return fechaFinGarantia;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public double getPrecio() {
		return precio;
	}

	public void setFechaFinGarantia(Date fechaFinGarantia) {
		this.fechaFinGarantia = fechaFinGarantia;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}
}
