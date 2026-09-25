import java.util.ArrayList;

/**
 * MODELO: la "bodega" del curso. Guarda los estudiantes en una lista dinámica y
 * aplica las reglas de negocio (validación de notas y cálculo del promedio).
 * No conoce la interfaz gráfica: no importa nada de javax.swing.
 */
public class CursoModel {
    public static final double NOTA_MINIMA = 0.0;
    public static final double NOTA_MAXIMA = 5.0;

    private ArrayList<Estudiante> estudiantes = new ArrayList<>();

    /**
     * Regla de negocio: el nombre es obligatorio y las notas deben estar entre 0.0 y 5.0.
     * Al registrar al estudiante se calcula de inmediato su nota final.
     */
    public void agregarEstudiante(String nombre, double nota1, double nota2) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del estudiante es obligatorio.");
        }
        validarNota(nota1, "Nota 1");
        validarNota(nota2, "Nota 2");

        Estudiante estudiante = new Estudiante(nombre.trim(), nota1, nota2);
        calcularNotaFinal(estudiante);
        estudiantes.add(estudiante);
    }

    /**
     * Calcula la nota final como el promedio de las dos notas, redondeado a dos decimales.
     */
    public void calcularNotaFinal(Estudiante estudiante) {
        double promedio = (estudiante.getNota1() + estudiante.getNota2()) / 2.0;
        estudiante.setNotaFinal(Math.round(promedio * 100.0) / 100.0);
    }

    /**
     * Recalcula la nota final de todos los estudiantes del curso.
     */
    public void calcularNotasFinales() {
        for (Estudiante e : estudiantes) {
            calcularNotaFinal(e);
        }
    }

    public ArrayList<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public int cantidadEstudiantes() {
        return estudiantes.size();
    }

    private void validarNota(double nota, String campo) {
        if (nota < NOTA_MINIMA || nota > NOTA_MAXIMA) {
            throw new IllegalArgumentException(campo + " debe estar entre " + NOTA_MINIMA + " y " + NOTA_MAXIMA + ".");
        }
    }
}
