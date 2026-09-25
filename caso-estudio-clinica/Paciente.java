/**
 * ENTIDAD: representa un paciente de la clínica.
 * Sus datos no cambian después del ingreso; el único estado que cambia es el alta,
 * y eso solo lo hace ClinicaModel, que aplica la regla de negocio.
 */
public class Paciente {
    private final String documento;
    private final String nombre;
    private final String diagnostico;
    private boolean dadoDeAlta;

    public Paciente(String documento, String nombre, String diagnostico) {
        this.documento = documento;
        this.nombre = nombre;
        this.diagnostico = diagnostico;
        this.dadoDeAlta = false; // Todo paciente ingresa hospitalizado
    }

    // Getters
    public String getDocumento() { return documento; }
    public String getNombre() { return nombre; }
    public String getDiagnostico() { return diagnostico; }
    public boolean isDadoDeAlta() { return dadoDeAlta; }

    // Sin modificador de acceso: pensado para que solo lo use ClinicaModel
    void marcarDadoDeAlta() {
        this.dadoDeAlta = true;
    }

    @Override
    public String toString() {
        return documento + " - " + nombre;
    }
}
