package byu.cs240.android.familymap.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import byu.cs240.android.familymap.DataCache;
import byu.cs240.android.familymap.R;

public class SettingsActivity extends AppCompatActivity
{
    private SwitchCompat lifeStoryLinesSwitch;
    private SwitchCompat familyTreeLinesSwitch;
    private SwitchCompat spouseLinesSwitch;
    private SwitchCompat fatherSideSwitch;
    private SwitchCompat motherSideSwitch;
    private SwitchCompat maleEventsSwitch;
    private SwitchCompat femaleEventsSwitch;
    private View logoutView;
    private final DataCache dataCache = DataCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        lifeStoryLinesSwitch = findViewById(R.id.life_story_lines);
        lifeStoryLinesSwitch.setChecked(dataCache.settings.isShowLifeStoryLines());
        familyTreeLinesSwitch = findViewById(R.id.family_tree_lines);
        familyTreeLinesSwitch.setChecked(dataCache.settings.isShowFamilyTreeLines());
        spouseLinesSwitch = findViewById(R.id.spouse_lines);
        spouseLinesSwitch.setChecked(dataCache.settings.isShowSpouseLines());
        fatherSideSwitch = findViewById(R.id.fathers_side);
        fatherSideSwitch.setChecked(dataCache.settings.isShowFatherSide());
        motherSideSwitch = findViewById(R.id.mothers_side);
        motherSideSwitch.setChecked(dataCache.settings.isShowMotherSide());
        maleEventsSwitch = findViewById(R.id.male_events);
        maleEventsSwitch.setChecked(dataCache.settings.isShowMaleEvents());
        femaleEventsSwitch = findViewById(R.id.female_events);
        femaleEventsSwitch.setChecked(dataCache.settings.isShowFemaleEvents());
        logoutView = findViewById(R.id.logout_option);
        logoutView.setClickable(true);
        logoutView.setOnClickListener(v ->
        {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }
}