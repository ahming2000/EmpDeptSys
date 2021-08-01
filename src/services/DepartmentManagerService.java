package services;

import database.PostgresDatabase;
import models.DepartmentManager;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;

@Dependent
@Transactional
@SuppressWarnings("unchecked")
public class DepartmentManagerService {

    @PersistenceContext(unitName = "EmpDeptSys")
    EntityManager em;

    @Inject
    public DepartmentManagerService(@PostgresDatabase EntityManager em) {
        this.em = em;
    }

    public ArrayList<DepartmentManager> getManagerInvolved(String employee_id, String department_id){
        // Make sure id is parseable to Long datatype
        long employee_id_long;
        try{
            employee_id_long = Long.parseLong(employee_id);
        } catch (NumberFormatException exception){
            return null;
        }

        try{
            Query q = em.createNamedQuery("DepartmentManager.find.empId.deptId");
            q.setParameter("employee_id", employee_id_long);
            q.setParameter("department_id", department_id);
            return (ArrayList<DepartmentManager>) q.getResultList();
        } catch (NoResultException exception){
            return null;
        }
    }

    public boolean isManager(String employee_id, String department_id){
        for (DepartmentManager dm: getManagerInvolved(employee_id, department_id)){
            if(dm.isActive()){
                return true;
            }
        }
        return false;
    }
}
