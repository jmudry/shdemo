package com.example.shdemo.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import com.example.shdemo.domain.Address;
import com.example.shdemo.domain.Car;
import com.example.shdemo.domain.Person;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/beans.xml" })
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionDbUnitTestExecutionListener.class })
public class SellingManagerTest2 {

	@Autowired
	SellingManager sellingManager;

	private final String NAME_1 = "Bolek";
	private final String PIN_1 = "1234";	
	
	private final String NAME_2 = "Lolek";
	private final String PIN_2 = "4321";
	
	private final String NAME_3 = "Kaziu";
	private final String PIN_3 = "8754";
	
	private final String MODEL_1 = "126p";
	private final String MAKE_1 = "Fiat";
	private final long CAR_ID_1 = 1L;
	
	private final String MODEL_2 = "Mondeo";
	private final String MAKE_2 = "Ford";
	private final long CAR_ID_2 = 2L;
	
	private final String MODEL_3 = "Ibiza";
	private final String MAKE_3 = "Seat";
	
	private static final String NUMER_DOMU_1 = "21";
	private static final String ULICA_1 = "Traktorowa";
	private static final String MIASTO_1 = "Olsztyn";


	@Test
	@DatabaseSetup("/fullData.xml")
	@ExpectedDatabase(value = "/addPersonData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void addPersonData(){
		assertEquals(2, sellingManager.getAllClients().size());
		
		Person p = new Person(NAME_3, PIN_3);
		p.setRegistrationDate(new Date());
		sellingManager.addClient(p);
	}
	
	
	@Test
	@DatabaseSetup("/fullData.xml")
	@ExpectedDatabase(value = "/addCarData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void addCarData() {
		Car car = new Car();
		car.setMake(MAKE_3);
		car.setModel(MODEL_3);
		sellingManager.addNewCar(car);
	}
	
	//@Test
	@DatabaseSetup("/fullData.xml")
	@ExpectedDatabase(value = "/sellCarData.xml")
	public void sellCarData() {
		Person person = sellingManager.findClientByPin(PIN_1);
		Car car = sellingManager.findCarById(CAR_ID_1);

		sellingManager.sellCar(person.getId(), car.getId());		
	}
	
	
	@Test
	@DatabaseSetup("/fullData.xml")
	public void sellCarDataTransactional() {
		Person person = sellingManager.findClientByPin(PIN_1);
		Car car = sellingManager.findCarById(CAR_ID_1);

		sellingManager.sellCar(person.getId(), car.getId());	

		List<Car> ownedCars = sellingManager.getOwnedCars(person);

		assertEquals(1, ownedCars.size());
		assertEquals(MAKE_1, ownedCars.get(0).getMake());
		assertEquals(MODEL_1, ownedCars.get(0).getModel());
		assertEquals(true, ownedCars.get(0).getSold());
	}
	
//	@Test
	@DatabaseSetup("/fullData.xml")
	@ExpectedDatabase(value = "/disposeCarData.xml")
	public void disposeCar () {
		Person person = sellingManager.findClientByPin(PIN_2);
		Car car = sellingManager.findCarById(CAR_ID_2);
		
		sellingManager.disposeCar(person, car);
	}
	
	@Test
	@DatabaseSetup("/fullData.xml")
	public void disposeCarTransactional() {
		Person person = sellingManager.findClientByPin(PIN_1);
		Car car1 = sellingManager.findCarById(CAR_ID_1);
		Car car2 = sellingManager.findCarById(CAR_ID_2);

		sellingManager.sellCar(person.getId(), car1.getId());
		sellingManager.sellCar(person.getId(), car2.getId());
		List<Car> ownedCars = sellingManager.getOwnedCars(person);

		assertEquals(2, ownedCars.size());
		
		sellingManager.disposeCar(person, car2);
	
		ownedCars = sellingManager.getOwnedCars(person);
		assertEquals(1, ownedCars.size());
		assertEquals(MAKE_1, ownedCars.get(0).getMake());
		assertEquals(MODEL_1, ownedCars.get(0).getModel());
		assertEquals(true, ownedCars.get(0).getSold());
		
		Car soldCar = sellingManager.findCarById(car2.getId());
		assertEquals(false, soldCar.getSold());
		
	}
	
	@Test
	@DatabaseSetup("/fullData.xml")
	@ExpectedDatabase(value = "/addAddress.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void addAddress () {
		Person person = sellingManager.findClientByPin(PIN_1);
		Address address = new Address(ULICA_1, NUMER_DOMU_1, MIASTO_1, person);
		sellingManager.addNewAddress(address);

		List<Address> addresses = sellingManager.getAddressess(person);
		assertEquals(1, addresses.size());
		
		//druga osoba dalej nie ma adresu
		person = sellingManager.findClientByPin(PIN_2);
		addresses = sellingManager.getAddressess(person);
		assertEquals(0, addresses.size());
		
	}
	

}
