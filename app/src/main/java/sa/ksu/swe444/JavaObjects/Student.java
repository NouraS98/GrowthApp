package sa.ksu.swe444.JavaObjects;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Student {

    String firstName;
    String lastName;
    String nationalID;
    String motherID;
    String fatherID;
    String classID;

    public Student() {
    }

    public Student(String firstName, String lastName, String nationalID, String motherID, String fatherID, String classID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationalID = nationalID;
        this.motherID = motherID;
        this.fatherID = fatherID;
        this.classID = classID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }
}
