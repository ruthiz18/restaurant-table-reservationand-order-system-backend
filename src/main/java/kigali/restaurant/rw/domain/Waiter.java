package kigali.restaurant.rw.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
 
@Entity
@Table (name="waiter")
public class Waiter {
      
    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    @Column (name="first_name")
    private String firstName;

    @Column (name="last_name")
    private String lastName;

    @Column (name="phone")
    private String phone;

    @ManyToOne 
    @JoinColumn (name="dining_table_id")
    private DiningTable diningTable;

    @ManyToMany 
    @JoinTable (
        name = "waiter_shift",
        joinColumns = @JoinColumn (name="waiter_id"),
        inverseJoinColumns = @JoinColumn (name="shift_id")
    )

    private List<Shift> shifts  =  new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName  =  firstName;
    }


    public String getLastName(){
        return lastName;
    }


    public void setLastName(String lastName){
        this.lastName = lastName;
    }  

    public String getPhone(){
        return phone;
    }

    public void setPhone (String phone){
        this.phone = phone;
    }

    public DiningTable getDiningTable(){
        return diningTable;
    }


    public void setDiningTable(DiningTable diningTable){
        this.diningTable  =  diningTable;
    }

    public List<Shift> getShifts(){
        return shifts;
    }

    public void setShifts(List<Shift> shifts) {
        this.shifts  =  shifts;
    }

}