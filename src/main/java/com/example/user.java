package com.example;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class user implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue // (strategy = GenerationType.AUTO)
	private int userid;

	@Column(name = "userpwd")
	private String userpwd;
	@Column(name = "vorname")
	private String vorname;
	@Column(name = "nachname")
	private String nachname;
	@Column(name = "role")
	private String role;
	@Column(name = "email")
	private String email;

	public user(String string, Set<events> set,Set<sportarten> abos) {
		setVorname(string);
		setNachname(string);
		setUserpwd(string);
		setEventSet(set);
		setRole("user");
		setAbos(abos);
	}

	public user(String name) {
		setNachname(name);
		setVorname(name);

	}

	public user() {

	}

	public user(String vorname, String nachname, String password, Set<events> set, boolean admin) {
		this.vorname = vorname;
		this.nachname = nachname;
		this.userpwd = password;
		this.eventSet = set;
		if (admin == true) {
			this.role = "admin";
		}
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	// @SuppressWarnings("unchecked")
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "eventsmembers", joinColumns = @JoinColumn(name = "userid", referencedColumnName = "userid"), inverseJoinColumns = @JoinColumn(name = "eventid", referencedColumnName = "eventid"))
	private Set<events> eventSet;

	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "abos", joinColumns = @JoinColumn(name = "userid", referencedColumnName = "userid"), inverseJoinColumns = @JoinColumn(name = "sportart", referencedColumnName = "sportart"))
	private Set<sportarten> abos;
	
	public Set<sportarten> getAbos() {
		return abos;
	}

	public void setAbos(Set<sportarten> abos) {
		this.abos = abos;
	}

	public String getUserpwd() {
		return userpwd;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public int getUserid() {
		return userid;
	}

	public void setEventSet(Set<events> eventSet) {
		this.eventSet = eventSet;
	}

	public Set<events> getEventSet() {
		return eventSet;
	};

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
