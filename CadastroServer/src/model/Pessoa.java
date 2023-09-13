/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Windows 10
 */
@Entity
@Table(name = "Pessoa")
@NamedQueries({
    @NamedQuery(name = "Pessoa.findAll", query = "SELECT p FROM Pessoa p")})
public class Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_pessoa")
    private Integer idPessoa;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @Column(name = "logradouro")
    private String logradouro;
    @Basic(optional = false)
    @Column(name = "cidade")
    private String cidade;
    @Basic(optional = false)
    @Column(name = "estado")
    private String estado;
    @Column(name = "telefone")
    private String telefone;
    @Column(name = "email")
    private String email;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "pessoa")
    private Pessoafisica pessoafisica;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pessoa")
    private Collection<Movimentacao> movimentacaoCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "pessoa")
    private Pessoajuridica pessoajuridica;

    public Pessoa() {
    }

    public Pessoa(Integer idPessoa) {
	this.idPessoa = idPessoa;
    }

    public Pessoa(Integer idPessoa, String nome, String logradouro, String cidade, String estado) {
	this.idPessoa = idPessoa;
	this.nome = nome;
	this.logradouro = logradouro;
	this.cidade = cidade;
	this.estado = estado;
    }

    public Integer getIdPessoa() {
	return idPessoa;
    }

    public void setIdPessoa(Integer idPessoa) {
	this.idPessoa = idPessoa;
    }

    public String getNome() {
	return nome;
    }

    public void setNome(String nome) {
	this.nome = nome;
    }

    public String getLogradouro() {
	return logradouro;
    }

    public void setLogradouro(String logradouro) {
	this.logradouro = logradouro;
    }

    public String getCidade() {
	return cidade;
    }

    public void setCidade(String cidade) {
	this.cidade = cidade;
    }

    public String getEstado() {
	return estado;
    }

    public void setEstado(String estado) {
	this.estado = estado;
    }

    public String getTelefone() {
	return telefone;
    }

    public void setTelefone(String telefone) {
	this.telefone = telefone;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public Pessoafisica getPessoafisica() {
	return pessoafisica;
    }

    public void setPessoafisica(Pessoafisica pessoafisica) {
	this.pessoafisica = pessoafisica;
    }

    public Collection<Movimentacao> getMovimentacaoCollection() {
	return movimentacaoCollection;
    }

    public void setMovimentacaoCollection(Collection<Movimentacao> movimentacaoCollection) {
	this.movimentacaoCollection = movimentacaoCollection;
    }

    public Pessoajuridica getPessoajuridica() {
	return pessoajuridica;
    }

    public void setPessoajuridica(Pessoajuridica pessoajuridica) {
	this.pessoajuridica = pessoajuridica;
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
	if (!(object instanceof Pessoa)) {
	    return false;
	}
	Pessoa other = (Pessoa) object;
	if ((this.idPessoa == null && other.idPessoa != null) || (this.idPessoa != null && !this.idPessoa.equals(other.idPessoa))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "model.Pessoa[ idPessoa=" + idPessoa + " ]";
    }
    
}
