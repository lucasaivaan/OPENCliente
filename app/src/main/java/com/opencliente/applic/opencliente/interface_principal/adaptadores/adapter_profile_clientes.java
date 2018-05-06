package com.opencliente.applic.opencliente.interface_principal.adaptadores;

/**
 * Created by lucas on 26/10/2017.
 */

public class adapter_profile_clientes {
    public adapter_profile_clientes() {
    }
    private String id;
    private String nombre;
    private String telefono;
    private String urlfotoPerfil;
    private Boolean nuevo_mensaje;
    private Double numreseñas;
    private String cardlayout="default";



    public String getCardlayout() { return cardlayout; }

    public void setCardlayout(String cardlayout) { this.cardlayout = cardlayout; }

    public String getUrlfotoPerfil() {return urlfotoPerfil;}

    public void setUrlfotoPerfil(String urlfotoPerfil) {this.urlfotoPerfil = urlfotoPerfil;}

    public Double getNumreseñas() {return numreseñas;}

    public void setNumreseñas(Double numreseñas) {this.numreseñas = numreseñas;}

    public Boolean getNuevo_mensaje() {
        return nuevo_mensaje;
    }

    public void setNuevo_mensaje(Boolean nuevo_mensaje) {
        this.nuevo_mensaje = nuevo_mensaje;
    }









    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }




    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


}
