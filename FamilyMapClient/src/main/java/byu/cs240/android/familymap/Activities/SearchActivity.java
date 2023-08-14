package byu.cs240.android.familymap.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;
import Model.Event;
import Model.Person;
import byu.cs240.android.familymap.DataCache;
import byu.cs240.android.familymap.R;

public class SearchActivity extends AppCompatActivity
{
    private static final int PERSON_ITEM_VIEW_TYPE = 0;
    private static final int EVENT_ITEM_VIEW_TYPE = 1;
    private final DataCache dataCache = DataCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        SearchView searchView = findViewById(R.id.search_view);
        RecyclerView recyclerView = findViewById(R.id.search_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        ArrayList<Person> people = new ArrayList<>();
        ArrayList<Event> events = new ArrayList<>();
        SearchAdapter adapter = new SearchAdapter(people, events);
        recyclerView.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                dataCache.setSearchResults(newText);
                people.clear();
                people.addAll(dataCache.searchPeople);
                events.clear();
                events.addAll(dataCache.searchEvents);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

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

    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private final TextView name;
        private final TextView eventInfo;
        private final ImageView icon;
        private final int viewType;
        private Person person;
        private Event event;

        public SearchViewHolder(@NonNull View view, int viewType)
        {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if (viewType == PERSON_ITEM_VIEW_TYPE)
            {
                name = itemView.findViewById(R.id.person_name);
                icon = itemView.findViewById(R.id.person_icon);
                eventInfo = null;
            }
            else
            {
                name = itemView.findViewById(R.id.associated_person);
                icon = itemView.findViewById(R.id.event_icon);
                eventInfo = itemView.findViewById(R.id.event_info);
            }
        }

        private void bind(Person person)
        {
            this.person = person;
            String fullName = person.getFirstName() + " " + person.getLastName();
            name.setText(fullName);
            if (person.getGender().equals("f"))
            {
                Drawable genderIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_female)
                        .colorRes(R.color.female_icon)
                        .sizeDp(40);
                icon.setImageDrawable(genderIcon);
            }
            else
            {
                Drawable genderIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_male)
                        .colorRes(R.color.male_icon)
                        .sizeDp(40);
                icon.setImageDrawable(genderIcon);
            }
        }

        private void bind(Event event)
        {
            this.event = event;
            String info = event.getEventType().toUpperCase() + ": "
                    + event.getCity() + ", "
                    + event.getCountry() + "("
                    + event.getYear() + ")";
            eventInfo.setText(info);
            Person person = dataCache.peopleByID.get(event.getPersonID());
            String fullName = null;
            if (person != null)
            {
                fullName = person.getFirstName() + " " + person.getLastName();
            }
            name.setText(fullName);
            Drawable mapMarkerIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker)
                    .colorRes(R.color.black)
                    .sizeDp(40);
            icon.setImageDrawable(mapMarkerIcon);
        }

        @Override
        public void onClick(View v)
        {
            if (viewType == PERSON_ITEM_VIEW_TYPE)
            {
                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                intent.putExtra("PersonID", person.getPersonID());
                startActivity(intent);
            }
            else if (viewType == EVENT_ITEM_VIEW_TYPE)
            {
                Intent intent = new Intent(SearchActivity.this, EventActivity.class);
                intent.putExtra("EventID", event.getEventID());
                intent.putExtra("PersonID", person.getPersonID());
                startActivity(intent);
            }
        }
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder>
    {
        private final List<Person> people;
        private final List<Event> events;

        SearchAdapter(List<Person> people, List<Event> events)
        {
            this.people = people;
            this.events = events;
        }

        @Override
        public int getItemViewType(int position)
        {
            return position < people.size() ? PERSON_ITEM_VIEW_TYPE : EVENT_ITEM_VIEW_TYPE;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view;
            if(viewType == PERSON_ITEM_VIEW_TYPE)
            {
                view = getLayoutInflater().inflate(R.layout.person_item2, parent, false);
            }
            else
            {
                view = getLayoutInflater().inflate(R.layout.event_item, parent, false);
            }
            return new SearchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position)
        {
            if (position < people.size())
            {
                holder.bind(people.get(position));
            }
            else
            {
                holder.bind(events.get(position - people.size()));
            }
        }

        @Override
        public int getItemCount() {
            return people.size() + events.size();
        }

    }
}