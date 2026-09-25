/**
 * ENTIDAD: representa un paciente de la clínica.
 */
public class Paciente {
    private String documento;
    private String nombre;
    private String diagnostico;
    private boolean dadoDeAlta;

    public Paciente(String documento, String nombre, String diagnostico) {
        this.documento = documento;
        this.nombre = nombre;
        this.diagnostico = diagnostico;
        this.dadoDeAlta = false; // Todo paciente ingresa hospitalizado
    }

    // Getters y Setters
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public boolean isDadoDeAlta() { return dadoDeAlta; }
    public void setDadoDeAlta(boolean dadoDeAlta) { this.dadoDeAlta = dadoDeAlta; }

    @Override
    public String toString() {
        return documento + " - " + nombre;
    }
}
