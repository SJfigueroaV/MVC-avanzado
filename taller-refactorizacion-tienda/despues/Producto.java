/**
 * ENTIDAD: reemplaza las tres listas paralelas (nombres, precios, cantidades)
 * del código spaghetti por un único objeto.
 */
public class Producto {
    private String nombre;
    private double precio;
    private int cantidad;

    public Producto(String nombre, double precio, int cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getSubtotal() {
        return precio * cantidad;
    }

    @Override
    public String toString() {
        return nombre + " x" + cantidad;
    }
}
