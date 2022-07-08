package com.services.customerservice.Controller;

import java.util.HashMap;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;

import com.services.customerservice.LoadBalancerConfig;
import com.services.customerservice.DTO.CustomerDTO;
import com.services.customerservice.DTO.CustomerValidateDTO;
import com.services.customerservice.Service.CustomerService;



//import com.simactivation.DTO.CustomerDTO;
//import com.simactivation.DTO.CustomerValidateDTO;
//import com.simactivation.Service.CustomerService;

@RestController
@RequestMapping("/customer")
@CrossOrigin
@EnableAutoConfiguration
//
//@LoadBalancerClient(name="MyloadBalancer", configuration=LoadBalancerConfig.class)
public class CustomerController {
	
	
	@Autowired
	private CustomerService customerService;
	
	
	/**
	 * 3RD CASE
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/validateDetails")
	public ResponseEntity<Object> validateDetails(@RequestBody @Valid CustomerValidateDTO dto ) throws Exception{
		
		
	
		String msg = customerService.validate(dto);
		
		HashMap<String,Object> hashmap = new HashMap<>();
		hashmap.put("data",msg);
		
	
		return ResponseEntity.ok(hashmap);
		
	}
	
	/**
	 *  customer/idproofvalidation
	 * @param customerDTO
	 * @return
	 * @throws Exception
	 */
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
    @PostMapping("/idproofvalidation")
	public ResponseEntity<Object> checkForIDProofAndActivate(@RequestBody CustomerDTO dto) throws Exception{
		
		Object dataFromService =  customerService.validateIDProofAndActivate(dto);
		
		return ResponseEntity.ok(dataFromService);
		
	}

}
