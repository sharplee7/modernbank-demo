package com.modernbank.customer.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import com.modernbank.customer.controller.CustomerController;

@WebMvcTest(controllers = {CustomerController.class}) // Controller만 테스트 
class CustomerControllerTest {
/*
	@Autowired
	private MockMvc mockMvc;
	
	// 가짜 객체 생성
	@MockBean(name = "customerService")
	private CustomerService customerService;
	
	@Test
	void retrieveCustomerDetail_WithCustomerId_ReturnCustomer() throws Exception {



		// given(테스트 준비 사항)
		String cstmId = "0001";

		Customer customer = new Customer(cstmId,"홍길동","24","남","테스트1","테스트2");
		
		// 만약 리스트를 받고 싶다면,
		// List<Customer> customers = Collections.singletonList(customer);
		// BDDMockito.given(customerService.retrieveCustomerDetail(cstmId)).willReturn(customers);
		
		// Behavior Driven Development(행위 주도 개발)
		// CustomerService를 스텁으로 만들기 위해... 결국 CustomerService를 임의로 하나 만들어서 위에서 정의한 값이 리턴되도록 정의
		// 이 객체가 CustomerController에 CustomerService를 대체한다.
		BDDMockito.given(customerService.retrieveCustomerDetail(cstmId)).willReturn(customer);
		
		// when(테스트 수행)
		// $.cstmId /"$[0].cstmId"
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/detail/" + cstmId)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.cstmId", Matchers.comparesEqualTo(customer.getCstmId())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.cstmNm", Matchers.comparesEqualTo(customer.getCstmNm())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.cstmAge", Matchers.comparesEqualTo(customer.getCstmAge())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.cstmGnd", Matchers.comparesEqualTo(customer.getCstmGnd())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.cstmPn", Matchers.comparesEqualTo(customer.getCstmPn())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.cstmAdr", Matchers.comparesEqualTo(customer.getCstmAdr())));
		
		// then(테스트 검증)
		BDDMockito.verify(customerService).retrieveCustomerDetail(cstmId);

	}

	final void existsCustomerId_WithCustomerId_RetrunCustomer() {
		
//		fail("Not yet implemented"); // TODO
	}

 */
}
