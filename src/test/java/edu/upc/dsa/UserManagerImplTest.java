package edu.upc.dsa;

import edu.upc.dsa.modelos.User;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UserManagerImplTest {

    private UserManagerImpl manager;

    @Before
    public void setUp() {
        // Reiniciamos la instancia manualmente para evitar datos previos
        manager = UserManagerImpl.getInstance();
        // Limpiar lista de usuarios si ya había alguno (por pruebas previas)
        manager.getUsuarios().clear();
    }

    @Test
    public void testRegistrarUsuario() {
        User u = manager.registrarUsuario("Guillermo", "gui@example.com", "1234");
        assertNotNull("El usuario no debe ser null", u);
        assertEquals("El nombre debe coincidir", "Guillermo", u.getNombre());
        assertEquals("El email debe coincidir", "gui@example.com", u.getEmail());
        assertEquals("Debe haber 1 usuario registrado", 1, manager.getUsuarios().size());
    }

    @Test
    public void testRegistrarUsuarioDuplicado() {
        manager.registrarUsuario("Guillermo", "gui@example.com", "1234");
        User repetido = manager.registrarUsuario("Otro", "gui@example.com", "abcd");
        assertNull("No se debe permitir registrar dos usuarios con el mismo email", repetido);
        assertEquals("Debe seguir habiendo solo un usuario", 1, manager.getUsuarios().size());
    }

    @Test
    public void testGetUsuario() {
        manager.registrarUsuario("Guillermo", "gui@example.com", "1234");
        User u = manager.getUsuario("gui@example.com");
        assertNotNull("El usuario debe existir", u);
        assertEquals("El nombre debe ser Guillermo", "Guillermo", u.getNombre());
    }

    @Test
    public void testGetUsuarios() {
        manager.registrarUsuario("A", "a@mail.com", "123");
        manager.registrarUsuario("B", "b@mail.com", "456");
        List<User> lista = manager.getUsuarios();
        assertEquals("Debe haber 2 usuarios registrados", 2, lista.size());
    }
}
