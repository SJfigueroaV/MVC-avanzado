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
                ReportesController reportes = new ReportesController(modelo, ventanaReportes);
                new ClinicaController(modelo, ventanaClinica, reportes);

                ventanaReportes.setLocationRelativeTo(ventanaClinica);
                ventanaClinica.setVisible(true);
            }
        });
    }
}
