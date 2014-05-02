package sp.webservices;

import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.ejb.Stateless;

import uk.ac.susx.inf.ianw.shareManagement.ShareBroker;
import uk.ac.susx.inf.ianw.shareManagement.UnknownCompanyException;


// accessing the executeShareBroker service once it's running

@Stateless
public class ShareBrokerClient {
	
	private double shareUnitPrice;

	public double _getPrice(String company) throws UnknownCompanyException {
		
		if (company.equals("--")) {
			shareUnitPrice = 0.0f;
		} else {
		
			try {
				
				Registry registry = LocateRegistry.getRegistry("127.0.0.1", 40090);
	            
				String name = ShareBroker.SHAREBROKER;
				ShareBroker service = (ShareBroker) registry.lookup(name);
				shareUnitPrice = service.getPrice(company);
				
			} catch (Exception e) {
				System.err.println("ShareBrokerClient exception " + e.getMessage());
				e.printStackTrace();	
			}
		}
		
		return shareUnitPrice;
	}
}


