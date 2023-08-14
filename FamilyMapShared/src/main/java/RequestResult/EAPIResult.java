package RequestResult;

import Model.Event;

public class EAPIResult extends Result
{
    /**
     * the username of the user associated with the event for the result of the Event API from the Family Map Server
     */
    private String associatedUsername;
    /**
     * the unique event ID of the event for the result of the Event API from the Family Map Server
     */
    private String eventID;
    /**
     * the personID of the user associated with the event for the result of the Event API from the Family Map Server
     */
    private String personID;
    /**
     * the latitude of the event for the result of the Event API from the Family Map Server
     */
    private Float latitude;
    /**
     * the longitude of the event for the result of the Event API from the Family Map Server
     */
    private Float longitude;
    /**
     * the country of the event for the result of the Event API from the Family Map Server
     */
    private String country;
    /**
     * the city of the event for the result of the Event API from the Family Map Server
     */
    private String city;
    /**
     * the type of event for the result of the Event API from the Family Map Server
     */
    private String eventType;
    /**
     * the year of the event for the result of the Event API from the Family Map Server
     */
    private Integer year;
    /**
     * the array of events of all family members of the user for the result of the Event API from the Family Map Server
     */
    private Event[] data;
    /**
     * Constructs the successful result of the Event API from the Family Map Server when an event ID is given
     * @param associatedUsername the associated username for the given event for the result
     * @param eventID the event ID for the given event for the result
     * @param personID the person ID for the person associated with the event for the given event for the result
     * @param latitude the latitude for the given event for the result
     * @param longitude the longitude for the given event for the result
     * @param country the country for the given event for the result
     * @param city the city for the given event for the result
     * @param eventType the type of the given event for the result
     * @param year the year for the given event for the result
     * @param success the status report for the given event for the result
     */
    public EAPIResult(String associatedUsername, String eventID, String personID, float latitude, float longitude,
                      String country, String city, String eventType, int year, boolean success)
    {
        this.associatedUsername = associatedUsername;
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
        super.success = success;
    }

    /**
     * Constructs the successful result of the Event API from the Family Map Server when no event ID is given
     * @param data the array of events for the result
     * @param success the status report for the result
     */
    public EAPIResult(Event[] data, boolean success) {
        this.data = data;
        super.success = success;
    }

    /**
     * Constructs the error result of the Event API from the Family Map Server
     * @param message the error message for the result
     * @param success the error status for the result
     */
    public EAPIResult(String message, boolean success) { super (message, success); }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEventType() {
        return eventType;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setData(Event[] data) {
        this.data = data;
    }
    public Event[] getData() {
        return data;
    }
}
