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

/*
 * Similar logic from the BuySharsBean only with reverse effect resulting in negative operation
 * and subtracting shares
 */

@ManagedBean(name="sellShares")
@SessionScoped
public class SellSharesBean {
	
	private String shareCompany;
	private List<SelectItem> options = null;
	private List<SelectItem> amountValueOptions = null;
	
	private Shares shares;
	private List<Shares> allShares;
	
	private double shareUnitPrice;
	private String sharesAmount; // used for fetching stored values
	private String sharesValue; // used for fetching stored values
	private String sharesAmountInput;
	private String sharesValueInput;
	private String amount_valueErrorMsg;
	private String amount_value_option;
	private boolean amountTxtBox;
	private boolean valueTxtBox;
	private String emptyTxtFieldErrorMsg = "field is empty";
	private double shareAmountTotalPrice;
	private double shareValueTotalPrice;
	private String sharesAmountOrValue;
	private String sharesSoldPriceTotal;
	private ShareTransaction st;
	private Shares sh;
	
	@EJB
	private ShareTransactionManipulationLocal stml;
	
	@EJB
	private ShareBrokerClient sbi;
	
	private String selectCompanyErrorMsg;

	
	public SellSharesBean() {
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
		shares = new Shares("", 0, 0, 0);
		
		amountValueOptions = new ArrayList<SelectItem>();
		SelectItem theItem0 = new SelectItem("Amount", "Amount");
		SelectItem theItem1 = new SelectItem("Value", "Value");
		amountValueOptions.add(theItem0);
		amountValueOptions.add(theItem1);
		setAmountTxtBox(false);
		setValueTxtBox(true);
		
		setSharesAmountInput("0");
		setSharesValueInput("0.0");
	}

	
	public double getShareUnitPrice() {
		return shareUnitPrice;
	}


	public void setShareUnitPrice(double shareUnitPrice) {
		this.shareUnitPrice = shareUnitPrice;
	}

	public void __getPrice() throws UnknownCompanyException {
		shareUnitPrice = formatDouble(sbi._getPrice(shareCompany));
	 // shareUnitPrice = sbi._getPrice(shareCompany);
		setSelectCompanyErrorMsg(" ");
	}

	public Shares getShares() {
		return shares;
	}

	public void setShares(Shares shares) {
		this.shares = shares;
	}
	
	
	public List<Shares> getAllShares() throws UnknownCompanyException {
		getSharesByCompany();
		return allShares;
	}
	
	
	public String getSharesAmountAndValueByCompany() throws UnknownCompanyException {
		
		getSharesByCompany();
		
		for (Shares s: allShares) {
			if (s.getCompany().equals(shareCompany)) {
				sharesAmount = Double.toString(s.getSharesAmount());
				sharesValue = Double.toString(s.getSharesValue());
			}
		}
		return null;
	}
	
	public void getSharesByCompany() throws UnknownCompanyException {
		
		if (shareCompany.equals("--")) {
			// list all company's shares
			allShares = stml.listAllShares();
		}
		else {
			allShares = stml.listSharesByCompany(shareCompany);
		}
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

	public List<SelectItem> getOptions() {
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
			return "sell_amount_or_value";
		}
	}


	public String getSharesAmount() {
		return sharesAmount;
	}


	public void setSharesAmount(String sharesAmount) {
		this.sharesAmount = sharesAmount;
	}


	public String getSharesValue() {
		return sharesValue;
	}


	public void setSharesValue(String sharesValue) {
		this.sharesValue = sharesValue;
	}

	
	public String getSharesAmountInput() {
		return sharesAmountInput;
	}


	public void setSharesAmountInput(String sharesAmountInput) {
		this.sharesAmountInput = sharesAmountInput;
	}


	public String getSharesValueInput() {
		return sharesValueInput;
	}


	public void setSharesValueInput(String sharesValueInput) {
		this.sharesValueInput = sharesValueInput;
	}

	public double getShareAmountTotalPrice() {
		return shareAmountTotalPrice;
	}
	
	// calculate most current price of shares from users shares amount
	public void setShareAmountTotalPrice() {
		shareAmountTotalPrice = Double.parseDouble(sharesAmountInput) * shareUnitPrice;
		formatDouble(shareAmountTotalPrice);
	}

	public double getShareValueTotalPrice() {
		return shareValueTotalPrice;
	}
	
	// calculate most current price of shares from users shares value
	public void setShareValueTotalPrice() {
		shareValueTotalPrice = Double.parseDouble(sharesValueInput);
		formatDouble(shareValueTotalPrice);
	}
	

	public String sell() throws UnknownCompanyException {
		
		if ((Double.parseDouble(sharesAmountInput) >= 1) && (Double.parseDouble(sharesAmountInput) <= Double.parseDouble(sharesAmount))) {
			if (Double.parseDouble(sharesValueInput) != 0.0) {
				setAmount_valueErrorMsg(" * ");
				return null;
			}

			__getPrice(); // get current share price
			// get amount of shares
			sharesAmountOrValue = sharesAmountInput;
			setShareAmountTotalPrice();
			shareValueTotalPrice = 0;
			
			// create new transaction object with negative sign to indicate sale
			st = new ShareTransaction(shareCompany, shareUnitPrice, - Double.parseDouble(sharesAmountOrValue), formatDouble(shareAmountTotalPrice));
					
			// create new shares object to update db
			sh = new Shares(shareCompany, shareUnitPrice, formatDouble(Double.parseDouble(sharesAmountOrValue)), formatDouble(shareAmountTotalPrice));
						
			return "sellconfirm";
		}
		
		else {
			setAmount_valueErrorMsg(" * ");
		}
		
		
		if ((Double.parseDouble(sharesValueInput) >= 0.1) && (Double.parseDouble(sharesValueInput) <= Double.parseDouble(sharesValue))) {
			if (Double.parseDouble(sharesAmountInput) != 0) {
				setAmount_valueErrorMsg(" * ");
				return null;
			}
			
			__getPrice(); // get current share price
			// get amount of shares from value
			sharesAmountOrValue = Double.toString(Double.parseDouble(sharesValueInput) / shareUnitPrice);
			setShareValueTotalPrice();
			shareAmountTotalPrice = 0;
		
			// create new transaction object with negative sign to indicate sale
			st = new ShareTransaction(shareCompany, shareUnitPrice, formatDouble(- Double.parseDouble(sharesAmountOrValue)), formatDouble(shareValueTotalPrice));
			
			// create new shares object to update db
			sh = new Shares(shareCompany, shareUnitPrice, formatDouble(Double.parseDouble(sharesAmountOrValue)), formatDouble(shareValueTotalPrice));
						
			return "sellconfirm";
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

	public String selectAmountOrValue() {
		
		if (amount_value_option.equals("Amount")) {
			setSharesAmountInput("0");
			setSharesValueInput("0.0");
			setAmountTxtBox(false);
			setValueTxtBox(true);
		}
		if (amount_value_option.equals("Value")) {
			setSharesAmountInput("0");
			setSharesValueInput("0.0");
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
		public String getSharesSoldPriceTotal() {
			
			if (shareValueTotalPrice == 0) {
				sharesSoldPriceTotal = Double.toString(formatDouble(shareAmountTotalPrice));
			}
			
			if (shareAmountTotalPrice == 0) {
				sharesSoldPriceTotal = Double.toString(formatDouble(shareValueTotalPrice));
			}
			return sharesSoldPriceTotal;
		}

		public void setSharesSoldPriceTotal(String sharesSoldPriceTotal) {
			this.sharesSoldPriceTotal = sharesSoldPriceTotal;
		}

		public String getEmptyTxtFieldErrorMsg() {
			return emptyTxtFieldErrorMsg;
		}

		public void setEmptyTxtFieldErrorMsg(String emptyTxtFieldErrorMsg) {
			this.emptyTxtFieldErrorMsg = emptyTxtFieldErrorMsg;
		}

		public String confirm() throws UnknownCompanyException {
			
			// call EJB business logic
			stml.createShareTransaction(st);
			int id = stml._getSharesCompanyId(shareCompany);
			stml._subtractShares(sh, id);
			
			// refresh current page
			return "statement";
		}
}
