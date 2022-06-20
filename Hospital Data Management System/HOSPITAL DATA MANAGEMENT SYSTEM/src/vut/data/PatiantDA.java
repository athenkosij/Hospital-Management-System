/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vut.data;

import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author A Jonase
 */
public class PatiantDA {

    private static Connection conn; //connecting to the database
    private static PreparedStatement ps; //for executing queries
    private static ResultSet rs; // storing data from the database
    private static Statement st; //for executing queries

    private static ArrayList<Patiant> arListPatiant = new ArrayList<>();

    public static void getConnection() throws DataStorageException {

        final String USERNAME = "root";
        final String PASSWORD = "";
        final String URL = "jdbc:mysql://localhost/Patiantsdb";
        final String DRIVER = "com.mysql.jdbc.Driver";

        try {
            //register driver
            Class.forName(DRIVER);

            //create connection
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            //driver class exception & connection exception
        } catch (ClassNotFoundException | SQLException e) {
            throw new DataStorageException("ERROR! Database driver not found \n" + e.getMessage());
        }
    }

    public static ArrayList<Patiant> getAll() throws NotFoundException {
        arListPatiant.clear();
        String query = "SELECT * FROM tblPatiants";

        try {
            //send your quesry to the database using your preparedstatement and your connection object
            ps = conn.prepareStatement(query);
            //execute and store the returned query result in resultest object
            rs = ps.executeQuery(); //when we want the query to return a table structre(return)
            while (rs.next()) {
                arListPatiant.add(new Patiant(rs.getString(1), rs.getString(2), rs.getDouble(4), Patiant.PatiantType.valueOf(rs.getString(3))));
            }

        } catch (SQLException e) {
            throw new NotFoundException("No Data retrived. \n" + e.getMessage());
        }
        return arListPatiant;
    }

    public static ArrayList<String> getIDnumbers() throws NotFoundException {
        ArrayList<String> arListIDNo = new ArrayList<>();

        String query = "SELECT ID_number FROM tblPatiants";

        try {
            //send your quesry to the database using your preparedstatement and your connection object
            ps = conn.prepareStatement(query);
            //execute and store the returned query result in resultest object
            rs = ps.executeQuery(); //when we want the query to return a table structre(return)
            while (rs.next()) {
                arListIDNo.add(rs.getString("ID_number"));
            }

        } catch (SQLException e) {
            throw new NotFoundException("No Data retrived. \n" + e.getMessage());
        }
        return arListIDNo;
    }

    public static ArrayList<Patiant> returnPatiantType(String patiantType) throws NotFoundException {
      
        String qry = "SELECT * FROM tblPatiants WHERE   Patiant_Type LIKE ?";
          arListPatiant.clear();
        try {
            ps = conn.prepareStatement(qry);
            ps.setString(1, "%" + patiantType + "%");
            rs = ps.executeQuery();
            while (rs.next())
               arListPatiant.add(new Patiant(rs.getString(1),rs.getString(2),rs.getDouble(4), Patiant.PatiantType.valueOf(rs.getString(3))));
        } catch (SQLException e) {
            throw new NotFoundException("No Data retrived. \n" + e.getMessage());
        }
        return arListPatiant;
    }

    public static Patiant getPatiantObj(String idNo) throws NotFoundException {
        Patiant objPatiant = null;

        String query = "SELECT * FROM tblPatiants WHERE ID_number = ?";

        try {
            //send your quesry to the database using your preparedstatement and your connection object
            ps = conn.prepareStatement(query);
            ps.setString(1, idNo);
            rs = ps.executeQuery();
            rs.next();
            objPatiant = new Patiant(rs.getString(1), rs.getString(2), rs.getDouble(4), Patiant.PatiantType.valueOf(rs.getString(3)));

        } catch (SQLException e) {
            throw new NotFoundException(e.getMessage());
        }
        return objPatiant; 
    }

    public static void updateAmountDue(double amount, String idNumber) throws NotFoundException {
        String query = "UPDATE tblPatiants  SET Amount_due = Amount_due - ? WHERE ID_number = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(2, idNumber);
            ps.setDouble(1, amount);
            ps.executeUpdate();
            

        } catch (SQLException e) {
            throw new NotFoundException("No Data retrived. \n" + e.getMessage());
        }
    }

    /*public static ArrayList<Patiant> returnPatiantType(String patiantType) throws NotFoundException{
    arListPatiant.clear();
    String query = "SELECT * FROM tblPatiants WHERE Patiant_Type = ?";
    
    try{
        //send your quesry to the database using your preparedstatement and your connection object
    ps = conn.prepareStatement(query);
    //execute and store the returned query result in resultest object
    rs = ps.executeQuery(); 
    while (rs.next()){
    arListPatiant.add(new Patiant(rs.getString(1), rs.getString(2), rs.getDouble(3)));
    }
    
    } catch (SQLException e){
    throw new NotFoundException("No Data retrived. \n" + e.getMessage());
    }
    return arListPatiant;
    } */
    public static void addPatiant(Patiant objPatiant) throws DuplicateException {
        String query = "INSERT INTO tblPatiants VALUES(?,?,?,?)";

        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, objPatiant.getIdNumber());
            ps.setString(2, objPatiant.getSurname());
            ps.setDouble(4, objPatiant.getAmountDue());
            ps.setString(3, objPatiant.getPatiantType().toString());
            ps.executeUpdate(); //we sending data to the database(set)

        } catch (SQLException e) {
            throw new DuplicateException(objPatiant.getSurname() + "Not added \n" + e.getMessage());
        }
    }

    public static double returnTotalAmountDue() throws NotFoundException {
        int totAmount=0;
        String qry = "SELECT Amount_Due FROM tblPatiants";
        try {
            ps = conn.prepareStatement(qry);
            rs = ps.executeQuery();
            while (rs.next())
                totAmount+=rs.getDouble(1);
        } catch (SQLException e) {
            throw new NotFoundException("No Data retrived. \n" + e.getMessage());
        }
        return totAmount;
    }
    
    public static double calculateTotalInPatiants() throws NotFoundException {
        int totAmount=0;
        String qry = "SELECT Amount_Due FROM tblPatiants WHERE Pariant_Type = 'In_Patiant'";
        try {
            ps = conn.prepareStatement(qry);
            rs = ps.executeQuery();
            while (rs.next())
                totAmount+=rs.getDouble(1);
        } catch (SQLException e) {
            throw new NotFoundException("No Data retrived. \n" + e.getMessage());
        }
        return totAmount;
    }
    
    public static double calculateTotalOutPatiants() throws NotFoundException {
        int totAmount=0;
        String qry = "SELECT Amount_Due FROM tblPatiants WHERE Patiant_Type = 'Out_Patiant'";
        try {
            ps = conn.prepareStatement(qry);
            rs = ps.executeQuery();
            while (rs.next())
                totAmount+=rs.getDouble(1);
        } catch (SQLException e) {
            throw new NotFoundException("No Data retrived. \n" + e.getMessage());
        }
        return totAmount;
    }
    
    public static void terminate() throws DataStorageException{
    try{
        if (conn != null){
        conn.close();
        }
    } catch (SQLException e){
    throw new DataStorageException(e.getMessage());}
    }
    
    

    /*public static void calculateTotalOutPatiants() throws NotFoundException {
        String qry = "SELECT SUM(Amount_Due) FROM tblPatiants WHERE Patiant_Type = Out_Patiant";
        try {
            ps = conn.prepareStatement(qry);
            //execute and store the returned query result in resultest object
            rs = ps.executeQuery();
        } catch (SQLException e) {
            throw new NotFoundException("No Data retrived. \n" + e.getMessage());
        }
    } 

    public static void calculateTotalInPatiants() throws NotFoundException {
        String qry = "SELECT SUM(Amount_Due) FROM tblPatiants WHERE Patiant_Type = In_Patiant";
        try {
            ps = conn.prepareStatement(qry);
            //execute and store the returned query result in resultest object
            rs = ps.executeQuery();
        } catch (SQLException e) {
            throw new NotFoundException("No Data retrived. \n" + e.getMessage());
        }
    } */
}
