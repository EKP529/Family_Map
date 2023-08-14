package DataAccess;

import Model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * a class to access the Family Map byu.cs240.android.familymapserver.Server database for Person information
 */
public class PersonDAO
{
    /**
     * the connection to the Family Map byu.cs240.android.familymapserver.Server database
     */
    private final Connection connection;
    /**
     * This creates the PersonDao object with the connection to the Family Map byu.cs240.android.familymapserver.Server database already established.
     */
    public PersonDAO(Connection conn) { connection = conn; }

    /**
     * Creates a new person in the Family Map byu.cs240.android.familymapserver.Server database.
     * @param person the person data to be added to the database
     * @throws DataAccessException if SQL error occurs
     */
    public void insertPerson(Person person) throws DataAccessException
    {
        String sql = "insert into person (personID, associatedUsername, firstName, lastName, gender, " +
                "fatherID, motherID, spouseID) values (?, ?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());
            stmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("Error encountered while inserting a person");
        }
    }
    /**
     * Gets all the data for a person from the Family Map byu.cs240.android.familymapserver.Server database based on the given personID.
     * @param personID the unique personID
     * @return the person associated with the personID
     * @throws DataAccessException if SQL error occurs
     */
    public Person getPerson(String personID) throws DataAccessException
    {
        String sql = "select associatedUsername, firstName, lastName, gender, " +
                "fatherID, motherID, spouseID from person where personID = ?";
        Person person;
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, personID);
            try (ResultSet rs = stmt.executeQuery())
            {
                rs.next();
                String associatedUsername = rs.getString(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                String gender = rs.getString(4);
                String fatherID = rs.getString(5);
                String motherID = rs.getString(6);
                String spouseID = rs.getString(7);
                person = new Person(associatedUsername, firstName, lastName, gender,
                        fatherID, motherID, spouseID, personID);
            }
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("Error encountered while getting a person");
        }
        return person;
    }
    /**
     * Confirms whether a person exists in the Family Map byu.cs240.android.familymapserver.Server database.
     * @param personID the unique personID of the person in question
     * @return true or false
     */
    public boolean personExists(String personID)
    {
        String sql = "select associatedUsername from person where personID = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, personID);
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
     * Gets all persons related to a given user in the Family Map
     * @param username the associated username for each person
     * @return an array of related people
     * @throws DataAccessException if SQL error occurs
     */
    public Person[] getAllPeople(String username) throws DataAccessException
    {
        ArrayList<Person> persons = new ArrayList<>();
        String sql = "select * from person where associatedUsername = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                String personID = rs.getString(1);
                String associatedUsername = rs.getString(2);
                String firstName = rs.getString(3);
                String lastName = rs.getString(4);
                String gender = rs.getString(5);
                String fatherID = rs.getString(6);
                String motherID = rs.getString(7);
                String spouseID = rs.getString(8);
                Person person = new Person(associatedUsername, firstName, lastName, gender,
                        fatherID, motherID, spouseID, personID);
                persons.add(person);
            }
            if (persons.size() == 0)
            {
                throw new DataAccessException("no persons in database");
            }
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("Error encountered while getting persons");
        }
        return persons.toArray(new Person[0]);
    }
    public int deletePeople(String username) throws DataAccessException
    {
        String sql = "delete from person where associatedUsername = ?";
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
     * Clears all person data from the Family Map byu.cs240.android.familymapserver.Server database.
     * @throws DataAccessException if SQL error occurs
     */
    public int clearPersonData() throws DataAccessException
    {
        String sql = "delete from person";
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
