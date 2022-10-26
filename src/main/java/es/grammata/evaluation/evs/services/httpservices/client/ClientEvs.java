package es.grammata.evaluation.evs.services.httpservices.client;  

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import es.grammata.evaluation.evs.services.httpservices.EvsService;
import es.grammata.evaluation.evs.services.httpservices.wrapper.BankWrap;
  
public class ClientEvs {  
	
	public EvsService evsService;
	
	public ClientEvs() {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/spring/client-beans.xml");  
		evsService = (EvsService)context.getBean("evsServiceBean");
	}
	
    public List<BankWrap> getBanks(){    
    	List<BankWrap> banks = evsService.getAllBanks();
    	
    	return banks;
    }  
    
    public BankWrap createBank(BankWrap bankWrap){    
    	evsService.createBank(bankWrap);
    	
    	return bankWrap;
    }  
    
    public BankWrap updateBank(BankWrap bankWrap){    
    	evsService.updateBank(bankWrap);
    	
    	return bankWrap;
    }  
}  