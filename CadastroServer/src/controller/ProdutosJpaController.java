/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Movimentacao;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Produtos;

/**
 *
 * @author Windows 10
 */
public class ProdutosJpaController implements Serializable {

    public ProdutosJpaController(EntityManagerFactory emf) {
	this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
	return emf.createEntityManager();
    }

    public void create(Produtos produtos) {
	if (produtos.getMovimentacaoCollection() == null) {
	    produtos.setMovimentacaoCollection(new ArrayList<Movimentacao>());
	}
	EntityManager em = null;
	try {
	    em = getEntityManager();
	    em.getTransaction().begin();
	    Collection<Movimentacao> attachedMovimentacaoCollection = new ArrayList<Movimentacao>();
	    for (Movimentacao movimentacaoCollectionMovimentacaoToAttach : produtos.getMovimentacaoCollection()) {
		movimentacaoCollectionMovimentacaoToAttach = em.getReference(movimentacaoCollectionMovimentacaoToAttach.getClass(), movimentacaoCollectionMovimentacaoToAttach.getIdMovimentacao());
		attachedMovimentacaoCollection.add(movimentacaoCollectionMovimentacaoToAttach);
	    }
	    produtos.setMovimentacaoCollection(attachedMovimentacaoCollection);
	    em.persist(produtos);
	    for (Movimentacao movimentacaoCollectionMovimentacao : produtos.getMovimentacaoCollection()) {
		Produtos oldProdutosOfMovimentacaoCollectionMovimentacao = movimentacaoCollectionMovimentacao.getProdutos();
		movimentacaoCollectionMovimentacao.setProdutos(produtos);
		movimentacaoCollectionMovimentacao = em.merge(movimentacaoCollectionMovimentacao);
		if (oldProdutosOfMovimentacaoCollectionMovimentacao != null) {
		    oldProdutosOfMovimentacaoCollectionMovimentacao.getMovimentacaoCollection().remove(movimentacaoCollectionMovimentacao);
		    oldProdutosOfMovimentacaoCollectionMovimentacao = em.merge(oldProdutosOfMovimentacaoCollectionMovimentacao);
		}
	    }
	    em.getTransaction().commit();
	} finally {
	    if (em != null) {
		em.close();
	    }
	}
    }

    public void edit(Produtos produtos) throws IllegalOrphanException, NonexistentEntityException, Exception {
	EntityManager em = null;
	try {
	    em = getEntityManager();
	    em.getTransaction().begin();
	    Produtos persistentProdutos = em.find(Produtos.class, produtos.getIdProduto());
	    Collection<Movimentacao> movimentacaoCollectionOld = persistentProdutos.getMovimentacaoCollection();
	    Collection<Movimentacao> movimentacaoCollectionNew = produtos.getMovimentacaoCollection();
	    List<String> illegalOrphanMessages = null;
	    for (Movimentacao movimentacaoCollectionOldMovimentacao : movimentacaoCollectionOld) {
		if (!movimentacaoCollectionNew.contains(movimentacaoCollectionOldMovimentacao)) {
		    if (illegalOrphanMessages == null) {
			illegalOrphanMessages = new ArrayList<String>();
		    }
		    illegalOrphanMessages.add("You must retain Movimentacao " + movimentacaoCollectionOldMovimentacao + " since its produtos field is not nullable.");
		}
	    }
	    if (illegalOrphanMessages != null) {
		throw new IllegalOrphanException(illegalOrphanMessages);
	    }
	    Collection<Movimentacao> attachedMovimentacaoCollectionNew = new ArrayList<Movimentacao>();
	    for (Movimentacao movimentacaoCollectionNewMovimentacaoToAttach : movimentacaoCollectionNew) {
		movimentacaoCollectionNewMovimentacaoToAttach = em.getReference(movimentacaoCollectionNewMovimentacaoToAttach.getClass(), movimentacaoCollectionNewMovimentacaoToAttach.getIdMovimentacao());
		attachedMovimentacaoCollectionNew.add(movimentacaoCollectionNewMovimentacaoToAttach);
	    }
	    movimentacaoCollectionNew = attachedMovimentacaoCollectionNew;
	    produtos.setMovimentacaoCollection(movimentacaoCollectionNew);
	    produtos = em.merge(produtos);
	    for (Movimentacao movimentacaoCollectionNewMovimentacao : movimentacaoCollectionNew) {
		if (!movimentacaoCollectionOld.contains(movimentacaoCollectionNewMovimentacao)) {
		    Produtos oldProdutosOfMovimentacaoCollectionNewMovimentacao = movimentacaoCollectionNewMovimentacao.getProdutos();
		    movimentacaoCollectionNewMovimentacao.setProdutos(produtos);
		    movimentacaoCollectionNewMovimentacao = em.merge(movimentacaoCollectionNewMovimentacao);
		    if (oldProdutosOfMovimentacaoCollectionNewMovimentacao != null && !oldProdutosOfMovimentacaoCollectionNewMovimentacao.equals(produtos)) {
			oldProdutosOfMovimentacaoCollectionNewMovimentacao.getMovimentacaoCollection().remove(movimentacaoCollectionNewMovimentacao);
			oldProdutosOfMovimentacaoCollectionNewMovimentacao = em.merge(oldProdutosOfMovimentacaoCollectionNewMovimentacao);
		    }
		}
	    }
	    em.getTransaction().commit();
	} catch (Exception ex) {
	    String msg = ex.getLocalizedMessage();
	    if (msg == null || msg.length() == 0) {
		Integer id = produtos.getIdProduto();
		if (findProdutos(id) == null) {
		    throw new NonexistentEntityException("The produtos with id " + id + " no longer exists.");
		}
	    }
	    throw ex;
	} finally {
	    if (em != null) {
		em.close();
	    }
	}
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
	EntityManager em = null;
	try {
	    em = getEntityManager();
	    em.getTransaction().begin();
	    Produtos produtos;
	    try {
		produtos = em.getReference(Produtos.class, id);
		produtos.getIdProduto();
	    } catch (EntityNotFoundException enfe) {
		throw new NonexistentEntityException("The produtos with id " + id + " no longer exists.", enfe);
	    }
	    List<String> illegalOrphanMessages = null;
	    Collection<Movimentacao> movimentacaoCollectionOrphanCheck = produtos.getMovimentacaoCollection();
	    for (Movimentacao movimentacaoCollectionOrphanCheckMovimentacao : movimentacaoCollectionOrphanCheck) {
		if (illegalOrphanMessages == null) {
		    illegalOrphanMessages = new ArrayList<String>();
		}
		illegalOrphanMessages.add("This Produtos (" + produtos + ") cannot be destroyed since the Movimentacao " + movimentacaoCollectionOrphanCheckMovimentacao + " in its movimentacaoCollection field has a non-nullable produtos field.");
	    }
	    if (illegalOrphanMessages != null) {
		throw new IllegalOrphanException(illegalOrphanMessages);
	    }
	    em.remove(produtos);
	    em.getTransaction().commit();
	} finally {
	    if (em != null) {
		em.close();
	    }
	}
    }

    public List<Produtos> findProdutosEntities() {
	return findProdutosEntities(true, -1, -1);
    }

    public List<Produtos> findProdutosEntities(int maxResults, int firstResult) {
	return findProdutosEntities(false, maxResults, firstResult);
    }

    private List<Produtos> findProdutosEntities(boolean all, int maxResults, int firstResult) {
	EntityManager em = getEntityManager();
	try {
	    CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
	    cq.select(cq.from(Produtos.class));
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

    public Produtos findProdutos(Integer id) {
	EntityManager em = getEntityManager();
	try {
	    return em.find(Produtos.class, id);
	} finally {
	    em.close();
	}
    }

    public int getProdutosCount() {
	EntityManager em = getEntityManager();
	try {
	    CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
	    Root<Produtos> rt = cq.from(Produtos.class);
	    cq.select(em.getCriteriaBuilder().count(rt));
	    Query q = em.createQuery(cq);
	    return ((Long) q.getSingleResult()).intValue();
	} finally {
	    em.close();
	}
    }
    
}
