package sp.web.backingbeans;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import sp.sharetransaction.domain.Shares;
import sp.sharetransaction.manipulation.ShareTransactionManipulationLocal;
import sp.webservices.ShareBrokerClient;
import uk.ac.susx.inf.ianw.shareManagement.UnknownCompanyException;

@ManagedBean(name="statement")
@SessionScoped
public class StatementBean {
	
	private String shareCompany;
	private double shareUnitPrice;
	private List<SelectItem> options = null;
	
	private Shares shares;
	private double totalSharesValue;
	
	private List<Shares> allShares;
	
	@EJB
	private ShareTransactionManipulationLocal stml;
	
	@EJB
	private ShareBrokerClient sbi;
	
	public StatementBean() {
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
	
	
	public void setAllShares(List<Shares> allShares) {
		this.allShares = allShares;
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
	
	// delegating the call to DAO to pull all the shares objects/ or for a particular company
	public void getSharesByCompany() throws UnknownCompanyException {
		
		if (shareCompany.equals("--")) {
			// pull before update
			allShares = updateShares(stml.listAllShares());
			calculateTotalSharesValue(allShares);
		}
		else {
			// pull before update
			allShares = updateShares(stml.listSharesByCompany(shareCompany));
			calculateTotalSharesValue(allShares);
		}
	}
	

	public double formatDouble(double price) {
		DecimalFormat df = new DecimalFormat("#.####");
		return Double.parseDouble(df.format(price));	
	}
	
	
	private double calculateTotalSharesValue(List<Shares> allShares) {
		double totalSharesValueTemp = 0;
		for (Shares sh: allShares) {
			totalSharesValueTemp += sh.getSharesValue();
		}
		
		totalSharesValue = formatDouble(totalSharesValueTemp);
		return totalSharesValue;	
	}

	
	public double getTotalSharesValue() {
		return totalSharesValue;
	}
	

	public double getShareUnitPrice() {
		return shareUnitPrice;
	}

	public void setShareUnitPrice(double shareUnitPrice) {
		this.shareUnitPrice = shareUnitPrice;
	}
	
	// updating the old share price by new price given by the shareBroker
	private List<Shares> updateShares(List<Shares> theAllShares) throws UnknownCompanyException {
		
		List<Shares> allSharesTemp = new ArrayList<Shares>();
		
		shareUnitPrice = formatDouble(sbi._getPrice(shareCompany));
		for (Shares sh: theAllShares) {
			shareUnitPrice = formatDouble(sbi._getPrice(sh.getCompany()));
			sh.setUnitPrice(shareUnitPrice);
			sh.setSharesValue(formatDouble(sh.getSharesAmount() * shareUnitPrice));
			stml._updateShares(sh, sh.getId());
			allSharesTemp.add(sh);
		}
		theAllShares = allSharesTemp;
		return theAllShares;
	}
	
}
