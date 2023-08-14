package DataAccess;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/**
 * The Database Access Object(Dao) which serves as the base for the
 * User, Person, Event, and AuthToken DAO classes.
 */
public class Database
{
    /**
     * the connection to the Family Map byu.cs240.android.familymapserver.Server database
     */
    private Connection connection;
    /**
     * This establishes the connection to the Family Map byu.cs240.android.familymapserver.Server database for the other Dao classes to utilize.
     * @throws DataAccessException if SQL error occurs
     */
    public void setConnection() throws DataAccessException
    {
        try
        {
            connection = DriverManager.getConnection("jdbc:sqlite:db" + File.separator + "FamilyMapServerDB.sqlite");
            connection.setAutoCommit(false);
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Error occurred while connecting to database");
        }
    }
    /**
     * checks if the connection to the Family Map byu.cs240.android.familymapserver.Server database is established
     * @return true or false; whether connection is established or not
     */
    public boolean checkConnection() { return !Objects.equals(connection, null);}
    /**
     * Closes the connection to the Family Map byu.cs240.android.familymapserver.Server database and either keeps or discards all changes
     * applied to the database.
     * @param commit true or false; whether to keep or discard the changes applied to the database before closing the connection
     * @throws DataAccessException if SQL error occurs
     */
    public void closeConnection(boolean commit) throws DataAccessException
    {
        try {
            if (commit)
            {
                connection.commit();
            } else
            {
                connection.rollback();
            }
            connection.close();
            connection = null;
        } catch (SQLException e)
        {
            throw new DataAccessException("Error occurred while disconnecting from database");
        }
    }
    public Connection getConnection() { return connection; }
}
