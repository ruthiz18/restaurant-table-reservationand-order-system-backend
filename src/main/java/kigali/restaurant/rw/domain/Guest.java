package kigali.restaurant.rw.domain;

import java.util.UUID;
   
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table (name="guest")
public class Guest {
  
    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    @Column (name="first_name")
    private String firstName;

    @Column (name="last_name")
    private String lastName;

    @Column (name="phone", unique = true)
    private String phone;

    @Column (name="email")
    private String email;

    public UUID getId(){
        return id;
    }


    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName  =  firstName;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName){  
        this.lastName = lastName;
    }


    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone) {
        this.phone  =  phone;  
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email  =  email;  
    }

}