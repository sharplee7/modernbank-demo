package com.modernbank.account.rest.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.modernbank.account.rest.customer.entity.Customer;

@Service("customerComposite")
public class CustomerCompositeImpl implements CustomerComposite {

    private static Logger LOGGER = LoggerFactory.getLogger(CustomerCompositeImpl.class);
	
    @Value("${customer.api.url}")
    private String CUSTOMER_API_URL;
    
    // @LoadBalanced
   
    @Autowired
   private final RestTemplate restTemplate;
    
   @Autowired
   public CustomerCompositeImpl(RestTemplate restTemplate) {
       this.restTemplate = restTemplate;
   }

    /**
     * 원본 소스에서는 rest call을 한 후 일정 시간 응답이 없으면 Hystrix를 통해 제일 아래 fallback 매커니점을 타도록 되어 있으나 
     * Spring Boot 3.x로 옮기면서 istio나 app mesh로 해당 로직 옮길 필요 있음 따라서 hystrix 로직은 주석 처리 
     * ----> 아래 this.restTemplate.getForObject를 istio로 변경 필요 (2024.12.16 by deok)
     * try {
     *      this.restTemplate.getForObject(CUSTOMER_API_URL + apiUrl, Customer.class, cstmId);
     * } catch (Exeption e) {
     *     fallbackRetriveCustomer(cstmId, (Throwable)e)
     * }
     */
    // @HystrixCommand(commandKey="retrieveCustomer", fallbackMethod="fallbackRetriveCustomer")
	@Override
	public Customer retrieveCustomer(String cstmId) throws Exception {
        String apiUrl =  "/{cstmId}";
		return this.restTemplate.getForObject(CUSTOMER_API_URL + apiUrl, Customer.class, cstmId);
	}

    public Customer fallbackRetriveCustomer(String cstmId, Throwable t) throws Exception {
    	String msg = "restTemplate를 이용하여 " + cstmId + " 고객정보 조회 서비스 호출에 문제가 있습니다.";
    	LOGGER.error(msg, t);
        throw new Exception();
    }
}
