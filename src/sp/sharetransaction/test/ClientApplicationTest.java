package sp.sharetransaction.test;

import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import sp.sharetransaction.domain.ShareTransaction;
import sp.sharetransaction.manipulation.ShareTransactionManipulationRemote;

public class ClientApplicationTest {
	
	public static void main (String []args) {
		
		try {
			
			Context jndi = new InitialContext();
				
			ShareTransactionManipulationRemote stm =
					(ShareTransactionManipulationRemote)jndi.lookup
					("java:global/SharePortfolio/ShareTransactionManipulationImplementation" +
							"!sp.sharetransaction.manipulation.ShareTransactionManipulationRemote");
						
			//stm.createShareTransaction(new ShareTransaction("Google", 44.6f, 14.6f));
			
			List<ShareTransaction> transactions = stm.listAllShareTransactions();
				
			for (ShareTransaction st: transactions) {
				
				System.out.println(st);
			}
			
			} catch (NamingException e) {
				
				System.out.println(e);
			}
		}
}


