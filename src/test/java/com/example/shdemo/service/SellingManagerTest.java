package com.example.shdemo.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
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

		// If there is a client with PIN_1 delete it
		for (Person client : retrievedClients) {
			if (client.getPin().equals(PIN_1)) {
				sellingManager.deleteClient(client);
			}
		}

		Person person = new Person();
		person.setFirstName(NAME_1);
		person.setPin(PIN_1);
		// ... other properties here

		// Pin is Unique
		sellingManager.addClient(person);

		Person retrievedClient = sellingManager.findClientByPin(PIN_1);

		assertEquals(NAME_1, retrievedClient.getFirstName());
		assertEquals(PIN_1, retrievedClient.getPin());
		// ... check other properties here
	}

	@Test
	public void addCarCheck() {

		Car car = new Car();
		car.setMake(MAKE_1);
		car.setModel(MODEL_1);
		// ... other properties here

		Long carId = sellingManager.addNewCar(car);

		Car retrievedCar = sellingManager.findCarById(carId);
		assertEquals(MAKE_1, retrievedCar.getMake());
		assertEquals(MODEL_1, retrievedCar.getModel());
		// ... check other properties here

	}

	@Test
	public void sellCarCheck() {

		Person person = new Person();
		person.setFirstName(NAME_2);
		person.setPin(PIN_2);

		sellingManager.addClient(person);

		Person retrievedPerson = sellingManager.findClientByPin(PIN_2);

		Car car = new Car();
		car.setMake(MAKE_2);
		car.setModel(MODEL_2);

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
		// Do it yourself
		Person person = new Person();
		person.setFirstName(NAME_2);
		person.setPin(PIN_2);

		sellingManager.addClient(person);

		Person retrievedPerson = sellingManager.findClientByPin(PIN_2);

		Car car1 = new Car();
		car1.setMake(MAKE_1);
		car1.setModel(MODEL_1);

		Car car2 = new Car();
		car2.setMake(MAKE_2);
		car2.setModel(MODEL_2);

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
	
	//@Test
	public void disposerCar2 () {
		Car car = new Car();
		car.setMake(MAKE_1);
		car.setModel(MODEL_1);
		
		sellingManager.addNewCar(car);
		
		car.setModel("Mirafiori");
	}
	
	@Test
	public void disposeCar3() {
		Person person = new Person();
		person.setFirstName(NAME_1);
		person.setPin(PIN_1);

		Car car1 = new Car();
		car1.setMake(MAKE_1);
		car1.setModel(MODEL_1);

		Car car2 = new Car();
		car2.setMake(MAKE_2);
		car2.setModel(MODEL_2);

		Long carId1 = sellingManager.addNewCar(car1);
		Long carId2 = sellingManager.addNewCar(car2);
		List<Car> cars = new ArrayList<Car>();
		cars.add(car1);
		cars.add(car2);
		person.setCars(cars);
		
		sellingManager.addClient(person);
		
		Person p = sellingManager.findClientByPin(PIN_1);
		
		assertEquals(2, p.getCars().size());
	}
	
	//@Test
	public void checkPersonAddress () {
		Person person = new Person();
		person.setFirstName(NAME_1);
		person.setPin(PIN_1);
		
		Address address = new Address();
		address.setMiasto(MIASTO_1);
		address.setUlica(ULICA_1);
		address.setNumerDomu(NUMER_DOMU_1);
		sellingManager.addNewAddress(address);
		
		person.addAddress(address);
		
		sellingManager.addClient(person);
/*		Person p = sellingManager.findClientByPin(PIN_1);
		
		assertEquals(1, p.getAddresses().size());*/
/*		assertEquals(MIASTO_1, p.getAddresses().get(0).getMiasto());
		assertEquals(ULICA_1, p.getAddress().getUlica());
		assertEquals(NUMER_DOMU_1, p.getAddress().getNumerDomu());*/
		
	}

}
