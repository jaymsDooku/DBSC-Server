package io.jayms.dbsc.server.repository;

import io.jayms.dbsc.interfaces.model.ConnectionSpec;
import io.jayms.dbsc.interfaces.model.Database;
import io.jayms.dbsc.interfaces.repository.DatabaseRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class DatabaseRepositoryImpl implements DatabaseRepository {

    @PersistenceContext(unitName = "dbsc")
    private EntityManager em;

    @Override
    public void create(Database database) {
        em.persist(database);
    }

    @Override
    public void update(Database database) {
        em.merge(database);
    }

    @Override
    public Database find(long id) {
        return em.find(Database.class, id);
    }

    @Override
    public List<Database> findAll(long connSpecId) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Database> query = builder.createQuery(Database.class);
        Root<Database> root =  query.from(Database.class);
        CriteriaQuery<Database> allDB = query.select(root)
                .where(builder.equal(root.get("conn_id"), connSpecId));
        TypedQuery<Database> allDBQuery = em.createQuery(allDB);
        return allDBQuery.getResultList();
    }

    @Override
    public List<Database> findAll() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Database> query = builder.createQuery(Database.class);
        Root<Database> root =  query.from(Database.class);
        CriteriaQuery<Database> all = query.select(root);
        TypedQuery<Database> allQuery = em.createQuery(all);
        return allQuery.getResultList();
    }

    @Override
    public void delete(long id) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<Database> criteriaDelete = builder.createCriteriaDelete(Database.class);

        Root<Database> root = criteriaDelete.from(Database.class);
        criteriaDelete.where(builder.equal(root.get("id"), id));

        Query query = em.createQuery(criteriaDelete);
        query.executeUpdate();
    }
}
