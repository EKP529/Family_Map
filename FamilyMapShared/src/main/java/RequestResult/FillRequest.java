package RequestResult;

/**
 * The class to hold the request from the Family Map Client
 * to the Family Map Server from the fill web API.
 */
public class FillRequest
{
    /**
     * the username for the request from the Fill API for the Family Map Client
     */
    String username;
    /**
     * the number of generations for the request from the Fill API for the Family Map Client
     */
    int generations = 4;
    /**
     * Constructs FillRequest object to hold the data from the request URI from the Fill API for the Family Map Client.
     * @param username username for the Fill request
     * @param generations the amount of generations for the Fill request
     */
    public FillRequest(String username, int generations)
    {
        this.username = username;
        this.generations = generations;
    }
    /**
     * Constructs standard FillRequest object when only the username is given
     * in the request URI from the Fill API for the Family Map Client.
     * @param username username for the Fill request
     */
    public FillRequest(String username) { this.username = username; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGenerations() {
        return generations;
    }
}
