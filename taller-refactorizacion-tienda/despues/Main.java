import javax.swing.SwingUtilities;

/**
 * CLASE PRINCIPAL: inicializa los componentes de la arquitectura MVC y los vincula.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TiendaModel modelo = new TiendaModel();
                TiendaView vista = new TiendaView();

                new TiendaController(modelo, vista);

                vista.setVisible(true);
            }
        });
    }
}
