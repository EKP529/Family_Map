package Service;

import DataAccess.*;

/**
 * The base class for the Clear, Fill, Load, Login, and Register byu.cs240.android.familymapserver.DataAccess.Service classes.
 */
public abstract class Service
{
    /**
     * the database object which hold the connection to the Family Map byu.cs240.android.familymapserver.Server database
     */
    private static final Database db = new Database();
    /**
     * the User Data Access object to perform user-related actions on the database
     */
    public static UserDAO myUserDAO;
    /**
     * the Person Data Access object to perform person-related actions on the database
     */
    public static PersonDAO myPersonDAO;
    /**
     * the Event Data Access object to perform event-related actions on the database
     */
    public static EventDAO myEventDAO;
    /**
     * the Authentication Token Data Access object to perform authentication token-related actions on the database
     */
    public static AuthTokenDAO myAuthTokenDAO;
    /**
     * Provides the connection for the Data Access Objects for the children classes to utilise,
     * as well as the Data Access Objects themselves.
     */
    public static void setConnection() throws DataAccessException
    {
        db.setConnection();
        myUserDAO = new UserDAO(db.getConnection());
        myPersonDAO = new PersonDAO(db.getConnection());
        myEventDAO = new EventDAO(db.getConnection());
        myAuthTokenDAO = new AuthTokenDAO(db.getConnection());
    }
    /**
     * Closes the connection to the Family Map byu.cs240.android.familymapserver.Server database utilized by the Data Access Objects
     * @param commit true or false; whether to keep or discard the changes made to the Family Map byu.cs240.android.familymapserver.Server using the connection
     * @throws DataAccessException if SQL error occurs
     */
    public static void closeConnection(boolean commit) throws DataAccessException
    {
        db.closeConnection(commit);
    }

    public static boolean checkConnection() { return db.checkConnection(); }
}
