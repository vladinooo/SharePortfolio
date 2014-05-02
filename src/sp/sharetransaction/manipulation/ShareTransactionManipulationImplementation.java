package sp.sharetransaction.manipulation;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import sp.dataaccess.DataAccess;
import sp.sharetransaction.domain.ShareTransaction;
import sp.sharetransaction.domain.Shares;
import sp.webservices.ShareBrokerClient;
import uk.ac.susx.inf.ianw.shareManagement.UnknownCompanyException;

@Stateless
public class ShareTransactionManipulationImplementation implements
		ShareTransactionManipulationRemote, ShareTransactionManipulationLocal {
	
	@EJB // dependency injection
	private DataAccess dao;
	
	@EJB
	private ShareBrokerClient sbi;
	

	/***************
	 * Transactions
	 ***************/
	
	public void createShareTransaction(ShareTransaction newShareTransaction) {
		dao.insertShareTransaction(newShareTransaction);
	}
	
	public void removeShareTransaction(ShareTransaction st) {
		dao.deleteShareTransaction(st);
	}
	
	public List<ShareTransaction> listAllShareTransactions() {
		return dao.findAllShareTransactions();
	}
	
	public List<ShareTransaction> listShareTransactionsByCompany(String company) {
		return dao.findShareTransactionByCompany(company);
	}
	
	
	/***************
	 * Shares
	 ***************/
	
	public int _getSharesCompanyId(String company) {
		return dao.getSharesCompanyId(company);
	}
	
	public Number _getNumberOfRows() {
		return dao.getNumberOfRows();
	}
	
	public void createShares(Shares newShares) {
		dao.insertShares(newShares);
	}
	
	public void _addShares(Shares sh, int id) {
		dao.addShares(sh, id);
	}
	
	public void _subtractShares(Shares sh, int id) {
		dao.subtractShares(sh, id);
	}
	
	public void _updateShares(Shares sh, int id) {
		dao.updateShares(sh, id);
	}
	
	public void removeShares(Shares sh) {
		dao.deleteShares(sh);
	}
	
	public List<Shares> listAllShares() {
		return dao.findAllShares();
	}
	
	public List<Shares> listSharesByCompany(String company) {
		return dao.findSharesByCompany(company);
	}
	
	
	
}
