package kigali.restaurant.rw.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity 
@Table (name="shift")
public class Shift {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

   @Column (name="name")
    private String name;

    @JsonIgnore 
    @ManyToMany (mappedBy = "shift")
    private List<Waiter> waiters = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public List<Waiter> getWaiters() {
        return waiters;
    }
    public void setWaiters(List<Waiter> waiters){
        this.waiters = waiters;
    }

}