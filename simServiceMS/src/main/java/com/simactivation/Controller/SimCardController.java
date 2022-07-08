package com.simactivation.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simactivation.DTO.SimDetailsDTO;
import com.simactivation.Entity.SimDetails;
import com.simactivation.Entity.SimOffers;
import com.simactivation.Exception.SimCardNotValidatedException;
import com.simactivation.Service.SimDetailService;

@RestController
@RequestMapping("/simcard")
public class SimCardController {

	
	private SimDetailService simCardservice;
	

    @Autowired
	public void setSimCardservice(SimDetailService simCardservice) {
		this.simCardservice = simCardservice;
	}

    /**
     *   simcard/getsimlist
     * @return
     */
    @GetMapping("/getsimlist")
    public List<SimDetailsDTO> getSimList() {
    	
    	return simCardservice.getsimlist();
    }
    
    @GetMapping("/getsim/{id}")
    public SimDetails getSim(@PathVariable("id") Integer id) {
    	
    	return simCardservice.getSimById(id).get();
    }
    
	@PostMapping("/validatesimcarddetails")
	public ResponseEntity<Object> validate(@RequestBody @Valid SimDetailsDTO simdetailsdto){
		
		
		
		//if no errors related to validation then
		return ResponseEntity.ok().body("VALID DETAILS");
		
	}
	
	
	/**
	 *    /simcard/addnewcarddetails
	 * @param simdetailsdto
	 * @return
	 * @throws Exception
	 */
	
	@PostMapping("/addnewcarddetails")
	public String add(@RequestBody @Valid SimDetailsDTO simdetailsdto) throws Exception{
		
		
		simCardservice.insertRecord(simdetailsdto);
		
		
//		{
//			"data":"success"
//		}
		return "success";
		
	}
	
	
	
	@PostMapping("/simcardvalidationcustomer")
	public ResponseEntity<Object> validateAndGetOffers(@RequestBody @Valid SimDetailsDTO dto) throws SimCardNotValidatedException{
		
		
		List<SimOffers> offers = simCardservice.getOffersByValidation(dto);
		HashMap<String,Object> hm= new HashMap<>();
		hm.put("data",offers);
		hm.put("message","SIM already active");
		return ResponseEntity.ok(hm);
		
	}
	
}
