import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

/**
 * CONTROLADOR: restablece la comunicación entre la Vista y el Modelo para que el
 * programa se comporte exactamente igual que la versión spaghetti.
 */
public class TiendaController {
    private TiendaModel modelo;
    private TiendaView vista;

    public TiendaController(TiendaModel modelo, TiendaView vista) {
        this.modelo = modelo;
        this.vista = vista;

        this.vista.agregarListenerAgregar(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarAgregar();
            }
        });

        this.vista.agregarListenerEliminar(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarEliminar();
            }
        });
    }

    private void procesarAgregar() {
        // 1. Validación temprana de las cajas de texto
        if (vista.getNombre().isEmpty() || vista.getPrecio().isEmpty() || vista.getCantidad().isEmpty()) {
            vista.mostrarMensaje("Complete todos los campos.");
            return;
        }

        double precio;
        int cantidad;
        try {
            precio = Double.parseDouble(vista.getPrecio());
            cantidad = Integer.parseInt(vista.getCantidad());
        } catch (NumberFormatException ex) {
            vista.mostrarMensaje("Precio o cantidad inválidos.");
            return;
        }

        try {
            // 2. Guardar en la bodega (el Modelo aplica las reglas de negocio)
            modelo.agregarProducto(vista.getNombre(), precio, cantidad);
            vista.limpiarCampos();
            actualizarVista();
        } catch (IllegalArgumentException ex) {
            vista.mostrarMensaje(ex.getMessage());
        }
    }

    private void procesarEliminar() {
        int fila = vista.getFilaSeleccionada();
        if (fila == -1) {
            vista.mostrarMensaje("Seleccione una fila.");
            return;
        }

        // Las filas se pintan en el mismo orden del ArrayList, así que el índice coincide
        modelo.eliminarProducto(fila);
        actualizarVista();
    }

    private void actualizarVista() {
        DefaultTableModel lienzo = vista.getModeloTabla();
        lienzo.setRowCount(0);

        for (Producto p : modelo.getProductos()) {
            Object[] filaNueva = { p.getNombre(), p.getPrecio(), p.getCantidad(), p.getSubtotal() };
            lienzo.addRow(filaNueva);
        }

        vista.mostrarTotal(modelo.calcularValorInventario());
    }
}
