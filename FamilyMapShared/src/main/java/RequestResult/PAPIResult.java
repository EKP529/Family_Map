package RequestResult;

import Model.Person;

public class PAPIResult extends Result
{
    /**
     * the unique username associated with the person for the result of the Person API from the Family Map Server
     */
    private String associatedUsername;
    /**
     * the unique person ID of the person for the result of the Person API from the Family Map Server
     */
    private String personID;
    /**
     * the first name of the person for the result of the Person API from the Family Map Server
     */
    private String firstName;
    /**
     * the last name of the person for the result of the Person API from the Family Map Server
     */
    private String lastName;
    /**
     * the gender of the person for the result of the Person API from the Family Map Server
     */
    private String gender;
    /**
     * the personID of the father of the person (if applicable) for the result of the Person API from the Family Map Server
     */
    private String fatherID;
    /**
     * the personID of the mother of the person (if applicable) for the result of the Person API from the Family Map Server
     */
    private String motherID;
    /**
     * the personID of the spouse of the person (if applicable) for the result of the Person API from the Family Map Server
     */
    private String spouseID;
    /**
     * the array of family members of the user for the result of the Person API from the Family Map Server
     */
    private Person[] data;
    /**
     * Constructs the successful result of the person API from the Family Map Server when a person ID is given
     * @param associatedUsername the associated username for the given person for the result
     * @param personID the person ID for the given person for the result
     * @param firstName the first name for the given person for the result
     * @param lastName the last name for the given person for the result
     * @param gender the gender for the given person for the result
     * @param fatherID the father ID (if applicable) for the given person for the result
     * @param motherID the mother ID (if applicable) for the given person for the result
     * @param spouseID the spouse ID (if applicable) for the given person for the result
     * @param success the status report for the result
     */
    public PAPIResult(String associatedUsername, String personID, String firstName, String lastName,
                      String gender, String fatherID, String motherID, String spouseID, boolean success)
    {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
        super.success = success;
    }
    /**
     * Constructs the successful result of the person API from the Family Map Server when no person ID is given
     * @param data the array of persons for the result
     * @param success the status report for the result
     */
    public PAPIResult(Person[] data, boolean success)
    {
        this.data = data;
        super.success = success;
    }
    /**
     * Constructs the error result of the person API from the Family Map Server
     * @param message the error message for the result
     * @param success the error status for the result
     */
    public PAPIResult(String message, boolean success) { super(message,success); }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public Person[] getData() {
        return data;
    }

    public void setData(Person[] data) {
        this.data = data;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }


}
