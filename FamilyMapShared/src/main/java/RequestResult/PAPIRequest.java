package RequestResult;

public class PAPIRequest
{
    /**
     * the person ID from the URI of the Person API for the Family Map Client
     */
    String personID;
    /**
     * the user(name) using the Person API for the Family Map Client
     */
    String username;
    /**
     * Constructs a Person API Request object to hold the possible person ID
     * from the URI from the Person API for the Family Map Client
     * @param personID the possible person ID
     */
    public PAPIRequest(String personID, String username)
    {
        this.personID = personID;
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
