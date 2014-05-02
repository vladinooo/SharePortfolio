package sp.web.backingbeans;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

import sp.sharetransaction.domain.ShareTransaction;
import sp.sharetransaction.manipulation.ShareTransactionManipulationLocal;

@ManagedBean(name="pastOrders")
public class PastOrdersBean {
	

	private String shareCompany;
	private List<SelectItem> options = null;
	
	private List<ShareTransaction> shareTransactions;
	
	@EJB
	private ShareTransactionManipulationLocal stml;
	
	
	public PastOrdersBean() {
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



	public void _deleteShareTransaction(ShareTransaction st) {
		stml.removeShareTransaction(st);
	}
	
	
	public List<ShareTransaction> getShareTransactions() {
		getShareTransactionsByCompany();
		return shareTransactions;
	}
	
	
	public String getShareTransactionsByCompany() {
		
		if (shareCompany.equals("--")) {
			shareTransactions = stml.listAllShareTransactions();
			return null;
		}
		else {
			shareTransactions = stml.listShareTransactionsByCompany(shareCompany);
			return null;
		}
	}

}
