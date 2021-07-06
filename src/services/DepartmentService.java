package services;

import database.PostgresDatabase;
import models.Department;
import models.DepartmentEmployee;
import models.Employee;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Dependent
@Transactional
@SuppressWarnings("unchecked")
public class DepartmentService {

    @PersistenceContext(unitName = "EmpDeptSys")
    EntityManager em;

    @Inject
    public DepartmentService(@PostgresDatabase EntityManager em) {
        this.em = em;
    }

    public Department getDepartment(String id) {
        try{
            Query q = em.createNamedQuery("Department.find");
            q.setParameter("id", id);
            return (Department) q.getSingleResult();
        } catch (NoResultException exception){
            return null;
        }
    }

    public ArrayList<Department> getAllDepartments() {
        try {
            Query q = em.createNamedQuery("Department.all");
            return (ArrayList<Department>) q.getResultList();
        } catch (NoResultException exception) {
            return new ArrayList<>();
        }
    }

}
