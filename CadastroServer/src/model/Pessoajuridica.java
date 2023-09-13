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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Windows 10
 */
@Entity
@Table(name = "Pessoa_juridica")
@NamedQueries({
    @NamedQuery(name = "Pessoajuridica.findAll", query = "SELECT p FROM Pessoajuridica p")})
public class Pessoajuridica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "id_cnpj")
    private int idCnpj;
    @Id
    @Basic(optional = false)
    @Column(name = "id_pessoa")
    private Integer idPessoa;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id_pessoa", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Pessoa pessoa;

    public Pessoajuridica() {
    }

    public Pessoajuridica(Integer idPessoa) {
	this.idPessoa = idPessoa;
    }

    public Pessoajuridica(Integer idPessoa, int idCnpj) {
	this.idPessoa = idPessoa;
	this.idCnpj = idCnpj;
    }

    public int getIdCnpj() {
	return idCnpj;
    }

    public void setIdCnpj(int idCnpj) {
	this.idCnpj = idCnpj;
    }

    public Integer getIdPessoa() {
	return idPessoa;
    }

    public void setIdPessoa(Integer idPessoa) {
	this.idPessoa = idPessoa;
    }

    public Pessoa getPessoa() {
	return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
	this.pessoa = pessoa;
    }

    @Override
    public int hashCode() {
	int hash = 0;
	hash += (idPessoa != null ? idPessoa.hashCode() : 0);
	return hash;
    }

    @Override
    public boolean equals(Object object) {
	// TODO: Warning - this method won't work in the case the id fields are not set
	if (!(object instanceof Pessoajuridica)) {
	    return false;
	}
	Pessoajuridica other = (Pessoajuridica) object;
	if ((this.idPessoa == null && other.idPessoa != null) || (this.idPessoa != null && !this.idPessoa.equals(other.idPessoa))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "model.Pessoajuridica[ idPessoa=" + idPessoa + " ]";
    }
    
}
