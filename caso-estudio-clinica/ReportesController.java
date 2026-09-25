import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 * CONTROLADOR DE REPORTES: lee la misma instancia de ClinicaModel que usa el
 * panel principal y pinta los indicadores y el historial de altas.
 */
public class ReportesController {
    private ClinicaModel modelo;
    private VentanaReportes vista;

    public ReportesController(ClinicaModel modelo, VentanaReportes vista) {
        this.modelo = modelo;
        this.vista = vista;

        // Cada vez que la bodega cambie (sin importar quién la cambió), se refresca el reporte.
        // Swing solo se puede tocar desde su propio hilo (EDT), así que el repintado se agenda ahí.
        this.modelo.agregarObservador(new Runnable() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        actualizarReporte();
                    }
                });
            }
        });

        actualizarReporte();
    }

    public void mostrarVentana() {
        actualizarReporte();
        vista.setVisible(true);
        vista.toFront();
    }

    private void actualizarReporte() {
        List<Paciente> altas = modelo.getDadosDeAlta();
        vista.mostrarResumen(modelo.totalPacientes(), modelo.getHospitalizados().size(), altas.size());

        // Borramos lo pintado anteriormente para no duplicar filas
        DefaultTableModel lienzo = vista.getModeloTabla();
        lienzo.setRowCount(0);

        for (Paciente p : altas) {
            Object[] filaNueva = { p.getDocumento(), p.getNombre(), p.getDiagnostico() };
            lienzo.addRow(filaNueva);
        }
    }
}
