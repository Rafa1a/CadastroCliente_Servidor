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
import model.Pessoajuridica;

/**
 *
 * @author Windows 10
 */
public class PessoajuridicaJpaController implements Serializable {

    public PessoajuridicaJpaController(EntityManagerFactory emf) {
	this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
	return emf.createEntityManager();
    }

    public void create(Pessoajuridica pessoajuridica) throws IllegalOrphanException, PreexistingEntityException, Exception {
	List<String> illegalOrphanMessages = null;
	Pessoa pessoaOrphanCheck = pessoajuridica.getPessoa();
	if (pessoaOrphanCheck != null) {
	    Pessoajuridica oldPessoajuridicaOfPessoa = pessoaOrphanCheck.getPessoajuridica();
	    if (oldPessoajuridicaOfPessoa != null) {
		if (illegalOrphanMessages == null) {
		    illegalOrphanMessages = new ArrayList<String>();
		}
		illegalOrphanMessages.add("The Pessoa " + pessoaOrphanCheck + " already has an item of type Pessoajuridica whose pessoa column cannot be null. Please make another selection for the pessoa field.");
	    }
	}
	if (illegalOrphanMessages != null) {
	    throw new IllegalOrphanException(illegalOrphanMessages);
	}
	EntityManager em = null;
	try {
	    em = getEntityManager();
	    em.getTransaction().begin();
	    Pessoa pessoa = pessoajuridica.getPessoa();
	    if (pessoa != null) {
		pessoa = em.getReference(pessoa.getClass(), pessoa.getIdPessoa());
		pessoajuridica.setPessoa(pessoa);
	    }
	    em.persist(pessoajuridica);
	    if (pessoa != null) {
		pessoa.setPessoajuridica(pessoajuridica);
		pessoa = em.merge(pessoa);
	    }
	    em.getTransaction().commit();
	} catch (Exception ex) {
	    if (findPessoajuridica(pessoajuridica.getIdPessoa()) != null) {
		throw new PreexistingEntityException("Pessoajuridica " + pessoajuridica + " already exists.", ex);
	    }
	    throw ex;
	} finally {
	    if (em != null) {
		em.close();
	    }
	}
    }

    public void edit(Pessoajuridica pessoajuridica) throws IllegalOrphanException, NonexistentEntityException, Exception {
	EntityManager em = null;
	try {
	    em = getEntityManager();
	    em.getTransaction().begin();
	    Pessoajuridica persistentPessoajuridica = em.find(Pessoajuridica.class, pessoajuridica.getIdPessoa());
	    Pessoa pessoaOld = persistentPessoajuridica.getPessoa();
	    Pessoa pessoaNew = pessoajuridica.getPessoa();
	    List<String> illegalOrphanMessages = null;
	    if (pessoaNew != null && !pessoaNew.equals(pessoaOld)) {
		Pessoajuridica oldPessoajuridicaOfPessoa = pessoaNew.getPessoajuridica();
		if (oldPessoajuridicaOfPessoa != null) {
		    if (illegalOrphanMessages == null) {
			illegalOrphanMessages = new ArrayList<String>();
		    }
		    illegalOrphanMessages.add("The Pessoa " + pessoaNew + " already has an item of type Pessoajuridica whose pessoa column cannot be null. Please make another selection for the pessoa field.");
		}
	    }
	    if (illegalOrphanMessages != null) {
		throw new IllegalOrphanException(illegalOrphanMessages);
	    }
	    if (pessoaNew != null) {
		pessoaNew = em.getReference(pessoaNew.getClass(), pessoaNew.getIdPessoa());
		pessoajuridica.setPessoa(pessoaNew);
	    }
	    pessoajuridica = em.merge(pessoajuridica);
	    if (pessoaOld != null && !pessoaOld.equals(pessoaNew)) {
		pessoaOld.setPessoajuridica(null);
		pessoaOld = em.merge(pessoaOld);
	    }
	    if (pessoaNew != null && !pessoaNew.equals(pessoaOld)) {
		pessoaNew.setPessoajuridica(pessoajuridica);
		pessoaNew = em.merge(pessoaNew);
	    }
	    em.getTransaction().commit();
	} catch (Exception ex) {
	    String msg = ex.getLocalizedMessage();
	    if (msg == null || msg.length() == 0) {
		Integer id = pessoajuridica.getIdPessoa();
		if (findPessoajuridica(id) == null) {
		    throw new NonexistentEntityException("The pessoajuridica with id " + id + " no longer exists.");
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
	    Pessoajuridica pessoajuridica;
	    try {
		pessoajuridica = em.getReference(Pessoajuridica.class, id);
		pessoajuridica.getIdPessoa();
	    } catch (EntityNotFoundException enfe) {
		throw new NonexistentEntityException("The pessoajuridica with id " + id + " no longer exists.", enfe);
	    }
	    Pessoa pessoa = pessoajuridica.getPessoa();
	    if (pessoa != null) {
		pessoa.setPessoajuridica(null);
		pessoa = em.merge(pessoa);
	    }
	    em.remove(pessoajuridica);
	    em.getTransaction().commit();
	} finally {
	    if (em != null) {
		em.close();
	    }
	}
    }

    public List<Pessoajuridica> findPessoajuridicaEntities() {
	return findPessoajuridicaEntities(true, -1, -1);
    }

    public List<Pessoajuridica> findPessoajuridicaEntities(int maxResults, int firstResult) {
	return findPessoajuridicaEntities(false, maxResults, firstResult);
    }

    private List<Pessoajuridica> findPessoajuridicaEntities(boolean all, int maxResults, int firstResult) {
	EntityManager em = getEntityManager();
	try {
	    CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
	    cq.select(cq.from(Pessoajuridica.class));
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

    public Pessoajuridica findPessoajuridica(Integer id) {
	EntityManager em = getEntityManager();
	try {
	    return em.find(Pessoajuridica.class, id);
	} finally {
	    em.close();
	}
    }

    public int getPessoajuridicaCount() {
	EntityManager em = getEntityManager();
	try {
	    CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
	    Root<Pessoajuridica> rt = cq.from(Pessoajuridica.class);
	    cq.select(em.getCriteriaBuilder().count(rt));
	    Query q = em.createQuery(cq);
	    return ((Long) q.getSingleResult()).intValue();
	} finally {
	    em.close();
	}
    }
    
}
