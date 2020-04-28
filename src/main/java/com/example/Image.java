package com.example;

import javax.persistence.*;

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(mappedBy = "image")
    private Car car;

    //GROUP setters and getters
    public int getId() {
        return id;
    }

    public Car getCar() {
        return car;
    }
    public void setCar(Car car) {

        this.car = car;

        if (car.getImage() != this)
            car.setImage(this);
    }

}