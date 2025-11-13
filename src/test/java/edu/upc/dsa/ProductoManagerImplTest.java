package edu.upc.dsa;

import edu.upc.dsa.modelos.Producto;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ProductoManagerImplTest {

    private ProductoManagerImpl manager;

    @Before
    public void setUp() {
        manager = ProductoManagerImpl.getInstance();
        manager.listadeproductos().clear();
    }

    @Test
    public void testAnadirProductoExitoso() {
        Producto p = manager.anadirproducto("Poción de Vida", 50);

        assertNotNull("El producto no debe ser null", p);
        assertNotNull("El producto debe tener un ID autogenerado", p.getId());
        assertEquals("El nombre debe coincidir", "Poción de Vida", p.getNombreproducto());
        assertEquals("El precio debe coincidir", 50, p.getPrecio());
        assertEquals("Debe haber 1 producto en la lista", 1, manager.listadeproductos().size());
    }

    @Test
    public void testAnadirProductoDuplicadoNombre() {
        manager.anadirproducto("Poción de Vida", 50);

        Producto repetido = manager.anadirproducto("Poción de Vida", 100);

        assertNull("No se debe permitir añadir un producto con nombre repetido", repetido);
        assertEquals("Debe seguir habiendo solo 1 producto", 1, manager.listadeproductos().size());
    }

    @Test
    public void testGetProductoPorNombre() {
        manager.anadirproducto("Poción de Vida", 50);
        Producto p = manager.getproducto("Poción de Vida");

        assertNotNull("El producto debe existir", p);
        assertEquals("El nombre debe ser 'Poción de Vida'", "Poción de Vida", p.getNombreproducto());
    }

    @Test
    public void testEncontrarProductoPorId() {
        Producto p1 = manager.anadirproducto("Poción de Vida", 50);
        String idGenerado = p1.getId();

        Producto p2 = manager.encontrarproducto(idGenerado);

        assertNotNull("El producto debe existir si se busca por su ID generado", p2);
        assertEquals("Los IDs deben coincidir", idGenerado, p2.getId());
    }

    @Test
    public void testListadoDeProductos() {
        manager.anadirproducto("Poción", 50);
        manager.anadirproducto("Espada", 150);

        List<Producto> lista = manager.listadeproductos();

        assertEquals("La lista debe contener 2 productos", 2, lista.size());
    }

    @Test
    public void testGetProductoNoExistente() {
        manager.anadirproducto("Poción", 50);

        Producto pNombre = manager.getproducto("Espada");
        assertNull("El producto 'Espada' no debería existir (buscado por nombre)", pNombre);

        Producto pId = manager.encontrarproducto("ID-Falso-12345");
        assertNull("El producto 'ID-Falso-12345' no debería existir (buscado por ID)", pId);
    }
}