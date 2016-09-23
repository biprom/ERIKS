package com.biprom.eriks.telem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Document
public class Person {

	@Id
	private String id;

	@Indexed
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date userDate;

	private String firstName;

	private String lastName;

	@Indexed(unique = true)
	private String loginName;

	private String passWord;

	private int pin;
	
	private String email;
	

	public Person(String first, String last, String login, String pass, String email) {
		setFirstName(first);
		setLastName(last);
		setLoginName(login);
		setPassWord(pass);
		setEmail(email);
	}

	private void setEmail(String email2) {
		this.email = email2;
		
	}

	public Person() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getUserDate() {
		return userDate;
	}

	public void setUserDate(Date userDate) {
		this.userDate = userDate;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}


}
