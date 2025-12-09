package edu.upc.dsa;

import edu.upc.dsa.modelos.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class UserManagerImplTest {

    private UserManagerImpl manager;

    @Before
    public void setUp() {
        manager = UserManagerImpl.getInstance();

        // --- LIMPIEZA AGRESIVA ---
        // Recuperamos la lista real de la BBDD
        List<User> usuarios = manager.getUsuarios();

        // Si hay alguien, lo borramos.
        if (usuarios != null && !usuarios.isEmpty()) {
            System.out.println("Limpiando BBDD... Usuarios encontrados: " + usuarios.size());
            for (User u : usuarios) {
                manager.eliminarUsuario(u.getEmail());
            }
        }

        // Verificación de seguridad: ¿Se borró todo?
        List<User> check = manager.getUsuarios();
        if (!check.isEmpty()) {
            System.out.println("ADVERTENCIA: La limpieza falló, quedan " + check.size() + " usuarios.");
            // Intentamos borrar otra vez o lanzamos error si es crítico,
            // pero normalmente con el bucle de arriba basta.
        }
    }

    @After
    public void tearDown() {
        // Limpiamos al acabar para no dejar basura para el siguiente test
        this.setUp();
    }

    @Test
    public void testRegistrarUsuario() {
        // 1. Registro
        User u = manager.registrarUsuario("Guillermo", "gui@example.com", "1234");

        // Si u es null, es porque el 'setUp' no limpió bien y el usuario ya existía.
        assertNotNull("El usuario no debería ser null (fallo al registrar)", u);

        assertEquals("Guillermo", u.getNombre());
        assertEquals("gui@example.com", u.getEmail());

        // 2. Verificamos tamaño de la lista
        List<User> lista = manager.getUsuarios();
        assertEquals("Debe haber exactamente 1 usuario en la lista", 1, lista.size());
    }

    @Test
    public void testRegistrarUsuarioDuplicado() {
        // 1. Primer registro (éxito)
        manager.registrarUsuario("Guillermo", "gui@example.com", "1234");

        // 2. Segundo registro (debe fallar)
        User repetido = manager.registrarUsuario("Impostor", "gui@example.com", "abcd");

        // 3. Verificaciones
        assertNull("El sistema debe bloquear emails duplicados", repetido);
        assertEquals("Solo debe quedar el usuario original", 1, manager.getUsuarios().size());
    }

    @Test
    public void testGetUsuario() {
        manager.registrarUsuario("Guillermo", "gui@example.com", "1234");

        User u = manager.getUsuario("gui@example.com");
        assertNotNull("El usuario debería encontrarse en BBDD", u);
        assertEquals("Guillermo", u.getNombre());
    }
}
