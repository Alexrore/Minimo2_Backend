package edu.upc.dsa;

import edu.upc.dsa.modelos.Producto;
import java.util.List;

public interface ProductoManager {
    Producto anadirproducto (String id, String nombreproducto, int precio);
    List<Producto> listadeproductos();
    Producto getproducto (String nombreproducto);
    Producto anadirproducto(String nombreproducto, int precio);//pongo este por si queremos que el id venga del rest
    Producto encontrarproducto (String id);
}
