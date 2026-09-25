import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

/**
 * CONTROLADOR: el "puente". Escucha el botón de la Vista, valida la entrada,
 * guarda al estudiante en el Modelo y vuelve a pintar la tabla con la lista completa.
 */
public class CursoController {
    private CursoModel modelo;
    private CursoView vista;

    public CursoController(CursoModel modelo, CursoView vista) {
        this.modelo = modelo;
        this.vista = vista;

        // Le indicamos a la vista que cuando hagan clic en el botón, ejecute nuestro método interno
        this.vista.agregarListenerBoton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarRegistro();
            }
        });
    }

    private void procesarRegistro() {
        // 1. Obtener datos de la Vista
        String nombre = vista.getNombre();
        String textoNota1 = vista.getNota1();
        String textoNota2 = vista.getNota2();

        // 2. Validación temprana: ninguna caja puede estar vacía antes de convertir a número
        if (nombre.isEmpty() || textoNota1.isEmpty() || textoNota2.isEmpty()) {
            vista.mostrarMensajeError("Por favor complete el nombre y las dos notas.");
            return;
        }

        try {
            // Se acepta coma o punto como separador decimal
            double nota1 = Double.parseDouble(textoNota1.replace(',', '.'));
            double nota2 = Double.parseDouble(textoNota2.replace(',', '.'));

            // 3. Guardar en la bodega (Modelo); el Modelo calcula la nota final
            modelo.agregarEstudiante(nombre, nota1, nota2);

            // 4. Limpiar cajas de texto
            vista.limpiarCampos();

            // 5. Refrescar la tabla en pantalla
            actualizarTabla();

        } catch (NumberFormatException ex) {
            vista.mostrarMensajeError("Las notas deben ser valores numéricos (ej: 3.5).");
        } catch (IllegalArgumentException ex) {
            vista.mostrarMensajeError(ex.getMessage());
        }
    }

    private void actualizarTabla() {
        // Pedimos el lienzo a la vista
        DefaultTableModel lienzo = vista.getModeloTabla();

        // Borramos todo lo pintado anteriormente para no duplicar datos
        lienzo.setRowCount(0);

        // Recorremos el ArrayList del Modelo y pintamos fila por fila
        for (Estudiante e : modelo.getEstudiantes()) {
            Object[] filaNueva = { e.getNombre(), formatear(e.getNota1()), formatear(e.getNota2()), formatear(e.getNotaFinal()) };
            lienzo.addRow(filaNueva); // Agrega la fila visual
        }

        vista.mostrarTotal(modelo.cantidadEstudiantes());
    }

    // Todas las notas con dos decimales (ej: 3.00, 2.63)
    private String formatear(double nota) {
        return String.format("%.2f", nota);
    }
}
