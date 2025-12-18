package edu.upc.dsa;

import edu.upc.dsa.modelos.*;
import java.util.List;

public interface UserManager {
    User registrarUsuario(String nombre, String email, String password);
    User loginUsuario(String email, String password);
    User getUsuario(String email);
    List<User> getUsuarios();
    boolean enviarCodigoVerificacion(User u);
    boolean verificarCodigo(String email, String codigo);
    void eliminarUsuario(String email);
    void addEvento(Evento e);
    List<Evento> getEventos();
    boolean participarEvento(String idUsuario, String idEvento);
}
