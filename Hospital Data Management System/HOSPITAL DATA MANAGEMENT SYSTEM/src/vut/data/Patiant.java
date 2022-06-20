/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vut.data;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 *
 * @author A Jonase
 */
public class Patiant {

    public enum PatiantType {
        In_Patiant, Out_Patiant
    }
    PatiantType patiantType;
    String idNumber, surname;
    double amountDue;

    public Patiant() {
    }

    public Patiant(String idNumber, String surname, double amountDue, PatiantType patiantType) {
        this.idNumber = idNumber;
        this.surname = surname;
        this.amountDue = amountDue;
        this.patiantType = patiantType;

    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getSurname() {
        return surname;
    }

    public double getAmountDue() {
        return amountDue;
    }

    public PatiantType getPatiantType() {
        return patiantType;
    }

    public void setIdNumber(String idNumber) {
        if (idNumber.matches("\\d{13}")) {
            this.idNumber = idNumber;
        } else {
            throw new IllegalArgumentException("ID number must be 13 digits");
        }
    }

    public void setSurname(String surname) {
        if (surname.isEmpty()) {
            throw new IllegalArgumentException("Surname must contain some charecter");
        } else {
            this.surname = surname;
        }
    }

    public void setAmountDue(double amountDue) {
        this.amountDue = amountDue;
    }

    public void setPatiantType(PatiantType patiantType) {
        this.patiantType = patiantType;
    }

    NumberFormat currFormat = NumberFormat.getCurrencyInstance();

    @Override
    public String toString() {
        return surname + "\t" + idNumber + "\t" + patiantType + "t" + currFormat.format(amountDue) + "\n";
    }
    
    //Calling methods

    public void getConnection() throws DataStorageException {
        PatiantDA.getConnection();
    }

    public ArrayList<Patiant> getAll() throws NotFoundException {
        return PatiantDA.getAll();
    }

    public ArrayList<String> getIDnumbers() throws NotFoundException {
        return PatiantDA.getIDnumbers();
    }

    public Patiant getPatiantObj(String idNo) throws NotFoundException {
        return PatiantDA.getPatiantObj(idNo);
    }

    public void updateAmountDue(double amount, String idNumber) throws NotFoundException {
        PatiantDA.updateAmountDue(amount, idNumber);
    }

    public ArrayList<Patiant> returnPatiantType(String patiantType) throws NotFoundException{
    return PatiantDA.returnPatiantType(patiantType);
    }
    
    public void addPatiant(Patiant objPatiant) throws DuplicateException {
        PatiantDA.addPatiant(objPatiant);
    }

    public double returnTotalAmountDue() throws NotFoundException {
        return PatiantDA.returnTotalAmountDue();
    }

    public double calculateTotalOutPatiants() throws NotFoundException {
       return PatiantDA.calculateTotalOutPatiants();
    }

    public double calculateTotalInPatiants() throws NotFoundException {
       return PatiantDA.calculateTotalInPatiants();
    }
}
