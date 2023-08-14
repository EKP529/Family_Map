package DataAccess;

import Model.Event;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * a class to access the Family Map byu.cs240.android.familymapserver.Server database for Event information
 */
public class EventDAO
{
    /**
     * the connection to the Family Map byu.cs240.android.familymapserver.Server database
     */
    private final Connection connection;
    /**
     * This creates the EventDao object with the connection to the Family Map byu.cs240.android.familymapserver.Server database already established.
     */
    public EventDAO(Connection conn) { connection = conn; }
    /**
     * Creates a new event in the Family Map byu.cs240.android.familymapserver.Server database.
     * @param event the event to be added
     * @throws DataAccessException if SQL error occurs
     */
    public void insertEvent(Event event) throws DataAccessException
    {
        String sql = "insert into event (eventID, associatedUsername, personID, latitude, longitude, " +
                "country, city, eventType, year) values(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getAssociatedUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setFloat(4, event.getLatitude());
            stmt.setFloat(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Error encountered while inserting an event");
        }
    }
    /**
     * Gets all event data from the Family Map byu.cs240.android.familymapserver.Server database for a certain event.
     * @param evID the unique eventID for the desired event
     * @throws DataAccessException if SQL error occurs
     * @return the event with all of its data
     */
    public Event getEvent(String evID) throws DataAccessException
    {
        String sql = "select * from event where eventID = ?";
        Event event;
        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, evID);
            try(ResultSet rs = stmt.executeQuery())
            {
                rs.next();
                String eventID = rs.getString(1);
                String username = rs.getString(2);
                String personID = rs.getString(3);
                float latitude = rs.getFloat(4);
                float longitude = rs.getFloat(5);
                String country = rs.getString(6);
                String city = rs.getString(7);
                String eventType = rs.getString(8);
                int year = rs.getInt(9);
                event = new Event(eventID, username, personID, latitude, longitude, country, city, eventType, year);
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Error encountered while finding an event");
        }
        return event;
    }
    /**
     * Confirms whether an event exists in the database.
     * @param eventID the unique eventID of the event in question
     * @return true or false
     */
    public boolean eventExists(String eventID)
    {
        String sql = "select eventID from event where eventID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, eventID);
            try (ResultSet rs = stmt.executeQuery())
            {
                rs.next();
                rs.getString(1);
                return true;
            }
        }
        catch (SQLException ex) { return false; }
    }
    /**
     * Gets all event correlating with a give user
     * @param username the unique personID of the given user
     * @return an array of all the events
     * @throws DataAccessException if SQL error occurs
     */
    public Event[] getAllEvents(String username) throws DataAccessException
    {
        ArrayList<Event> events = new ArrayList<>();
        String sql = "select * from event where associatedUsername = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                String eventID = rs.getString(1);
                String associatedUsername = rs.getString(2);
                String personID = rs.getString(3);
                float latitude = rs.getFloat(4);
                float longitude = rs.getFloat(5);
                String country = rs.getString(6);
                String city = rs.getString(7);
                String eventType = rs.getString(8);
                int year = rs.getInt(9);
                Event event = new Event(eventID, associatedUsername, personID, latitude, longitude,
                        country, city, eventType, year);
                events.add(event);
            }
            if (events.size() == 0)
            {
                throw new DataAccessException("no events in database");
            }
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("Error encountered while getting events");
        }
        return events.toArray(new Event[0]);
    }
    /**
     * Deletes certain events from the Family Map byu.cs240.android.familymapserver.Server database.
     * @param username the associated username for the desired events
     */
    public int deleteEvents(String username) throws DataAccessException
    {
        String sql = "delete from event where associatedUsername = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, username);
            return stmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("Error encountered while deleting all person data");
        }
    }
    /**
     * Clears all event data from the Family Map byu.cs240.android.familymapserver.Server database.
     * @throws DataAccessException if SQL error occurs
     */
    public int clearEventData() throws DataAccessException
    {
        String sql = "delete from event";
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            return stmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("Error encountered while deleting all person data");
        }
    }

}
