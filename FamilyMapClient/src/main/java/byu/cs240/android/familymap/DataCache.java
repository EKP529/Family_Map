package byu.cs240.android.familymap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Model.Event;
import Model.Person;

public class DataCache
{
    private static DataCache instance;

    public static DataCache getInstance()
    {
        if (instance == null)
        {
            instance = new DataCache();
        }
        return instance;
    }

    private DataCache() {}

    public String authenToken;
    public String username;
    public String personID;
    public Person[] people;
    public Event[] events;
    //String --> PersonID
    public Map<String, Person> peopleByID;
    //String --> EventID
    public Map<String, Event> eventsByID;
    //String --> PersonID
    public Map<String, List<Event>> personEvents;
    //String --> PersonID
    public Map<String, Person[]> personFamily;
    public Set<Event> maleEvents;
    public Set<Event> femaleEvents;
    public List<Person> searchPeople;
    public List<Event> searchEvents;

    public final Settings settings = new Settings();

    public void setGenderedEvents()
    {
        maleEvents = new HashSet<>();
        femaleEvents = new HashSet<>();
        for (Event event : events)
        {
            String personID = event.getPersonID();
            Person person = peopleByID.get(personID);
            if ((person != null) && (person.getGender().equals("m")))
            {
                maleEvents.add(event);
            }
            else
            {
                femaleEvents.add(event);
            }
        }
    }

    public void setPeopleByID()
    {
        peopleByID = new HashMap<>();
        for (Person person : people)
        {
            peopleByID.put(person.getPersonID(), person);
        }
    }

    public void setEventsByID()
    {
        eventsByID = new HashMap<>();
        for (Event event : events)
        {
            eventsByID.put(event.getEventID(), event);
        }
    }

    public void setPersonEvents()
    {
        personEvents = new HashMap<>();
        for (Person person : people)
        {
            String key = person.getPersonID();
            ArrayList<Event> associatedEvents = new ArrayList<>();
            for (Event event : events)
            {
                if (event.getPersonID().equals(key))
                {
                    associatedEvents.add(event);
                }
            }
            personEvents.put(key, associatedEvents);
        }
    }

    public void setPersonFamily()
    {
        personFamily = new HashMap<>();
        for (Person person : people)
        {
            String key = person.getPersonID();
            String fatherID = person.getFatherID();
            String motherID = person.getMotherID();
            String spouseID = person.getSpouseID();
            Person[] family = new Person[4];
            for (Person person2 : people)
            {
                if (person2.getPersonID().equals(fatherID))
                {
                    family[0] = person2;
                }
                else if (person2.getPersonID().equals(motherID))
                {
                    family[1] = person2;
                }
                else if (person2.getPersonID().equals(spouseID))
                {
                    family[2] = person2;
                }
                else if (person2.getFatherID() != null)
                {
                    if (person2.getFatherID().equals(key) || person2.getMotherID().equals(key))
                    {
                        family[3] = person2;
                    }
                }
            }
            personFamily.put(key, family);
        }
    }

    public void setSearchResults(String search)
    {
        searchPeople = new ArrayList<>();
        searchEvents = new ArrayList<>();
        for (Person person : people)
        {
            if (checkPerson(search, person))
            {
                searchPeople.add(person);
            }
        }
        for (Event event : events)
        {
            if (checkEvent(search, event))
            {
                searchEvents.add(event);
            }
        }
    }

    private boolean checkPerson(String search, Person person)
    {
        search = search.toLowerCase();
        if (person.getFirstName().toLowerCase().contains(search)) { return true; }
        else { return person.getLastName().toLowerCase().contains(search); }
    }

    private boolean checkEvent(String search, Event event)
    {
        search = search.toLowerCase();
        if (event.getCountry().toLowerCase().contains(search)) { return true; }
        else if (event.getCity().toLowerCase().contains(search)) { return true; }
        else if (event.getEventType().toLowerCase().contains(search)) { return true; }
        else { return Integer.toString(event.getYear()).toLowerCase().contains(search); }
    }

    public static class Settings
    {
        private boolean showLifeStoryLines = true;
        private boolean showFamilyTreeLines = true;
        private boolean showSpouseLines = true;
        private boolean showFatherSide = true;
        private boolean showMotherSide = true;
        private boolean showMaleEvents = true;
        private boolean showFemaleEvents = true;

        public boolean isShowLifeStoryLines() {
            return showLifeStoryLines;
        }

        public void setShowLifeStoryLines(boolean showLifeStoryLines) {
            this.showLifeStoryLines = showLifeStoryLines;
        }

        public boolean isShowFamilyTreeLines() {
            return showFamilyTreeLines;
        }

        public void setShowFamilyTreeLines(boolean showFamilyTreeLines) {
            this.showFamilyTreeLines = showFamilyTreeLines;
        }

        public boolean isShowSpouseLines() {
            return showSpouseLines;
        }

        public void setShowSpouseLines(boolean showSpouseLines) {
            this.showSpouseLines = showSpouseLines;
        }

        public boolean isShowFatherSide() {
            return showFatherSide;
        }

        public void setShowFatherSide(boolean showFatherSide) {
            this.showFatherSide = showFatherSide;
        }

        public boolean isShowMotherSide() {
            return showMotherSide;
        }

        public void setShowMotherSide(boolean showMotherSide) {
            this.showMotherSide = showMotherSide;
        }

        public boolean isShowMaleEvents() {
            return showMaleEvents;
        }

        public void setShowMaleEvents(boolean showMaleEvents) {
            this.showMaleEvents = showMaleEvents;
        }

        public boolean isShowFemaleEvents() {
            return showFemaleEvents;
        }

        public void setShowFemaleEvents(boolean showFemaleEvents) {
            this.showFemaleEvents = showFemaleEvents;
        }
    }
}
