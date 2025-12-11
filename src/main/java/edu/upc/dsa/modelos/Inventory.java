package edu.upc.dsa.modelos;

public class Inventory {
    int id;
    int userId;
    int itemId;
    int cantidad;

    // --- NUEVO CAMPO (NO ESTÁ EN BBDD, SOLO PARA JSON) ---
    String nombre;

    public Inventory() {}

    public Inventory(int userId, int itemId, int cantidad) {
        this.userId = userId;
        this.itemId = itemId;
        this.cantidad = cantidad;
    }

    // --- GETTERS Y SETTERS ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    // ¡¡¡ IMPORTANTE !!!
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}