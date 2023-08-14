package byu.cs240.android.familymap.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;

import Model.Event;
import Model.Person;
import byu.cs240.android.familymap.DataCache;
import byu.cs240.android.familymap.R;

public class PersonActivity extends AppCompatActivity
{
    private final DataCache dataCache = DataCache.getInstance();
    private final String TAG = "PersonActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ExpandableListView expandableListView = findViewById(R.id.person_expandable_list_view);
        String personID = getIntent().getStringExtra("PersonID");
        Log.d(TAG, "ExpandableListView set");
        Person person = dataCache.peopleByID.get(personID);
        TextView firstNameView = findViewById(R.id.person_first_name);
        TextView lastNameView = findViewById(R.id.person_last_name);
        TextView genderView = findViewById(R.id.person_gender);
        if (person != null)
        {
            firstNameView.setText(person.getFirstName());
            firstNameView.setTextSize(20);
            lastNameView.setText(person.getLastName());
            lastNameView.setTextSize(20);
        }
        if ((person != null) && (person.getGender().equals("f"))) { genderView.setText(R.string.female_label); }
        else { genderView.setText(R.string.male_label); }

        genderView.setTextSize(20);

        Person[] people = dataCache.personFamily.get(personID);
        dataCache.setPersonEvents();
        List<Event> events = dataCache.personEvents.get(personID);
        ExpandableListAdapter adapter = new ExpandableListAdapter(events, people);
        expandableListView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter
    {
        private static final int EVENT_GROUP_POSITION = 0;
        private static final int PERSON_GROUP_POSITION = 1;

        private final List<Event> events;
        private final Person[] people;

        ExpandableListAdapter(List<Event> events, Person[] people)
        {
            this.events = events;
            this.people = people;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition)
        {
            switch (groupPosition)
            {
                case EVENT_GROUP_POSITION:
                    return events.size();
                case PERSON_GROUP_POSITION:
                    return people.length;
                default:
                    return 0;//throw new IllegalAccessException("Bad group position");
            }
        }

        @Override
        public Object getGroup(int groupPosition)
        {
            switch (groupPosition)
            {
                case EVENT_GROUP_POSITION:
                    return getString(R.string.event_group_label);
                case PERSON_GROUP_POSITION:
                    return getString(R.string.person_group_label);
                default:
                    return "SOMETHING'S WRONG"; ////throw new IllegalAccessException("Bad group position");
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition)
        {
            switch (groupPosition)
            {
                case EVENT_GROUP_POSITION:
                    return events.get(childPosition);
                case PERSON_GROUP_POSITION:
                    return people[childPosition];
                default:
                    return "SOMETHING'S WRONG"; ////throw new IllegalAccessException("Bad group position");
            }
        }

        @Override
        public long getGroupId(int groupPosition) { return groupPosition; }

        @Override
        public long getChildId(int groupPosition, int childPosition) { return childPosition; }

        @Override
        public boolean hasStableIds() { return false; }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }
            TextView titleView = convertView.findViewById(R.id.list_title);
            switch (groupPosition)
            {
                case EVENT_GROUP_POSITION:
                    titleView.setText(R.string.event_group_label);
                    break;
                case PERSON_GROUP_POSITION:
                    titleView.setText(R.string.person_group_label);
                    break;
                default:
                     ////throw new IllegalAccessException("Bad group position");
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
        {
            View itemView = null;
            switch (groupPosition)
            {
                case EVENT_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.event_item, parent, false);
                    initializeEventView(itemView, childPosition);
                    break;
                case PERSON_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.person_item2, parent, false);
                    initializePersonView(itemView, childPosition);
                    break;
                default:
                    ////throw new IllegalAccessException("Bad group position");
                    break;
            }
            return itemView;
        }

        private void initializePersonView(View personItemView, int childPosition)
        {
            TextView personNameView = personItemView.findViewById(R.id.person_name);
            TextView familyRoleView = personItemView.findViewById(R.id.family_role);
            ImageView genderIconView = personItemView.findViewById(R.id.person_icon);
            Person person = people[childPosition];
            if (person != null)
            {
                String fullName = person.getFirstName()
                        + " "
                        + person.getLastName();
                personNameView.setText(fullName);
                switch(childPosition)
                {
                    case 0:
                        familyRoleView.setText(R.string.father);
                        break;
                    case 1:
                        familyRoleView.setText(R.string.mother);
                        break;
                    case 2:
                        familyRoleView.setText(R.string.spouse);
                        break;
                    case 3:
                        familyRoleView.setText(R.string.child);
                        break;
                    default:
                        familyRoleView.setText(R.string.error);
                        break;
                }
                if (person.getGender().equals("f"))
                {
                    Drawable genderIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_female)
                            .colorRes(R.color.female_icon)
                            .sizeDp(40);
                    genderIconView.setImageDrawable(genderIcon);
                }
                else
                {
                    Drawable genderIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male)
                            .colorRes(R.color.male_icon)
                            .sizeDp(40);
                    genderIconView.setImageDrawable(genderIcon);
                }
                personItemView.setOnClickListener(v ->
                {
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    intent.putExtra("PersonID", person.getPersonID());
                    startActivity(intent);
                });
            }
        }

        private void initializeEventView(View eventItemView, int childPosition)
        {
            TextView eventInfoView = eventItemView.findViewById(R.id.event_info);
            TextView eventAssociatedPersonView = eventItemView.findViewById(R.id.associated_person);
            ImageView eventIconView = eventItemView.findViewById(R.id.event_icon);
            Event event = events.get(childPosition);
            String info = event.getEventType().toUpperCase() + ": "
                    + event.getCity() + ", "
                    + event.getCountry() + "("
                    + event.getYear() + ")";
            eventInfoView.setText(info);
            Person person = dataCache.peopleByID.get(event.getPersonID());
            String fullName = null;
            if (person != null)
            {
                fullName = person.getFirstName() + " " + person.getLastName();
            }
            eventAssociatedPersonView.setText(fullName);
            Drawable mapMarkerIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker)
                    .colorRes(R.color.black)
                    .sizeDp(40);
            eventIconView.setImageDrawable(mapMarkerIcon);
            eventItemView.setOnClickListener(v ->
            {
                Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                intent.putExtra("EventID", event.getEventID());
                if (person != null)
                {
                    intent.putExtra("PersonID", person.getPersonID());
                }
                startActivity(intent);
            });
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}