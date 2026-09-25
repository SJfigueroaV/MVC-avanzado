import javax.swing.SwingUtilities;

/**
 * CLASE PRINCIPAL: inicializa los componentes de la arquitectura MVC y los vincula.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Creamos el Modelo (Los Datos) y la Vista (La Pantalla)
                CursoModel modelo = new CursoModel();
                CursoView vista = new CursoView();

                // Creamos el Controlador y le pasamos el Modelo y la Vista
                new CursoController(modelo, vista);

                // Hacemos visible la ventana
                vista.setVisible(true);
            }
        });
    }
}
