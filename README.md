# Taller: MVC Avanzado — Listas dinámicas con JTable

Fundamentos de Diseño de Software
Programa Ciencias de la Computación e Inteligencia Artificial
Escuela de Ciencias Exactas e Ingeniería — Universidad Sergio Arboleda

**Integrantes:**

- Santiago Figueroa
- Sarai Mejía

Aplicaciones de escritorio en Java Swing que muestran listas dinámicas (`ArrayList`) en un `JTable`
aplicando el patrón MVC, junto con las respuestas de la actividad evaluativa y las preguntas para discusión.

## Contenido

| Carpeta | Punto del taller |
|---|---|
| [`caso-estudio-clinica/`](caso-estudio-clinica/) | Caso de estudio: El Panel de Control de la Clínica SaludFatal |
| [`laboratorio-gestor-academico/`](laboratorio-gestor-academico/) | Laboratorio práctico: Gestor Académico con JTable |
| [`taller-refactorizacion-tienda/`](taller-refactorizacion-tienda/) | Taller: Mantenimiento y Refactorización Colaborativa (`TiendaSpaghetti` → MVC) |
| [`actividad-evaluativa/`](actividad-evaluativa/) | Actividad evaluativa (ensayo, análisis de código, selección múltiple) y preguntas para discusión |

## Requisitos

Un JDK (Java 8 o superior). No se necesitan librerías externas: Java Swing viene incluido en el JDK.

## Ejecutar

Cada ejercicio se compila y se ejecuta por separado:

```bash
cd laboratorio-gestor-academico        && javac *.java && java Main
cd caso-estudio-clinica                && javac *.java && java Main
cd taller-refactorizacion-tienda/antes   && javac *.java && java TiendaSpaghetti
cd taller-refactorizacion-tienda/despues && javac *.java && java Main
```

## El patrón aplicado

- **El Modelo** (la bodega) guarda el `ArrayList` y las reglas de negocio, y nunca importa `javax.swing`.
- **La Vista** (la vitrina) prepara el `JTable` vacío dentro de un `JScrollPane` y expone sus botones.
- **El Controlador** (el puente) escucha los botones, valida la entrada antes de convertir números, le
  pide el trabajo al Modelo y repinta el `DefaultTableModel`. Siempre llama a `setRowCount(0)` antes del
  `for`, para no duplicar filas.
