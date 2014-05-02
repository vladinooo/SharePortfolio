package sp.dataaccess;


import java.text.DecimalFormat;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import sp.sharetransaction.domain.ShareTransaction;
import sp.sharetransaction.domain.Shares;

@Stateless
public class DataAccessImplementation implements DataAccess {
	
	@PersistenceContext
	private EntityManager em;
	
	
	/***************
	 * Transactions
	 ***************/
	
	public void insertShareTransaction(ShareTransaction newShareTransaction) {
		em.persist(newShareTransaction);
	}
	
	public void deleteShareTransaction(ShareTransaction st) {
		ShareTransaction shareTransaction = em.find(ShareTransaction.class, st.getId());
		em.remove(shareTransaction);
	}
	
	public List<ShareTransaction> findAllShareTransactions() {
		Query q = em.createNamedQuery("listAllTransactions");
		List<ShareTransaction> results = q.getResultList();
		return results;
	}
	
	public List<ShareTransaction> findShareTransactionByCompany(String company) {
		Query q = em.createNamedQuery("listSelectedTransactions");
		q.setParameter("name", company);
		List<ShareTransaction> results = q.getResultList();
		return results;
	}

	
	/***************
	 * Shares
	 ***************/
	
	public int getSharesCompanyId(String company) {
		Query q = em.createNamedQuery("getSharesCompanyId");
		q.setParameter("name", company);
		List<Shares> results = q.getResultList();
		return results.get(0).getId();
	}
	
	public Number getNumberOfRows() {
		Query q = em.createNamedQuery ("getNumberOfRows");
		Number result = (Number) q.getSingleResult();
		return result;
	}
	
	public void insertShares(Shares newShares) {
		em.persist(newShares);
	}
	
	public void addShares(Shares sh, int id) {
		Shares shares = em.find(Shares.class, id);
		double oldSharesAmount = shares.getSharesAmount();
		double oldSharesValue = shares.getSharesValue();
		shares.setUnitPrice(formatDouble(sh.getUnitPrice()));
		shares.setSharesAmount(formatDouble(oldSharesAmount + sh.getSharesAmount()));
		shares.setSharesValue(formatDouble(oldSharesValue + sh.getSharesValue()));
	}
	
	public void subtractShares(Shares sh, int id) {
		Shares shares = em.find(Shares.class, id);
		double oldSharesAmount = shares.getSharesAmount();
		double oldSharesValue = shares.getSharesValue();
		shares.setSharesAmount(formatDouble(oldSharesAmount - sh.getSharesAmount()));
		shares.setSharesValue(formatDouble(oldSharesValue - sh.getSharesValue()));
	}
	
	public void updateShares(Shares sh, int id) {
		Shares shares = em.find(Shares.class, id);
		shares.setUnitPrice(sh.getUnitPrice());
		shares.setSharesValue(sh.getSharesValue());
	}
	
	public void deleteShares(Shares sh) {
		Shares shares = em.find(Shares.class, sh.getId());
		em.remove(shares);
	}
	
	public List<Shares> findAllShares() {
		Query q = em.createNamedQuery("listAllShares");
		List<Shares> results = q.getResultList();
		return results;
	}
	
	public List<Shares> findSharesByCompany(String company) {
		Query q = em.createNamedQuery("listSelectedShares");
		q.setParameter("name", company);
		List<Shares> results = q.getResultList();
		return results;
	}
	
	
	private double formatDouble(double price) {
		DecimalFormat df = new DecimalFormat("#.####");
		return Double.parseDouble(df.format(price));
	}
	

}
