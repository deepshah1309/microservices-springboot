package com.services.customerservice.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.services.customerservice.DTO.CustomerIdentityDTO;
import com.services.customerservice.Entity.CustomerIdentity;
import com.services.customerservice.Repository.CustomerIdentityRepository;




@Service("customerIdentityService")
public class CustomerIdentityService {
     
	private CustomerIdentityRepository identityRepo;
	
	

	@Autowired
	public void setIdentityRepo(CustomerIdentityRepository identityRepo) {
		this.identityRepo = identityRepo;
	}

	
	
	public void add(CustomerIdentityDTO dto) throws Exception{
	
		System.out.println(dto.getUniqueIdNumber()+"   "+identityRepo.existsById(dto.getUniqueIdNumber()));
	
		
//		Optional<CustomerIdentity> optIdentity = identityRepo.findById(dto.getUniqueIdNumber());
//		System.out.println("Optional check"+optIdentity.get());
		if(identityRepo.existsById(dto.getUniqueIdNumber())) {
			throw new Exception("Customer is already present");
		}
		CustomerIdentity identityEntity = CustomerIdentityDTO.convertDTOToEntity(dto);
		identityRepo.save(identityEntity);
		
	}
}
