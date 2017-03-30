package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.avaje.ebean.Model;

@Entity
public class Teacher extends Model {

	@Id
	@GeneratedValue
	private long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "hash")
	@Lob
	private byte[] hash;
	
	@Column(name = "salt")
	@Lob
	private byte[] salt;

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

	public byte[] getHash() {
		return hash;
	}

	public void setHash(byte[] hash) {
		this.hash = hash;
	}

	public byte[] getSalt() {
		return salt;
	}

	public void setSalt(byte[] salt) {
		this.salt = salt;
	}
}
