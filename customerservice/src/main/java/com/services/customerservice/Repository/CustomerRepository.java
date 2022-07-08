package com.services.customerservice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.services.customerservice.Entity.Customer;


@Repository("customerRepositories")
public interface CustomerRepository extends JpaRepository<Customer,String>{

}
