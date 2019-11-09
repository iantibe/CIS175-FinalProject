package project.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="item")
public class Item {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
	private long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="type")
	private String type;
	
	// CONSTRUCTORS
	public Item() {
	}
	
	public Item(long id, String n, String t) {
		this.setId(id);
		this.setName(n);
		this.setType(t);
	}
	
	public Item(String n, String t) {
		this.setName(n);
		this.setType(t);
	}

	// GETTERS AND SETTERS
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
