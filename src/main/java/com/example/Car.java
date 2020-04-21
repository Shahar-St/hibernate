package com.example;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String licensePlate;
    private double price;
    private int year;
    @ManyToOne
    private Person owner;
    @OneToOne
    private Image image;
    @ManyToMany
    private final List<Garage> garageList;

    //GROUP C'tors
    public Car(String licensePlate, double price, int year) {
        this();
        this.licensePlate = licensePlate;
        this.price = price;
        this.year = year;
    }
    public Car(){
        garageList = new ArrayList<>();
    }

    //GROUP adders
    public void addGarage(Garage garage) {
        if (!garageList.contains(garage))
        {
            garageList.add(garage);
            garage.addCar(this);
        }
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
        if (this.image != image)
        {
            this.image = image;
            image.setCar(this);
        }
    }

    public Person getOwner() {
        return owner;
    }
    public void setOwner(Person owner) {

        if (this.owner != owner)
        {
            if (this.owner != null)
                this.owner.removeCar(this);
            this.owner = owner;
            owner.addCar(this);
        }
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