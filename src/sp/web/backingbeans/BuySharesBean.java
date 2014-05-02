package sp.web.backingbeans;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import sp.sharetransaction.domain.ShareTransaction;
import sp.sharetransaction.domain.Shares;
import sp.sharetransaction.manipulation.ShareTransactionManipulationLocal;
import sp.webservices.ShareBrokerClient;
import uk.ac.susx.inf.ianw.shareManagement.UnknownCompanyException;

@ManagedBean(name="buyShares")
@SessionScoped
public class BuySharesBean {
	
	private String shareCompany;
	private double shareUnitPrice;
	private String shareAmount;
	private double shareAmountTotalPrice;
	private String shareValue;
	private double shareValueTotalPrice;
	private String selectCompanyErrorMsg;
	private String amount_valueErrorMsg;
	private String amount_value_option;
	private boolean amountTxtBox;
	private boolean valueTxtBox;
	private String sharesAmountOrValue;
	private String sharesBoughtPriceTotal;
	private String emptyTxtFieldErrorMsg = "field is empty";
	private ShareTransaction st;
	private Shares sh1, sh2, sh3, sh4;
	private Shares tempSh;
	
	private List<SelectItem> options = null;
	private List<SelectItem> amountValueOptions = null;
	
	@EJB
	private ShareBrokerClient sbi;
	
	@EJB
	private ShareTransactionManipulationLocal stml;
	
	
	public BuySharesBean() {
		this.shareCompany = "--";
		options = new ArrayList<SelectItem>();
		SelectItem item0 = new SelectItem("--", "--");
		SelectItem item1 = new SelectItem("Google", "Google");
		SelectItem item2 = new SelectItem("Apple", "Apple");
		SelectItem item3 = new SelectItem("Oracle", "Oracle");
		SelectItem item4 = new SelectItem("Microsoft", "Microsoft");
		options.add(item0);
		options.add(item1);
		options.add(item2);
		options.add(item3);
		options.add(item4);
		
		amountValueOptions = new ArrayList<SelectItem>();
		SelectItem theItem0 = new SelectItem("Amount", "Amount");
		SelectItem theItem1 = new SelectItem("Value", "Value");
		amountValueOptions.add(theItem0);
		amountValueOptions.add(theItem1);
		setAmountTxtBox(false);
		setValueTxtBox(true);
		
		setShareAmount("0");
		setShareValue("0.0");
	}
	
	public List<SelectItem>  getOptions() {
		return options;
	}
	
	public void setOptions(List<SelectItem> options) {
		this.options = options;
	}
	
	public List<SelectItem>  getAmountValueOptions() {
		return amountValueOptions;
	}
	
	public void setAmountValueOptions(List<SelectItem> theAmountValueOptions) {
		this.amountValueOptions = theAmountValueOptions;
	}
	
	// access the shareBroker and get current share price
	public String __getPrice() throws UnknownCompanyException {
		shareUnitPrice = formatDouble(sbi._getPrice(shareCompany));
	 // shareUnitPrice = sbi._getPrice(shareCompany);
		setSelectCompanyErrorMsg(" ");
		return null;
	}

	public double formatDouble(double price) {
		DecimalFormat df = new DecimalFormat("#.####");
		return Double.parseDouble(df.format(price));
	}

	public String getShareCompany() {
		return shareCompany;
	}


	public void setShareCompany(String shareCompany) {
		this.shareCompany = shareCompany;
	}


	public double getShareUnitPrice() {
		return shareUnitPrice;
	}


	public void setShareUnitPrice(double shareUnitPrice) {
		this.shareUnitPrice = shareUnitPrice;
	}

	
	public String getShareAmount() {
		return shareAmount;
	}

	public void setShareAmount(String theShareAmount) {
		if (theShareAmount == null) {
			theShareAmount = "0";
		}
		else {
			shareAmount = theShareAmount;
		}
	}

	public String getShareValue() {
		return shareValue;
	}

	public void setShareValue(String theShareValue) {
		if (theShareValue == null) {
			theShareValue = "0.0";
		}
		else {
			shareValue = theShareValue;
		}
	}

	public double getShareAmountTotalPrice() {
		return shareAmountTotalPrice;
	}

	public void setShareAmountTotalPrice() {
		shareAmountTotalPrice = Integer.parseInt(shareAmount) * shareUnitPrice;
		formatDouble(shareAmountTotalPrice);
	}

	public double getShareValueTotalPrice() {
		return shareValueTotalPrice;
	}

	public void setShareValueTotalPrice() {
		shareValueTotalPrice = Double.parseDouble(shareValue);
		formatDouble(shareValueTotalPrice);
	}

	
	public String getSelectCompanyErrorMsg() {
		return selectCompanyErrorMsg;
	}

	public void setSelectCompanyErrorMsg(String selectCompanyErrorMsg) {
		this.selectCompanyErrorMsg = selectCompanyErrorMsg;
	}

	public String next() {
		
		if (shareCompany.equals("--")) {
			setSelectCompanyErrorMsg(" * ");
			return null;
		}
		else {
			return "buy_amount_or_value";
		}
	}
	
	/*
	 * Buy shares.
	 * Check the allowed amount/value and perform a simple input validation.
	 * Create a transaction object.
	 * Create a share object.
	 */
	public String buy() {
		
		initDbWithEmptyShares();
		
		if ((Integer.parseInt(shareAmount) >= 1) && (Integer.parseInt(shareAmount) <= 5000)) {
			if (Double.parseDouble(shareValue) != 0.0) {
				setAmount_valueErrorMsg(" * ");
				return null;
			}
			// get amount of shares
			sharesAmountOrValue = shareAmount;
			setShareAmountTotalPrice();
			shareValueTotalPrice = 0;
			
			// create new share transaction object
			st = new ShareTransaction(shareCompany, shareUnitPrice, Double.parseDouble(sharesAmountOrValue), formatDouble(shareAmountTotalPrice));
			
			// create new shares object
			tempSh = new Shares(shareCompany, shareUnitPrice, formatDouble(Double.parseDouble(sharesAmountOrValue)), formatDouble(shareAmountTotalPrice));
			
			return "buyconfirm";
		}
		
		else {
			setAmount_valueErrorMsg(" * ");
		}
		
		
		if ((Double.parseDouble(shareValue) >= 0.1) && (Double.parseDouble(shareValue) <= 10000.00)) {
			if (Integer.parseInt(shareAmount) != 0) {
				setAmount_valueErrorMsg(" * ");
				return null;
			}
			// get amount of shares from value
			sharesAmountOrValue = Double.toString(Double.parseDouble(shareValue) / shareUnitPrice);
			setShareValueTotalPrice();
			shareAmountTotalPrice = 0;
			
			// create new share transaction object
			st = new ShareTransaction(shareCompany, shareUnitPrice, formatDouble(Double.parseDouble(sharesAmountOrValue)), formatDouble(shareValueTotalPrice));
			
			// create new shares object
			tempSh = new Shares(shareCompany, shareUnitPrice, formatDouble(Double.parseDouble(sharesAmountOrValue)), formatDouble(shareValueTotalPrice));
			
			return "buyconfirm";
		}
		
		else {
			setAmount_valueErrorMsg(" * ");
			return null;
		}
		
	}
	

	public String getAmount_valueErrorMsg() {
		return amount_valueErrorMsg;
	}

	public void setAmount_valueErrorMsg(String amount_valueErrorMsg) {
		this.amount_valueErrorMsg = amount_valueErrorMsg;
	}

	public String getAmount_value_option() {
		return amount_value_option;
	}

	public void setAmount_value_option(String amount_value_option) {
		this.amount_value_option = amount_value_option;
	}

	
	public boolean isAmountTxtBox() {
		return amountTxtBox;
	}

	public void setAmountTxtBox(boolean amountTxtBox) {
		this.amountTxtBox = amountTxtBox;
	}

	public boolean isValueTxtBox() {
		return valueTxtBox;
	}

	public void setValueTxtBox(boolean valueTxtBox) {
		this.valueTxtBox = valueTxtBox;
	}
	
	
	// reset the input fields when switching the options
	public String selectAmountOrValue() {
		
		if (amount_value_option.equals("Amount")) {
			setShareAmount("0");
			setShareValue("0.0");
			setAmountTxtBox(false);
			setValueTxtBox(true);
		}
		if (amount_value_option.equals("Value")) {
			setShareAmount("0");
			setShareValue("0.0");
			setAmountTxtBox(true);
			setValueTxtBox(false);
		}
		return null;
	}
	
	public String getSharesAmountOrValue() {
		return Double.toString(formatDouble(Double.parseDouble(sharesAmountOrValue)));
	}
		
	public void setSharesAmountOrValue(String sharesAmountOrValue) {
		this.sharesAmountOrValue = sharesAmountOrValue;
	}

	// displays interchangeably in table depending on what type of purchase (amount/value)
	public String getSharesBoughtPriceTotal() {
		
		if (shareValueTotalPrice == 0) {
			sharesBoughtPriceTotal = Double.toString(formatDouble(shareAmountTotalPrice));
		}
		
		if (shareAmountTotalPrice == 0) {
			sharesBoughtPriceTotal = Double.toString(formatDouble(shareValueTotalPrice));
		}
		return sharesBoughtPriceTotal;
	}

	public void setSharesBoughtPriceTotal(String sharesBoughtPriceTotal) {
		this.sharesBoughtPriceTotal = sharesBoughtPriceTotal;
	}

	public String getEmptyTxtFieldErrorMsg() {
		return emptyTxtFieldErrorMsg;
	}

	public void setEmptyTxtFieldErrorMsg(String emptyTxtFieldErrorMsg) {
		this.emptyTxtFieldErrorMsg = emptyTxtFieldErrorMsg;
	}

	// Delegate call to input the transaction into db
	// Delegate call to input the shares into db
	public String confirm() {
		
		// call EJB business logic
		stml.createShareTransaction(st);
		int id = stml._getSharesCompanyId(shareCompany);
		stml._addShares(tempSh, id);
		
		// refresh current page
		return "statement";
	}
	
	//Initialize db with empty share objects for later update.
	private void initDbWithEmptyShares() {
		Number number = stml._getNumberOfRows();
		int rows = number.intValue();
		
		if (rows == 0) {
			sh1 = new Shares("Google", 0, 0, 0);
			sh2 = new Shares("Apple", 0, 0, 0);
			sh3 = new Shares("Oracle", 0, 0, 0);
			sh4 = new Shares("Microsoft", 0, 0, 0);
			stml.createShares(sh1); // Google
			stml.createShares(sh2); // Apple
			stml.createShares(sh3); // Oracle
			stml.createShares(sh4); // Microsoft
		}
		else {
			return;
		}
	}
	
}
