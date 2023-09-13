/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Windows 10
 */
@Entity
@Table(name = "Movimentacao")
@NamedQueries({
    @NamedQuery(name = "Movimentacao.findAll", query = "SELECT m FROM Movimentacao m")})
public class Movimentacao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_movimentacao")
    private Integer idMovimentacao;
    @Basic(optional = false)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @Column(name = "quantidade_E_S")
    private int quantidadeES;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "preco_unitario")
    private BigDecimal precoUnitario;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id_pessoa")
    @ManyToOne(optional = false)
    private Pessoa pessoa;
    @JoinColumn(name = "id_produto", referencedColumnName = "id_produto")
    @ManyToOne(optional = false)
    private Produtos produtos;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario usuario;

    public Movimentacao() {
    }

    public Movimentacao(Integer idMovimentacao) {
	this.idMovimentacao = idMovimentacao;
    }

    public Movimentacao(Integer idMovimentacao, String tipo, int quantidadeES, BigDecimal precoUnitario) {
	this.idMovimentacao = idMovimentacao;
	this.tipo = tipo;
	this.quantidadeES = quantidadeES;
	this.precoUnitario = precoUnitario;
    }

    public Integer getIdMovimentacao() {
	return idMovimentacao;
    }

    public void setIdMovimentacao(Integer idMovimentacao) {
	this.idMovimentacao = idMovimentacao;
    }

    public String getTipo() {
	return tipo;
    }

    public void setTipo(String tipo) {
	this.tipo = tipo;
    }

    public int getQuantidadeES() {
	return quantidadeES;
    }

    public void setQuantidadeES(int quantidadeES) {
	this.quantidadeES = quantidadeES;
    }

    public BigDecimal getPrecoUnitario() {
	return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
	this.precoUnitario = precoUnitario;
    }

    public Pessoa getPessoa() {
	return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
	this.pessoa = pessoa;
    }

    public Produtos getProdutos() {
	return produtos;
    }

    public void setProdutos(Produtos produtos) {
	this.produtos = produtos;
    }

    public Usuario getUsuario() {
	return usuario;
    }

    public void setUsuario(Usuario usuario) {
	this.usuario = usuario;
    }

    @Override
    public int hashCode() {
	int hash = 0;
	hash += (idMovimentacao != null ? idMovimentacao.hashCode() : 0);
	return hash;
    }

    @Override
    public boolean equals(Object object) {
	// TODO: Warning - this method won't work in the case the id fields are not set
	if (!(object instanceof Movimentacao)) {
	    return false;
	}
	Movimentacao other = (Movimentacao) object;
	if ((this.idMovimentacao == null && other.idMovimentacao != null) || (this.idMovimentacao != null && !this.idMovimentacao.equals(other.idMovimentacao))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "model.Movimentacao[ idMovimentacao=" + idMovimentacao + " ]";
    }
    
}
