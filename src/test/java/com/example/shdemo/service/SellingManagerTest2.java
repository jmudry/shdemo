package com.example.shdemo.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
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
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/beans.xml" })
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
public class SellingManagerTest2 {



	@Autowired
	SellingManager sellingManager;

	private final String NAME_1 = "Kaziu";
	private final String PIN_1 = "8754";
	
	private final String MODEL_1 = "126p";
	private final String MAKE_1 = "Fiat";

	private final String MODEL_2 = "Mondeo";
	private final String MAKE_2 = "Ford";

	@Test
	@DatabaseSetup("/fullData.xml")
	@ExpectedDatabase(value = "/addPersonData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void addPersonData(){
		assertEquals(2, sellingManager.getAllClients().size());
		
		Person p = new Person();
		p.setPin(PIN_1);
		p.setFirstName(NAME_1);
		p.setRegistrationDate(new Date());
		sellingManager.addClient(p);
	}
	
	@Test
	@DatabaseSetup("/fullData.xml")
	@ExpectedDatabase(value = "/addPersonData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void addPersonData(){
		assertEquals(2, sellingManager.getAllClients().size());
		
		Person p = new Person();
		p.setPin(PIN_1);
		p.setFirstName(NAME_1);
		p.setRegistrationDate(new Date());
		sellingManager.addClient(p);
	}
	
	
	@Test
	@DatabaseSetup("/fullData.xml")
	@ExpectedDatabase(value = "/addCarData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void addCarData() {

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
	

}
