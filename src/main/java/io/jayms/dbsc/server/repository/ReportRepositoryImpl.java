package io.jayms.dbsc.server.repository;

import io.jayms.dbsc.interfaces.model.Database;
import io.jayms.dbsc.interfaces.model.Report;
import io.jayms.dbsc.interfaces.repository.ReportRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.List;

@ApplicationScoped
public class ReportRepositoryImpl implements ReportRepository {

    public ReportRepositoryImpl() {
    }

    @PersistenceContext(unitName = "dbsc")
    private EntityManager em;

    @Override
    @Transactional(TxType.REQUIRED)
    public void create(Report report) {
        em.persist(report);
    }

    @Override
    @Transactional(TxType.REQUIRED)
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
                .where(builder.equal(root.get("database").get("id"), databaseId));

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
    @Transactional(TxType.REQUIRED)
    public void delete(long id) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<Report> criteriaDelete = builder.createCriteriaDelete(Report.class);

        Root<Report> root = criteriaDelete.from(Report.class);
        criteriaDelete.where(builder.equal(root.get("id"), id));

        Query query = em.createQuery(criteriaDelete);
        query.executeUpdate();
    }
}
