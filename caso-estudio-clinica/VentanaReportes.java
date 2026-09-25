import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * VISTA SECUNDARIA: la ventana de "Reportes". Antes no podía existir porque los
 * datos estaban atrapados en VentanaClinica; ahora lee la misma bodega (ClinicaModel)
 * a través de su propio controlador.
 */
public class VentanaReportes extends JFrame {
    private JLabel lblTotal = new JLabel("Total de pacientes: 0");
    private JLabel lblHospitalizados = new JLabel("Hospitalizados: 0");
    private JLabel lblAltas = new JLabel("Dados de alta: 0");

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    public VentanaReportes() {
        setTitle("Clínica SaludFatal - Reportes");
        setSize(460, 360);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // Cerrarla no termina el programa

        // Zona superior: los indicadores
        JPanel panelResumen = new JPanel(new GridLayout(3, 1, 5, 5));
        panelResumen.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        panelResumen.add(lblTotal);
        panelResumen.add(lblHospitalizados);
        panelResumen.add(lblAltas);

        // Zona central: el historial de altas
        String[] columnas = {"Documento", "Nombre", "Diagnóstico"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Pacientes dados de alta"));

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        panelTabla.add(scroll, BorderLayout.CENTER);

        add(panelResumen, BorderLayout.NORTH);
        add(panelTabla, BorderLayout.CENTER);
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public void mostrarResumen(int total, int hospitalizados, int altas) {
        lblTotal.setText("Total de pacientes: " + total);
        lblHospitalizados.setText("Hospitalizados: " + hospitalizados);
        lblAltas.setText("Dados de alta: " + altas);
    }
}
