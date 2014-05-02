package sp.sharetransaction.manipulation;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import sp.sharetransaction.domain.ShareTransaction;
import sp.sharetransaction.domain.Shares;
import uk.ac.susx.inf.ianw.shareManagement.UnknownCompanyException;

@Local
public interface ShareTransactionManipulationLocal {
	
	public void createShareTransaction(ShareTransaction newShareTransaction);
	
	public void removeShareTransaction(ShareTransaction st);
	
	public List<ShareTransaction> listAllShareTransactions();
	
	public List<ShareTransaction> listShareTransactionsByCompany(String company);
	
	public int _getSharesCompanyId(String company);
	
	public Number _getNumberOfRows();
	
	public void createShares(Shares newShares);
	
	public void _addShares(Shares sh, int id);
	
	public void _subtractShares(Shares sh, int id);
	
	public void _updateShares(Shares sh, int id);
	
	public void removeShares(Shares sh);
	
	public List<Shares> listAllShares();
	
	public List<Shares> listSharesByCompany(String company);
	

}
