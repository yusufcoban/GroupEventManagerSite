package com.example;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "events")
public class events implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int eventid;
	@DateTimeFormat(pattern="dd.MM.yyyy HH:mm")
	@Column(name = "eventdate")
	private Date eventdate;
	@Column(name = "eventplace")
	private String eventplace;
	@Column(name = "eventstatus")
	private boolean status;
	@Column(name = "infos")
	private String infos;
	
	@ManyToMany(mappedBy = "eventSet")
	private Set<user> userliste;

	private String sportart;
	
	@Column(name="useranzahl")
	private int useranzahl;

	
	
	@Column(name="maxspieler")
	private int maxspieler;

	public events() {
		setsportart("Yoga");
		setMaxspieler(15);
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime(); // get back a Date object
		setEventdate(date);
	}

	
	public int getUseranzahl() {
		useranzahl=getUserliste().size();
		return useranzahl;
	}


	public void setUseranzahl(int useranzahl) {
		this.useranzahl = useranzahl;
	}


	public events(String name) {
		setEventplace(name);
	}

	public events(events search, HashSet<user> hashSet) {
		setUser(hashSet);
	}

	public Set<user> getUser() {
		return userliste;
	}
	
	public int getUserAnzahl(){
		return userliste.size();
	}

	public void setUser(Set<user> user) {
		this.userliste = user;
	}

	public void setEventid(int eventid) {
		this.eventid = eventid;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "sportart")
	public String getsportart() {
		return sportart;
	}

	public void setsportart(sportarten sportartenclass) {
		this.sportart = sportartenclass.getsportart();
	}

	public void setsportart(String sportartString) {
		this.sportart = sportartString;
	}

	public Set<user> getUserliste() {
		return userliste;
	}

	public void setUserliste(Set<user> userliste) {
		this.userliste = userliste;
	}

	public int getEventid() {
		return eventid;
	}

	public Date getEventdate() {
		return eventdate;
	}

	public void setEventdate(Date eventdate) {
		this.eventdate = eventdate;
	}

	public String getEventplace() {
		return eventplace;
	}

	public void setEventplace(String eventplace) {
		this.eventplace = eventplace;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "maxspieler")
	public int getmaxspieler() {
		return maxspieler;
	}
	
	public void setMaxspieler(int maxspieler) {
		this.maxspieler = maxspieler;
	}
	
	public void setMaxspieler(sportarten sport) {
		this.maxspieler = sport.getMaxspieler();
	}

	@Override
	public String toString() {
		return ("Eventid : " + getEventid() + "Eventplace" + getEventplace());
	}

	public String getInfos() {
		return infos;
	}

	public void setInfos(String infos) {
		this.infos = infos;
	}

}
