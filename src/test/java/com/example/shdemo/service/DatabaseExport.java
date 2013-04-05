package com.example.shdemo.service;

import java.sql.Connection;
import java.sql.DriverManager;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import java.io.FileOutputStream;

public class DatabaseExport {

	public static void main(String[] args) throws Exception {
		Connection jdbcConnetion = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/workdb","sa","");
		
		IDatabaseConnection connection = new DatabaseConnection(jdbcConnetion);

		FlatDtdDataSet.write(connection.createDataSet(), new FileOutputStream("src/test/resources/dataSet.dtd"));
		
		FlatXmlDataSet.write(connection.createDataSet(), new FileOutputStream("src/test/resources/fuldata.xml"));
	
	}
	
	

}
