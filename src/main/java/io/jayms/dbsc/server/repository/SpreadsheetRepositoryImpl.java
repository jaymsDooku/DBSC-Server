package io.jayms.dbsc.server.repository;

import io.jayms.dbsc.interfaces.model.Report;
import io.jayms.dbsc.interfaces.model.Spreadsheet;
import io.jayms.dbsc.interfaces.repository.SpreadsheetRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.List;

@ApplicationScoped
public class SpreadsheetRepositoryImpl implements SpreadsheetRepository {

    public SpreadsheetRepositoryImpl() {
    }

    @PersistenceUnit(unitName = "dbsc")
    private EntityManagerFactory emf;

    @Override
    @Transactional(TxType.REQUIRED)
    public void create(Spreadsheet spreadsheet) {
        EntityManager em = emf.createEntityManager();
        em.persist(spreadsheet);
        em.close();
    }

    @Override
    @Transactional(TxType.REQUIRED)
    public void update(Spreadsheet spreadsheet) {
        EntityManager em = emf.createEntityManager();
        em.merge(spreadsheet);
        em.close();
    }

    @Override
    public Spreadsheet find(long id) {
        EntityManager em = emf.createEntityManager();
        Spreadsheet spreadsheet = em.find(Spreadsheet.class, id);
        em.close();
        return spreadsheet;
    }

    @Override
    public List<Spreadsheet> findAll(long databaseId) {
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Spreadsheet> query = builder.createQuery(Spreadsheet.class);

        Root<Spreadsheet> root = query.from(Spreadsheet.class);
        CriteriaQuery<Spreadsheet> allReport = query.select(root)
                .where(builder.equal(root.get("report").get("id"), databaseId));

        TypedQuery<Spreadsheet> allReportQuery = em.createQuery(allReport);
        em.close();
        return allReportQuery.getResultList();
    }

    @Override
    public List<Spreadsheet> findAll() {
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Spreadsheet> query = builder.createQuery(Spreadsheet.class);

        Root<Spreadsheet> root = query.from(Spreadsheet.class);
        CriteriaQuery<Spreadsheet> all = query.select(root);

        TypedQuery<Spreadsheet> allQuery = em.createQuery(query);
        em.close();
        return allQuery.getResultList();
    }

    @Override
    @Transactional(TxType.REQUIRED)
    public void delete(long id) {
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<Spreadsheet> criteriaDelete = builder.createCriteriaDelete(Spreadsheet.class);

        Root<Spreadsheet> root = criteriaDelete.from(Spreadsheet.class);
        criteriaDelete.where(builder.equal(root.get("id"), id));

        Query query = em.createQuery(criteriaDelete);
        query.executeUpdate();
        em.close();
    }

}
