package kigali.restaurant.rw.domain;

import java.util.UUID;
 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name="dining_table")
public class DiningTable {  
  
    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;  

    @Column (name="table_number")
    private int tableNumber;

    @Column (name="name")
    private String name;

    public UUID getId() {
        return id;
    }

    public int getTableNumber() {
        return tableNumber;
    }   


    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;  
    }
      
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name  =   name;
    }  

  

    
}
