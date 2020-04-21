package com.example;

import java.util.List;
import java.util.Random;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class App {

    private static final int NUM_OF_PERSONS = 5;
    private static final int NUM_OF_CARS = 10;
    private static final int NUM_OF_GARAGES = 2;
    private static final int NUM_OF_IMAGES = 10;
    private static Session session;

    public static void main(String[] args) {
        try
        {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
//            generateCars();
//            generatePersons();
//            generateGarages();
//            generateImages();
//            connectEntities();
            printAllGarages();
            printAllCars();
        }
        catch (Exception exception)
        {
            if (session != null)
            {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
        } finally
        {
            assert session != null;
            session.close();
        }
    }
    private static void printAllGarages() {
        List<Garage> garages = getAllGarages();
        for (Garage garage : garages)
            System.out.println(garage);


    }


    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();
        // Add ALL of your entities here. You can also try adding a whole package.
        configuration.addAnnotatedClass(Car.class);
        configuration.addAnnotatedClass(Person.class);
        configuration.addAnnotatedClass(Garage.class);
        configuration.addAnnotatedClass(Image.class);
        ServiceRegistry serviceRegistry =
                new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    private static void generateCars() {
        Random random = new Random();
        for (int i = 0; i < NUM_OF_CARS; i++)
        {
            Car car = new Car("MOO-" + random.nextInt(999999), 100000, 2000 + random.nextInt(19));
            session.save(car);
        }
    }

    private static void generatePersons() {

        Random random = new Random();
        String[] firstNames = {"Avi", "Dan", "John", "Didi", "Avihu"};
        String[] lastNames = {"Hemmo", "Bilzerian", "Snow", "Harrari", "Medina"};
        for (int i = 0; i < NUM_OF_PERSONS; i++)
        {
            Person person = new Person(firstNames[i], lastNames[i], random.nextInt(99999),
                    firstNames[i] + lastNames[i] + "@fake.com");
            session.save(person);
        }
    }

    private static List<Person> getAllPersons() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Person> query = builder.createQuery(Person.class);
        query.from(Person.class);
        return session.createQuery(query).getResultList();
    }

    private static void generateGarages() {

        String[] addresses = {"1 1st st, New York, NY", "5 5th st, New York, NY"};
        for (int i = 0; i < NUM_OF_GARAGES; i++)
        {
            Garage garage = new Garage(addresses[i]);
            session.save(garage);
        }
    }

    private static List<Garage> getAllGarages() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Garage> query = builder.createQuery(Garage.class);
        query.from(Garage.class);
        return session.createQuery(query).getResultList();
    }

    private static List<Car> getAllCars() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Car> query = builder.createQuery(Car.class);
        query.from(Car.class);
        return session.createQuery(query).getResultList();
    }

    private static void printAllCars() {
        List<Car> cars = getAllCars();
        for (Car car : cars)
            System.out.println(car);
    }

    private static void generateImages() {
        for (int i = 0; i < NUM_OF_IMAGES; i++)
        {
            session.save(new Image());
        }
    }
    private static List<Image> getAllImages() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Image> query = builder.createQuery(Image.class);
        query.from(Image.class);
        return session.createQuery(query).getResultList();
    }

    private static void connectEntities() {

        List<Person> persons = getAllPersons();
        List<Car> cars = getAllCars();
        List<Garage> garages = getAllGarages();
        List<Image> images = getAllImages();

        // connect cars with owners & cars with garages
        for (int i = 0; i < cars.size(); i++)
        {
            cars.get(i).setOwner(persons.get(i % persons.size()));
            cars.get(i).addGarage(garages.get(i % garages.size()));
            cars.get(i).setImage(images.get(i % images.size()));
            session.update(cars.get(i));
            session.update(persons.get(i % persons.size()));
            session.update(garages.get(i % garages.size()));
            session.update(images.get(i % images.size()));
        }

        // connect persons with garages
        for (int i = 0; i < persons.size(); i++)
        {
            persons.get(i).addGarage(garages.get(i % garages.size()));
            session.update(persons.get(i));
            session.update(garages.get(i % garages.size()));
        }

    }


}