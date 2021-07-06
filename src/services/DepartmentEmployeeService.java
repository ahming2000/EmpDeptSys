package services;

import database.PostgresDatabase;
import models.Department;
import models.DepartmentEmployee;
import models.DepartmentEmployeePK;
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
import java.util.Calendar;

@Dependent
@Transactional
public class DepartmentEmployeeService {

    @PersistenceContext(unitName = "EmpDeptSys")
    EntityManager em;

    @Inject
    public DepartmentEmployeeService(@PostgresDatabase EntityManager em) {
        this.em = em;
    }

    public ArrayList<DepartmentEmployee> getDepartmentsInvolved(String employee_id) {
        try {
            Query q = em.createNamedQuery("DepartmentEmployee.find.empId");
            q.setParameter("employeeId", Long.parseLong(employee_id));
            return (ArrayList<DepartmentEmployee>) q.getResultList();
        } catch (NoResultException exception) {
            return new ArrayList<>();
        }
    }

    public boolean isDuplicated(String employee_id, String department_id){
        long empId;
        try{
            empId = Long.parseLong(employee_id);
        } catch (NumberFormatException exception){
            return true;
        }

        try {
            Query q = em.createNamedQuery("DepartmentEmployee.find.empId.deptId");
            q.setParameter("employeeId", empId);
            q.setParameter("departmentId", department_id);
            q.getSingleResult();
            return true;
        } catch (NoResultException exception){
            return false;
        }
    }

    public int getEmployeeCount(String departmentId){
        try {
            Query q = em.createNativeQuery("SELECT DISTINCT COUNT(de.employee_id) AS count FROM employees.department_employee de WHERE de.department_id = :departmentId");
            q.setParameter("departmentId", departmentId);
            return ((BigInteger) q.getSingleResult()).intValue();
        } catch (NoResultException exception){
            return 0;
        }
    }

    public String getCurrentDepartmentName(String employee_id){
        for(DepartmentEmployee de: getDepartmentsInvolved(employee_id)){
            if(de.isActive()){
                return de.getDepartment().getDeptName();
            }
        }
        return "Resigned/Retired";
    }

    public String getCurrentDepartmentId(String employee_id){
        for(DepartmentEmployee de: getDepartmentsInvolved(employee_id)){
            if(de.isActive()){
                return de.getDepartment().getId();
            }
        }
        return "Resigned/Retired";
    }

    public DepartmentEmployee getCurrentDepartmentEmployee(String employee_id){
        for(DepartmentEmployee de: getDepartmentsInvolved(employee_id)){
            if(de.isActive()){
                return de;
            }
        }
        return null;
    }

    public void addDepartmentEmployee(Employee employee, Department department){
        DepartmentEmployeePK dePK = new DepartmentEmployeePK();
        dePK.setEmployeeId(employee.getId());
        dePK.setDepartmentId(department.getId());

        DepartmentEmployee de = new DepartmentEmployee();
        de.setEmployee(employee);
        de.setDepartment(department);
        try {
            java.util.Date date = new java.util.Date();
            de.setFromDate(new java.sql.Date(date.getTime()));
            date = new java.util.Date(8099, Calendar.JANUARY, 1);
            de.setToDate(new java.sql.Date(date.getTime()));
        } catch (Exception ignored){}
        de.setId(dePK);

        em.persist(de);
    }

    public void updateDepartmentEmployee(String employee_id, String toDate){
        DepartmentEmployee de = getCurrentDepartmentEmployee(employee_id);

        try {
            java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
            de.setToDate(new java.sql.Date(date.getTime()));
        } catch (Exception ignored){}

        em.merge(de);
    }

    public void deleteDepartmentEmployee(String employee_id){
        ArrayList<DepartmentEmployee> deList = getDepartmentsInvolved(employee_id);
        for (DepartmentEmployee de: deList){
            em.remove(de);
        }
    }

}
