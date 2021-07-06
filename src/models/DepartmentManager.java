package models;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the department_manager database table.
 * 
 */
@Entity
@Table(name="department_manager", schema="employees")
@NamedQuery(name="DepartmentManager.find", query="SELECT d FROM DepartmentManager d")
public class DepartmentManager implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DepartmentManagerPK id;

	@Temporal(TemporalType.DATE)
	@Column(name="from_date")
	private Date fromDate;

	@Temporal(TemporalType.DATE)
	@Column(name="to_date")
	private Date toDate;

	//bi-directional many-to-one association to Department
	@ManyToOne
	@JoinColumn(name="department_id", insertable=false, updatable=false)
	private Department department;

	//bi-directional many-to-one association to Employee
	@ManyToOne
	@JoinColumn(name="employee_id",insertable=false, updatable=false)
	private Employee employee;

	public DepartmentManager() {
	}

	public DepartmentManagerPK getId() {
		return this.id;
	}

	public void setId(DepartmentManagerPK id) {
		this.id = id;
	}

	public Date getFromDate() {
		return this.fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return this.toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Department getDepartment() {
		return this.department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Employee getEmployee() {
		return this.employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}