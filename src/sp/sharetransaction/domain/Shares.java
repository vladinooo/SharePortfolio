package sp.sharetransaction.domain;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Shares implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@Temporal(TemporalType.DATE)
	private Date date;
	private String dateFormatted;
	private String company;
	private double unitPrice;
	private double sharesAmount;
	private double sharesValue;

	
	public Shares() { 
		// for JPA use only }
	}
	
	public Shares(String company, double unitPrice, double sharesAmount, double sharesValue) {
		
		date = new Date();
		this.company = company;
		this.unitPrice = unitPrice;
		this.sharesAmount = sharesAmount;
		this.sharesValue = sharesValue;		
	}

	
	public int getId() {
		return id;
	}


	public Date getDate() {
		return date;
	}

	
	public String getDateFormatted() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
		return dateFormat.format(date);
	}

	public void setDateFormatted(String dateFormatted) {
		this.dateFormatted = dateFormatted;
	}

	public String getCompany() {
		return company;
	}
	
	public void setCompany(String company) {
		this.company = company;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getSharesAmount() {
		return sharesAmount;
	}

	public void setSharesAmount(double sharesAmount) {
		this.sharesAmount = sharesAmount;
	}

	public double getSharesValue() {
		return sharesValue;
	}
	
	public void setSharesValue(double sharesValue) {
		this.sharesValue = sharesValue;
	}


}
