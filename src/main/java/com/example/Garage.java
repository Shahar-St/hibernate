package com.example;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Garage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String address;
    @ManyToMany(
            mappedBy = "garageList",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    private final List<Person> owners;
    @ManyToMany(
            mappedBy = "garageList",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    private final List<Car> carList;

    //GROUP C'tors
    public Garage(String address) {
        this();
        this.address = address;
    }

    public Garage() {
        owners = new ArrayList<>();
        carList = new ArrayList<>();
    }

    //GROUP adders
    public void addOwner(Person person) {

        if (!owners.contains(person))
        {
            owners.add(person);
            person.addGarage(this);
        }
    }

    public void addCar(Car car) {

        if (!carList.contains(car))
        {
            carList.add(car);
            car.addGarage(this);
        }
    }

    //GROUP setters and getters
    public int getId() {
        return id;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {

        StringBuilder string = new StringBuilder("Garage: ID = " + id + ", address = " + address +
                "\nThe Garage can serve the following license plates:\n");
        for (Car car : carList)
            string.append("\t").append(car.getLicensePlate()).append("\n");

        return string.toString();
    }
}
