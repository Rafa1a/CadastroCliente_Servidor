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
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import model.Usuario;

/**
 *
 * @author Windows 10
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
	this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
	return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
	if (usuario.getMovimentacaoCollection() == null) {
	    usuario.setMovimentacaoCollection(new ArrayList<Movimentacao>());
	}
	EntityManager em = null;
	try {
	    em = getEntityManager();
	    em.getTransaction().begin();
	    Collection<Movimentacao> attachedMovimentacaoCollection = new ArrayList<Movimentacao>();
	    for (Movimentacao movimentacaoCollectionMovimentacaoToAttach : usuario.getMovimentacaoCollection()) {
		movimentacaoCollectionMovimentacaoToAttach = em.getReference(movimentacaoCollectionMovimentacaoToAttach.getClass(), movimentacaoCollectionMovimentacaoToAttach.getIdMovimentacao());
		attachedMovimentacaoCollection.add(movimentacaoCollectionMovimentacaoToAttach);
	    }
	    usuario.setMovimentacaoCollection(attachedMovimentacaoCollection);
	    em.persist(usuario);
	    for (Movimentacao movimentacaoCollectionMovimentacao : usuario.getMovimentacaoCollection()) {
		Usuario oldUsuarioOfMovimentacaoCollectionMovimentacao = movimentacaoCollectionMovimentacao.getUsuario();
		movimentacaoCollectionMovimentacao.setUsuario(usuario);
		movimentacaoCollectionMovimentacao = em.merge(movimentacaoCollectionMovimentacao);
		if (oldUsuarioOfMovimentacaoCollectionMovimentacao != null) {
		    oldUsuarioOfMovimentacaoCollectionMovimentacao.getMovimentacaoCollection().remove(movimentacaoCollectionMovimentacao);
		    oldUsuarioOfMovimentacaoCollectionMovimentacao = em.merge(oldUsuarioOfMovimentacaoCollectionMovimentacao);
		}
	    }
	    em.getTransaction().commit();
	} finally {
	    if (em != null) {
		em.close();
	    }
	}
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
	EntityManager em = null;
	try {
	    em = getEntityManager();
	    em.getTransaction().begin();
	    Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
	    Collection<Movimentacao> movimentacaoCollectionOld = persistentUsuario.getMovimentacaoCollection();
	    Collection<Movimentacao> movimentacaoCollectionNew = usuario.getMovimentacaoCollection();
	    List<String> illegalOrphanMessages = null;
	    for (Movimentacao movimentacaoCollectionOldMovimentacao : movimentacaoCollectionOld) {
		if (!movimentacaoCollectionNew.contains(movimentacaoCollectionOldMovimentacao)) {
		    if (illegalOrphanMessages == null) {
			illegalOrphanMessages = new ArrayList<String>();
		    }
		    illegalOrphanMessages.add("You must retain Movimentacao " + movimentacaoCollectionOldMovimentacao + " since its usuario field is not nullable.");
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
	    usuario.setMovimentacaoCollection(movimentacaoCollectionNew);
	    usuario = em.merge(usuario);
	    for (Movimentacao movimentacaoCollectionNewMovimentacao : movimentacaoCollectionNew) {
		if (!movimentacaoCollectionOld.contains(movimentacaoCollectionNewMovimentacao)) {
		    Usuario oldUsuarioOfMovimentacaoCollectionNewMovimentacao = movimentacaoCollectionNewMovimentacao.getUsuario();
		    movimentacaoCollectionNewMovimentacao.setUsuario(usuario);
		    movimentacaoCollectionNewMovimentacao = em.merge(movimentacaoCollectionNewMovimentacao);
		    if (oldUsuarioOfMovimentacaoCollectionNewMovimentacao != null && !oldUsuarioOfMovimentacaoCollectionNewMovimentacao.equals(usuario)) {
			oldUsuarioOfMovimentacaoCollectionNewMovimentacao.getMovimentacaoCollection().remove(movimentacaoCollectionNewMovimentacao);
			oldUsuarioOfMovimentacaoCollectionNewMovimentacao = em.merge(oldUsuarioOfMovimentacaoCollectionNewMovimentacao);
		    }
		}
	    }
	    em.getTransaction().commit();
	} catch (Exception ex) {
	    String msg = ex.getLocalizedMessage();
	    if (msg == null || msg.length() == 0) {
		Integer id = usuario.getIdUsuario();
		if (findUsuario(id) == null) {
		    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
	    Usuario usuario;
	    try {
		usuario = em.getReference(Usuario.class, id);
		usuario.getIdUsuario();
	    } catch (EntityNotFoundException enfe) {
		throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
	    }
	    List<String> illegalOrphanMessages = null;
	    Collection<Movimentacao> movimentacaoCollectionOrphanCheck = usuario.getMovimentacaoCollection();
	    for (Movimentacao movimentacaoCollectionOrphanCheckMovimentacao : movimentacaoCollectionOrphanCheck) {
		if (illegalOrphanMessages == null) {
		    illegalOrphanMessages = new ArrayList<String>();
		}
		illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Movimentacao " + movimentacaoCollectionOrphanCheckMovimentacao + " in its movimentacaoCollection field has a non-nullable usuario field.");
	    }
	    if (illegalOrphanMessages != null) {
		throw new IllegalOrphanException(illegalOrphanMessages);
	    }
	    em.remove(usuario);
	    em.getTransaction().commit();
	} finally {
	    if (em != null) {
		em.close();
	    }
	}
    }

    public List<Usuario> findUsuarioEntities() {
	return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
	return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
	EntityManager em = getEntityManager();
	try {
	    CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
	    cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
	EntityManager em = getEntityManager();
	try {
	    return em.find(Usuario.class, id);
	} finally {
	    em.close();
	}
    }
    public Usuario findUsuariosenha(String login, String senha) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.logon = :login AND u.senha = :senha", Usuario.class);
            query.setParameter("login", login);
            query.setParameter("senha", senha);
            return query.getSingleResult(); // Retorna o usuário se encontrado
        } catch (NoResultException e) {
            return null; // Retorna null se não encontrar um usuário com as credenciais
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
	EntityManager em = getEntityManager();
	try {
	    CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
	    Root<Usuario> rt = cq.from(Usuario.class);
	    cq.select(em.getCriteriaBuilder().count(rt));
	    Query q = em.createQuery(cq);
	    return ((Long) q.getSingleResult()).intValue();
	} finally {
	    em.close();
	}
    }
    
}
