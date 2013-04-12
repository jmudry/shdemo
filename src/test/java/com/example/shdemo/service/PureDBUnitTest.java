package com.example.shdemo.service;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import junit.framework.TestCase;

public class PureDBUnitTest extends TestCase {
	
	Connection jdbcConnetion;
	IDatabaseConnection connection;
	private IDatabaseTester databaseTester;
	private IDataSet dataset;
	
	protected void setUp() throws Exception {
		jdbcConnetion = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/workdb","sa","");
		
		connection = new DatabaseConnection(jdbcConnetion);
		
		databaseTester = new  JdbcDatabaseTester("org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://localhost/workdb","sa","");
		
		dataset = new FlatXmlDataSetBuilder().build(new FileInputStream(new File("src/test/resources/fullData.xml")));
		databaseTester.setDataSet(dataset);
		databaseTester.onSetup();
		
		//DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
	}
	protected void tearDown() throws Exception {
		databaseTester.onTearDown();
	}
	
	public void test() throws Exception {
		
		IDataSet dbDataSet = connection.createDataSet();
		ITable actualTable = dbDataSet.getTable("PERSON");
		ITable filteredTable = DefaultColumnFilter.excludedColumnsTable(actualTable, new String[]{"ID"});
		
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/persondata.xml"));
		
		ITable expectedTable = expectedDataSet.getTable("PERSON");
		
		Assertion.assertEquals(expectedTable, filteredTable);
	}
	
}
