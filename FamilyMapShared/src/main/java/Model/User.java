package Model;

import java.util.Objects;
import java.util.UUID;

/**
 * a user to be given access to the Family Map app
 */
public class User
{
    /**
     * the unique username of the user
     */
    private String username;
    /**
     * the password of the user
     */
    private final String password;
    /**
     * the email of the user
     */
    private final String email;
    /**
     * the first name of the user
     */
    private final String firstName;
    /**
     * the last name of the user
     */
    private final String lastName;
    /**
     * the gender of the user
     */
    private final String gender;
    /**
     * the unique personID of the user to match the personID found for
     * their corresponding person object in the Family Map Server database.
     */
    private String personID = UUID.randomUUID().toString();
    /**
     * Constructs a user with all necessary data for the Family Map Server.
     * @param username username for the user
     * @param password password for the user
     * @param email email for the user
     * @param firstName user's first name
     * @param lastName user's last name
     * @param gender user's gender
     */
    public User(String username, String password, String email, String firstName, String lastName, String gender)
    {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }
    /**
     * Another User constructor with the option to give an initial personID value.
     * @param username username for the user
     * @param password password for the user
     * @param email email for the user
     * @param firstName user's first name
     * @param lastName user's last name
     * @param gender user's gender
     * @param personID the desired personID
     */
    public User(String username, String password, String email,
                String firstName, String lastName, String gender, String personID)
    {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }
    /**
     * Confirms if the user object has all necessary values
     * @return true or false;
     */
    public boolean isComplete()
    {
        return ((!Objects.equals(username, null))
                && (!Objects.equals(password, null))
                && (!Objects.equals(email, null))
                && (!Objects.equals(firstName, null))
                && (!Objects.equals(lastName, null))
                && (!Objects.equals(gender, null))
                && (!Objects.equals(personID, null)));
    }
    /**
     * Checks if an object is equal in value to the current User object
     * @param obj the object in question
     * @return true or false
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User)obj;
        return (Objects.equals(personID, user.personID)
                && Objects.equals(username, user.username)
                && Objects.equals(firstName, user.firstName)
                && Objects.equals(lastName, user.lastName)
                && Objects.equals(gender, user.gender)
                && Objects.equals(password, user.password)
                && Objects.equals(email, user.email));
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) { this.personID = personID; }
}
