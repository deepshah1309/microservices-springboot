package com.simactivation.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.simactivation.DTO.SimDetailsDTO;
import com.simactivation.Entity.SimDetails;
import com.simactivation.Entity.SimOffers;
//import com.simactivation.Entity.SimDetails;
//import com.simactivation.Entity.SimOffers;
import com.simactivation.Exception.SimCardNotValidatedException;
import com.simactivation.Repository.SimDetailsRepository;


@Service("SimdetailsService")
public class SimDetailService {

	private SimDetailsRepository simDetailsRepository;
    @Autowired
	public void setSimDetailsRepository(SimDetailsRepository simDetailsRepository) {
		this.simDetailsRepository = simDetailsRepository;
	}

    public List<SimDetailsDTO>  getsimlist() {
        
    
    	List<SimDetailsDTO> simDetails = simDetailsRepository.findRandomlyAndActivate().stream().map((s)->{
    		SimDetailsDTO dto = new SimDetailsDTO();
    		dto.setServiceNumber(s.getServiceNumber());
    		dto.setSimId(s.getSimId());
    		dto.setSimNumber(s.getSimNumber());
    		dto.setSimStatus(s.getSimStatus());
    		return dto;
    	}).toList();
    	
    	System.out.println("log"+simDetails);
    	return simDetails;
    }


	public void insertRecord(SimDetailsDTO dto) throws Exception {
		// TODO Auto-generated method stub
		
//		List<SimDetails> details = simDetailsRepository.findAll();
//		System.out.println("list"+details);

		//check for PAIR VALIDATION
		List<SimDetails> detailsList = simDetailsRepository.checkForPair(dto.getSimNumber(),dto.getServiceNumber());
		
		
		System.out.println("VALUE"+detailsList);
//		
//		 if(!detailsList.isEmpty()) {
//			 
//			 throw new Exception("CAN'T ADD AS RECORD ALREADY EXISTS");
//		 }
//		
		

			 SimDetails simDetails = SimDetailsDTO.convertDTOToEntity(dto);
			 simDetailsRepository.saveAndFlush(simDetails);
			 
		
		
		
	}


	public List<SimOffers> getOffersByValidation(SimDetailsDTO dto) throws SimCardNotValidatedException {
		// TODO Auto-generated method stub

		Optional<SimDetails> optSimCardDetails = simDetailsRepository.checkForBoth(dto.getSimNumber(), dto.getServiceNumber());
		
		
		if(optSimCardDetails.isEmpty() || !optSimCardDetails.get().getSimStatus()) {
			throw new SimCardNotValidatedException("NOT VALID DETAILS");
		}
		
	
		List<SimOffers> offers = optSimCardDetails.get().getOffers();
		
	    return offers;
	    
	}

	public Optional<SimDetails> getSimById(Integer id) {
		// TODO Auto-generated method stub
		
		return simDetailsRepository.findById(id);
	}







}
