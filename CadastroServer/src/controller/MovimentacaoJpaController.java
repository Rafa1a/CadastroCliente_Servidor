/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Movimentacao;
import model.Pessoa;
import model.Produtos;
import model.Usuario;

/**
 *
 * @author Windows 10
 */
public class MovimentacaoJpaController implements Serializable {

    public MovimentacaoJpaController(EntityManagerFactory emf) {
	this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
	return emf.createEntityManager();
    }

    public void create(Movimentacao movimentacao) {
	EntityManager em = null;
	try {
	    em = getEntityManager();
	    em.getTransaction().begin();
	    Pessoa pessoa = movimentacao.getPessoa();
	    if (pessoa != null) {
		pessoa = em.getReference(pessoa.getClass(), pessoa.getIdPessoa());
		movimentacao.setPessoa(pessoa);
	    }
	    Produtos produtos = movimentacao.getProdutos();
	    if (produtos != null) {
		produtos = em.getReference(produtos.getClass(), produtos.getIdProduto());
		movimentacao.setProdutos(produtos);
	    }
	    Usuario usuario = movimentacao.getUsuario();
	    if (usuario != null) {
		usuario = em.getReference(usuario.getClass(), usuario.getIdUsuario());
		movimentacao.setUsuario(usuario);
	    }
	    em.persist(movimentacao);
	    if (pessoa != null) {
		pessoa.getMovimentacaoCollection().add(movimentacao);
		pessoa = em.merge(pessoa);
	    }
	    if (produtos != null) {
		produtos.getMovimentacaoCollection().add(movimentacao);
		produtos = em.merge(produtos);
	    }
	    if (usuario != null) {
		usuario.getMovimentacaoCollection().add(movimentacao);
		usuario = em.merge(usuario);
	    }
	    em.getTransaction().commit();
	} finally {
	    if (em != null) {
		em.close();
	    }
	}
    }

    public void edit(Movimentacao movimentacao) throws NonexistentEntityException, Exception {
	EntityManager em = null;
	try {
	    em = getEntityManager();
	    em.getTransaction().begin();
	    Movimentacao persistentMovimentacao = em.find(Movimentacao.class, movimentacao.getIdMovimentacao());
	    Pessoa pessoaOld = persistentMovimentacao.getPessoa();
	    Pessoa pessoaNew = movimentacao.getPessoa();
	    Produtos produtosOld = persistentMovimentacao.getProdutos();
	    Produtos produtosNew = movimentacao.getProdutos();
	    Usuario usuarioOld = persistentMovimentacao.getUsuario();
	    Usuario usuarioNew = movimentacao.getUsuario();
	    if (pessoaNew != null) {
		pessoaNew = em.getReference(pessoaNew.getClass(), pessoaNew.getIdPessoa());
		movimentacao.setPessoa(pessoaNew);
	    }
	    if (produtosNew != null) {
		produtosNew = em.getReference(produtosNew.getClass(), produtosNew.getIdProduto());
		movimentacao.setProdutos(produtosNew);
	    }
	    if (usuarioNew != null) {
		usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getIdUsuario());
		movimentacao.setUsuario(usuarioNew);
	    }
	    movimentacao = em.merge(movimentacao);
	    if (pessoaOld != null && !pessoaOld.equals(pessoaNew)) {
		pessoaOld.getMovimentacaoCollection().remove(movimentacao);
		pessoaOld = em.merge(pessoaOld);
	    }
	    if (pessoaNew != null && !pessoaNew.equals(pessoaOld)) {
		pessoaNew.getMovimentacaoCollection().add(movimentacao);
		pessoaNew = em.merge(pessoaNew);
	    }
	    if (produtosOld != null && !produtosOld.equals(produtosNew)) {
		produtosOld.getMovimentacaoCollection().remove(movimentacao);
		produtosOld = em.merge(produtosOld);
	    }
	    if (produtosNew != null && !produtosNew.equals(produtosOld)) {
		produtosNew.getMovimentacaoCollection().add(movimentacao);
		produtosNew = em.merge(produtosNew);
	    }
	    if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
		usuarioOld.getMovimentacaoCollection().remove(movimentacao);
		usuarioOld = em.merge(usuarioOld);
	    }
	    if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
		usuarioNew.getMovimentacaoCollection().add(movimentacao);
		usuarioNew = em.merge(usuarioNew);
	    }
	    em.getTransaction().commit();
	} catch (Exception ex) {
	    String msg = ex.getLocalizedMessage();
	    if (msg == null || msg.length() == 0) {
		Integer id = movimentacao.getIdMovimentacao();
		if (findMovimentacao(id) == null) {
		    throw new NonexistentEntityException("The movimentacao with id " + id + " no longer exists.");
		}
	    }
	    throw ex;
	} finally {
	    if (em != null) {
		em.close();
	    }
	}
    }

    public void destroy(Integer id) throws NonexistentEntityException {
	EntityManager em = null;
	try {
	    em = getEntityManager();
	    em.getTransaction().begin();
	    Movimentacao movimentacao;
	    try {
		movimentacao = em.getReference(Movimentacao.class, id);
		movimentacao.getIdMovimentacao();
	    } catch (EntityNotFoundException enfe) {
		throw new NonexistentEntityException("The movimentacao with id " + id + " no longer exists.", enfe);
	    }
	    Pessoa pessoa = movimentacao.getPessoa();
	    if (pessoa != null) {
		pessoa.getMovimentacaoCollection().remove(movimentacao);
		pessoa = em.merge(pessoa);
	    }
	    Produtos produtos = movimentacao.getProdutos();
	    if (produtos != null) {
		produtos.getMovimentacaoCollection().remove(movimentacao);
		produtos = em.merge(produtos);
	    }
	    Usuario usuario = movimentacao.getUsuario();
	    if (usuario != null) {
		usuario.getMovimentacaoCollection().remove(movimentacao);
		usuario = em.merge(usuario);
	    }
	    em.remove(movimentacao);
	    em.getTransaction().commit();
	} finally {
	    if (em != null) {
		em.close();
	    }
	}
    }

    public List<Movimentacao> findMovimentacaoEntities() {
	return findMovimentacaoEntities(true, -1, -1);
    }

    public List<Movimentacao> findMovimentacaoEntities(int maxResults, int firstResult) {
	return findMovimentacaoEntities(false, maxResults, firstResult);
    }

    private List<Movimentacao> findMovimentacaoEntities(boolean all, int maxResults, int firstResult) {
	EntityManager em = getEntityManager();
	try {
	    CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
	    cq.select(cq.from(Movimentacao.class));
	    Query q = em.createQuery(cq);
	    if (!all) {
		q.setMaxResults(maxResults);
		q.setFirstResult(firstResult);
	    }
	    return q.getResultList();
	} finally {
	    em.close();
	}
    }

    public Movimentacao findMovimentacao(Integer id) {
	EntityManager em = getEntityManager();
	try {
	    return em.find(Movimentacao.class, id);
	} finally {
	    em.close();
	}
    }

    public int getMovimentacaoCount() {
	EntityManager em = getEntityManager();
	try {
	    CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
	    Root<Movimentacao> rt = cq.from(Movimentacao.class);
	    cq.select(em.getCriteriaBuilder().count(rt));
	    Query q = em.createQuery(cq);
	    return ((Long) q.getSingleResult()).intValue();
	} finally {
	    em.close();
	}
    }
    
}
