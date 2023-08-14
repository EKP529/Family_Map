package RequestResult;

import Model.Event;
import Model.Person;
import Model.User;

/**
 * The class to hold the request from the Family Map Client
 * to the Family Map Server from the load web API.
 */
public class LoadRequest
{
    /**
     * the array of users from the request body of the Load API for the Family Map Client
     */
    private User[] users;
    /**
     * the array of persons from the request body of the Load API for the Family Map Client
     */
    private Person[] persons;
    /**
     * the array of events from the request body of the Load API for the Family Map Client
     */
    private Event[] events;
    /**
     * Constructs LoadRequest object to hold the arrays from the request from the Load API for the Family Map Client
     * @param users the array of users
     * @param persons the array of persons
     * @param events the array of events
     */
    public LoadRequest(User[] users, Person[] persons, Event[] events)
    {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public User[] getUsers() {
        return users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
}
