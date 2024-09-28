package BinaryDatabase;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


public class Database {
	
    public static void main(String[] args) {
        
        String tableDefFile = "TableDef.bin"; // table definition file name
        String tableDataFile = "TableData.bin"; // table data file name
        String recordFile = "storeRecord.bin"; // record file name
        String filePath = "C:\\Users\\ilayd\\eclipse-workspace\\BinaryDatabase\\src"; // file path
        // Combine the file path and file names to get the complete file paths
        String completeTableDefFilePath = filePath + "\\" + tableDefFile;
        String completeTableDataFilePath = filePath + "\\" + tableDataFile;
        String completeRecordFilePath = filePath + "\\" + recordFile;

        try {           
            Scanner scanner = new Scanner(System.in);

            // Write table definition to the binary file
            System.out.print("Enter table name: ");
            String tableName = scanner.nextLine();

            System.out.print("Enter how many fields that you want: ");
            int fieldsNum = scanner.nextInt();
            scanner.nextLine(); // for waiting primary key input

            ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();
            
            ArrayList<String> mainRow = new ArrayList<String>();
            
            System.out.println("Enter the first field's name for primary key: ");
    		String primaryKey = scanner.nextLine();
    		mainRow.add(primaryKey);
    		
    		for(int i = 1; i < fieldsNum; i++) {
            	System.out.println("Enter a name for field " + (i+1) + ":");
            	String field = scanner.nextLine();
            	mainRow.add(field);
            }          	
            

            table.add(mainRow);
            
            FileOutputStream tableDefFileOut = new FileOutputStream(completeTableDefFilePath); 
            ObjectOutputStream tableDefOut = new ObjectOutputStream(tableDefFileOut); 

            // Write table metadata
            tableDefOut.writeObject(tableName); 
            tableDefOut.writeObject(mainRow); 

            tableDefOut.close(); 
            tableDefFileOut.close(); 
            System.out.println("Table definition written to binary file successfully.");

            // Write table data to the binary file
           
            FileOutputStream tableDataFileOut = new FileOutputStream(completeTableDataFilePath); 
            ObjectOutputStream tableDataOut = new ObjectOutputStream(tableDataFileOut); 

            ArrayList<String> row1 = new ArrayList<String>();
              
           
            System.out.println("Enter an ID: ");
        	primaryKey = scanner.nextLine();
        	row1.add(primaryKey);
        	
        	for(int i = 1; i < fieldsNum; i++) {
            	System.out.println("Enter a data for field " + (i+1) + ":");
            	String field = scanner.nextLine();
            	row1.add(field);
            }    
        		            
            table.add(row1);
            tableDataOut.writeObject(table);   
            
            tableDataOut.close(); 
            tableDataFileOut.close(); 
            System.out.println("Table data written to binary file successfully.");
            
            FileInputStream tableDataFileIn = new FileInputStream(completeTableDataFilePath);
            ObjectInputStream tableDataIn = new ObjectInputStream(tableDataFileIn);

            ArrayList<ArrayList<String>> tableData = (ArrayList<ArrayList<String>>) tableDataIn.readObject();

            System.out.println("Table Data:");

            // Print the table data
            for (ArrayList<String> row : tableData) {
                for (String field : row) {
                    System.out.print(field + "\t");
                }
                System.out.println();
            }

            tableDataIn.close();
            tableDataFileIn.close();

            // Copy data from TableData.dat to storeRecord.dat
            FileInputStream tableDataFileInCopy = new FileInputStream(completeTableDataFilePath);
            ObjectInputStream tableDataInCopy = new ObjectInputStream(tableDataFileInCopy);
            
            ArrayList<ArrayList<String>> tableDataCopy = (ArrayList<ArrayList<String>>)tableDataInCopy.readObject();
            
            tableDataInCopy.close();
            tableDataFileInCopy.close();
            
            FileOutputStream recordFileOut = new FileOutputStream(completeRecordFilePath);
            ObjectOutputStream recordOut = new ObjectOutputStream(recordFileOut);
            
            // Write table data to storeRecord.dat
            recordOut.writeObject(tableDataCopy);
            
            recordOut.close();
            recordFileOut.close();
            System.out.println("Data copied from TableData. dat to storeRecord.dat succesfully.");
            
            // Read data from storeRecord.dat
            FileInputStream recordFileIn = new FileInputStream(completeRecordFilePath);
            ObjectInputStream recordIn = new ObjectInputStream(recordFileIn);
            
            ArrayList<ArrayList<String>> recordData = (ArrayList<ArrayList<String>>) recordIn.readObject();
            
            recordIn.close();
            recordFileIn.close();
            
            System.out.println("Data in storeRecord.dat");
            
            //Print data from storeRecord.dat
            for(ArrayList<String> row: recordData) {
            	for (String field : row) {
            		System.out.println(field + "\t");
            	}
            	System.out.println();
            }
        } catch (IOException | ClassNotFoundException e) {
        	e.printStackTrace();
        }
    }
}
           
