package Model;

import java.util.Objects;
import java.util.UUID;

/**
 * a life event of a given person in the Family Map
 */
public class Event
{
    /**
     * the unique event ID of the event
     */
    private String eventID = UUID.randomUUID().toString();
    /**
     * the username of the user associated with the event
     */
    private String associatedUsername;
    /**
     * the personID of the user associated with the event
     */
    private String personID;
    /**
     * the latitude of the event
     */
    private final float latitude;
    /**
     * the longitude of the event
     */
    private final float longitude;
    /**
     * the country of the event
     */
    private final String country;
    /**
     * the city of the event
     */
    private final String city;
    /**
     * the type of event
     */
    private final String eventType;
    /**
     * the year of the event
     */
    private final int year;

    /**
     * Constructs an event object with all necessary data for an event in the Family Map.
     * @param associatedUsername the username associated with this event
     * @param personID the unique personID for the person to which this event corresponds
     * @param latitude the latitude of the event location
     * @param longitude the longitude of the event location
     * @param country the country in which the event happened
     * @param city the city in which the event happened
     * @param eventType the type of event
     * @param year the year the event happened
     */
    public Event(String associatedUsername, String personID,
                 float latitude, float longitude, String country,
                 String city, String eventType, int year)
    {
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    /**
     * An event constructor with the option to add the unique eventID as well as the other standard values.
     * @param eventID the unique event ID
     * @param associatedUsername the username associated with this event
     * @param personID the unique personID for the person to which this event corresponds
     * @param latitude the latitude of the event location
     * @param longitude the longitude of the event location
     * @param country the country in which the event happened
     * @param city the city in which the event happened
     * @param eventType the type of event
     * @param year the year the event happened
     */
    public Event(String eventID, String associatedUsername, String personID,
                 float latitude, float longitude, String country,
                 String city, String eventType, int year)
    {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }
    /**
     * Confirms if the event object has all necessary values
     * @return true or false;
     */
    public boolean isComplete()
    {
        return ((!Objects.equals(associatedUsername, null))
                && (!Objects.equals(eventID, null))
                && (!Objects.equals(eventType, null))
                && (!Objects.equals(longitude, 0.0f))
                && (!Objects.equals(latitude, 0.0f))
                && (!Objects.equals(country, null))
                && (!Objects.equals(city, null))
                && (!Objects.equals(year, 0))
                && (!Objects.equals(personID, null)));
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }
    public void setAssociatedUsername(String username) { this.associatedUsername = username; }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
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

    public int getYear() {
        return year;
    }

    /**
     * Checks if an object is equal in value to the current Event object
     * @param obj the object in question
     * @return true or false
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Event event = (Event) obj;
        return (Objects.equals(eventID, event.eventID)
                && Objects.equals(associatedUsername, event.associatedUsername)
                && Objects.equals(personID, event.personID)
                && Objects.equals(latitude, event.latitude)
                && Objects.equals(longitude, event.longitude)
                && Objects.equals(country, event.country)
                && Objects.equals(city, event.city)
                && Objects.equals(eventType, event.eventType)
                && Objects.equals(year, event.year));
    }


}
