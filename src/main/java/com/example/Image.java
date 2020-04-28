package com.example;

import javax.persistence.*;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    private Car car;

    //GROUP setters and getters
    public Car getCar() {
        return car;
    }
    public void setCar(Car car) {
        if (this.car!= car)
        {
            this.car = car;
            car.setImage(this);
        }
    }

}