package com.example;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName, lastName, emailAddress, password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.DETACH})
    private List<Car> carList;

    @ManyToMany
    @Cascade({CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "persons_garages",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "garage_id")
    )
    private List<Garage> garageList;

    //GROUP C'tors
    public Person(String firstName, String lastName, String password, String emailAddress) {
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
            garageList.add(garage);

        if (!garage.getOwners().contains(this))
            garage.getOwners().add(this);
    }

    public void removeCar(Car car) {
        carList.remove(car);
    }

    //GROUP setters and getters
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

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<Car> getCarList() {
        return carList;
    }
    public void setCarList(List<Car> carList) {
        this.carList = carList;
    }

    public List<Garage> getGarageList() {
        return garageList;
    }
    public void setGarageList(List<Garage> garageList) {
        this.garageList = garageList;
    }
}
