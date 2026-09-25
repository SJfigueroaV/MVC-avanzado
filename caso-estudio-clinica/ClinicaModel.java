import java.util.ArrayList;

/**
 * MODELO: la "bodega" de la clínica. El ArrayList de pacientes ya no está atrapado
 * dentro de VentanaClinica: vive aquí, y cualquier ventana puede leerlo a través
 * de su controlador. No importa nada de javax.swing.
 */
public class ClinicaModel {
    private ArrayList<Paciente> pacientes = new ArrayList<>();

    // Quienes quieren enterarse cuando los datos cambian (Patrón Observer, sin Swing)
    private ArrayList<Runnable> observadores = new ArrayList<>();

    /**
     * Regla de negocio: todos los campos son obligatorios y el documento no se puede repetir.
     */
    public void registrarPaciente(String documento, String nombre, String diagnostico) {
        if (documento == null || documento.trim().isEmpty()
                || nombre == null || nombre.trim().isEmpty()
                || diagnostico == null || diagnostico.trim().isEmpty()) {
            throw new IllegalArgumentException("El documento, el nombre y el diagnóstico son obligatorios.");
        }
        if (buscarPorDocumento(documento.trim()) != null) {
            throw new IllegalArgumentException("Ya existe un paciente con el documento " + documento.trim() + ".");
        }
        pacientes.add(new Paciente(documento.trim(), nombre.trim(), diagnostico.trim()));
        notificarCambio();
    }

    /**
     * Regla de negocio: solo se puede dar de alta a un paciente que esté hospitalizado.
     */
    public void darDeAlta(String documento) {
        Paciente paciente = buscarPorDocumento(documento);
        if (paciente == null) {
            throw new IllegalArgumentException("No existe un paciente con el documento " + documento + ".");
        }
        if (paciente.isDadoDeAlta()) {
            throw new IllegalArgumentException(paciente.getNombre() + " ya fue dado de alta.");
        }
        paciente.setDadoDeAlta(true);
        notificarCambio();
    }

    public ArrayList<Paciente> getHospitalizados() {
        ArrayList<Paciente> hospitalizados = new ArrayList<>();
        for (Paciente p : pacientes) {
            if (!p.isDadoDeAlta()) {
                hospitalizados.add(p);
            }
        }
        return hospitalizados;
    }

    public ArrayList<Paciente> getDadosDeAlta() {
        ArrayList<Paciente> altas = new ArrayList<>();
        for (Paciente p : pacientes) {
            if (p.isDadoDeAlta()) {
                altas.add(p);
            }
        }
        return altas;
    }

    public int totalPacientes() {
        return pacientes.size();
    }

    public void agregarObservador(Runnable observador) {
        observadores.add(observador);
    }

    private Paciente buscarPorDocumento(String documento) {
        for (Paciente p : pacientes) {
            if (p.getDocumento().equals(documento)) {
                return p;
            }
        }
        return null;
    }

    private void notificarCambio() {
        for (Runnable observador : observadores) {
            observador.run();
        }
    }
}
