import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * VISTA: la "vitrina". Prepara el formulario y la tabla vacía, y expone métodos
 * públicos para que el controlador pueda leerla y actualizarla.
 */
public class CursoView extends JFrame {
    private JTextField txtNombre = new JTextField(15);
    private JTextField txtNota1 = new JTextField(5);
    private JTextField txtNota2 = new JTextField(5);
    private JButton btnRegistrar = new JButton("Registrar");
    private JLabel lblTotal = new JLabel("Estudiantes registrados: 0");

    // El lienzo (DefaultTableModel) y el marco (JTable)
    private DefaultTableModel modeloTabla;
    private JTable tabla;

    public CursoView() {
        // Configuración de la ventana principal
        setTitle("Gestor Académico - MVC");
        setSize(560, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla

        // Zona superior: el formulario de captura
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Nota 1 (0.0 - 5.0):"));
        panelFormulario.add(txtNota1);
        panelFormulario.add(new JLabel("Nota 2 (0.0 - 5.0):"));
        panelFormulario.add(txtNota2);
        panelFormulario.add(new JLabel(""));
        panelFormulario.add(btnRegistrar);

        // Zona central: la tabla de 4 columnas. Las celdas no se editan a mano,
        // porque los datos solo cambian a través del Modelo.
        String[] columnas = {"Nombre", "Nota 1", "Nota 2", "Nota Final"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.getTableHeader().setReorderingAllowed(false);

        // Siempre envolver la tabla en un JScrollPane para que se vean los títulos
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Estudiantes"));

        JPanel panelTabla = new JPanel(new BorderLayout(10, 10));
        panelTabla.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        panelTabla.add(scroll, BorderLayout.CENTER);
        panelTabla.add(lblTotal, BorderLayout.SOUTH);

        // Añadir las dos zonas a la ventana
        add(panelFormulario, BorderLayout.NORTH);
        add(panelTabla, BorderLayout.CENTER);
    }

    // Métodos Getters para que el controlador obtenga lo que escribió el usuario
    public String getNombre() {
        return txtNombre.getText().trim();
    }

    public String getNota1() {
        return txtNota1.getText().trim();
    }

    public String getNota2() {
        return txtNota2.getText().trim();
    }

    // El controlador pide el lienzo para pintar las filas
    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    // Métodos para actualizar la pantalla
    public void mostrarTotal(int cantidad) {
        lblTotal.setText("Estudiantes registrados: " + cantidad);
    }

    public void limpiarCampos() {
        txtNombre.setText("");
        txtNota1.setText("");
        txtNota2.setText("");
        txtNombre.requestFocus();
    }

    // Método para conectar la acción del botón con el Controlador (Patrón Observer / Listener)
    public void agregarListenerBoton(ActionListener listenForRegistrarBtn) {
        btnRegistrar.addActionListener(listenForRegistrarBtn);
    }

    // Método auxiliar para mostrar alertas de error emergentes
    public void mostrarMensajeError(String errorMsg) {
        JOptionPane.showMessageDialog(this, errorMsg, "Error de Validación", JOptionPane.ERROR_MESSAGE);
    }
}
