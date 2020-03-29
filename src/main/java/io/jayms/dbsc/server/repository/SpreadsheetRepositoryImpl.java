package io.jayms.dbsc.server.repository;

import io.jayms.dbsc.interfaces.model.Report;
import io.jayms.dbsc.interfaces.model.Spreadsheet;
import io.jayms.dbsc.interfaces.repository.SpreadsheetRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class SpreadsheetRepositoryImpl implements SpreadsheetRepository {

    @PersistenceUnit(unitName = "dbsc")
    private EntityManager em;

    @Override
    public void create(Spreadsheet report) {
        em.persist(report);
    }

    @Override
    public void update(Spreadsheet report) {
        em.merge(report);
    }

    @Override
    public Spreadsheet find(long id) {
        return em.find(Spreadsheet.class, id);
    }

    @Override
    public List<Spreadsheet> findAll(long databaseId) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Spreadsheet> query = builder.createQuery(Spreadsheet.class);

        Root<Spreadsheet> root = query.from(Spreadsheet.class);
        CriteriaQuery<Spreadsheet> allReport = query.select(root)
                .where(builder.equal(root.get("report_id"), databaseId));

        TypedQuery<Spreadsheet> allReportQuery = em.createQuery(allReport);
        return allReportQuery.getResultList();
    }

    @Override
    public List<Spreadsheet> findAll() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Spreadsheet> query = builder.createQuery(Spreadsheet.class);

        Root<Spreadsheet> root = query.from(Spreadsheet.class);
        CriteriaQuery<Spreadsheet> all = query.select(root);

        TypedQuery<Spreadsheet> allQuery = em.createQuery(query);
        return allQuery.getResultList();
    }

    @Override
    public void delete(long id) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<Spreadsheet> criteriaDelete = builder.createCriteriaDelete(Spreadsheet.class);

        Root<Spreadsheet> root = criteriaDelete.from(Spreadsheet.class);
        criteriaDelete.where(builder.equal(root.get("id"), id));

        Query query = em.createQuery(criteriaDelete);
        query.executeUpdate();
    }

}
