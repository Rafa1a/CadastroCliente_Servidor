/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Pessoa;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Pessoafisica;

/**
 *
 * @author Windows 10
 */
public class PessoafisicaJpaController implements Serializable {

    public PessoafisicaJpaController(EntityManagerFactory emf) {
	this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
	return emf.createEntityManager();
    }

    public void create(Pessoafisica pessoafisica) throws IllegalOrphanException, PreexistingEntityException, Exception {
	List<String> illegalOrphanMessages = null;
	Pessoa pessoaOrphanCheck = pessoafisica.getPessoa();
	if (pessoaOrphanCheck != null) {
	    Pessoafisica oldPessoafisicaOfPessoa = pessoaOrphanCheck.getPessoafisica();
	    if (oldPessoafisicaOfPessoa != null) {
		if (illegalOrphanMessages == null) {
		    illegalOrphanMessages = new ArrayList<String>();
		}
		illegalOrphanMessages.add("The Pessoa " + pessoaOrphanCheck + " already has an item of type Pessoafisica whose pessoa column cannot be null. Please make another selection for the pessoa field.");
	    }
	}
	if (illegalOrphanMessages != null) {
	    throw new IllegalOrphanException(illegalOrphanMessages);
	}
	EntityManager em = null;
	try {
	    em = getEntityManager();
	    em.getTransaction().begin();
	    Pessoa pessoa = pessoafisica.getPessoa();
	    if (pessoa != null) {
		pessoa = em.getReference(pessoa.getClass(), pessoa.getIdPessoa());
		pessoafisica.setPessoa(pessoa);
	    }
	    em.persist(pessoafisica);
	    if (pessoa != null) {
		pessoa.setPessoafisica(pessoafisica);
		pessoa = em.merge(pessoa);
	    }
	    em.getTransaction().commit();
	} catch (Exception ex) {
	    if (findPessoafisica(pessoafisica.getIdPessoa()) != null) {
		throw new PreexistingEntityException("Pessoafisica " + pessoafisica + " already exists.", ex);
	    }
	    throw ex;
	} finally {
	    if (em != null) {
		em.close();
	    }
	}
    }

    public void edit(Pessoafisica pessoafisica) throws IllegalOrphanException, NonexistentEntityException, Exception {
	EntityManager em = null;
	try {
	    em = getEntityManager();
	    em.getTransaction().begin();
	    Pessoafisica persistentPessoafisica = em.find(Pessoafisica.class, pessoafisica.getIdPessoa());
	    Pessoa pessoaOld = persistentPessoafisica.getPessoa();
	    Pessoa pessoaNew = pessoafisica.getPessoa();
	    List<String> illegalOrphanMessages = null;
	    if (pessoaNew != null && !pessoaNew.equals(pessoaOld)) {
		Pessoafisica oldPessoafisicaOfPessoa = pessoaNew.getPessoafisica();
		if (oldPessoafisicaOfPessoa != null) {
		    if (illegalOrphanMessages == null) {
			illegalOrphanMessages = new ArrayList<String>();
		    }
		    illegalOrphanMessages.add("The Pessoa " + pessoaNew + " already has an item of type Pessoafisica whose pessoa column cannot be null. Please make another selection for the pessoa field.");
		}
	    }
	    if (illegalOrphanMessages != null) {
		throw new IllegalOrphanException(illegalOrphanMessages);
	    }
	    if (pessoaNew != null) {
		pessoaNew = em.getReference(pessoaNew.getClass(), pessoaNew.getIdPessoa());
		pessoafisica.setPessoa(pessoaNew);
	    }
	    pessoafisica = em.merge(pessoafisica);
	    if (pessoaOld != null && !pessoaOld.equals(pessoaNew)) {
		pessoaOld.setPessoafisica(null);
		pessoaOld = em.merge(pessoaOld);
	    }
	    if (pessoaNew != null && !pessoaNew.equals(pessoaOld)) {
		pessoaNew.setPessoafisica(pessoafisica);
		pessoaNew = em.merge(pessoaNew);
	    }
	    em.getTransaction().commit();
	} catch (Exception ex) {
	    String msg = ex.getLocalizedMessage();
	    if (msg == null || msg.length() == 0) {
		Integer id = pessoafisica.getIdPessoa();
		if (findPessoafisica(id) == null) {
		    throw new NonexistentEntityException("The pessoafisica with id " + id + " no longer exists.");
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
	    Pessoafisica pessoafisica;
	    try {
		pessoafisica = em.getReference(Pessoafisica.class, id);
		pessoafisica.getIdPessoa();
	    } catch (EntityNotFoundException enfe) {
		throw new NonexistentEntityException("The pessoafisica with id " + id + " no longer exists.", enfe);
	    }
	    Pessoa pessoa = pessoafisica.getPessoa();
	    if (pessoa != null) {
		pessoa.setPessoafisica(null);
		pessoa = em.merge(pessoa);
	    }
	    em.remove(pessoafisica);
	    em.getTransaction().commit();
	} finally {
	    if (em != null) {
		em.close();
	    }
	}
    }

    public List<Pessoafisica> findPessoafisicaEntities() {
	return findPessoafisicaEntities(true, -1, -1);
    }

    public List<Pessoafisica> findPessoafisicaEntities(int maxResults, int firstResult) {
	return findPessoafisicaEntities(false, maxResults, firstResult);
    }

    private List<Pessoafisica> findPessoafisicaEntities(boolean all, int maxResults, int firstResult) {
	EntityManager em = getEntityManager();
	try {
	    CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
	    cq.select(cq.from(Pessoafisica.class));
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

    public Pessoafisica findPessoafisica(Integer id) {
	EntityManager em = getEntityManager();
	try {
	    return em.find(Pessoafisica.class, id);
	} finally {
	    em.close();
	}
    }

    public int getPessoafisicaCount() {
	EntityManager em = getEntityManager();
	try {
	    CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
	    Root<Pessoafisica> rt = cq.from(Pessoafisica.class);
	    cq.select(em.getCriteriaBuilder().count(rt));
	    Query q = em.createQuery(cq);
	    return ((Long) q.getSingleResult()).intValue();
	} finally {
	    em.close();
	}
    }
    
}
