package models;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the title database table.
 * 
 */
@Entity
@NamedQuery(name="Title.findAll", query="SELECT t FROM Title t")
@Table(name="title", schema="employees")
public class Title implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TitlePK id;

	@Temporal(TemporalType.DATE)
	@Column(name="to_date")
	private Date toDate;

	//bi-directional many-to-one association to Employee
	@ManyToOne
	@JoinColumn(name="employee_id",insertable=false, updatable=false)
	private Employee employee;

	public Title() {
	}

	public TitlePK getId() {
		return this.id;
	}

	public void setId(TitlePK id) {
		this.id = id;
	}

	public Date getToDate() {
		return this.toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Employee getEmployee() {
		return this.employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}