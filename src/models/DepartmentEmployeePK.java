package models;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the department_employee database table.
 * 
 */
@Embeddable
public class DepartmentEmployeePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="employee_id", insertable=false, updatable=false)
	private Long employeeId;

	@Column(name="department_id", insertable=false, updatable=false)
	private String departmentId;

	public DepartmentEmployeePK() {
	}
	public Long getEmployeeId() {
		return this.employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public String getDepartmentId() {
		return this.departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof DepartmentEmployeePK)) {
			return false;
		}
		DepartmentEmployeePK castOther = (DepartmentEmployeePK)other;
		return 
			this.employeeId.equals(castOther.employeeId)
			&& this.departmentId.equals(castOther.departmentId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.employeeId.hashCode();
		hash = hash * prime + this.departmentId.hashCode();
		
		return hash;
	}
}