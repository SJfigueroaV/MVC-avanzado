import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

/**
 * CLASE PRINCIPAL: crea UNA sola bodega (ClinicaModel) y la inyecta en los dos
 * controladores, de modo que el panel de control y los reportes leen los mismos datos.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Un único Modelo compartido
                ClinicaModel modelo = new ClinicaModel();

                // Las dos Vistas
                VentanaClinica ventanaClinica = new VentanaClinica();
                VentanaReportes ventanaReportes = new VentanaReportes();

                // Los Controladores reciben el MISMO Modelo
                final ReportesController reportes = new ReportesController(modelo, ventanaReportes);
                new ClinicaController(modelo, ventanaClinica);

                // La navegación entre ventanas se conecta aquí, así ningún controlador depende del otro
                ventanaClinica.agregarListenerReportes(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        reportes.mostrarVentana();
                    }
                });

                ventanaReportes.setLocationRelativeTo(ventanaClinica);
                ventanaClinica.setVisible(true);
            }
        });
    }
}
