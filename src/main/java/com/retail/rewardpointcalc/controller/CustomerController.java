package com.retail.rewardpointcalc.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.retail.rewardpointcalc.entity.Customer;
import com.retail.rewardpointcalc.entity.Transaction;
import com.retail.rewardpointcalc.model.CustomerRequest;
import com.retail.rewardpointcalc.model.CustomerResponse;
import com.retail.rewardpointcalc.model.RewardPointRequest;
import com.retail.rewardpointcalc.model.RewardPointResponse;
import com.retail.rewardpointcalc.model.TransactionResponse;
import com.retail.rewardpointcalc.repository.CustomerRepository;

@RestController
public class CustomerController {
	@Autowired
	private CustomerRepository repository;

	CustomerResponse custResponse;
	TransactionResponse transResponse;
	 List<TransactionResponse> list = new ArrayList<TransactionResponse>();
	 List<TransactionResponse> listTrans ;
	 int totalRewardPoints;

	@PostMapping("/customer")
	public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerRequest customerRequest) {

		Customer cutomerEntity = Customer.builder().address(customerRequest.getAddress())
				.customerName(customerRequest.getCustomerName()).emailID(customerRequest.getEmailID())
				.phone(customerRequest.getPhone()).created_at(new Date(System.currentTimeMillis()))
				.created_by(customerRequest.getCustomerName()).build();

		cutomerEntity = repository.save(cutomerEntity);
		custResponse = buildCustomerResponse(cutomerEntity);
		return ResponseEntity.status(HttpStatus.CREATED).body(custResponse);
	}

	@GetMapping("/customer/{customerId}")
	public ResponseEntity<CustomerResponse> getCustomerData(@PathVariable int customerId) {
		Optional<Customer> customer = repository.findById(customerId);
		custResponse = buildCustomerResponse(customer);
		return ResponseEntity.status(HttpStatus.OK).body(custResponse);
	}
	
	@GetMapping("/customer/{customerId}/transactions")
	public ResponseEntity<CustomerResponse> getCustomerAndTransaction(@PathVariable int customerId) {
		Optional<Customer> customer = repository.findByCustomerId(customerId);
		custResponse = buildCustomerResponse(customer);
		return ResponseEntity.status(HttpStatus.OK).body(custResponse);
	}
	
	
	@GetMapping("/customer/{customerId}/rewardpointsum")
	public ResponseEntity<CustomerResponse> getCustomerWith3MonthPoint(@PathVariable int customerId) {
		Optional<Customer> customer = repository.find(customerId);
		custResponse = buildCustomerResponse(customer);
		return ResponseEntity.status(HttpStatus.OK).body(custResponse);
	}
	
	

	private CustomerResponse buildCustomerResponse(Optional<Customer> customer) {
		customer.ifPresent(customerData -> {
			
			customerData.getTransActionList().forEach(transaction ->{
				 listTrans =buildTransactionResponse(transaction);
				 totalRewardPoints = calculateTotRewardPoints(transaction.getRewardPoints());
						}
					);
			
			
			custResponse = CustomerResponse.builder().address(customerData.getAddress())
					.created_at(customerData.getCreated_at()).created_by(customerData.getCreated_by())
					.customerId(customerData.getCustomerId()).customerName(customerData.getCustomerName())
					.emailID(customerData.getEmailID()).phone(customerData.getPhone())
					.updated_at(customerData.getUpdated_at()).updated_by(customerData.getUpdated_by()).
					 transList(listTrans).totalRewardPoints(totalRewardPoints)
					.build();
		});

		return custResponse;
	}
	
	private int calculateTotRewardPoints(int rewardPoints) {
		totalRewardPoints = totalRewardPoints + rewardPoints;
		// TODO Auto-generated method stub
		return totalRewardPoints;
	}

	private List<TransactionResponse> buildTransactionResponse(Transaction transactionData) {		
			transResponse = TransactionResponse.builder().rewardpoints(transactionData.getRewardPoints())
					.transAmt(transactionData.getTransAmt()).transDate(transactionData.getTransDate())
					.transId(transactionData.getTransId()).build();
		
		list.add(transResponse);
		return list;
	}

	private CustomerResponse buildCustomerResponse(Customer customerData) {

		custResponse = CustomerResponse.builder().address(customerData.getAddress())
				.created_at(customerData.getCreated_at()).created_by(customerData.getCreated_by())
				.customerId(customerData.getCustomerId()).customerName(customerData.getCustomerName())
				.emailID(customerData.getEmailID()).phone(customerData.getPhone())
				.updated_at(customerData.getUpdated_at()).updated_by(customerData.getUpdated_by()).build();

		return custResponse;
	}

}
