package com.example;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "sportarten")
public class sportarten implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "sportart")
	private String sportart;

	@Column(name = "maxspieler")
	private int maxspieler;

	@ElementCollection(targetClass = events.class)
	private Set<events> eventinkat;

	public sportarten(String sportartin, int maxspieler, HashSet<events> hashSet) {
		super();
		sportart = sportartin;
		this.maxspieler = maxspieler;

	}

	public String getsportart() {
		return sportart;
	}

	public void setsportart(String sportartin) {
		sportart = sportartin;
	}

	@OneToMany(targetEntity = events.class, mappedBy = "maxspieler", fetch = FetchType.EAGER)
	public int getMaxspieler() {
		return maxspieler;
	}

	public void setMaxspieler(int maxspieler) {
		this.maxspieler = maxspieler;
	}

	@OneToMany(targetEntity = events.class, mappedBy = "sportart", fetch = FetchType.EAGER)
	public Set<events> getEventinkat() {
		return eventinkat;
	}

	public void setEventinkat(Set<events> eventListe) {
		this.eventinkat = eventListe;
	}

	public sportarten() {

	}

}