package BinaryDatabase;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DatabaseGUI {

    private String tableDefFile = "TableDef.bin"; // table definition file name
    private String tableDataFile = "TableData.bin"; // table data file name
    private String filePath = "C:\\Users\\ilayd\\eclipse-workspace\\BinaryDatabase\\src"; // file path
    private ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();
    private JFrame frame;
    private JTable tableData;
    private DefaultTableModel tableModel;

    public static void main(String[] args) {
        DatabaseGUI dbGUI = new DatabaseGUI();
        dbGUI.createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create table model
        tableModel = new DefaultTableModel();
        tableData = new JTable(tableModel);

        // Table definition button
        JButton tableDefButton = new JButton("Make Table");
        tableDefButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTableDefinitionDialog();
            }
        });

        // Table data button
        JButton tableDataButton = new JButton("Table Data");
        tableDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTableDataDialog();
            }
        });

        
        // Insert button
        JButton insertButton = new JButton("Insert");
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInsertDialog();
            }
        });
        
        // Update button
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Prompt the user for the primary key and field to update
                String primaryKey = JOptionPane.showInputDialog(null, "Enter primary key:", "Update Record",
                        JOptionPane.PLAIN_MESSAGE);
                String fieldToUpdate = JOptionPane.showInputDialog(null, "Enter field to update:", "Update Record",
                        JOptionPane.PLAIN_MESSAGE);

                // Get the table model
                DefaultTableModel tableModel = (DefaultTableModel) tableData.getModel();

                // Number of rows in the table
                int rowCount = tableModel.getRowCount();

                boolean primaryKeyExists = false;

                // Update the underlying data model with the changes from the table
                for (int i = 0; i < rowCount; i++) {
                    ArrayList<String> rowData = new ArrayList<>(); //BURAYA BAK
                    String currentPrimaryKey = (String) tableModel.getValueAt(i, 0); // Assuming primary key is in the first column

                    if (primaryKey.equals(currentPrimaryKey)) {
                        primaryKeyExists = true;
                        String newValue = JOptionPane.showInputDialog(null, "Enter new value for " + fieldToUpdate + ":",
                                "Update Record", JOptionPane.PLAIN_MESSAGE);
                        tableModel.setValueAt(newValue, i, tableModel.findColumn(fieldToUpdate));
                        break;
                    }
                }

                if (primaryKeyExists) {
                    // Save the updated table data to file
                    saveTableData();

                    // Show a message to indicate that the changes have been saved
                    JOptionPane.showMessageDialog(null, "Table data has been updated and saved.", "Update Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Show an error message if the specified primary key does not exist in the table
                    JOptionPane.showMessageDialog(null, "Primary key not found in the table.", "Update Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Delete button
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Prompt the user for the primary key to delete
                String primaryKey = JOptionPane.showInputDialog(null, "Enter primary key to delete:", "Delete Record",
                        JOptionPane.PLAIN_MESSAGE);

                // Get the table model
                DefaultTableModel tableModel = (DefaultTableModel) tableData.getModel();

                // Get the number of rows in the table
                int rowCount = tableModel.getRowCount();

                boolean primaryKeyExists = false;

                // Delete the record with the specified primary key from the table
                for (int i = 0; i < rowCount; i++) {
                    String currentPrimaryKey = (String) tableModel.getValueAt(i, 0); // Assuming primary key is in the first column

                    if (primaryKey.equals(currentPrimaryKey)) {
                        primaryKeyExists = true;
                        tableModel.removeRow(i);
                        break;
                    }
                }

                if (primaryKeyExists) {
                    // Save the updated table data to file
                	// PRİMARY KEY DİĞERLERİ İLE AYNI OLAMAZ
                    saveTableData();

                    // Show a message to indicate that the record has been deleted
                    JOptionPane.showMessageDialog(null, "Record with primary key " + primaryKey + " has been deleted.",
                            "Delete Successful", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Show an error message if the specified primary key does not exist in the table
                    JOptionPane.showMessageDialog(null, "Primary key not found in the table.", "Delete Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

     // Search button
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Prompt the user for the primary key to search
                String primaryKey = JOptionPane.showInputDialog(null, "Enter primary key to search:", "Search Record",
                        JOptionPane.PLAIN_MESSAGE);

                // Get the table model
                DefaultTableModel tableModel = (DefaultTableModel) tableData.getModel();

                // Get the number of rows and columns in the table
                int rowCount = tableModel.getRowCount();
                int columnCount = tableModel.getColumnCount();

                boolean primaryKeyExists = false;

                // search table data and find the row with the matching primary key
                for (int i = 0; i < rowCount; i++) {
                    String currentPrimaryKey = (String) tableModel.getValueAt(i, 0); // primary key is in the first column

                    if (primaryKey.equals(currentPrimaryKey)) {
                        primaryKeyExists = true;

                        // Display a dialog box with the field values for the matching row
                        String rowData = "";
                        for (int j = 0; j < columnCount; j++) {
                            String columnName = tableModel.getColumnName(j);
                            String columnValue = (String) tableModel.getValueAt(i, j);
                            rowData += columnName + ": " + columnValue + "\n";
                        }

                        JOptionPane.showMessageDialog(null, rowData, "Search Result", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }

                if (!primaryKeyExists) {
                    // Show an error message if the specified primary key does not exist in the table
                    JOptionPane.showMessageDialog(null, "Primary key not found in the table.", "Search Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        
        JPanel mainPanel = new JPanel(new GridLayout(3, 2)); // Create a panel with 3 row and 2 columns
        mainPanel.add(tableDefButton);
        mainPanel.add(tableDataButton);
        mainPanel.add(insertButton);
        mainPanel.add(searchButton);
        mainPanel.add(updateButton);
        mainPanel.add(deleteButton);

        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }
  
    
    private void showTableDefinitionDialog() {
        try {
            String tableName = JOptionPane.showInputDialog(frame, "Enter table name:");
            int fieldsNum = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter how many fields that you want:"));
            ArrayList<String> mainRow = new ArrayList<String>();
            String primaryKey = JOptionPane.showInputDialog(frame, "Enter the first field's name for primary key:");
            mainRow.add(primaryKey);
            for (int i = 1; i < fieldsNum; i++) {
                String field = JOptionPane.showInputDialog(frame, "Enter a name for field " + (i + 1) + ":");
                mainRow.add(field);
            }
            table.clear();
            table.add(mainRow);
            saveTableDefinition(tableName, mainRow);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input! Please enter a valid number.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showTableDataDialog() {
        try {
            FileInputStream tableDataFileIn = new FileInputStream(filePath + "\\" + tableDataFile);
            ObjectInputStream tableDataIn = new ObjectInputStream(tableDataFileIn);

            table.clear();
            table = (ArrayList<ArrayList<String>>) tableDataIn.readObject();

            tableDataIn.close();
            tableDataFileIn.close();

            tableModel.setRowCount(0);
            tableModel.setColumnIdentifiers(table.get(0).toArray());
            for (ArrayList<String> row : table.subList(1, table.size())) {
            	tableModel.addRow(row.toArray());
            }
            
            JFrame tableDataframe = new JFrame("Table Data");
            tableDataframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JScrollPane scrollPane = new JScrollPane(tableData);
            tableDataframe.getContentPane().add(scrollPane);

            tableDataframe.pack();
            tableDataframe.setVisible(true);

        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(frame, "Table data not found!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error reading table data!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(frame, "Error loading table data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTableData(ArrayList<String> mainRow) {
        // Clear existing table data
        table.clear();
        table.add(mainRow); // Add the new main row to the table

        // Save the updated table data to file
        saveTableData();

        // Refresh the JTable to display the updated data
        DefaultTableModel tableModel = (DefaultTableModel) tableData.getModel();
        tableModel.setRowCount(0); // Clear existing rows
        for (ArrayList<String> row : table) {
            tableModel.addRow(row.toArray());
        }
    }
    
    private void saveTableDefinition(String tableName, ArrayList<String> mainRow) {
        try {
            FileOutputStream tableDefFileOut = new FileOutputStream(filePath + "\\" + tableDefFile);
            ObjectOutputStream tableDefOut = new ObjectOutputStream(tableDefFileOut);

            tableDefOut.writeObject(tableName);
            tableDefOut.writeObject(mainRow);

            tableDefOut.close();
            tableDefFileOut.close();
            JOptionPane.showMessageDialog(frame, "Table Definition saved successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving table definition!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        saveTableData(); // Save the table data before updating the main row

        updateTableData(mainRow); // Update the table data with the new main row
    }

    private void showInsertDialog() {
        ArrayList<String> newRow = new ArrayList<String>();
        for (String field : table.get(0)) {
            String value = JOptionPane.showInputDialog(frame, "Enter value for field " + field + ":");
            newRow.add(value);
        }
        table.add(newRow);
        saveTableData();
    }

    private void saveTableData() {
    	try {
    		FileOutputStream tableDataFileOut = new FileOutputStream(filePath + "\\" +  tableDataFile);
    		ObjectOutputStream tableDataOut = new ObjectOutputStream(tableDataFileOut);
    		
    		tableDataOut.writeObject(table);
    		
    		tableDataOut.close();
    		tableDataFileOut.close();
    		JOptionPane.showMessageDialog(frame, "Data saved succesfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    		
    	}catch (IOException e) {
    		JOptionPane.showMessageDialog(frame, "Error saving data!", "Error", JOptionPane.ERROR_MESSAGE);
    	}
    }
}