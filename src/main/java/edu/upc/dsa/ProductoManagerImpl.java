package edu.upc.dsa;

import edu.upc.dsa.modelos.Producto;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductoManagerImpl implements ProductoManager {

    private static ProductoManagerImpl instance;
    private List<Producto> productos;

    private ProductoManagerImpl() {
        this.productos = new ArrayList<>();
    }

    public static ProductoManagerImpl getInstance() {
        if (instance == null) {
            instance = new ProductoManagerImpl();
        }
        return instance;
    }

    @Override
    public List<Producto> listadeproductos() {
        return this.productos;
    }

    @Override
    public Producto getproducto(String nombreproducto) {
        for (Producto p : this.productos) {
            if (p.getNombreproducto().equals(nombreproducto)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public Producto anadirproducto(String nombreproducto, int precio) {

        if (getproducto(nombreproducto) != null) {
            return null;
        }

        String id = UUID.randomUUID().toString();
        Producto nuevoProducto = new Producto(id, nombreproducto, precio);
        this.productos.add(nuevoProducto);
        return nuevoProducto;
    }

    @Override
    public Producto encontrarproducto(String nombreproducto) {
        for (Producto p : this.productos) {
            if (p.getId().equals(nombreproducto)) {
                return p;
            }
        }
        return null;
    }
}
