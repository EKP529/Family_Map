package Model;

import java.util.Objects;
import java.util.UUID;


/**
 * a Person to be represented in the Family Map
 */
public class Person
{
    /**
     * the unique person ID of the person
     */
    private String personID = UUID.randomUUID().toString();
    /**
     * the unique username associated with the person
     */
    private String associatedUsername;
    /**
     * the first name of the person
     */
    private final String firstName;
    /**
     * the last name of the person
     */
    private final String lastName;
    /**
     * the gender of the person
     */
    private final String gender;
    /**
     * the personID of the father of the person (if applicable)
     */
    private String fatherID;
    /**
     * the personID of the mother of the person (if applicable)
     */
    private String motherID;
    /**
     * the personID of the spouse of the person (if applicable)
     */
    private String spouseID;

    /**
     * Constructs a person object with all the necessary data for a person in the Family Map.
     * @param associatedUsername the username associated with the person
     * @param firstName the person's first name
     * @param lastName the person's last name
     * @param gender the person's gender
     * @param iDs (optional)fatherID, (optional)motherID, (optional)spouseID, (optional)personID
     */
    public Person(String associatedUsername, String firstName, String lastName, String gender,
                  String... iDs)
    {
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        if (iDs.length >= 1)
        {
            fatherID = iDs[0];
        }
        if (iDs.length >= 2)
        {
            motherID = iDs[1];
        }
        if (iDs.length >= 3)
        {
            spouseID = iDs[2];
        }
        if (iDs.length >= 4)
        {
            personID = iDs[3];
        }
    }

    /**
     * Confirms if the person object has all necessary values
     * @return true or false;
     */
    public boolean isComplete()
    {
        return ((!Objects.equals(associatedUsername, null))
                && (!Objects.equals(firstName, null))
                && (!Objects.equals(lastName, null))
                && (!Objects.equals(gender, null))
                && (!Objects.equals(personID, null)));
    }
    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
    public String getAssociatedUsername() {
        return associatedUsername;
    }
    public void setAssociatedUsername(String username) { this.associatedUsername = username;}

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
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

    /**
     * Checks if an object is equal in value to the current Person object
     * @param obj the object in question
     * @return true or false
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person)obj;
        return (Objects.equals(personID, person.personID)
                && Objects.equals(associatedUsername, person.associatedUsername)
                && Objects.equals(firstName, person.firstName)
                && Objects.equals(lastName, person.lastName)
                && Objects.equals(gender, person.gender)
                && Objects.equals(fatherID, person.fatherID)
                && Objects.equals(motherID, person.motherID)
                && Objects.equals(spouseID, person.spouseID));
    }
}
