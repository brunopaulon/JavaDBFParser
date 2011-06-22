package entities;

// Produto.java
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Entity
public class TableDBF {

	@Id
	@GeneratedValue
	int id;
	String name;
	String dbfFileType;
	Date lastUpdate;
	int numberRecords;
	String tableFlag;
	Date createdAt;
	Date modifiedAt;
	
	public int getId() {
		return id;
	}
	
	
//	private void setId(int id) {
//		this.id = id;
//	}
	
	
	public String getName() {
		return name;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getDbfFileType() {
		return dbfFileType;
	}
	
	
	public void setDbfFileType(String dbfFileType) {
		this.dbfFileType = dbfFileType;
	}
	
	
	public Date getLastUpdate(){
		return lastUpdate;
	}
	
	
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	
	public int getNumberRecords(){
		return numberRecords;
	}
	
	
	public void setNumberRecords(int numberRecords) {
		this.numberRecords = numberRecords;
	}
	
	
	public String getTableFlag() {
		return tableFlag;
	}
	
	
	public void setTableFlag(String tableFlag) {
		this.tableFlag = tableFlag;
	}
	
	
	// Timestamps
	
	@PrePersist
	protected void onCreate() {
		createdAt = new Date();
		modifiedAt = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		modifiedAt = new Date();
	}
	
	
	
	public Date getCreatedAt() {
		return createdAt;
	}
		
	public void setCreatedAt(Date someUselessDate) {}

	public Date getModifiedAt() {
		return modifiedAt;
	}
	
	public void setModifiedAt(Date modifiedAt) {}
}

