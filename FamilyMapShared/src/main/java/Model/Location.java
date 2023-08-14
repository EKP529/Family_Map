package Model;

/**
 * a class to hold the data for a possible location for event generation in the Family Map
 */
public class Location
{
    /**
     * the country of the location
     */
    private final String country;
    /**
     * the city of the location
     */
    private final String city;
    /**
     * the latitude of the location
     */
    private final float latitude;
    /**
     * the longitude of the location
     */
    private final float longitude;

    /**
     * constructs a Location object for event generation in the Family Map
     * @param country the country of the location
     * @param city the city of the location
     * @param latitude the latitude of the location
     * @param longitude the longitude of the location
     */
    public Location(String country, String city, float latitude, float longitude)
    {
        this.country = country;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

}
