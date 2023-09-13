/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Windows 10
 */
@Entity
@Table(name = "Usuario")
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_usuario")
    private Integer idUsuario;
    @Basic(optional = false)
    @Column(name = "logon")
    private String logon;
    @Basic(optional = false)
    @Column(name = "senha")
    private String senha;

    public Usuario() {
    }

    public Usuario(Integer idUsuario) {
	this.idUsuario = idUsuario;
    }

    public Usuario(Integer idUsuario, String logon, String senha) {
	this.idUsuario = idUsuario;
	this.logon = logon;
	this.senha = senha;
    }

    public Integer getIdUsuario() {
	return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
	this.idUsuario = idUsuario;
    }

    public String getLogon() {
	return logon;
    }

    public void setLogon(String logon) {
	this.logon = logon;
    }

    public String getSenha() {
	return senha;
    }

    public void setSenha(String senha) {
	this.senha = senha;
    }

    @Override
    public int hashCode() {
	int hash = 0;
	hash += (idUsuario != null ? idUsuario.hashCode() : 0);
	return hash;
    }

    @Override
    public boolean equals(Object object) {
	// TODO: Warning - this method won't work in the case the id fields are not set
	if (!(object instanceof Usuario)) {
	    return false;
	}
	Usuario other = (Usuario) object;
	if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "model.Usuario[ idUsuario=" + idUsuario + " ]";
    }
    
}
