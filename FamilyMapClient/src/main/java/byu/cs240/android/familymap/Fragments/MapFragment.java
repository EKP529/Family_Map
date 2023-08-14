package byu.cs240.android.familymap.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import Model.Event;
import Model.Person;
import byu.cs240.android.familymap.Activities.PersonActivity;
import byu.cs240.android.familymap.Activities.SearchActivity;
import byu.cs240.android.familymap.Activities.SettingsActivity;
import byu.cs240.android.familymap.DataCache;
import byu.cs240.android.familymap.R;

public class MapFragment extends Fragment
{
    private GoogleMap familyMap;
    private TextView eventInfoView;

    private OnMapReadyCallback callback = new OnMapReadyCallback()
    {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap)
        {

            familyMap = googleMap;
            DataCache dataCache = DataCache.getInstance();
            for (int i = 0; i < dataCache.events.length; i++)
            {
                float markerColor;
                Event event = dataCache.events[i];
                switch (event.getEventType())
                {
                    case "birth":
                        markerColor = BitmapDescriptorFactory.HUE_AZURE;
                        break;
                    case "marriage":
                        markerColor = BitmapDescriptorFactory.HUE_MAGENTA;
                        break;
                    case "death":
                        markerColor = BitmapDescriptorFactory.HUE_CYAN;
                        break;
                    default:
                        markerColor = BitmapDescriptorFactory.HUE_GREEN + (i * 10);
                        break;
                }
                Marker marker = familyMap.addMarker(new MarkerOptions()
                       .position(new LatLng(event.getLatitude(), event.getLongitude()))
                       .icon(BitmapDescriptorFactory.defaultMarker(markerColor)));
                marker.setTag(event);
            }
            if ((getActivity() != null) && (getActivity().getIntent() != null))
            {
                String eventID = getActivity().getIntent().getStringExtra("EventID");
                if (eventID != null)
                {
                    String personID = getActivity().getIntent().getStringExtra("PersonID");
                    Event event = dataCache.eventsByID.get(eventID);
                    Person person = dataCache.peopleByID.get(personID);
                    if ((event != null) && (person != null))
                    {
                        LatLng location = new LatLng(event.getLatitude(), event.getLongitude());
                        familyMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                        String eventText = person.getFirstName() + " "
                                + person.getLastName() + "\n"
                                + event.getEventType().toUpperCase() + ": "
                                + event.getCity() + ", "
                                + event.getCountry() + "("
                                + event.getYear() + ")";
                        Drawable genderIcon;
                        if (person.getGender().equals("f"))
                        {
                            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female)
                                    .colorRes(R.color.female_icon)
                                    .sizeDp(40);
                            eventInfoView.setCompoundDrawables(genderIcon, null, null, null);
                            eventInfoView.setCompoundDrawablePadding(20);
                        }
                        else
                        {
                            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male)
                                    .colorRes(R.color.male_icon)
                                    .sizeDp(40);
                            eventInfoView.setCompoundDrawables(genderIcon, null, null, null);
                            eventInfoView.setCompoundDrawablePadding(20);
                        }
                        eventInfoView.setText(eventText);
                    }
                }
            }
            familyMap.setOnMarkerClickListener(marker ->
            {
                //familyMap.addPolyLine(...)
                Event event = (Event)marker.getTag();
                Person person = dataCache.peopleByID.get(event.getPersonID());
                LatLng location = new LatLng(event.getLatitude(), event.getLongitude());
                familyMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                String eventText = person.getFirstName() + " "
                        + person.getLastName() + "\n"
                        + event.getEventType().toUpperCase() + ": "
                        + event.getCity() + ", "
                        + event.getCountry() + "("
                        + event.getYear() + ")";
                Drawable genderIcon;
                if (person.getGender().equals("f"))
                {
                    genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female)
                            .colorRes(R.color.female_icon)
                            .sizeDp(40);
                    eventInfoView.setCompoundDrawables(genderIcon, null, null, null);
                    eventInfoView.setCompoundDrawablePadding(20);
                }
                else
                {
                    genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male)
                            .colorRes(R.color.male_icon)
                            .sizeDp(40);
                    eventInfoView.setCompoundDrawables(genderIcon, null, null, null);
                    eventInfoView.setCompoundDrawablePadding(20);
                }
                eventInfoView.setText(eventText);
                eventInfoView.setOnClickListener(v ->
                {
                    Intent intent = new Intent(getActivity(), PersonActivity.class);
                    intent.putExtra("PersonID", event.getPersonID());
                    startActivity(intent);
                });
                return true;
            });
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Iconify.with(new FontAwesomeModule());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        eventInfoView = view.findViewById(R.id.event_info);
        Drawable android = new IconDrawable(getActivity(), FontAwesomeIcons.fa_android)
                .colorRes(R.color.teal_200)
                .sizeDp(40);
        eventInfoView.setCompoundDrawables(android, null, null, null);
        eventInfoView.setCompoundDrawablePadding(20);
        eventInfoView.setClickable(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null)
        {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.search_menu_item);
        MenuItem settingsMenuItem = menu.findItem(R.id.settings_menu_item);
        Drawable searchIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_search)
                .colorRes(R.color.white)
                .actionBarSize();
        Drawable settingsIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_gear)
                .colorRes(R.color.white)
                .actionBarSize();
        searchMenuItem.setIcon(searchIcon);
        settingsMenuItem.setIcon(settingsIcon);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        Intent intent;
        switch(item.getItemId())
        {
            case R.id.search_menu_item:
                intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                return true;
            case R.id.settings_menu_item:
                intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }
}