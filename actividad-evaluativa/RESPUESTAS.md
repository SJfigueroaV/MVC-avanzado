# Actividad Evaluativa y Preguntas para Discusión

## 1. Pregunta tipo ensayo

**Enunciado:** explique detalladamente el proceso que debe ocurrir en la arquitectura MVC desde el
momento en que el usuario hace clic en el botón "Eliminar Fila" en la Vista, hasta que la fila desaparece
visualmente de la tabla. Indique las responsabilidades de cada uno de los tres componentes en este ciclo.

### El recorrido paso a paso

1. **Preparación (antes del clic).** Al arrancar el programa, el Controlador se registra como oyente del
   botón a través de un método de la Vista, por ejemplo `vista.agregarListenerEliminar(...)`. La Vista no
   sabe qué va a pasar cuando alguien pulse el botón; solo sabe a quién avisar.
2. **El clic (Vista).** El usuario selecciona una fila del `JTable` y pulsa **Eliminar Fila**. Swing
   dispara un `ActionEvent` y la Vista, que no toma ninguna decisión, se lo entrega al Controlador
   invocando su `actionPerformed`.
3. **El Controlador interpreta el evento.** Le pregunta a la Vista qué fila está seleccionada
   (`vista.getFilaSeleccionada()`). Si devuelve `-1` (no hay selección), le pide a la Vista que muestre un
   mensaje de error y el ciclo termina ahí. Si hay selección, traduce esa fila visual a un dato del
   negocio: un índice del `ArrayList` o, mejor aún, un identificador (código, documento).
4. **El Modelo ejecuta la operación.** El Controlador llama a `modelo.eliminarProducto(indice)`. El Modelo
   valida la regla de negocio (que el elemento exista, que se pueda eliminar) y lo quita del
   `ArrayList`. Si la regla falla, lanza una excepción que el Controlador atrapa y traduce en un mensaje
   para la Vista. El Modelo no toca la tabla ni sabe que existe.
5. **El Controlador sincroniza la pantalla.** Una vez que el Modelo cambió, el Controlador pide el lienzo
   (`vista.getModeloTabla()`), lo vacía con `setRowCount(0)` y recorre la lista actualizada del Modelo
   con un `for`, agregando una fila por cada elemento (`addRow`). También actualiza lo que dependa de los
   datos, como totales o contadores.
6. **La Vista se redibuja.** Al cambiar el `DefaultTableModel`, este le notifica internamente al `JTable`
   (`fireTableDataChanged`) y Swing vuelve a pintar la cuadrícula. La fila eliminada ya no está en el
   lienzo, así que desaparece de la pantalla.

### Responsabilidades de cada componente

| Componente | Qué hace en este ciclo | Qué **no** hace |
|---|---|---|
| **Vista** | Muestra la tabla y el botón, informa del clic y de qué fila está seleccionada, y se redibuja. | No borra datos, no valida reglas de negocio, no decide nada. |
| **Controlador** | Escucha el evento, obtiene la selección, valida la entrada, le ordena al Modelo eliminar, atrapa errores y vuelve a pintar la tabla desde el Modelo. | No guarda la lista ni implementa las reglas del negocio. |
| **Modelo** | Es la fuente de verdad: valida la regla de negocio y elimina el elemento del `ArrayList`. | No conoce Swing, ni la tabla, ni el botón. |

La idea clave es que la fila **no se borra de la tabla directamente**: se borra del Modelo, y la tabla
desaparece porque se repinta a partir de él. Así, la pantalla nunca muestra algo distinto de lo que hay
en la bodega.

---

## 2. Pregunta de análisis de código

**Enunciado:** se presenta un método `actionPerformed` que contiene sentencias SQL mezcladas con
sentencias `System.out.println` y `tabla.setValueAt()`. Identifique al menos 3 violaciones a la
arquitectura de software.

Un ejemplo del tipo de código descrito:

```java
public void actionPerformed(ActionEvent e) {
    int fila = tabla.getSelectedRow();
    double precio = Double.parseDouble(txtPrecio.getText());
    precio = precio * 1.19; // IVA
    try {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/tienda", "root", "1234");
        Statement st = con.createStatement();
        st.executeUpdate("UPDATE productos SET precio=" + precio + " WHERE id=" + tabla.getValueAt(fila, 0));
        System.out.println("Precio actualizado: " + precio);
        tabla.setValueAt(precio, fila, 2);
    } catch (SQLException ex) {
        System.out.println("Error: " + ex.getMessage());
    }
}
```

### Violaciones encontradas

1. **Acceso a datos dentro de la Vista/Controlador.** La conexión (`DriverManager.getConnection`) y la
   sentencia `UPDATE` pertenecen al Modelo (o a una capa de persistencia / DAO). Colocarlas en un
   `ActionListener` acopla la interfaz a una base de datos concreta. Si mañana se cambia MySQL por otro
   motor, hay que tocar la pantalla.
2. **Lógica de negocio en el manejador de eventos.** El cálculo del IVA (`precio * 1.19`) es una regla del
   negocio y debe vivir en el Modelo. Aquí no se puede reutilizar ni probar sin abrir la ventana, y se
   rompe el Principio de Responsabilidad Única.
3. **La tabla se modifica sin pasar por el Modelo.** `tabla.setValueAt(precio, fila, 2)` escribe
   directamente en la cuadrícula. Si la sentencia SQL falla a medias o el Modelo en memoria tiene otro
   valor, la pantalla y los datos quedan desincronizados. Lo correcto es actualizar el Modelo y que el
   Controlador repinte el `DefaultTableModel` a partir de él.
4. **`System.out.println` como forma de comunicarse con el usuario.** Los mensajes de éxito y de error se
   imprimen en una consola que el usuario de una aplicación de escritorio no ve. Informar resultados es
   tarea de la Vista (`JOptionPane`, una etiqueta de estado), y los registros técnicos deben ir a un
   sistema de *logging*, no mezclados con la lógica.
5. **Operación pesada en el hilo de la interfaz.** La consulta a la base de datos corre dentro del
   `actionPerformed`, es decir, en el *Event Dispatch Thread*. Mientras la base de datos responde, la
   ventana se congela ("No Responde"). Debería ejecutarse en segundo plano, por ejemplo con `SwingWorker`.
6. **Falta de validación temprana y SQL inseguro.** Se llama a `Double.parseDouble` sin verificar que la
   caja no esté vacía, y no se revisa que haya una fila seleccionada (`fila` podría ser `-1`). Además, la
   consulta se arma concatenando texto, lo que la expone a inyección SQL. Lo correcto es usar
   `PreparedStatement` dentro de la capa de datos. La conexión tampoco se cierra (debería usarse
   *try-with-resources*).

---

## 3. Pregunta de selección múltiple

**¿Cuál es el propósito del `DefaultTableModel` en Java Swing?**

**Respuesta: c)** Proveer una estructura en memoria (filas y columnas) que el componente visual `JTable`
utiliza para dibujar los datos en pantalla.

- **a)** es incorrecta: el `DefaultTableModel` vive en la memoria RAM y se pierde al cerrar el programa.
  No persiste nada en disco.
- **b)** es incorrecta: a pesar de llamarse "Model", no es el Controlador ni el Modelo de MVC. Es parte de
  la Vista: es el lienzo que el Controlador pinta.
- **d)** es incorrecta: los colores y el tamaño de la ventana se configuran en el `JFrame` y en los
  *renderers*, no en el modelo de la tabla.

---

## 4. Preguntas para discusión

### 4.1 ¿Por qué es un grave error de diseño que la clase `FacturaModel` tenga una línea que diga `import javax.swing.JTextField;`?

Porque rompe la regla central de MVC: **el Modelo no debe conocer la interfaz gráfica**. Ese `import`
indica que el Modelo lee o escribe directamente en una caja de texto, y eso trae varias consecuencias:

- **Acoplamiento a una tecnología de presentación.** `FacturaModel` solo funciona si existe una ventana
  Swing. No se podría reutilizar en una aplicación web, en una app móvil, en una API REST ni en un
  proceso por lotes que genere facturas de noche.
- **Dependencia invertida.** En MVC, la Vista y el Controlador dependen del Modelo, nunca al revés. Si el
  diseñador renombra o reemplaza el `JTextField` (por ejemplo, por un `JSpinner`), el cálculo de la
  factura deja de compilar, aunque las reglas de facturación no hayan cambiado.
- **Imposible de probar de forma aislada.** Una prueba unitaria que verifique el cálculo del IVA o del
  total tendría que crear componentes gráficos, y en un servidor de integración continua sin pantalla ni
  siquiera se podría ejecutar.
- **Viola el Principio de Responsabilidad Única.** La clase tendría dos razones para cambiar: cambios en
  las reglas de facturación y cambios en el diseño de la pantalla.

Lo correcto es que `FacturaModel` reciba y devuelva **tipos de dato puros** (`double`, `String`,
`Factura`). El Controlador es quien toma el texto del `JTextField`, lo convierte y se lo entrega al
Modelo.

### 4.2 En sistemas web (HTML/JavaScript), ¿qué rol jugaría el navegador web y qué rol jugaría el servidor donde está alojado el código Java?

En el MVC web clásico (por ejemplo, Spring MVC o Jakarta EE con servlets y JSP/Thymeleaf):

- **El navegador (Chrome/Firefox) actúa como la Vista**, o más exactamente, como el lugar donde se
  muestra la Vista. Renderiza el HTML/CSS, captura los eventos del usuario (clics, formularios) y los
  envía al servidor como peticiones HTTP (`GET`, `POST`, `DELETE`). Cumple el mismo papel que el `JFrame`:
  muestra y avisa, pero no guarda la lógica de negocio ni los datos oficiales.
- **El servidor Java aloja el Controlador y el Modelo.**
  - El **Controlador** es la clase que recibe la petición HTTP (un `@Controller` o un servlet). Cumple el
    mismo papel que el `ActionListener`: lee los parámetros del formulario, los valida y llama al Modelo.
  - El **Modelo** son las clases de dominio, los servicios y el acceso a la base de datos. Aplica las
    reglas de negocio y guarda la información de forma persistente.
  - Finalmente, el Controlador elige una plantilla (la Vista del lado del servidor), la llena con los
    datos del Modelo y devuelve HTML (o JSON) como respuesta.

La diferencia más importante con Swing es que **Vista y Controlador se comunican por la red, mediante
HTTP**, en lugar de llamarse directamente en memoria. En cada clic, el ciclo "evento → Controlador →
Modelo → repintar la Vista" viaja del navegador al servidor y de vuelta.

En las aplicaciones modernas de una sola página (React, Angular, Vue), el navegador también ejecuta un
MVC propio en JavaScript para la interfaz. El servidor Java queda como una API REST que expone el Modelo
y devuelve JSON. Aun así, se mantiene el mismo principio: la **fuente de verdad** de los datos y las
reglas de negocio vive en el servidor, y el navegador solo presenta e interactúa.
