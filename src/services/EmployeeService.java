package services;

import database.PostgresDatabase;
import models.Employee;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@Dependent
@Transactional
@SuppressWarnings("unchecked")
public class EmployeeService {

    @PersistenceContext(unitName = "EmpDeptSys")
    EntityManager em;

    @Inject
    public EmployeeService(@PostgresDatabase EntityManager em) {
        this.em = em;
    }

    public ArrayList<Employee> getEmployees(int page, int paginate, String search, String department) {
        try {
            Query q = em.createNativeQuery("SELECT DISTINCT e.id FROM employees.employee e JOIN employees.department_employee de ON e.id = de.employee_id WHERE CONCAT(e.first_name, e.last_name, e.gender) LIKE '%" + search + "%' AND de.department_id LIKE '%" + department + "%' OFFSET :start LIMIT :paginate");
            q.setParameter("start", page * paginate - paginate);
            q.setParameter("paginate", paginate);
            ArrayList<BigInteger> ids = (ArrayList<BigInteger>) q.getResultList();
            
            String idsStr = implodeEmployeeId(ids);
            if (idsStr.equals("")){
                return new ArrayList<>();
            } else {
                q = em.createNativeQuery("SELECT * FROM employees.employee e WHERE e.id IN (" + implodeEmployeeId(ids) + ")", Employee.class);
                return (ArrayList<Employee>) q.getResultList();
            }
        } catch (NoResultException exception) {
            return new ArrayList<>();
        }
    }

    private String implodeEmployeeId(ArrayList<BigInteger> arrayList){
        ArrayList<String> list = new ArrayList<>();
        for (BigInteger i: arrayList){
            list.add(String.valueOf(i));
        }

        return String.join(", ", list);
    }

    public Employee getEmployee(String id) {
        // Make sure id is not null
        if (id == null) return null;

        // Make sure id is not empty string
        if (id.equals("")) return null;

        // Make sure id is parseable to Long datatype
        long idL;
        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException exception) {
            return null;
        }

        // Make sure result is found
        try {
            Query q = em.createNamedQuery("Employee.find");
            q.setParameter("id", idL);
            return (Employee) q.getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }

    public int getEmployeeCount() {
        try {
            Query q = em.createNativeQuery("SELECT COUNT(DISTINCT id) as count FROM employees.employee");
            return ((BigInteger) q.getSingleResult()).intValue();
        } catch (NoResultException exception) {
            return 0;
        }
    }

    public int getEmployeeCount(String search, String department){
        try {
            Query q = em.createNativeQuery("SELECT COUNT(DISTINCT e.id) AS count FROM employees.employee e JOIN employees.department_employee de ON e.id = de.employee_id WHERE CONCAT(e.first_name, e.last_name, e.gender) LIKE '%" + search + "%' AND de.department_id LIKE '%" + department + "%'");
            BigInteger count = (BigInteger) q.getSingleResult();

            return count.intValue();
        } catch (NoResultException exception) {
            return 0;
        }
    }

    public void addEmployee(Employee employee){
        em.persist(employee);
    }

    public void updateEmployee(Employee employee){
        em.merge(employee);
    }

    public void deleteEmployee(String id){
        Employee employee = getEmployee(id);
        em.remove(employee);
    }

    public boolean isEmployee(String id, String first_name, String last_name){
        long id_long;
        try{
            id_long = Long.parseLong(id);
        } catch (NumberFormatException exception){
            return false;
        }

        try {
            Query q = em.createNamedQuery("Employee.verify");
            q.setParameter("id", id_long);
            q.setParameter("first_name", first_name);
            q.setParameter("last_name", last_name);
            q.getSingleResult();
            return true;
        } catch(NoResultException exception) {
            return false;
        }
    }

}
