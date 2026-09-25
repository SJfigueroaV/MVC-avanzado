import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        double notaFinal = calcularNotaFinal(nota1, nota2);
        estudiantes.add(new Estudiante(nombre.trim(), nota1, nota2, notaFinal));
    }

    /**
     * Calcula la nota final como el promedio de las dos notas, redondeado a dos decimales.
     */
    public double calcularNotaFinal(double nota1, double nota2) {
        double promedio = (nota1 + nota2) / 2.0;
        return Math.round(promedio * 100.0) / 100.0;
    }

    /**
     * Devuelve la lista en modo solo lectura: quien la recibe puede recorrerla,
     * pero no agregar ni borrar estudiantes saltándose las reglas del Modelo.
     */
    public List<Estudiante> getEstudiantes() {
        return Collections.unmodifiableList(estudiantes);
    }

    public int cantidadEstudiantes() {
        return estudiantes.size();
    }

    private void validarNota(double nota, String campo) {
        // NaN no es menor ni mayor que nada, por eso se descarta aparte
        if (Double.isNaN(nota) || nota < NOTA_MINIMA || nota > NOTA_MAXIMA) {
            throw new IllegalArgumentException(campo + " debe estar entre " + NOTA_MINIMA + " y " + NOTA_MAXIMA + ".");
        }
    }
}
