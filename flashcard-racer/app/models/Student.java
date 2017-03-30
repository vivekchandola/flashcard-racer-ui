package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.avaje.ebean.Model;

@Entity
public class Student extends Model {

	@Id
	@GeneratedValue
	private long id;
	
	@Column(name = "hash")
	@Lob
	private byte[] hash;
	
	@Column(name = "salt")
	@Lob
	private byte[] salt;
	
	@Column(name= "name")
	private String name;
	
	public String getName() {
		return name;
	}

	public long getId() {
		return id;
	}

	public void setName(String string) {
		this.name =string;
	}

	public void setId(long l) {
		this.id = l;
	}

	public byte[] getHash() {
		return hash;
	}

	public byte[] getSalt() {
		return salt;
	}

	public void setHash(byte[] hash_b) {
		this.hash = hash_b;
	}

	public void setSalt(byte[] salt_b) {
		this.salt = salt_b;
	}

}
