package io.jayms.dbsc.server.repository;

import io.jayms.dbsc.interfaces.model.Database;
import io.jayms.dbsc.interfaces.model.Report;
import io.jayms.dbsc.interfaces.repository.ReportRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ReportRepositoryImpl implements ReportRepository {

    @PersistenceUnit(unitName = "dbsc")
    private EntityManager em;

    @Override
    public void create(Report report) {
        em.persist(report);
    }

    @Override
    public void update(Report report) {
        em.merge(report);
    }

    @Override
    public Report find(long id) {
        return em.find(Report.class, id);
    }

    @Override
    public List<Report> findAll(long databaseId) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Report> query = builder.createQuery(Report.class);

        Root<Report> root = query.from(Report.class);
        CriteriaQuery<Report> allReport = query.select(root)
                .where(builder.equal(root.get("database_id"), databaseId));

        TypedQuery<Report> allReportQuery = em.createQuery(allReport);
        return allReportQuery.getResultList();
    }

    @Override
    public List<Report> findAll() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Report> query = builder.createQuery(Report.class);

        Root<Report> root = query.from(Report.class);
        CriteriaQuery<Report> all = query.select(root);

        TypedQuery<Report> allQuery = em.createQuery(query);
        return allQuery.getResultList();
    }

    @Override
    public void delete(long id) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<Report> criteriaDelete = builder.createCriteriaDelete(Report.class);

        Root<Report> root = criteriaDelete.from(Report.class);
        criteriaDelete.where(builder.equal(root.get("id"), id));

        Query query = em.createQuery(criteriaDelete);
        query.executeUpdate();
    }
}
