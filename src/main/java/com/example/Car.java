package com.example;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String licensePlate;
    private double price;
    private int year;

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "owner_id")
    private Person owner;

    @OneToOne(fetch = FetchType.LAZY)
    @Cascade(CascadeType.ALL)
    private Image image;

    @ManyToMany
    @Cascade({CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "cars_garages",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "garage_id")
    )
    private List<Garage> garageList;

    //GROUP C'tors
    public Car(String licensePlate, double price, int year) {
        this();
        this.licensePlate = licensePlate;
        this.price = price;
        this.year = year;
    }
    public Car() {
        garageList = new ArrayList<>();
    }

    //GROUP adders
    public void addGarage(Garage garage) {
        if (!garageList.contains(garage))
            garageList.add(garage);

        if (!garage.getCarList().contains(this))
            garage.getCarList().add(this);
    }

    //GROUP setters and getters
    public int getId() {
        return id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public Image getImage() {
        return image;
    }
    public void setImage(Image image) {
        this.image = image;

        if (image.getCar() != this)
            image.setCar(this);
    }

    public Person getOwner() {
        return owner;
    }
    public void setOwner(Person owner) {

        this.owner = owner;
        if (owner != null && !owner.getCarList().contains(this))
            owner.getCarList().add(this);
    }

    public List<Garage> getGarageList() {
        return garageList;
    }
    public void setGarageList(List<Garage> garageList) {
        this.garageList = garageList;
    }

    @Override
    public String toString() {

        StringBuilder string = new StringBuilder("Car: ID = " + id + ", license plate = " + licensePlate +
                ", price = " + price + ", year = " + year + "\nowner details:\n\t" + "ID: " + owner.getId()
                + ", full name = " + owner.getFirstName() + " " + owner.getLastName() + ", email address = " +
                owner.getEmailAddress() + ", password = " + owner.getPassword() + "\n"
                + "This car can get service at the following addresses:\n");
        for (Garage garage : garageList)
            string.append("\t").append(garage.getAddress()).append("\n");

        return string.toString();
    }
}