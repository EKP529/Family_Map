package DataAccess;

import Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A class to access the Family Map byu.cs240.android.familymapserver.Server database for User information.
 */
public class UserDAO
{
    /**
     * the connection to the Family Map byu.cs240.android.familymapserver.Server database
     */
    private final Connection connection;
    /**
     * This creates the UserDao object with the connection to the Family Map byu.cs240.android.familymapserver.Server database already established.
     */
    public UserDAO(Connection conn) { connection = conn; }
    /**
     * Inserts a user into the User table of the Family Map byu.cs240.android.familymapserver.Server database.
     * @param user the new user to be added to the Family Map byu.cs240.android.familymapserver.Server database.
     * @throws DataAccessException if an SQL error occurs.
     */
    public void insertUser(User user) throws DataAccessException
    {
        String sql = "insert into user (username, password, email, firstName, lastName, gender, personID) " +
                "values (?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());
            stmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("Error encountered while inserting a user");
        }
    }
    /**
     * Gets all information of a given user from the Family Map byu.cs240.android.familymapserver.Server database
     * using their username.
     * @param username the username for a user
     * @return the user correlating with the given username and password
     * @throws DataAccessException if SQL error occurs
     */
    public User getUser(String username, String password) throws DataAccessException
    {
        String sql = "select * from user where username = ? and password = ?";
        User user;
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try(ResultSet rs = stmt.executeQuery())
            {
                rs.next();
                String uName = rs.getString(1);
                String pWord = rs.getString(2);
                String email = rs.getString(3);
                String firstName = rs.getString(4);
                String lastName = rs.getString(5);
                String gender = rs.getString(6);
                String personID = rs.getString(7);
                user = new User(uName, pWord, email, firstName, lastName, gender, personID);
            }
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("Error encountered while getting a user");
        }
        return user;
    }
    public User getUser(String username) throws DataAccessException
    {
        String sql = "select * from user where username = ?";
        User user;
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, username);
            try(ResultSet rs = stmt.executeQuery())
            {
                rs.next();
                String uName = rs.getString(1);
                String password = rs.getString(2);
                String email = rs.getString(3);
                String firstName = rs.getString(4);
                String lastName = rs.getString(5);
                String gender = rs.getString(6);
                String personID = rs.getString(7);
                user = new User(uName, password, email, firstName, lastName, gender, personID);
            }
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("Error encountered while getting a user");
        }
        return user;
    }
    /**
     * Confirms whether a user exists in the Family Map byu.cs240.android.familymapserver.Server database.
     * @param username the username of the user in question
     * @return true or false
     */
    public boolean userExists(String username, String password)
    {
        String sql = "select username from user where username = ? and password = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                rs.getString(1);
                return true;
            }
        }
        catch (SQLException ex) { return false; }
    }
    public boolean userExists(String username)
    {
        String sql = "select username from user where username = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                rs.getString(1);
                return true;
            }
        }
        catch (SQLException ex) { return false; }
    }
    /**
     * Clears all user data from the Family Map byu.cs240.android.familymapserver.Server database.
     * @throws DataAccessException if SQL error occurs
     */
    public int clearUserData() throws DataAccessException
    {
        String sql = "delete from user";
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            return stmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("Error encountered while deleting all user data");
        }
    }
}
