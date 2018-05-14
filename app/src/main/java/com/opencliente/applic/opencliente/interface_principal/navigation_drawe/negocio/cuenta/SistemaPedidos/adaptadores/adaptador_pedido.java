package com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.cuenta.SistemaPedidos.adaptadores;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class adaptador_pedido {
    public adaptador_pedido() { }

    private String id_cliente;
    private String id;
    private String contacto;
    private String telefono;
    private String tipo_entrega;
    private String forma_pago;
    private String nota;
    private Map<String,Object> direccion= new HashMap<String, Object>();
    private Date timestamp;
    private Boolean estado=false;



    public String getId() { return id; }
    public String getContacto() { return contacto; }
    public String getTipo_entrega() { return tipo_entrega; }
    public String getForma_pago() { return forma_pago; }
    public String getNota() { return nota; }
    public Map<String,Object> getDireccion() { return direccion; }
    public Boolean getEstado() { return estado; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }


    public String getId_cliente() { return id_cliente; }
    public void setId_cliente(String id_cliente) { this.id_cliente = id_cliente; }

    public void setId(String id) { this.id = id; }
    public void setContacto(String contacto) { this.contacto = contacto; }
    public void setTipo_entrega(String tipo_entrega) { this.tipo_entrega = tipo_entrega; }
    public void setForma_pago(String forma_pago) { this.forma_pago = forma_pago; }
    public void setNota(String nota) { this.nota = nota; }
    public void setDireccion(Map<String,Object> direccion) { this.direccion = direccion; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    @ServerTimestamp
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }


}
