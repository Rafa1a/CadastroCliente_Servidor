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
import model.Pessoafisica;
import model.Pessoajuridica;
import model.Movimentacao;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Pessoa;

/**
 *
 * @author Windows 10
 */
public class PessoaJpaController implements Serializable {

    public PessoaJpaController(EntityManagerFactory emf) {
	this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
	return emf.createEntityManager();
    }

    public void create(Pessoa pessoa) throws PreexistingEntityException, Exception {
	if (pessoa.getMovimentacaoCollection() == null) {
	    pessoa.setMovimentacaoCollection(new ArrayList<Movimentacao>());
	}
	EntityManager em = null;
	try {
	    em = getEntityManager();
	    em.getTransaction().begin();
	    Pessoafisica pessoafisica = pessoa.getPessoafisica();
	    if (pessoafisica != null) {
		pessoafisica = em.getReference(pessoafisica.getClass(), pessoafisica.getIdPessoa());
		pessoa.setPessoafisica(pessoafisica);
	    }
	    Pessoajuridica pessoajuridica = pessoa.getPessoajuridica();
	    if (pessoajuridica != null) {
		pessoajuridica = em.getReference(pessoajuridica.getClass(), pessoajuridica.getIdPessoa());
		pessoa.setPessoajuridica(pessoajuridica);
	    }
	    Collection<Movimentacao> attachedMovimentacaoCollection = new ArrayList<Movimentacao>();
	    for (Movimentacao movimentacaoCollectionMovimentacaoToAttach : pessoa.getMovimentacaoCollection()) {
		movimentacaoCollectionMovimentacaoToAttach = em.getReference(movimentacaoCollectionMovimentacaoToAttach.getClass(), movimentacaoCollectionMovimentacaoToAttach.getIdMovimentacao());
		attachedMovimentacaoCollection.add(movimentacaoCollectionMovimentacaoToAttach);
	    }
	    pessoa.setMovimentacaoCollection(attachedMovimentacaoCollection);
	    em.persist(pessoa);
	    if (pessoafisica != null) {
		Pessoa oldPessoaOfPessoafisica = pessoafisica.getPessoa();
		if (oldPessoaOfPessoafisica != null) {
		    oldPessoaOfPessoafisica.setPessoafisica(null);
		    oldPessoaOfPessoafisica = em.merge(oldPessoaOfPessoafisica);
		}
		pessoafisica.setPessoa(pessoa);
		pessoafisica = em.merge(pessoafisica);
	    }
	    if (pessoajuridica != null) {
		Pessoa oldPessoaOfPessoajuridica = pessoajuridica.getPessoa();
		if (oldPessoaOfPessoajuridica != null) {
		    oldPessoaOfPessoajuridica.setPessoajuridica(null);
		    oldPessoaOfPessoajuridica = em.merge(oldPessoaOfPessoajuridica);
		}
		pessoajuridica.setPessoa(pessoa);
		pessoajuridica = em.merge(pessoajuridica);
	    }
	    for (Movimentacao movimentacaoCollectionMovimentacao : pessoa.getMovimentacaoCollection()) {
		Pessoa oldPessoaOfMovimentacaoCollectionMovimentacao = movimentacaoCollectionMovimentacao.getPessoa();
		movimentacaoCollectionMovimentacao.setPessoa(pessoa);
		movimentacaoCollectionMovimentacao = em.merge(movimentacaoCollectionMovimentacao);
		if (oldPessoaOfMovimentacaoCollectionMovimentacao != null) {
		    oldPessoaOfMovimentacaoCollectionMovimentacao.getMovimentacaoCollection().remove(movimentacaoCollectionMovimentacao);
		    oldPessoaOfMovimentacaoCollectionMovimentacao = em.merge(oldPessoaOfMovimentacaoCollectionMovimentacao);
		}
	    }
	    em.getTransaction().commit();
	} catch (Exception ex) {
	    if (findPessoa(pessoa.getIdPessoa()) != null) {
		throw new PreexistingEntityException("Pessoa " + pessoa + " already exists.", ex);
	    }
	    throw ex;
	} finally {
	    if (em != null) {
		em.close();
	    }
	}
    }

    public void edit(Pessoa pessoa) throws IllegalOrphanException, NonexistentEntityException, Exception {
	EntityManager em = null;
	try {
	    em = getEntityManager();
	    em.getTransaction().begin();
	    Pessoa persistentPessoa = em.find(Pessoa.class, pessoa.getIdPessoa());
	    Pessoafisica pessoafisicaOld = persistentPessoa.getPessoafisica();
	    Pessoafisica pessoafisicaNew = pessoa.getPessoafisica();
	    Pessoajuridica pessoajuridicaOld = persistentPessoa.getPessoajuridica();
	    Pessoajuridica pessoajuridicaNew = pessoa.getPessoajuridica();
	    Collection<Movimentacao> movimentacaoCollectionOld = persistentPessoa.getMovimentacaoCollection();
	    Collection<Movimentacao> movimentacaoCollectionNew = pessoa.getMovimentacaoCollection();
	    List<String> illegalOrphanMessages = null;
	    if (pessoafisicaOld != null && !pessoafisicaOld.equals(pessoafisicaNew)) {
		if (illegalOrphanMessages == null) {
		    illegalOrphanMessages = new ArrayList<String>();
		}
		illegalOrphanMessages.add("You must retain Pessoafisica " + pessoafisicaOld + " since its pessoa field is not nullable.");
	    }
	    if (pessoajuridicaOld != null && !pessoajuridicaOld.equals(pessoajuridicaNew)) {
		if (illegalOrphanMessages == null) {
		    illegalOrphanMessages = new ArrayList<String>();
		}
		illegalOrphanMessages.add("You must retain Pessoajuridica " + pessoajuridicaOld + " since its pessoa field is not nullable.");
	    }
	    for (Movimentacao movimentacaoCollectionOldMovimentacao : movimentacaoCollectionOld) {
		if (!movimentacaoCollectionNew.contains(movimentacaoCollectionOldMovimentacao)) {
		    if (illegalOrphanMessages == null) {
			illegalOrphanMessages = new ArrayList<String>();
		    }
		    illegalOrphanMessages.add("You must retain Movimentacao " + movimentacaoCollectionOldMovimentacao + " since its pessoa field is not nullable.");
		}
	    }
	    if (illegalOrphanMessages != null) {
		throw new IllegalOrphanException(illegalOrphanMessages);
	    }
	    if (pessoafisicaNew != null) {
		pessoafisicaNew = em.getReference(pessoafisicaNew.getClass(), pessoafisicaNew.getIdPessoa());
		pessoa.setPessoafisica(pessoafisicaNew);
	    }
	    if (pessoajuridicaNew != null) {
		pessoajuridicaNew = em.getReference(pessoajuridicaNew.getClass(), pessoajuridicaNew.getIdPessoa());
		pessoa.setPessoajuridica(pessoajuridicaNew);
	    }
	    Collection<Movimentacao> attachedMovimentacaoCollectionNew = new ArrayList<Movimentacao>();
	    for (Movimentacao movimentacaoCollectionNewMovimentacaoToAttach : movimentacaoCollectionNew) {
		movimentacaoCollectionNewMovimentacaoToAttach = em.getReference(movimentacaoCollectionNewMovimentacaoToAttach.getClass(), movimentacaoCollectionNewMovimentacaoToAttach.getIdMovimentacao());
		attachedMovimentacaoCollectionNew.add(movimentacaoCollectionNewMovimentacaoToAttach);
	    }
	    movimentacaoCollectionNew = attachedMovimentacaoCollectionNew;
	    pessoa.setMovimentacaoCollection(movimentacaoCollectionNew);
	    pessoa = em.merge(pessoa);
	    if (pessoafisicaNew != null && !pessoafisicaNew.equals(pessoafisicaOld)) {
		Pessoa oldPessoaOfPessoafisica = pessoafisicaNew.getPessoa();
		if (oldPessoaOfPessoafisica != null) {
		    oldPessoaOfPessoafisica.setPessoafisica(null);
		    oldPessoaOfPessoafisica = em.merge(oldPessoaOfPessoafisica);
		}
		pessoafisicaNew.setPessoa(pessoa);
		pessoafisicaNew = em.merge(pessoafisicaNew);
	    }
	    if (pessoajuridicaNew != null && !pessoajuridicaNew.equals(pessoajuridicaOld)) {
		Pessoa oldPessoaOfPessoajuridica = pessoajuridicaNew.getPessoa();
		if (oldPessoaOfPessoajuridica != null) {
		    oldPessoaOfPessoajuridica.setPessoajuridica(null);
		    oldPessoaOfPessoajuridica = em.merge(oldPessoaOfPessoajuridica);
		}
		pessoajuridicaNew.setPessoa(pessoa);
		pessoajuridicaNew = em.merge(pessoajuridicaNew);
	    }
	    for (Movimentacao movimentacaoCollectionNewMovimentacao : movimentacaoCollectionNew) {
		if (!movimentacaoCollectionOld.contains(movimentacaoCollectionNewMovimentacao)) {
		    Pessoa oldPessoaOfMovimentacaoCollectionNewMovimentacao = movimentacaoCollectionNewMovimentacao.getPessoa();
		    movimentacaoCollectionNewMovimentacao.setPessoa(pessoa);
		    movimentacaoCollectionNewMovimentacao = em.merge(movimentacaoCollectionNewMovimentacao);
		    if (oldPessoaOfMovimentacaoCollectionNewMovimentacao != null && !oldPessoaOfMovimentacaoCollectionNewMovimentacao.equals(pessoa)) {
			oldPessoaOfMovimentacaoCollectionNewMovimentacao.getMovimentacaoCollection().remove(movimentacaoCollectionNewMovimentacao);
			oldPessoaOfMovimentacaoCollectionNewMovimentacao = em.merge(oldPessoaOfMovimentacaoCollectionNewMovimentacao);
		    }
		}
	    }
	    em.getTransaction().commit();
	} catch (Exception ex) {
	    String msg = ex.getLocalizedMessage();
	    if (msg == null || msg.length() == 0) {
		Integer id = pessoa.getIdPessoa();
		if (findPessoa(id) == null) {
		    throw new NonexistentEntityException("The pessoa with id " + id + " no longer exists.");
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
	    Pessoa pessoa;
	    try {
		pessoa = em.getReference(Pessoa.class, id);
		pessoa.getIdPessoa();
	    } catch (EntityNotFoundException enfe) {
		throw new NonexistentEntityException("The pessoa with id " + id + " no longer exists.", enfe);
	    }
	    List<String> illegalOrphanMessages = null;
	    Pessoafisica pessoafisicaOrphanCheck = pessoa.getPessoafisica();
	    if (pessoafisicaOrphanCheck != null) {
		if (illegalOrphanMessages == null) {
		    illegalOrphanMessages = new ArrayList<String>();
		}
		illegalOrphanMessages.add("This Pessoa (" + pessoa + ") cannot be destroyed since the Pessoafisica " + pessoafisicaOrphanCheck + " in its pessoafisica field has a non-nullable pessoa field.");
	    }
	    Pessoajuridica pessoajuridicaOrphanCheck = pessoa.getPessoajuridica();
	    if (pessoajuridicaOrphanCheck != null) {
		if (illegalOrphanMessages == null) {
		    illegalOrphanMessages = new ArrayList<String>();
		}
		illegalOrphanMessages.add("This Pessoa (" + pessoa + ") cannot be destroyed since the Pessoajuridica " + pessoajuridicaOrphanCheck + " in its pessoajuridica field has a non-nullable pessoa field.");
	    }
	    Collection<Movimentacao> movimentacaoCollectionOrphanCheck = pessoa.getMovimentacaoCollection();
	    for (Movimentacao movimentacaoCollectionOrphanCheckMovimentacao : movimentacaoCollectionOrphanCheck) {
		if (illegalOrphanMessages == null) {
		    illegalOrphanMessages = new ArrayList<String>();
		}
		illegalOrphanMessages.add("This Pessoa (" + pessoa + ") cannot be destroyed since the Movimentacao " + movimentacaoCollectionOrphanCheckMovimentacao + " in its movimentacaoCollection field has a non-nullable pessoa field.");
	    }
	    if (illegalOrphanMessages != null) {
		throw new IllegalOrphanException(illegalOrphanMessages);
	    }
	    em.remove(pessoa);
	    em.getTransaction().commit();
	} finally {
	    if (em != null) {
		em.close();
	    }
	}
    }

    public List<Pessoa> findPessoaEntities() {
	return findPessoaEntities(true, -1, -1);
    }

    public List<Pessoa> findPessoaEntities(int maxResults, int firstResult) {
	return findPessoaEntities(false, maxResults, firstResult);
    }

    private List<Pessoa> findPessoaEntities(boolean all, int maxResults, int firstResult) {
	EntityManager em = getEntityManager();
	try {
	    CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
	    cq.select(cq.from(Pessoa.class));
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

    public Pessoa findPessoa(Integer id) {
	EntityManager em = getEntityManager();
	try {
	    return em.find(Pessoa.class, id);
	} finally {
	    em.close();
	}
    }

    public int getPessoaCount() {
	EntityManager em = getEntityManager();
	try {
	    CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
	    Root<Pessoa> rt = cq.from(Pessoa.class);
	    cq.select(em.getCriteriaBuilder().count(rt));
	    Query q = em.createQuery(cq);
	    return ((Long) q.getSingleResult()).intValue();
	} finally {
	    em.close();
	}
    }
    
}
