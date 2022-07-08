package com.services.customerservice.Service;

import java.net.URI;
//import java.net.http.HttpHeaders;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.services.customerservice.LoadBalancerConfig;

import com.services.customerservice.DTO.CustomerDTO;
import com.services.customerservice.DTO.CustomerValidateDTO;
import com.services.customerservice.DTO.SimDetailsDTO;
import com.services.customerservice.Entity.Customer;
import com.services.customerservice.Entity.CustomerIdentity;
import com.services.customerservice.Repository.CustomerIdentityRepository;

import com.services.customerservice.Repository.CustomerRepository;

@Service("customerservices")
@Transactional
public class CustomerService {

	
	@Autowired
	private CustomerIdentityRepository customerIdentityRepo;
	
	@Autowired
	private DiscoveryClient discoveryClient;
	
	@Autowired
	private CustomerRepository customerRepo;
	
	
	@Autowired
	private RestTemplate template;
//	@Autowired
//	private SimPlanFeign simfeignClient;
//	
//	@Autowired
//	private SimDetailsRepository simDetailsRepo; 
	
	@Autowired
	private SessionFactory sessionFactory;
	
//	@Autowired
//	private AddressRepository addressRepo;
	
	
	
	public String validate(CustomerValidateDTO dto) throws Exception {
		
		
		String firstName = dto.getFirstName();
		String lastName = dto.getLastName();
		
		
		String email = dto.getEmailAddress();
		
		
	   LocalDate date = dto.getDateOfbirth();
	   
		Optional<CustomerIdentity> optCustomerIdentiy = customerIdentityRepo.findByFirstNameLastNameAndEmail(firstName, lastName, email);
		
		if(optCustomerIdentiy.isEmpty()) {
			throw new Exception("Customer Not Available");
		}
		
		if(!optCustomerIdentiy.get().getDateOfbirth().equals(date)) {
			throw new Exception("No request placed for you");
		}

			
		return "VALID";
		
	}
	public static int getRandomNumberUsingNextInt(int min, int max) {
	    Random random = new Random();
	    return random.nextInt(max - min) + min;
	}
	
	
	public Object validateIDProofAndActivate(CustomerDTO dto) throws Exception  {
		
		
		//check if RECORD EXISTS WITH GIVEN UNIQUE NUMBER
		
		Optional<CustomerIdentity>  customerOptional = customerIdentityRepo.findById(dto.getUniqueIdNumber());
		
		if(customerOptional.isEmpty()) {
			throw new Exception("Invalid details");
		}
//		
		CustomerIdentity customerFromRecord = customerOptional.get();
//	    
		
		//VERIFICATION PROCESS STARTS FOR NAME
		if(customerFromRecord.getFirstName().toLowerCase().equals(dto.getFirstName().toLowerCase()) 
				
			&& customerFromRecord.getLastName().toLowerCase().equals(dto.getLastName().toLowerCase())
				
			&&	customerFromRecord.getDateOfbirth().equals(dto.getDateOfBirth())) {
			
			
			
//			
//			//ADDRESS PROOF PROCESS
//			
	
//			Optional<CustomerAddress> addressOptional = addressRepo.findBypincode(dto.getPincode()); 
//			
//			CustomerAddress customerAddress;
//			
//			if(!addressOptional.isPresent()) {
//				customerAddress = new CustomerAddress();
//				customerAddress.setAddress(dto.getAddress());
//				customerAddress.setPincode(dto.getPincode());
//				customerAddress.setCity(dto.getCity());
//				customerAddress.setState(dto.getState());
//			}
//			else {
//				customerAddress = addressOptional.get();
//			}
			//VALID
			// FIND THE SIM WHICH IS AVAILABLE AND CURRENTLY NOT ACTIVE
				
//   		    List<SimDetailsDTO> availableSimCards = simDetailsRepo.findRandomlyAndActivate();
			
   		    
   		    //REST CALL
   		    
		   List<ServiceInstance> instances = discoveryClient.getInstances("simservice");
		   String simUrl = instances.get(0).getUri().toString();
		   System.out.println(simUrl);
   		   @SuppressWarnings("unchecked")
		   List<SimDetailsDTO> availableSimCards =(List<SimDetailsDTO>) template.getForObject("http://simservice/simcard/getsimlist",List.class);
			
   		   List<SimDetailsDTO> pojos = new JsonMapper().convertValue(
   				availableSimCards,
   			    new TypeReference<List<SimDetailsDTO>>() { });
   		 
   		   System.out.println("simCardList"+availableSimCards);
			
			
			//GIVE ONLY THOSE SIM WHICH HAVE OFFERS
			
//			List<SimDetailsDTO> filterWithAvailableOffers = availableSimCards.stream().filter(
//					
//					sim-> sim.getOffers().size()>=1
//					
//					).toList();
//			
			
			
			int size = availableSimCards.size();
			
			if(size==0) {
				
				throw new Exception("NO SIMCARD AVAILABLE /OFFERS. CONTACT SERVICE PROVIDER");
				
			}
//			
			int generateRnadomNumberInRange  = CustomerService.getRandomNumberUsingNextInt(0,size);
			
			if(generateRnadomNumberInRange <0) {
				throw new Exception("Out of Range");
			}
		 
			//GET THE SIM NOW FROM THE RANDOM INDEX
			
			System.out.println("181"+generateRnadomNumberInRange);
			SimDetailsDTO simToBeAlloted = pojos.get(generateRnadomNumberInRange);
					
					
//			
			
		
			System.out.println(simToBeAlloted+" "+pojos);
			
//			
			//ACTIVATE THE SIM
		     
			
			simToBeAlloted.setSimStatus(true);
			
			//get ORIGINAL OBJECT TO UPDATE
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<SimDetailsDTO> httpEntity = new HttpEntity<>(simToBeAlloted, headers);
			
//			simToBeAlloted.setSimStatus(true);
		   template.postForObject("http://simservice/simcard/addnewcarddetails",httpEntity,String.class);
			
			

			
//			PREPARE CUSTOMER ENTITY
			Customer customer = CustomerDTO.convertDTOToEntity(dto);
			customer.setSimId(simToBeAlloted.getSimId());
//			
			
//			customer.setAddress(customerAddress);
			Session session = sessionFactory.openSession();
		    session.beginTransaction();
		    session.save(customer);
		    session.getTransaction().commit();
		    session.close();
//			 ADDRESS PROVIDED
			
			
			// CUSTOMER RECORD UPDATE WITH SIM
//			
//			customerRepo.saveAndFlush(customer);
			
			//CUSTOMER DETAILS UPDATED
			
			HashMap<String,Object> dataObject = new HashMap<>();
//			dataObject.put("simActiveStatus",Boolean.TRUE);
			dataObject.put("customerDetails",availableSimCards);
//			dataObject.put("SimDetails",customer.getDetails());
			dataObject.put("verified_Through",customer.getIdType());
//			dataObject.put("SimOffers",customer.getDetails().getOffers());
//			dataObject.put("Addressusedforverification", customerAddress);
			dataObject.put("ActivatedOn",LocalDateTime.now());
			
		    return dataObject;
			
		}
		else {
			
			throw new Exception("DETAILS NOT VALID");
		}
		
		
		
		
	}
}
