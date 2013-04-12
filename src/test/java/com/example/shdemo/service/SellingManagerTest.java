package com.example.shdemo.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.example.shdemo.domain.Address;
import com.example.shdemo.domain.Car;
import com.example.shdemo.domain.Person;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/beans.xml" })
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class SellingManagerTest {

	@Autowired
	SellingManager sellingManager;

	private final String NAME_1 = "Bolek";
	private final String PIN_1 = "1234";

	private final String NAME_2 = "Lolek";
	private final String PIN_2 = "4321";

	private final String MODEL_1 = "126p";
	private final String MAKE_1 = "Fiat";

	private final String MODEL_2 = "Mondeo";
	private final String MAKE_2 = "Ford";
	
	private static final String NUMER_DOMU_1 = "21";
	private static final String ULICA_1 = "Traktorowa";
	private static final String MIASTO_1 = "Olsztyn";

	@Test
	public void addClientCheck() {

		List<Person> retrievedClients = sellingManager.getAllClients();

		for (Person client : retrievedClients) {
			if (client.getPin().equals(PIN_1)) {
				sellingManager.deleteClient(client);
			}
		}

		Person person = new Person(NAME_1, PIN_1);

		sellingManager.addClient(person);

		Person retrievedClient = sellingManager.findClientByPin(PIN_1);

		assertEquals(NAME_1, retrievedClient.getFirstName());
		assertEquals(PIN_1, retrievedClient.getPin());
	}

	@Test
	public void addCarCheck() {
		Car car = new Car(MAKE_1, MODEL_1);

		Long carId = sellingManager.addNewCar(car);

		Car retrievedCar = sellingManager.findCarById(carId);
		assertEquals(MAKE_1, retrievedCar.getMake());
		assertEquals(MODEL_1, retrievedCar.getModel());

	}

	@Test
	public void sellCarCheck() {
		Person person = new Person(NAME_2, PIN_2);

		sellingManager.addClient(person);

		Person retrievedPerson = sellingManager.findClientByPin(PIN_2);

		Car car = new Car(MAKE_2, MODEL_2);

		Long carId = sellingManager.addNewCar(car);

		sellingManager.sellCar(retrievedPerson.getId(), carId);

		List<Car> ownedCars = sellingManager.getOwnedCars(retrievedPerson);

		assertEquals(1, ownedCars.size());
		assertEquals(MAKE_2, ownedCars.get(0).getMake());
		assertEquals(MODEL_2, ownedCars.get(0).getModel());
		assertEquals(true, ownedCars.get(0).getSold());
	}

	@Test
	public void disposeCarCheck() {
		Person person = new Person(NAME_2, PIN_2);

		sellingManager.addClient(person);

		Person retrievedPerson = sellingManager.findClientByPin(PIN_2);

		Car car1 = new Car(MAKE_1, MODEL_1);
		Car car2 = new Car(MAKE_2, MODEL_2);

		Long carId1 = sellingManager.addNewCar(car1);
		Long carId2 = sellingManager.addNewCar(car2);
		sellingManager.sellCar(retrievedPerson.getId(), carId1);
		sellingManager.sellCar(retrievedPerson.getId(), carId2);
		List<Car> ownedCars = sellingManager.getOwnedCars(retrievedPerson);

		assertEquals(2, ownedCars.size());
		
		sellingManager.disposeCar(person, car2);
	
		ownedCars = sellingManager.getOwnedCars(retrievedPerson);
		assertEquals(1, ownedCars.size());
		assertEquals(MAKE_1, ownedCars.get(0).getMake());
		assertEquals(MODEL_1, ownedCars.get(0).getModel());
		assertEquals(true, ownedCars.get(0).getSold());
		
		Car soldCar = sellingManager.findCarById(carId2);
		assertEquals(false, soldCar.getSold());
	}
	
/*	//@Test
	public void changedModelOrNot () {
		Car car = new Car();
		car.setMake(MAKE_1);
		car.setModel(MODEL_1);
		
		sellingManager.addNewCar(car);
		
		car.setModel("Mirafiori");
	}*/
	
	@Test
	public void checkAddAddress () {
		Person person = new Person(NAME_1, PIN_1);
		
		Address address = new Address(ULICA_1, NUMER_DOMU_1, MIASTO_1, person);
		
		Long addressId = sellingManager.addNewAddress(address);
		
		address = sellingManager.findAddressById(addressId);
		assertEquals(MIASTO_1, address.getMiasto());
		assertEquals(ULICA_1, address.getUlica());
		assertEquals(NUMER_DOMU_1, address.getNumerDomu());
		assertEquals(NAME_1, address.getPerson().getFirstName());
		assertEquals(PIN_1, address.getPerson().getPin());
	}
	
	@Test
	public void checkPersonAddress () {
		Person person1 = new Person(NAME_1, PIN_1);
		Person person2 = new Person(NAME_2, PIN_2);
		
		sellingManager.addClient(person1);
		sellingManager.addClient(person2);
		
		Address address = new Address(ULICA_1, NUMER_DOMU_1, MIASTO_1, person1);
		sellingManager.addNewAddress(address);

		Person person = sellingManager.findClientByPin(PIN_1);
		
		List<Address> addresses = sellingManager.getAddressess(person);
		assertEquals(1, addresses.size());
		assertEquals(MIASTO_1, addresses.get(0).getMiasto());
		assertEquals(ULICA_1, addresses.get(0).getUlica());
		assertEquals(NUMER_DOMU_1, addresses.get(0).getNumerDomu());
		
		//druga osoba dalej nie ma adresu
		person = sellingManager.findClientByPin(PIN_2);
		addresses = sellingManager.getAddressess(person);
		assertEquals(0, addresses.size());
		
	}

}
