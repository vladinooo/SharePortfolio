package sp.webservices;


import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import uk.ac.susx.inf.ianw.shareManagement.ShareBroker;



public class ShareBrokerClientTest {

	
	public static void main (String []args) {
		
		try {
				
			if (System.getSecurityManager() == null) {
				
					System.setSecurityManager(new RMISecurityManager());
				}
			
			Registry registry = LocateRegistry.getRegistry("127.0.0.1", 40090);
            
			String name = ShareBroker.SHAREBROKER;
			ShareBroker service = (ShareBroker) registry.lookup(name);
			System.out.println(service.getPrice("Google"));
			
		} catch (Exception e) {
			System.err.println("ShareBrokerClient exception " + e.getMessage());
			e.printStackTrace();
			
		}
	}

}
