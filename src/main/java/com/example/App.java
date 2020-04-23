package com.example;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;

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
    private static final int NUM_OF_CARS = 5;
    private static final int NUM_OF_GARAGES = 2;
    private static final int NUM_OF_IMAGES = 5;
    private static Session session;

    public static void main(String[] args) {
        try
        {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            generateCars();
            generatePersons();
            generateGarages();
            generateImages();
            connectEntities();
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

    private static SessionFactory getSessionFactory() throws HibernateException {

        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        Configuration configuration = new Configuration();
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

    private static void generateGarages() {

        String[] addresses = {"1 1st st, New York, NY", "5 5th st, New York, NY"};
        for (int i = 0; i < NUM_OF_GARAGES; i++)
        {
            Garage garage = new Garage(addresses[i]);
            session.save(garage);
        }
    }

    private static void generateImages() {
        for (int i = 0; i < NUM_OF_IMAGES; i++)
            session.save(new Image());
    }

    private static <T> List<T> getAllOfType(Class<T> objectType) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(objectType);
        query.from(objectType);
        return session.createQuery(query).getResultList();
    }

    private static void printAllGarages() {
        List<Garage> garages = getAllOfType(Garage.class);
        for (Garage garage : garages)
            System.out.println(garage);
    }

    private static void printAllCars() {
        List<Car> cars = getAllOfType(Car.class);
        for (Car car : cars)
            System.out.println(car);
    }

    private static void connectEntities() {

        List<Person> persons = getAllOfType(Person.class);
        List<Car> cars = getAllOfType(Car.class);
        List<Garage> garages = getAllOfType(Garage.class);
        List<Image> images = getAllOfType(Image.class);

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