package com.opencliente.applic.opencliente.interface_principal.navigation_drawe.Chat.adaptador;

import java.util.Date;

/**
 * Created by lucas on 30/10/2017.
 */

public class MensajeRecibir extends Mensaje {

    private Long hora;

    public MensajeRecibir() {
    }

    public MensajeRecibir(Long hora) {
        this.hora = hora;
    }

    public MensajeRecibir(String mensaje, String urlfotoPerfil, String nombre, String fotoPerfil, String type_mensaje, Long hora, Date timestamp,String categoria) {
        super(mensaje, urlfotoPerfil, nombre, fotoPerfil, type_mensaje,timestamp,categoria);
        this.hora = hora;
    }

    public Long getHora() {
        return hora;
    }

    public void setHora(Long hora) {
        this.hora = hora;
    }
}