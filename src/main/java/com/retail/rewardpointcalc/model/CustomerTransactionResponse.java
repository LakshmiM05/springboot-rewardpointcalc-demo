package com.retail.rewardpointcalc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTransactionResponse {

	private int customerId;
	private String customerName;
	private String address ;
	private String  emailID; 
	private int  phone ;
	private Date  created_at;
	private String  created_by; 
	private Date  updated_at; 
	private String updated_by; 
	
	 private List<TransactionResponse> transactionResponse;
	 private int rewardsum;
}
