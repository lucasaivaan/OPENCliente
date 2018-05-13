package com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.adaptadores;

public class adapter_producto {


    private String urlimagen="";
    private String categoria="";
    private String subcategoria="";
    private String subcategoria_2="";
    private String info1="";
    private String info2="";
    private Double precio=0.0;
    private String id="";
    private String codigo="";
    private Integer cantidad;


    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setUrlimagen(String urlimagen) { this.urlimagen = urlimagen; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setSubcategoria(String subcategoria) { this.subcategoria = subcategoria; }
    public void setSubcategoria_2(String subcategoria_2) { this.subcategoria_2 = subcategoria_2; }
    public void setInfo1(String info1) { this.info1 = info1; }
    public void setInfo2(String info2) { this.info2 = info2; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public void setId(String id) { this.id = id; }




    public String getUrlimagen() { return urlimagen; }
    public String getCategoria() { return categoria; }
    public String getSubcategoria() { return subcategoria; }
    public String getSubcategoria_2() { return subcategoria_2; }
    public String getInfo1() { return info1; }
    public String getInfo2() { return info2; }
    public Double getPrecio() { return precio; }
    public String getId() { return id; }



}
