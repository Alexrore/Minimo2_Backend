package edu.upc.dsa;

import edu.upc.dsa.modelos.User;

import edu.upc.dsa.modelos.Inventory;
import java.util.*;
import edu.upc.dsa.dao.*;
import org.apache.log4j.Logger;

public class UserManagerImpl implements UserManager {
    private static UserManagerImpl instance;
    private List<User> usuarios;
    final static Logger logger = Logger.getLogger(UserManagerImpl.class);

    private UserManagerImpl() {
        usuarios = new ArrayList<>();
    }

    public static UserManagerImpl getInstance() {
        if (instance == null) instance = new UserManagerImpl();
        return instance;
    }

    @Override
    public User registrarUsuario(String nombre, String email, String password) {
        Session session = null;
        User u = null;



            // 1. Crear objeto Java
            u = new User(nombre, email, password);

            // 2. Generar y asignar código AQUÍ (Antes de tocar la BBDD)
            // Esto elimina la necesidad de usar funciones complejas de update luego.
            String codigo = String.valueOf((int) (Math.random() * 900000) + 100000);
            u.setCodigoVerificacion(codigo);
            u.setEmailVerificado(false); // O el valor numérico 0 si usas int

        try {
            session = FactorySession.openSession();
            session.save(u); // Si esto falla (duplicado), salta al catch

            logger.info("Usuario registrado: " + email);
            return u; // Solo llegamos aquí si NO hay error

        } catch (Exception e) {
            // SI ENTRA AQUÍ, ES QUE HUBO ERROR (DUPLICADO)
            logger.error("Error al registrar: " + e.getMessage());
            return null; // <--- ESTO ES LA CLAVE. Tenemos que devolver null para que el test sepa que falló.
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public User loginUsuario(String email, String password) {
        Session session = null;
        try {
            session = FactorySession.openSession();
            List<Object> usersList = session.findAll(User.class);

            for (Object obj : usersList) {
                User u = (User) obj;
                if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                    logger.info("Login exitoso: " + email);

                    // --- NUEVO CÓDIGO: CARGAR INVENTARIO ---
                    // Llamamos al método que creaste en ProductoManager para llenar la lista
                    List<Inventory> suInventario = ProductoManagerImpl.getInstance().getInventario(u.getId());
                    u.setInventario(suInventario);
                    // ---------------------------------------

                    return u;
                }
            }
        } catch (Exception e) {
            logger.error("Error en login: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }

        logger.warn("Usuario o password incorrectos: " + email);
        return null;
    }

    public User getUsuario(String email) {
        Session session = null;
        User usuarioEncontrado = null;
        try {
            session = FactorySession.openSession();
            List<Object> allUsers = session.findAll(User.class);

            if (allUsers != null) {
                for (Object obj : allUsers) {
                    User u = (User) obj;
                    if (u.getEmail().equals(email)) {

                        // --- NUEVO CÓDIGO: CARGAR INVENTARIO ---
                        // Recuperamos el inventario para este usuario específico
                        List<Inventory> suInventario = ProductoManagerImpl.getInstance().getInventario(u.getId());
                        u.setInventario(suInventario);
                        // ---------------------------------------

                        usuarioEncontrado = u;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error buscando usuario: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return usuarioEncontrado;
    }


    // En UserManagerImpl.java

    @Override
    public List<User> getUsuarios() {
        Session session = null;
        List<User> lista = new ArrayList<>();
        try {
            session = FactorySession.openSession();
            // findAll devuelve List<Object>, lo casteamos a User
            List<Object> rawList = session.findAll(User.class);

            if (rawList != null) {
                for (Object o : rawList) {
                    lista.add((User) o);
                }
            }
        } catch (Exception e) {
            // Si la tabla está vacía o falla, devolvemos lista vacía, no error
            logger.warn("Error o lista vacía: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }
        return lista;
    }


    public boolean enviarCodigoVerificacion(User u) {
        Session session = null;
        try {
            // 1. LÓGICA DE SEGURIDAD (Esto arregla tu null)
            // Si por alguna razón el objeto no tiene el código cargado, lo generamos aquí.
            if (u.getCodigoVerificacion() == null || u.getCodigoVerificacion().isEmpty()) {
                String nuevoCodigo = String.valueOf((int) (Math.random() * 900000) + 100000);
                u.setCodigoVerificacion(nuevoCodigo);
                System.out.println("⚠️ El código venía nulo, se ha generado uno nuevo: " + nuevoCodigo);
            }

            // 2. Ahora sí, actualizamos la BBDD con un código VALIDO.
            session = FactorySession.openSession();
            session.update(u);

            System.out.println("═══════════════════════════════════════");
            System.out.println("📧 CÓDIGO DE VERIFICACIÓN");
            System.out.println("Email: " + u.getEmail());
            // Ahora esto NUNCA será null
            System.out.println("Código: " + u.getCodigoVerificacion());
            System.out.println("═══════════════════════════════════════");
            return true;

        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error completo para ver si es SQL
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public boolean verificarCodigo(String email, String codigoUsuario) {
        Session session = null;
        try {
            // 1. Obtener usuario desde la BBDD (Tendremos el ID correcto)
            User u = this.getUsuario(email);

            if (u == null) {
                logger.warn("Usuario no encontrado: " + email);
                return false;
            }

            // 2. Comparar códigos
            if (u.getCodigoVerificacion() != null && u.getCodigoVerificacion().equals(codigoUsuario)) {

                // 3. Modificar objeto
                u.setEmailVerificado(true); // O set(1)

                // 4. Actualizar en BBDD
                session = FactorySession.openSession();
                session.update(u); // Esto funcionará porque 'u' tiene ID al venir del 'findAll'

                logger.info("Usuario validado: " + email);
                return true;
            }

            logger.warn("Código incorrecto.");
            return false;

        } catch (Exception e) {
            logger.error("Error verificando: " + e.getMessage());
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    // En UserManagerImpl.java

    public void eliminarUsuario(String email) {
        Session session = null;
        try {
            // 1. PRIMERO RECUPERAMOS EL USUARIO COMPLETO (CON ID)
            // Usamos tu función getUsuario que sabemos que funciona
            User usuarioReal = this.getUsuario(email);

            if (usuarioReal != null) {
                session = FactorySession.openSession();
                // 2. AHORA BORRAMOS EL OBJETO QUE SÍ TIENE ID
                session.delete(usuarioReal);
                logger.info("Usuario eliminado correctamente de BBDD: " + email);
            } else {
                logger.warn("Intentando borrar usuario inexistente: " + email);
            }
        } catch (Exception e) {
            logger.error("Error al eliminar usuario: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }
    }
}
