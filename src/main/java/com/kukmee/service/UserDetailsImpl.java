package com.kukmee.service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.kukmee.entity.Admin;
import com.kukmee.entity.Customer;
import com.kukmee.entity.DeliveryPartner;
import com.kukmee.entity.Hub;
import com.kukmee.entity.SubAdmin;
import com.kukmee.entity.Warehouse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    // Build from Admin
    public static UserDetailsImpl build(Admin admin) {
        List<GrantedAuthority> authorities = admin.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
        return new UserDetailsImpl(admin.getId(), admin.getUsername(), admin.getPassword(), authorities);
    }

    // Build from Customer
    public static UserDetailsImpl build(Customer customer) {
        List<GrantedAuthority> authorities = customer.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
        return new UserDetailsImpl(customer.getCustomerid(), customer.getUsername(), customer.getPassword(), authorities);
    }

    // Build from SubAdmin
    public static UserDetailsImpl build(SubAdmin subAdmin) {
        List<GrantedAuthority> authorities = subAdmin.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
        return new UserDetailsImpl(subAdmin.getId(), subAdmin.getUsername(), subAdmin.getPassword(), authorities);
    }

    public static UserDetailsImpl build(DeliveryPartner deliveryPartner) {
        List<GrantedAuthority> authorities = deliveryPartner.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
        return new UserDetailsImpl(deliveryPartner.getId(), deliveryPartner.getUsername(), deliveryPartner.getPassword(), authorities);
    }
    
    public static UserDetailsImpl build(Warehouse warehouse) {
        List<GrantedAuthority> authorities = warehouse.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
        return new UserDetailsImpl(warehouse.getId(), warehouse.getUsername(), warehouse.getPassword(), authorities);
    }
    
    public static UserDetailsImpl build(Hub hub) {
        List<GrantedAuthority> authorities = hub.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
        return new UserDetailsImpl(hub.getId(), hub.getUsername(), hub.getPassword(), authorities);
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
