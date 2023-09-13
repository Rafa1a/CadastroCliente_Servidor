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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Windows 10
 */
@Entity
@Table(name = "Produtos")
@NamedQueries({
    @NamedQuery(name = "Produtos.findAll", query = "SELECT p FROM Produtos p")})
public class Produtos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_produto")
    private Integer idProduto;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @Column(name = "quantidade")
    private int quantidade;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "preco_de_venda")
    private BigDecimal precoDeVenda;

    public Produtos() {
    }

    public Produtos(Integer idProduto) {
	this.idProduto = idProduto;
    }

    public Produtos(Integer idProduto, String nome, int quantidade, BigDecimal precoDeVenda) {
	this.idProduto = idProduto;
	this.nome = nome;
	this.quantidade = quantidade;
	this.precoDeVenda = precoDeVenda;
    }

    public Integer getIdProduto() {
	return idProduto;
    }

    public void setIdProduto(Integer idProduto) {
	this.idProduto = idProduto;
    }

    public String getNome() {
	return nome;
    }

    public void setNome(String nome) {
	this.nome = nome;
    }

    public int getQuantidade() {
	return quantidade;
    }

    public void setQuantidade(int quantidade) {
	this.quantidade = quantidade;
    }

    public BigDecimal getPrecoDeVenda() {
	return precoDeVenda;
    }

    public void setPrecoDeVenda(BigDecimal precoDeVenda) {
	this.precoDeVenda = precoDeVenda;
    }

    @Override
    public int hashCode() {
	int hash = 0;
	hash += (idProduto != null ? idProduto.hashCode() : 0);
	return hash;
    }

    @Override
    public boolean equals(Object object) {
	// TODO: Warning - this method won't work in the case the id fields are not set
	if (!(object instanceof Produtos)) {
	    return false;
	}
	Produtos other = (Produtos) object;
	if ((this.idProduto == null && other.idProduto != null) || (this.idProduto != null && !this.idProduto.equals(other.idProduto))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "model.Produtos[ idProduto=" + idProduto + " ]";
    }
    
}
