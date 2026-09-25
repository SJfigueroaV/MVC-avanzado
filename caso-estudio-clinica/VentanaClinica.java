import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * VISTA PRINCIPAL: formulario de ingreso y tabla de pacientes hospitalizados.
 * Ya no guarda ningún ArrayList: solo muestra lo que el controlador le pinta.
 */
public class VentanaClinica extends JFrame {
    private JTextField txtDocumento = new JTextField(12);
    private JTextField txtNombre = new JTextField(15);
    private JTextField txtDiagnostico = new JTextField(15);
    private JButton btnRegistrar = new JButton("Registrar Paciente");
    private JButton btnDarDeAlta = new JButton("Dar de Alta");
    private JButton btnReportes = new JButton("Ver Reportes");

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    public VentanaClinica() {
        // Configuración de la ventana principal
        setTitle("Clínica SaludFatal - Panel de Control");
        setSize(620, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla

        // Zona superior: el formulario de ingreso
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        panelFormulario.add(new JLabel("Documento:"));
        panelFormulario.add(txtDocumento);
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Diagnóstico:"));
        panelFormulario.add(txtDiagnostico);
        panelFormulario.add(new JLabel(""));
        panelFormulario.add(btnRegistrar);

        // Zona central: la tabla de hospitalizados (solo lectura, una fila seleccionable a la vez)
        String[] columnas = {"Documento", "Nombre", "Diagnóstico"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Pacientes hospitalizados"));

        // Zona inferior: acciones sobre la tabla
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.add(btnReportes);
        panelBotones.add(btnDarDeAlta);

        JPanel panelTabla = new JPanel(new BorderLayout(10, 10));
        panelTabla.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        panelTabla.add(scroll, BorderLayout.CENTER);
        panelTabla.add(panelBotones, BorderLayout.SOUTH);

        add(panelFormulario, BorderLayout.NORTH);
        add(panelTabla, BorderLayout.CENTER);
    }

    // Métodos Getters para que el controlador obtenga lo que escribió el usuario
    public String getDocumento() {
        return txtDocumento.getText().trim();
    }

    public String getNombre() {
        return txtNombre.getText().trim();
    }

    public String getDiagnostico() {
        return txtDiagnostico.getText().trim();
    }

    /**
     * Devuelve el documento del paciente seleccionado en la tabla, o null si no hay selección.
     */
    public String getDocumentoSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            return null;
        }
        return (String) modeloTabla.getValueAt(fila, 0);
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public void limpiarCampos() {
        txtDocumento.setText("");
        txtNombre.setText("");
        txtDiagnostico.setText("");
        txtDocumento.requestFocus();
    }

    // Métodos para conectar las acciones de los botones con el Controlador
    public void agregarListenerRegistrar(ActionListener listener) {
        btnRegistrar.addActionListener(listener);
    }

    public void agregarListenerDarDeAlta(ActionListener listener) {
        btnDarDeAlta.addActionListener(listener);
    }

    public void agregarListenerReportes(ActionListener listener) {
        btnReportes.addActionListener(listener);
    }

    // Métodos auxiliares para mostrar alertas emergentes
    public void mostrarMensajeError(String errorMsg) {
        JOptionPane.showMessageDialog(this, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Clínica SaludFatal", JOptionPane.INFORMATION_MESSAGE);
    }
}
