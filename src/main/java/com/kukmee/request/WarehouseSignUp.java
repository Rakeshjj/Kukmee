package com.kukmee.request;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class WarehouseSignUp {

    @Size(max = 50)
    @NotBlank
    @Pattern(regexp = "^[A-Za-z]*$", message = "Invalid Input: Product name only accept characters")
    private String username;

    @Size(max = 120)
    @NotBlank
    private String password;

    @NotBlank
    @Email(message = "Invalid email format")
    @Size(max = 50, message = "Email should be no more than 50 characters long")
    private String email;

    @Size(max = 50)
    @NotBlank
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9]*$", message = "Warehouse name cannot start with a number")
    private String warehousename;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9]*$", message = "Location cannot start with a number")
    private String location;

    private Long phonenumber;

    @NotBlank
    private String managerName;

    private Set<String> role;

    // Getters and Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        // Trim email to remove leading/trailing spaces
        this.email = email.trim();
    }

    public String getWarehousename() {
        return warehousename;
    }

    public void setWarehousename(String warehousename) {
        this.warehousename = warehousename;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(Long phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Set<String> getRole() {
        return role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }
}
