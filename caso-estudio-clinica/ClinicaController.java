import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 * CONTROLADOR PRINCIPAL: escucha los botones del panel de control, le pide los
 * cambios al Modelo y repinta la tabla de hospitalizados.
 */
public class ClinicaController {
    private ClinicaModel modelo;
    private VentanaClinica vista;

    public ClinicaController(ClinicaModel modelo, VentanaClinica vista) {
        this.modelo = modelo;
        this.vista = vista;

        this.vista.agregarListenerRegistrar(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarRegistro();
            }
        });

        this.vista.agregarListenerDarDeAlta(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarAlta();
            }
        });

        // La tabla se repinta sola cada vez que la bodega cambia (siempre en el hilo de Swing)
        this.modelo.agregarObservador(new Runnable() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        actualizarTabla();
                    }
                });
            }
        });
    }

    private void procesarRegistro() {
        // 1. Obtener datos de la Vista
        String documento = vista.getDocumento();
        String nombre = vista.getNombre();
        String diagnostico = vista.getDiagnostico();

        // 2. Validación temprana
        if (documento.isEmpty() || nombre.isEmpty() || diagnostico.isEmpty()) {
            vista.mostrarMensajeError("Por favor complete el documento, el nombre y el diagnóstico.");
            return;
        }

        try {
            // 3. Guardar en la bodega (Modelo). El Modelo avisa a los observadores y la tabla se refresca.
            modelo.registrarPaciente(documento, nombre, diagnostico);

            // 4. Limpiar cajas de texto
            vista.limpiarCampos();

        } catch (IllegalArgumentException ex) {
            vista.mostrarMensajeError(ex.getMessage());
        }
    }

    private void procesarAlta() {
        // 1. Preguntarle a la Vista qué paciente seleccionó el médico
        String documento = vista.getDocumentoSeleccionado();
        if (documento == null) {
            vista.mostrarMensajeError("Seleccione un paciente de la tabla para darle de alta.");
            return;
        }

        try {
            // 2. El Modelo aplica la regla de negocio y cambia el estado del paciente
            modelo.darDeAlta(documento);
            vista.mostrarMensaje("El paciente con documento " + documento + " fue dado de alta.");

        } catch (IllegalArgumentException ex) {
            vista.mostrarMensajeError(ex.getMessage());
        }
    }

    private void actualizarTabla() {
        // Pedimos el lienzo a la vista y borramos lo pintado para no duplicar filas
        DefaultTableModel lienzo = vista.getModeloTabla();
        lienzo.setRowCount(0);

        // Recorremos solo los hospitalizados y pintamos fila por fila
        for (Paciente p : modelo.getHospitalizados()) {
            Object[] filaNueva = { p.getDocumento(), p.getNombre(), p.getDiagnostico() };
            lienzo.addRow(filaNueva);
        }
    }
}
