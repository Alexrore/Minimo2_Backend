package edu.upc.dsa.modelos;

public class Evento {
    String id;
    String nombre;
    String fechaInicio;
    String fechaFin;
    String imageUrl;
    String descripcion;

    // Constructor vacío necesario para JSON
    public Evento() {}

    public Evento(String nombre, String fechaInicio,String fechaFin,String imageUrl, String descripcion) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.imageUrl = imageUrl;
        this.descripcion = descripcion;
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }
    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}