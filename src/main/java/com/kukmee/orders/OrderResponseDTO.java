package com.kukmee.orders;

import java.util.Date;
import java.util.List;

import com.kukmee.entity.Customer;

public class OrderResponseDTO {

    private Long orderid;
    private Customer customer;
    private Date orderdate;
    private String status;
    private Double totalamount;
    private List<OrderItemDTO> orderitems;
	public Long getOrderid() {
		return orderid;
	}
	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Date getOrderdate() {
		return orderdate;
	}
	public void setOrderdate(Date orderdate) {
		this.orderdate = orderdate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Double getTotalamount() {
		return totalamount;
	}
	public void setTotalamount(Double totalamount) {
		this.totalamount = totalamount;
	}
	public List<OrderItemDTO> getOrderitems() {
		return orderitems;
	}
	public void setOrderitems(List<OrderItemDTO> orderitems) {
		this.orderitems = orderitems;
	}

    // getters and setters
    
    
}



