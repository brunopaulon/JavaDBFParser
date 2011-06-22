package entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Entity
public class FieldDBF {

	@Id
	@GeneratedValue
	int id;
	int tableDBFId;
	String fieldName;
	String fieldType;
	
	// TODO discover what are this fields for
	String displacementOfField;
	int decimalPlaces;
	
	// Field Length unit is bytes
	int fieldLength;
	// The flags tells something importante about the column. 
	// TODO define the default values here
	String fieldFlags;
	int autoIncrStep;
	int autoIncrNext;
	String reserved;
	
	Date createdAt;
	Date modifiedAt;
	
	public int getId() {
		return id;
	}
	
	public int getTableDBFId(){
		return tableDBFId;
	}
	
	public void setTableDBFId(int tableDBFId){
		this.tableDBFId = tableDBFId;
	}
	
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	
	public String getFieldType() {
		return fieldType;
	}
	
	public void setDisplacementOfField(String displacementOfField) {
		this.displacementOfField = displacementOfField;
	}
	
	public String getDisplacementOfField() {
		return displacementOfField;
	}
	
	public void setFieldLength(int fieldLength) {
		this.fieldLength = fieldLength;
	}
	
	public int getFieldLength() {
		return fieldLength;
	}	
	
	public void setDecimalPlaces(int decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}
	
	public int getDecimalPlaces() {
		return decimalPlaces;
	}
	
	public void setFieldFlags(String fieldFlags) {
		this.fieldFlags = fieldFlags;
	}
	
	public String getFieldFlags() {
		return fieldFlags;
	}
	
	public void setAutoIncrStep(int autoIncrStep) {
		this.autoIncrStep = autoIncrStep;
	}
	
	public int getAutoIncrStep() {
		return autoIncrStep;
	}
	
	public void setAutoIncrNext(int autoIncrNext) {
		this.autoIncrNext = autoIncrNext;
	}
	
	public int getAutoIncrNext() {
		return autoIncrNext;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	
	public String getReserved() {
		return reserved;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	
	public Date getModifiedAt(){
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
