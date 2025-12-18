package edu.upc.dsa.modelos;

public class EventoParticipacion {
    String idUsuario;
    String idEvento;

    public EventoParticipacion() {}

    public EventoParticipacion(String idUsuario, String idEvento) {
        this.idUsuario = idUsuario;
        this.idEvento = idEvento;
    }

    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
    public String getIdEvento() { return idEvento; }
    public void setIdEvento(String idEvento) { this.idEvento = idEvento; }
}