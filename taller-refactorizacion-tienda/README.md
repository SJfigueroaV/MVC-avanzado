# Taller: Mantenimiento y Refactorización Colaborativa

Refactorización de `TiendaSpaghetti.java`, un inventario de tienda en el que los datos, la interfaz y la
lógica están mezclados en un solo método, hacia la arquitectura MVC. El programa se comporta **exactamente
igual** antes y después: agregar productos, eliminar la fila seleccionada y ver el valor total del
inventario.

> El archivo original no venía adjunto con el taller. En `antes/` hay una versión representativa con los
> mismos vicios: listas dentro de la interfaz, reglas de negocio dentro de los botones y un único método
> gigante.

## Ejecutar

```bash
cd antes   && javac *.java && java TiendaSpaghetti
cd despues && javac *.java && java Main
```

## ¿A dónde fue cada pieza?

| Código en `TiendaSpaghetti` | Nuevo lugar |
|---|---|
| Listas paralelas `nombres`, `precios`, `cantidades` | Un `ArrayList<Producto>` en `TiendaModel` (y la entidad `Producto`). |
| Validación "precio positivo, cantidad no negativa" | `TiendaModel.agregarProducto()` |
| Cálculo del total (duplicado en los dos botones) | Un solo método: `TiendaModel.calcularValorInventario()` |
| Creación de `JFrame`, paneles, cajas, botones y tabla | Constructor de `TiendaView` |
| `ActionListener` de *Agregar* y *Eliminar Fila* | `TiendaController.procesarAgregar()` y `procesarEliminar()` |
| `addRow` / `removeRow` sueltos en cada botón | `TiendaController.actualizarVista()`: `setRowCount(0)` + repintar la lista completa |
| `main` que hacía todo | `Main`: solo crea el Modelo y la Vista y los une con el Controlador |

## Reflexión final: ¿por qué el spaghetti es más difícil de leer y reparar?

- **No hay dónde buscar.** En el spaghetti, para cambiar cómo se calcula el total hay que leer toda la
  construcción de la ventana y los dos botones. En MVC basta con abrir `TiendaModel`.
- **Lógica duplicada.** El cálculo del total estaba copiado en dos `ActionListener`. Si se corrige en uno y
  se olvida el otro, la pantalla muestra valores distintos según el botón que se pulsó. En MVC hay un solo
  método.
- **Datos frágiles.** Tres listas paralelas deben mantenerse sincronizadas a mano. Un `remove` olvidado
  desalinea precios y nombres. Con `Producto` cada dato viaja junto a los suyos.
- **La pantalla y los datos pueden desincronizarse.** El spaghetti modifica el `ArrayList` y la tabla por
  separado (`add` + `addRow`, `remove` + `removeRow`). En MVC la tabla siempre se repinta desde el Modelo,
  así que no puede mostrar algo que no esté en la bodega.
- **Imposible de reutilizar o probar.** La lógica del spaghetti solo existe dentro de los botones: no se
  puede usar desde una consola, una web o una prueba unitaria sin abrir la ventana. `TiendaModel` no
  depende de Swing y se puede probar solo.
- **Trabajo en equipo.** Con un único archivo de 500 líneas, dos personas que lo editan a la vez chocan en
  cada cambio. Con tres archivos, el diseñador trabaja en la Vista y otro compañero en el Modelo sin
  pisarse.
