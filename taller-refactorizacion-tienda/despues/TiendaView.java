import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * VISTA: aquí termina todo el código que construía la ventana en el spaghetti.
 * Solo dibuja y avisa de los clics; no guarda datos ni calcula.
 */
public class TiendaView extends JFrame {
    private JTextField txtNombre = new JTextField(15);
    private JTextField txtPrecio = new JTextField(8);
    private JTextField txtCantidad = new JTextField(5);
    private JButton btnAgregar = new JButton("Agregar Producto");
    private JButton btnEliminar = new JButton("Eliminar Fila");
    private JLabel lblTotal = new JLabel("Valor del inventario: $ 0.00");

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    public TiendaView() {
        setTitle("Tienda - Inventario");
        setSize(600, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla

        // Zona superior: el formulario
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Precio:"));
        panelFormulario.add(txtPrecio);
        panelFormulario.add(new JLabel("Cantidad:"));
        panelFormulario.add(txtCantidad);
        panelFormulario.add(new JLabel(""));
        panelFormulario.add(btnAgregar);

        // Zona central: la tabla dentro de su JScrollPane
        modeloTabla = new DefaultTableModel(new String[]{"Nombre", "Precio", "Cantidad", "Subtotal"}, 0);
        tabla = new JTable(modeloTabla);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(lblTotal, BorderLayout.WEST);
        panelInferior.add(btnEliminar, BorderLayout.EAST);

        JPanel panelTabla = new JPanel(new BorderLayout(10, 10));
        panelTabla.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        panelTabla.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panelTabla.add(panelInferior, BorderLayout.SOUTH);

        add(panelFormulario, BorderLayout.NORTH);
        add(panelTabla, BorderLayout.CENTER);
    }

    // Métodos Getters para que el controlador obtenga lo que escribió el usuario
    public String getNombre() {
        return txtNombre.getText().trim();
    }

    public String getPrecio() {
        return txtPrecio.getText().trim();
    }

    public String getCantidad() {
        return txtCantidad.getText().trim();
    }

    public int getFilaSeleccionada() {
        return tabla.getSelectedRow();
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    // Métodos para actualizar la pantalla
    public void mostrarTotal(double total) {
        lblTotal.setText(String.format("Valor del inventario: $ %.2f", total));
    }

    public void limpiarCampos() {
        txtNombre.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
    }

    // Métodos para conectar los botones con el Controlador
    public void agregarListenerAgregar(ActionListener listener) {
        btnAgregar.addActionListener(listener);
    }

    public void agregarListenerEliminar(ActionListener listener) {
        btnEliminar.addActionListener(listener);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
