package edu.upc.dsa;

import edu.upc.dsa.dao.FactorySession;
import edu.upc.dsa.dao.Session;
import edu.upc.dsa.modelos.Producto;
import edu.upc.dsa.modelos.User;
import edu.upc.dsa.modelos.Inventory;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

public class ProductoManagerImpl implements ProductoManager {
    final static Logger logger = Logger.getLogger(ProductoManagerImpl.class);
    private static ProductoManagerImpl instance;

    public static ProductoManager getInstance() {
        if (instance == null) instance = new ProductoManagerImpl();
        return instance;
    }

    private ProductoManagerImpl() {}

    // 1. Obtener todos los productos
    @Override
    public List<Producto> getProductos() {
        Session session = null;
        List<Producto> lista = new LinkedList<>();
        try {
            session = FactorySession.openSession();
            // Traemos todo sin filtrar (tu ORM no soporta parámetros aquí)
            List<Object> objetos = session.findAll(Producto.class);

            for (Object obj : objetos) {
                lista.add((Producto) obj);
            }
        } catch (Exception e) {
            logger.error("Error al obtener productos: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }
        return lista;
    }

    // 2. Obtener producto por nombre (Iterando la lista manualmente)
    @Override
    public Producto getProducto(String nombre) {
        Session session = null;
        try {
            session = FactorySession.openSession();
            List<Object> objetos = session.findAll(Producto.class);

            for (Object obj : objetos) {
                Producto p = (Producto) obj;
                if (p.getNombre().equals(nombre)) {
                    return p;
                }
            }
        } catch (Exception e) {
            logger.error("Error buscando producto " + nombre + ": " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }
        return null;
    }

    // 3. Añadir producto (SIN DESCRIPCIÓN)
    @Override
    public Producto addProducto(String nombre, int precio) {
        // 1. Verificamos si ya existe (para el error 409 Conflict del servicio)
        if (this.getProducto(nombre) != null) {
            logger.warn("El producto " + nombre + " ya existe.");
            return null;
        }

        Session session = null;
        try {
            session = FactorySession.openSession();
            Producto p = new Producto(nombre, precio);
            session.save(p);
            logger.info("Producto añadido: " + nombre);
            return p; // Retornamos el objeto para que el servicio devuelva 201 Created
        } catch (Exception e) {
            logger.error("Error al añadir producto: " + e.getMessage());
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    // 4. Comprar Producto
    @Override
    public int comprarProducto(int itemId, int userId) {
        Session session = null;
        try {
            session = FactorySession.openSession();

            // Recuperamos usuario y producto
            User usuario = (User) session.get(User.class, userId);
            Producto producto = (Producto) session.get(Producto.class, itemId);

            // CASE 1: Usuario no existe
            if (usuario == null) {
                return 1;
            }
            // CASE 2: Producto no existe
            if (producto == null) {
                return 2;
            }
            // CASE 3: Saldo insuficiente
            if (usuario.getMonedas() < producto.getPrecio()) {
                return 3;
            }

            // CASE 0: ÉXITO - Procedemos a la compra

            // A. Restar dinero (Igual que antes)
            int nuevoSaldo = usuario.getMonedas() - producto.getPrecio();
            usuario.setMonedas(nuevoSaldo);
            session.update(usuario);

            // ---------------------------------------------------------
            // B. GESTIÓN DEL INVENTARIO (CORREGIDO)
            // ---------------------------------------------------------

            // 1. Buscamos si el usuario ya tiene este objeto en su inventario.
            // NOTA: Como tu ORM parece simple (DSA), es probable que no tengas un método "findBy".
            // Tienes dos opciones aquí:

            // OPCIÓN A: Si tu session soporta queries SQL nativas (Más eficiente)
            // session.execute("INSERT INTO Inventory (userId, itemId, cantidad) VALUES (?, ?, 1) ON DUPLICATE KEY UPDATE cantidad = cantidad + 1", userId, itemId);

            // OPCIÓN B: Lógica Java pura (Compatible con cualquier ORM básico)
            // Recuperamos todos los objetos de inventario (o filtrados si tu ORM deja)
            // Y buscamos manualmenete.

            List<Object> todosLosInventarios = session.findAll(Inventory.class); // Asumo que tienes findAll
            Inventory inventarioEncontrado = null;

            for (Object obj : todosLosInventarios) {
                Inventory inv = (Inventory) obj;
                if (inv.getUserId() == userId && inv.getItemId() == itemId) {
                    inventarioEncontrado = inv;
                    break;
                }
            }

            if (inventarioEncontrado != null) {
                // -- ESCENARIO 1: YA LO TIENE -> ACTUALIZAMOS CANTIDAD --
                inventarioEncontrado.setCantidad(inventarioEncontrado.getCantidad() + 1);
                session.update(inventarioEncontrado);
            } else {
                // -- ESCENARIO 2: NO LO TIENE -> CREAMOS NUEVO --
                Inventory registro = new Inventory(userId, itemId, 1);
                session.save(registro);
            }
            // ---------------------------------------------------------

            return 0; // Todo OK

        } catch (Exception e) {
            // Logueamos el error pero devolvemos un código de error controlado
            e.printStackTrace();
            return 500;
        } finally {
            if (session != null) session.close();
        }
    }


    @Override
    public List<Inventory> getInventario(int userId) {
        Session session = null;
        List<Inventory> inventarioUsuario = new LinkedList<>();
        try {
            session = FactorySession.openSession();
            // Traemos toda la tabla de inventarios (limitación de tu ORM)
            List<Object> todos = session.findAll(Inventory.class);

            for (Object obj : todos) {
                Inventory inv = (Inventory) obj;
                // Filtramos manualmente: Solo los de este usuario
                if (inv.getUserId() == userId) {
                    // OPCIONAL: Si tu objeto Inventory solo tiene ID de producto,
                    // aquí podrías buscar el nombre del producto para que el frontend no reciba solo números.
                    // Pero por ahora, devolvamos el objeto tal cual.
                    inventarioUsuario.add(inv);
                }
            }
        } catch (Exception e) {
            logger.error("Error al obtener inventario: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }
        return inventarioUsuario;
    }

    @Override
    public void eliminarProducto(int id) { // O String id, según como tengas tu ID
        Session session = null;
        try {
            session = FactorySession.openSession();
            Producto p = new Producto();
            p.setId(id); // Asegúrate de que tu objeto tiene el ID seteado
            session.delete(p); // Tu ORM debe tener función delete
        } catch (Exception e) {
            // Log del error
        } finally {
            if (session != null) session.close();
        }
    }
}