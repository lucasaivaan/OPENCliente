package com.opencliente.applic.opencliente.interface_principal.adaptadores;

/**
 * Created by lucas on 24/10/2017.
 */

public class adapter_ofertas_negocio {
    public adapter_ofertas_negocio() {
    }

    private String titulo;
    private String descripcion;
    private String precio;
    private String id;
    private String id_negocio;
    private String urlFoto ="default";



    public String getId_negocio() { return id_negocio; }
    public void setId_negocio(String id_negocio) { this.id_negocio = id_negocio; }
    public String getUrlFoto() { return urlFoto;}
    public void setUrlFoto(String urlFoto) { this.urlFoto = urlFoto; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPrecio() {
        return precio;
    }




}
