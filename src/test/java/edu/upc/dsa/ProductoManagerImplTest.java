package edu.upc.dsa;

import edu.upc.dsa.modelos.Producto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class ProductoManagerImplTest {

    // Usamos la interfaz
    private ProductoManager productoManager;

    @Before
    public void setUp() {
        productoManager = ProductoManagerImpl.getInstance();

        // --- LIMPIEZA REAL DE LA BASE DE DATOS ---
        // 1. Pedimos todos los productos que hay en la BBDD
        List<Producto> lista = productoManager.getProductos();

        // 2. Si hay productos, los borramos uno a uno
        if (lista != null && !lista.isEmpty()) {
            for (Producto p : lista) {
                // ATENCIÓN: Necesitas tener este método 'eliminarProducto' en tu Manager
                // Si tu método pide ID, usa p.getId(). Si pide nombre, usa p.getNombre()
                productoManager.eliminarProducto(p.getId());
            }
        }
        // -----------------------------------------
    }

    @After
    public void tearDown() {
        // Opcional: Dejamos la BBDD limpia al terminar también
        this.setUp();
    }

    @Test
    public void testAnadirProductoExitoso() {
        // Ahora sí funcionará porque la BBDD está vacía al empezar
        Producto p = productoManager.addProducto("Poción de Vida", 50);

        assertNotNull("El producto no debería ser null (falló al crear)", p);
        assertEquals("El nombre debe coincidir", "Poción de Vida", p.getNombre());
        // Delta 0.0 es margen de error para doubles
        assertEquals("El precio debe coincidir", 50, p.getPrecio(), 0.0);

        // Verificamos que ahora hay 1 solo producto
        assertEquals("Debe haber 1 producto en la lista", 1, productoManager.getProductos().size());
    }

    @Test
    public void testAnadirProductoDuplicadoNombre() {
        // 1. Añadimos el primero
        productoManager.addProducto("Poción de Vida", 50);

        // 2. Intentamos añadir el segundo idéntico
        Producto repetido = productoManager.addProducto("Poción de Vida", 100);

        // 3. Debe devolver null porque ya existe
        assertNull("No se debe permitir añadir duplicados", repetido);

        // 4. La lista debe seguir teniendo solo 1
        assertEquals("Debe seguir habiendo solo 1 producto", 1, productoManager.getProductos().size());
    }

    @Test
    public void testListaProductos() {
        productoManager.addProducto("Escudo", 100);
        productoManager.addProducto("Espada", 200);

        // Ahora sí dará 2, porque el setUp borró los 8 antiguos
        assertEquals(2, productoManager.getProductos().size());
    }
}