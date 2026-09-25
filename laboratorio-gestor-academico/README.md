# Laboratorio práctico: Gestor Académico con JTable

Aplicación de escritorio en Java Swing que administra las notas de los estudiantes en un
`ArrayList<Estudiante>` alojado en el Modelo y las muestra en un `JTable`, aplicando el patrón
Modelo-Vista-Controlador.

## Ejecutar

```bash
javac *.java && java Main
```

## Estructura

| Clase | Capa | Responsabilidad |
|---|---|---|
| `Estudiante` | Entidad | Nombre, nota 1, nota 2 y nota final. |
| `CursoModel` | Modelo | Guarda los estudiantes en un `ArrayList<Estudiante>`, valida que el nombre no esté vacío y que las notas estén entre 0.0 y 5.0, y calcula la nota final (promedio de las dos notas). No importa Swing. |
| `CursoView` | Vista | Ventana con las cajas Nombre, Nota 1 y Nota 2, el botón *Registrar* y un `JTable` de 4 columnas (Nombre, Nota 1, Nota 2, Nota Final) envuelto en un `JScrollPane`. |
| `CursoController` | Controlador | Escucha el botón, valida que las cajas no estén vacías antes de usar `Double.parseDouble()`, le pasa los datos al Modelo y vuelve a pintar el `DefaultTableModel` con la lista completa. |
| `Main` | — | Crea el Modelo y la Vista, los conecta con el Controlador y muestra la ventana. |

## Cumplimiento de la rúbrica

- **Diseño de la interfaz (30%):** formulario arriba y tabla abajo; la tabla va dentro de un
  `JScrollPane` para que se vean los títulos de las columnas, y sus celdas no se pueden editar a mano.
- **Lógica de promedios en el Modelo (30%):** `CursoModel.calcularNotaFinal()` calcula
  `(nota1 + nota2) / 2` redondeado a dos decimales. El Controlador nunca hace la cuenta.
- **Actualización dinámica sin duplicar filas (40%):** `CursoController.actualizarTabla()` llama a
  `lienzo.setRowCount(0)` antes del `for` que vuelve a pintar el `ArrayList` completo. Así, cada
  registro deja en la tabla exactamente una fila por estudiante.
