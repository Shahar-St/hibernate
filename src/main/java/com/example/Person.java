package com.example;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int password;
    private String firstName, lastName, emailAddress;

    @OneToMany(
            cascade = CascadeType.ALL
            , fetch = FetchType.LAZY
            , mappedBy = "owner"
    )
    private final List<Car> carList;

    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @JoinTable(
            name = "person_garage",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "garage_id")
    )
    private final List<Garage> garageList;

    //GROUP C'tors
    public Person(String firstName, String lastName, int password, String emailAddress) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.emailAddress = emailAddress;
    }
    public Person() {
        this.carList = new ArrayList<>();
        this.garageList = new ArrayList<>();
    }

    //GROUP adders
    public void addCar(Car car) {

        if (!carList.contains(car))
        {
            carList.add(car);
            car.setOwner(this);
        }
    }

    public void addGarage(Garage garage) {

        if (!garageList.contains(garage))
        {
            garageList.add(garage);
            garage.addOwner(this);
        }
    }

    public void removeCar(Car car) {
        carList.remove(car);
    }

    //GROUP setters and getters

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getPassword() {
        return password;
    }
    public void setPassword(int password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

}
