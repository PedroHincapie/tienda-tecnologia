package persistencia.builder;

import dominio.Producto;
import persistencia.entitad.ProductoEntity;

public class ProductoBuilder {
	
	private ProductoBuilder() {}
	
	public static Producto convertirADominio(ProductoEntity productoEntity) {
		
		Producto producto = null;
		
		if(productoEntity != null) {
			producto = new Producto(productoEntity.getCodigo(), productoEntity.getNombre(), productoEntity.getPrecio());
		}
		
		return producto;
	}
	
	public static ProductoEntity convertirAEntity(Producto producto) {
		
		ProductoEntity productoEntity = new ProductoEntity();
		
		productoEntity.setCodigo(producto.getCodigo());
		productoEntity.setNombre(producto.getNombre());
		productoEntity.setPrecio(producto.getPrecio());
		
		return productoEntity;
	}
}
