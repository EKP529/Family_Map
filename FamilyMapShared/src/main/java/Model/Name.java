package Model;

import java.util.Random;

/**
 * this class is used to store the possible names for person generation in the Family Map
 */
public class Name
{
    /**
     * an array of possible names for people in the Family Map
     */
    private String[] data;
    /**
     * an array of all possible male names for people in the Family Map
     */
    private String[] male;
    /**
     * an array of all possible female names for people in the Family Map
     */
    private String[] female;
    /**
     * an array of all possible surnames for people in the Family Map
     */
    private String[] sur;
    public String getMaleName() { return male[new Random().nextInt(male.length)]; }
    public String getFemaleName()
    {
        return female[new Random().nextInt(female.length)];
    }
    public String getSurname()
    {
        return sur[new Random().nextInt(sur.length)];
    }

    public String[] getData()
    {
        return data;
    }
    public void setMaleNames(String[] male) {
        this.male = male;
    }
    public void setFemaleNames(String[] female) {
        this.female = female;
    }
    public void setSurnames(String[] sur) {
        this.sur = sur;
    }
}
