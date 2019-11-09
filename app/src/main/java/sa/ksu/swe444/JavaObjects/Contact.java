package sa.ksu.swe444.JavaObjects;

public class Contact {

    String contactID;
    String teacherID;
    String parentEmail;
    String studentName;

    public Contact(String teacherID, String parentEmail, String studentName) {
        this.teacherID = teacherID;
        this.parentEmail = parentEmail;
        this.studentName = studentName;
    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
