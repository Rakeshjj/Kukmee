package com.kukmee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kukmee.entity.Admin;
import com.kukmee.entity.Customer;
import com.kukmee.entity.DeliveryPartner;
import com.kukmee.entity.Hub;
import com.kukmee.entity.SubAdmin;
import com.kukmee.entity.Warehouse;
import com.kukmee.repository.AdminRepository;
import com.kukmee.repository.CustomerRepository;
import com.kukmee.repository.DeliverPartnerRepo;
import com.kukmee.repository.HubRepository;
import com.kukmee.repository.SubAdminRepository;
import com.kukmee.repository.WarehouseRepository;

import jakarta.transaction.Transactional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private SubAdminRepository subAdminRepository;

	@Autowired
	private DeliverPartnerRepo deliverPartnerRepo;

	@Autowired
	private WarehouseRepository warehouseRepository;

	@Autowired
	private HubRepository hubRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Admin admin = adminRepository.findByUsername(username).orElse(null);
		if (admin != null) {
			return UserDetailsImpl.build(admin);
		}

		SubAdmin subAdmin = subAdminRepository.findByUsername(username).orElse(null);
		if (subAdmin != null) {
			return UserDetailsImpl.build(subAdmin);
		}

		DeliveryPartner deliveryPartner = deliverPartnerRepo.findByUsername(username).orElse(null);
		if (deliveryPartner != null) {
			return UserDetailsImpl.build(deliveryPartner);
		}

		Warehouse warehouse = warehouseRepository.findByUsername(username).orElse(null);
		if (warehouse != null) {
			return UserDetailsImpl.build(warehouse);
		}

		Hub hub = hubRepository.findByUsername(username).orElse(null);
		if (hub != null) {
			return UserDetailsImpl.build(hub);
		}

		Customer customer = customerRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
		return UserDetailsImpl.build(customer);
	}
	
	

}
