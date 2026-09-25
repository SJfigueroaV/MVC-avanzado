import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * CÓDIGO SPAGHETTI (versión "antes"): los datos, las reglas de negocio, la interfaz
 * gráfica y los eventos están mezclados dentro de un solo método enorme.
 * Se conserva únicamente como punto de partida de la refactorización.
 */
public class TiendaSpaghetti {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Los datos viven dentro de la interfaz...
                final ArrayList<String> nombres = new ArrayList<>();
                final ArrayList<Double> precios = new ArrayList<>();
                final ArrayList<Integer> cantidades = new ArrayList<>();

                JFrame ventana = new JFrame("Tienda - Inventario");
                ventana.setSize(600, 480);
                ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ventana.setLocationRelativeTo(null);

                final JTextField txtNombre = new JTextField(15);
                final JTextField txtPrecio = new JTextField(8);
                final JTextField txtCantidad = new JTextField(5);
                JButton btnAgregar = new JButton("Agregar Producto");
                JButton btnEliminar = new JButton("Eliminar Fila");
                final JLabel lblTotal = new JLabel("Valor del inventario: $ 0.00");

                JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
                panelFormulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
                panelFormulario.add(new JLabel("Nombre:"));
                panelFormulario.add(txtNombre);
                panelFormulario.add(new JLabel("Precio:"));
                panelFormulario.add(txtPrecio);
                panelFormulario.add(new JLabel("Cantidad:"));
                panelFormulario.add(txtCantidad);
                panelFormulario.add(new JLabel(""));
                panelFormulario.add(btnAgregar);

                final DefaultTableModel modeloTabla = new DefaultTableModel(
                        new String[]{"Nombre", "Precio", "Cantidad", "Subtotal"}, 0);
                final JTable tabla = new JTable(modeloTabla);

                JPanel panelInferior = new JPanel(new BorderLayout());
                panelInferior.add(lblTotal, BorderLayout.WEST);
                panelInferior.add(btnEliminar, BorderLayout.EAST);

                JPanel panelTabla = new JPanel(new BorderLayout(10, 10));
                panelTabla.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
                panelTabla.add(new JScrollPane(tabla), BorderLayout.CENTER);
                panelTabla.add(panelInferior, BorderLayout.SOUTH);

                ventana.add(panelFormulario, BorderLayout.NORTH);
                ventana.add(panelTabla, BorderLayout.CENTER);

                // ...y las reglas de negocio viven dentro de los botones
                btnAgregar.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String nombre = txtNombre.getText().trim();
                        if (nombre.isEmpty() || txtPrecio.getText().trim().isEmpty()
                                || txtCantidad.getText().trim().isEmpty()) {
                            JOptionPane.showMessageDialog(ventana, "Complete todos los campos.");
                            return;
                        }
                        double precio;
                        int cantidad;
                        try {
                            precio = Double.parseDouble(txtPrecio.getText().trim());
                            cantidad = Integer.parseInt(txtCantidad.getText().trim());
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(ventana, "Precio o cantidad inválidos.");
                            return;
                        }
                        if (precio <= 0 || cantidad < 0) {
                            JOptionPane.showMessageDialog(ventana, "El precio debe ser positivo y la cantidad no negativa.");
                            return;
                        }
                        nombres.add(nombre);
                        precios.add(precio);
                        cantidades.add(cantidad);
                        modeloTabla.addRow(new Object[]{nombre, precio, cantidad, precio * cantidad});
                        double total = 0;
                        for (int i = 0; i < precios.size(); i++) {
                            total += precios.get(i) * cantidades.get(i);
                        }
                        lblTotal.setText(String.format("Valor del inventario: $ %.2f", total));
                        txtNombre.setText("");
                        txtPrecio.setText("");
                        txtCantidad.setText("");
                    }
                });

                btnEliminar.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int fila = tabla.getSelectedRow();
                        if (fila == -1) {
                            JOptionPane.showMessageDialog(ventana, "Seleccione una fila.");
                            return;
                        }
                        nombres.remove(fila);
                        precios.remove(fila);
                        cantidades.remove(fila);
                        modeloTabla.removeRow(fila);
                        double total = 0;
                        for (int i = 0; i < precios.size(); i++) {
                            total += precios.get(i) * cantidades.get(i);
                        }
                        lblTotal.setText(String.format("Valor del inventario: $ %.2f", total));
                    }
                });

                ventana.setVisible(true);
            }
        });
    }
}
