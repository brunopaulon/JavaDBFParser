package entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Entity
public class DiseaseOccurrence {

	@Id
	@GeneratedValue
	private int id;
	
	private String disease;
	private Date occurrenceDate;
	
	private Date birthday;
	private String zipCode; // Initially only brazilian
	private String birthProvince;
	private String gender;
	private String cpf;
	
	
	private Date createdAt;
	private Date modifiedAt;
	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDisease() {
		return disease;
	}

	public void setDisease(String disease) {
		this.disease = disease;
	}

	public Date getOccurrenceDate() {
		return occurrenceDate;
	}

	public void setOccurrenceDate(Date occurrenceDate) {
		this.occurrenceDate = occurrenceDate;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getBirthProvince() {
		return birthProvince;
	}

	public void setBirthProvince(String birthProvince) {
		this.birthProvince = birthProvince;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	@PrePersist
	protected void onCreate() {
		createdAt = new Date();
		modifiedAt = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		modifiedAt = new Date();
	}
	
}
