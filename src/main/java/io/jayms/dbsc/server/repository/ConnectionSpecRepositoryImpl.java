package io.jayms.dbsc.server.repository;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import io.jayms.dbsc.interfaces.model.ConnectionSpec;
import io.jayms.dbsc.interfaces.repository.ConnectionSpecRepository;

@ApplicationScoped
public class ConnectionSpecRepositoryImpl implements ConnectionSpecRepository {

	public ConnectionSpecRepositoryImpl() {
	}

	@PersistenceContext(unitName = "dbsc")
	private EntityManager em;

	@Override
	@Transactional(TxType.REQUIRED)
	public void create(ConnectionSpec connSpec) {
		em.persist(connSpec);
	}

	@Override
	@Transactional(TxType.REQUIRED)
	public ConnectionSpec update(ConnectionSpec connSpec) {
		return em.merge(connSpec);
	}

	@Override
	public ConnectionSpec find(long id) {
		return em.find(ConnectionSpec.class, id);
	}

	@Override
	public List<ConnectionSpec> findAll() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<ConnectionSpec> query = builder.createQuery(ConnectionSpec.class);
		Root<ConnectionSpec> root = query.from(ConnectionSpec.class);
		CriteriaQuery<ConnectionSpec> all = query.select(root);
		TypedQuery<ConnectionSpec> allQuery = em.createQuery(all);
		return allQuery.getResultList();
	}
	
	@Override
	@Transactional(TxType.REQUIRED)
	public void delete(long id) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaDelete<ConnectionSpec> criteriaDelete = builder.createCriteriaDelete(ConnectionSpec.class);

		Root<ConnectionSpec> root = criteriaDelete.from(ConnectionSpec.class);
		criteriaDelete.where(builder.equal(root.get("id"), id));

		Query query = em.createQuery(criteriaDelete);
		query.executeUpdate();
	}
	
}
