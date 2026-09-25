import java.util.ArrayList;

/**
 * MODELO: aquí terminan los datos y las reglas de negocio que en el código
 * spaghetti estaban dentro de los ActionListener. No importa javax.swing.
 */
public class TiendaModel {
    private ArrayList<Producto> productos = new ArrayList<>();

    /**
     * Regla de negocio: el nombre es obligatorio, el precio debe ser positivo
     * y la cantidad no puede ser negativa.
     */
    public void agregarProducto(String nombre, double precio, int cantidad) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        if (precio <= 0 || cantidad < 0) {
            throw new IllegalArgumentException("El precio debe ser positivo y la cantidad no negativa.");
        }
        productos.add(new Producto(nombre.trim(), precio, cantidad));
    }

    public void eliminarProducto(int indice) {
        if (indice < 0 || indice >= productos.size()) {
            throw new IllegalArgumentException("El producto seleccionado no existe.");
        }
        productos.remove(indice);
    }

    /**
     * Valor total del inventario: suma de precio x cantidad de cada producto.
     */
    public double calcularValorInventario() {
        double total = 0;
        for (Producto p : productos) {
            total += p.getSubtotal();
        }
        return total;
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }
}
