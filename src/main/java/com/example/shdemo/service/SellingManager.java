package com.example.shdemo.service;

import java.util.List;

import com.example.shdemo.domain.Address;
import com.example.shdemo.domain.Car;
import com.example.shdemo.domain.Person;

public interface SellingManager {
	
	void addClient(Person person);
	List<Person> getAllClients();
	void deleteClient(Person person);
	Person findClientByPin(String pin);
	
	Long addNewCar(Car car);
	Long addNewAddress(Address address);
	List<Car> getAvailableCars();
	void disposeCar(Person person, Car car);
	List<Address> getAddressess(Person person);
	Car findCarById(Long id);
	Address findAddressById(Long id);

	List<Car> getOwnedCars(Person person);
	void sellCar(Long personId, Long carId);

}
