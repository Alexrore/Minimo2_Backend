package edu.upc.dsa;

import edu.upc.dsa.modelos.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserManagerImpl implements UserManager {
    private static UserManagerImpl instance;
    private List<User> usuarios;

    private UserManagerImpl() {
        usuarios = new ArrayList<>();
    }

    public static UserManagerImpl getInstance() {
        if (instance == null) instance = new UserManagerImpl();
        return instance;
    }

    @Override
    public User registrarUsuario(String nombre, String email, String password) {
        for (User u : usuarios) {
            if (u.getEmail().equals(email))
                return null;
        }

        User nuevo = new User(UUID.randomUUID().toString(), nombre, email, password);
        usuarios.add(nuevo);
        return nuevo;
    }

    @Override
    public User getUsuario(String email) {
        for (User u : usuarios) {
            if (u.getEmail().equals(email)) return u;
        }
        return null;
    }

    @Override
    public List<User> getUsuarios() {
        return usuarios;
    }
}
