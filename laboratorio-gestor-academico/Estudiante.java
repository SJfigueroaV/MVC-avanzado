/**
 * ENTIDAD: representa un estudiante con sus dos notas parciales y su nota final.
 * Es inmutable: la nota final la calcula el Modelo al crearlo, y nadie puede
 * cambiar una nota sin que la nota final quede desactualizada.
 */
public class Estudiante {
    private final String nombre;
    private final double nota1;
    private final double nota2;
    private final double notaFinal;

    public Estudiante(String nombre, double nota1, double nota2, double notaFinal) {
        this.nombre = nombre;
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.notaFinal = notaFinal;
    }

    // Getters
    public String getNombre() { return nombre; }
    public double getNota1() { return nota1; }
    public double getNota2() { return nota2; }
    public double getNotaFinal() { return notaFinal; }

    @Override
    public String toString() {
        return nombre + " - " + notaFinal;
    }
}
