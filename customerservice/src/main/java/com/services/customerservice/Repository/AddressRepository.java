package com.services.customerservice.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.services.customerservice.Entity.CustomerAddress;


@Repository("AddressRepo")
public interface AddressRepository extends JpaRepository<CustomerAddress,Integer> {

	public Optional<CustomerAddress> findBypincode(Integer pincode);
	
}
