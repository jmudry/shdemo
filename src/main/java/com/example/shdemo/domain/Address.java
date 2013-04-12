package com.example.shdemo.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries({
	@NamedQuery(name = "address.all", query = "Select a from Address a"),
	@NamedQuery(name = "address.forPerson", query = "Select a from Address a where a.person = :person_id"),
	@NamedQuery(name = "person.byId", query = "Select a from Address a where a.id = :id")
})
public class Address {
	
	private Long id;

	private String ulica;
	private String miasto;
	private String numerDomu;
	private Person person;
	
	public Address(String ulica, String numerDomu, String miasto, Person person) {
		super();
		this.ulica = ulica;
		this.miasto = miasto;
		this.numerDomu = numerDomu;
		this.person = person;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUlica() {
		return ulica;
	}
	public void setUlica(String ulica) {
		this.ulica = ulica;
	}
	public String getMiasto() {
		return miasto;
	}
	public void setMiasto(String miasto) {
		this.miasto = miasto;
	}
	public String getNumerDomu() {
		return numerDomu;
	}
	public void setNumerDomu(String numerDomu) {
		this.numerDomu = numerDomu;
	}

	@ManyToOne
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}

}
