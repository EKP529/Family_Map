package RequestResult;

public class EAPIRequest
{
    /**
     * the possible event ID from the URI of the Event API for the Family Map Client
     */
    String eventID;
    /**
     * the user(name) that used the Event API for the Family Map Client
     */
    String username;

    /**
     * Constructs an Event API Request object to hold the possible eventID
     * in the URI from the Event API for the Family Map Client
     * @param eventID the possible event ID
     */
    public EAPIRequest(String eventID, String username)
    {
        this.eventID = eventID;
        this.username = username;
    }

    public String getEventID() {
        return eventID;
    }
    public void setEventID(String personID) {
        this.eventID = personID;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
