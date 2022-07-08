package com.services.customerservice.Client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import com.services.customerservice.DTO.SimDetailsDTO;

@FeignClient(name="simservice",url="http://localhost:4000/")
public interface SimPlanFeign {
	
	
	@RequestMapping("/listofsimdetails")
	List<SimDetailsDTO>  randomlyFindSimCards();

}
