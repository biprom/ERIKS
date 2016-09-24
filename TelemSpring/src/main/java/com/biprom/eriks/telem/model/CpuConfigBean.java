package com.biprom.eriks.telem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CpuConfigBean {

	@Id
	private String id;


	@Indexed
	private String cardNumber;

	private String ioNumber;

	private String ioName;


	public CpuConfigBean(String cardnumb, String ionumb, String ioname) {
		this.cardNumber = cardnumb;
		this.ioNumber = ionumb;
		this.ioName = ioname;


	}


	public CpuConfigBean() {


	}


	public String getCardNumber() {
		return cardNumber;
	}


	public void setCardNumber(String cardNumber1) {
		this.cardNumber = cardNumber1;
	}


	public String getIoNumber() {
		return ioNumber;
	}


	public void setIoNumber(String ioNumber1) {
		this.ioNumber = ioNumber1;
	}


	public String getIoName() {
		return ioName;
	}


	public void setIoName(String ioName1) {
		this.ioName = ioName1;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


}
