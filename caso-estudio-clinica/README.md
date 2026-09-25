# Caso de estudio: El Panel de Control de la Clínica SaludFatal

Solución al problema del programador anterior, que había metido el `ArrayList` de pacientes dentro de
`VentanaClinica`. Así, la nueva ventana de **Reportes** no tenía cómo acceder a los datos.

## Ejecutar

```bash
javac *.java && java Main
```

## Análisis

Con el `ArrayList` dentro de la ventana, los datos quedan atrapados en la interfaz. Otra ventana solo
podría leerlos pidiéndole la lista a `VentanaClinica`, y así una vista quedaría acoplada a otra. Si la
ventana principal se cierra o se rediseña, los reportes se rompen.

## Solución

1. El `ArrayList<Paciente>` se extrae a una clase propia, `ClinicaModel` (la bodega), que no importa Swing.
2. `Main` crea **una sola instancia** de `ClinicaModel` y la inyecta en los dos controladores:
   `ClinicaController` (panel principal) y `ReportesController` (ventana de reportes).
3. Ninguna de las dos vistas conoce a la otra ni guarda datos: cada una recibe lo que su controlador le
   pinta a partir de la misma bodega.
4. La navegación (botón *Ver Reportes*) se conecta en `Main`, así ninguno de los dos controladores
   depende del otro.
5. Cuando la bodega cambia, avisa a sus observadores (`agregarObservador`, con simples `Runnable`, sin
   Swing). Así, si el médico da de alta a un paciente con la ventana de reportes abierta, las dos tablas
   se actualizan al instante. El repintado se agenda con `SwingUtilities.invokeLater`, porque Swing solo
   se puede modificar desde su propio hilo (EDT).

### Por qué el Modelo se inyecta en los controladores y no directamente en las ventanas

El enunciado propone que el Controlador inyecte el mismo Modelo en `VentanaClinica` y `VentanaReportes`.
Aquí la misma instancia de `ClinicaModel` se comparte, pero se inyecta en los **controladores**
(`ClinicaController` y `ReportesController`), y cada uno pinta su ventana. Así se cumple lo que busca el
caso (las dos vistas muestran la misma bodega, sin estar acopladas) y se respeta la regla de las
diapositivas: la Vista no conoce al Modelo, solo muestra lo que el Controlador le pinta. Si mañana cambia
la forma de guardar los pacientes, ninguna ventana tiene que modificarse.

## Estructura

| Clase | Capa | Responsabilidad |
|---|---|---|
| `Paciente` | Entidad | Documento, nombre, diagnóstico y si ya fue dado de alta. Sin setters públicos: solo el Modelo puede darle de alta. |
| `ClinicaModel` | Modelo | Guarda el `ArrayList<Paciente>` (y solo entrega copias de él), valida los datos y que no se repita el documento, da de alta y avisa a los observadores cuando algo cambia. |
| `VentanaClinica` | Vista | Formulario de ingreso, tabla de hospitalizados y botones *Registrar Paciente*, *Dar de Alta* y *Ver Reportes*. |
| `VentanaReportes` | Vista | Indicadores (total, hospitalizados, altas) y tabla con el historial de altas. |
| `ClinicaController` | Controlador | Registra pacientes, da de alta al paciente seleccionado en la tabla y repinta la tabla de hospitalizados. |
| `ReportesController` | Controlador | Repinta los indicadores y el historial de altas a partir del mismo Modelo. |
| `Main` | — | Crea el Modelo compartido, las dos vistas y los dos controladores, y conecta el botón *Ver Reportes*. |

## Flujo de "Dar de Alta"

1. El médico selecciona una fila y hace clic en **Dar de Alta** (Vista).
2. `ClinicaController` le pide a la Vista el documento seleccionado y llama a `modelo.darDeAlta(documento)`.
3. El Modelo marca al paciente como dado de alta y avisa a sus observadores.
4. Los dos controladores ejecutan `setRowCount(0)` y vuelven a pintar sus tablas: el paciente desaparece
   de *Hospitalizados* y aparece en el historial de *Reportes*.
