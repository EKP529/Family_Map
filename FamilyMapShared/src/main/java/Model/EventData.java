package Model;

import java.util.Random;

/**
 * a class to hold the possible locations for event generation in the Family Map
 * @see Location
 */
public class EventData
{
    /**
     * an array of locations for event generation in the Family Map
     */
    private Location[] data;
    public Location getLocation()
    {
        return data[new Random().nextInt(data.length)];
    }
    public int getYear(int upper, int lower)
    {
        return new Random().nextInt(upper - lower) + lower;
    }

}
