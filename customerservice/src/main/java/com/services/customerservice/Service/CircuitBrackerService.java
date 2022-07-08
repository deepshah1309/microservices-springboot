package com.services.customerservice.Service;

import java.util.List;
import java.util.concurrent.Future;

import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.services.customerservice.Client.SimPlanFeign;
import com.services.customerservice.DTO.SimDetailsDTO;
//
//@Service
public class CircuitBrackerService {

//	@Autowired
//	RestTemplate template;
//	
//	@Autowired
//	private SimPlanFeign feignClient;
//	
//	
//	@SuppressAjWarnings("unchecked")
//	public Future<List<SimDetailsDTO>>  getSpecificList(){
//		return Future.of(()->template.getForObject("http://localhost:4000/ser"));
//		
//		
//	}
	
}
