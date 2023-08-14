package DataAccess;

import Model.AuthToken;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * a class to access the Family Map byu.cs240.android.familymapserver.Server database for Authentication Token information
 */
public class AuthTokenDAO
{
    /**
     * the connection to the Family Map byu.cs240.android.familymapserver.Server database
     */
    private final Connection connection;
    /**
     * This creates the AuthTokenDao object with the connection to the Family Map byu.cs240.android.familymapserver.Server database already established.
     */
    public AuthTokenDAO(Connection conn) { connection = conn; }
    /**
     * Creates a new authentication token for a user in the Family Map byu.cs240.android.familymapserver.Server database.
     * @param authToken the authentication token object to be added to the database
     * @throws DataAccessException if SQL Error occurs
     */
    public void insertAuthToken(AuthToken authToken) throws DataAccessException
    {
        String sql = "insert into authToken (authToken, username) values (?, ?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, authToken.getAuthToken());
            stmt.setString(2, authToken.getUsername());
            stmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("Error encountered while inserting an authentication token");
        }
    }
    /**
     * Gets all Authentication token data from the Family Map byu.cs240.android.familymapserver.Server database for a given user
     * @param input either a personID or username for a given user
     * @return the authentication token object for the given user
     * @throws DataAccessException if SQL error occurs
     */
    public AuthToken getAuthToken(String input) throws DataAccessException
    {
        String sql = "select * from authToken where username = ? or authToken = ?";
        AuthToken aToken;
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, input);
            stmt.setString(2, input);
            try (ResultSet rs = stmt.executeQuery())
            {
                rs.next();
                String authToken = rs.getString(1);
                String username = rs.getString(2);
                aToken = new AuthToken(authToken, username);
            }
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("Error encountered while getting an authentication token");
        }
        return aToken;
    }
    /**
     * Confirms if an authentication token exists in the Family Map byu.cs240.android.familymapserver.Server database.
     * @param authToken the authentication token in question.
     * @return true or false
     */
    public boolean authTokenExists(String authToken)
    {
        String sql = "select authToken from authToken where authToken = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, authToken);
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
     * Clears all authentication token data from the Family Map byu.cs240.android.familymapserver.Server database.
     * @throws DataAccessException if SQL error occurs
     */
    public int clearAuthTokenData() throws DataAccessException
    {
        String sql = "delete from authToken";
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            return stmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("Error encountered while deleting all authentication token data");
        }
    }
}
