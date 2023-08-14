package byu.cs240.android.familymap.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import byu.cs240.android.familymap.Fragments.LoginFragment;
import byu.cs240.android.familymap.Fragments.MapFragment;
import byu.cs240.android.familymap.R;

public class MainActivity extends AppCompatActivity implements LoginFragment.Listener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment == null)
        {
            Fragment fragment = createLoginFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
        else
        {
            if (currentFragment instanceof LoginFragment)
            {
                ((LoginFragment) currentFragment).registerListener(MainActivity.this);
            }
        }
    }

    private Fragment createLoginFragment()
    {
        LoginFragment fragment = new LoginFragment();
        fragment.registerListener(MainActivity.this);
        return fragment;
    }

    @Override
    public void notifySwitch()
    {
        Fragment fragment = new MapFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}