package com.example;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "garages")
public class Garage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String address;

    @ManyToMany(mappedBy = "garageList")
    @Cascade({CascadeType.PERSIST, CascadeType.MERGE})
    private List<Person> owners;

    @ManyToMany(mappedBy = "garageList")
    @Cascade({CascadeType.PERSIST, CascadeType.MERGE})
    private List<Car> carList;

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
            owners.add(person);

        if (!person.getGarageList().contains(this))
            person.addGarage(this);
    }

    public void addCar(Car car) {

        if (!carList.contains(car))
            carList.add(car);

        if (!car.getGarageList().contains(this))
            car.getGarageList().add(this);
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

    public List<Person> getOwners() {
        return owners;
    }
    public void setOwners(List<Person> owners) {
        this.owners = owners;
    }

    public List<Car> getCarList() {
        return carList;
    }
    public void setCarList(List<Car> carList) {
        this.carList = carList;
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
