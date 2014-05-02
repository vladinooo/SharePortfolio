package sp.dataaccess;

import java.util.List;

import javax.ejb.Local;

import sp.sharetransaction.domain.ShareTransaction;
import sp.sharetransaction.domain.Shares;

@Local
public interface DataAccess {
	
	public void insertShareTransaction(ShareTransaction newShareTransaction);
	
	public void deleteShareTransaction(ShareTransaction st);
	
	public List<ShareTransaction> findAllShareTransactions();
	
	public List<ShareTransaction> findShareTransactionByCompany(String company);
	
	public int getSharesCompanyId(String company);
	
	public Number getNumberOfRows();
	
	public void insertShares(Shares newShares);
	
	public void addShares(Shares sh, int id);
	
	public void subtractShares(Shares sh, int id);
	
	public void updateShares(Shares sh, int id);
	
	public void deleteShares(Shares sh);
	
	public List<Shares> findAllShares();
	
	public List<Shares> findSharesByCompany(String company);
	

}
