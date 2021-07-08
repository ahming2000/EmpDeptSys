package models;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the employee database table.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Employee.all", query = "SELECT e FROM Employee e"),
        @NamedQuery(name = "Employee.find", query = "SELECT e FROM Employee e WHERE e.id = :id"),
})
@Table(name = "employee", schema = "employees")
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "first_name")
    private String firstName;

    private String gender;

    @Temporal(TemporalType.DATE)
    @Column(name = "hire_date")
    private Date hireDate;

    @Column(name = "last_name")
    private String lastName;

    //bi-directional many-to-one association to DepartmentEmployee
    @OneToMany(mappedBy = "employee")
    private List<DepartmentEmployee> departmentEmployees;

    public Employee() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getHireDate() {
        return this.hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<DepartmentEmployee> getDepartmentEmployees() {
        return this.departmentEmployees;
    }

    public void setDepartmentEmployees(List<DepartmentEmployee> departmentEmployees) {
        this.departmentEmployees = departmentEmployees;
    }

    public DepartmentEmployee addDepartmentEmployee(DepartmentEmployee departmentEmployee) {
        getDepartmentEmployees().add(departmentEmployee);
        departmentEmployee.setEmployee(this);

        return departmentEmployee;
    }

    public DepartmentEmployee removeDepartmentEmployee(DepartmentEmployee departmentEmployee) {
        getDepartmentEmployees().remove(departmentEmployee);
        departmentEmployee.setEmployee(null);

        return departmentEmployee;
    }

    // Custom Getter
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getGenderLabel() {
        if (gender.equals("M")) {
            return "Male";
        } else if (gender.equals("F")) {
            return "Female";
        } else {
            return "Other";
        }
    }
    
}





