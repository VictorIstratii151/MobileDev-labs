package com.example.vic.pamlab5;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class BottomNavigation extends AppCompatActivity
{
    private BottomNavigationView bottomNavigationView;
    private List<Fragment> fragments = new ArrayList(3);

    private static final String TAG_FRAGMENT_HOME = "tag_frag_home";
    private static final String TAG_FRAGMENT_NOTIFICATIONS = "tag_frag_notifications";
    private static final String TAG_FRAGMENT_REQUEST = "tag_frag_request";
    private static final String TAG_FRAGMENT_SCHEDULE = "tag_frag_schedule";
    private static final String TAG_FRAGMENT_PROFILE = "tag_frag_profile";

    private void buildFragmentsList()
    {
        HomeFragment homeFragment = new HomeFragment();
        NotificationsFragment notificationsFragment = new NotificationsFragment();
        NewRequestFragment newRequestFragment = new NewRequestFragment();
        ScheduleFragment scheduleFragment = new ScheduleFragment();
        ProfileFragment profileFragment = new ProfileFragment();

        homeFragment = buildFragmentGeneric(homeFragment, "Home");
        notificationsFragment = buildFragmentGeneric(notificationsFragment, "Notifications");
        newRequestFragment = buildFragmentGeneric(newRequestFragment, "New Request");
        scheduleFragment = buildFragmentGeneric(scheduleFragment, "Schedule");
        profileFragment = buildFragmentGeneric(profileFragment, "Profile");

        fragments.add(homeFragment);
        fragments.add(notificationsFragment);
        fragments.add(newRequestFragment);
        fragments.add(scheduleFragment);
        fragments.add(profileFragment);
    }

    private <T extends ReturnTitle> T buildFragmentGeneric(T fragment, String title)
    {
        Bundle bundle = new Bundle();
        bundle.putString(fragment.Title(), title);
        fragment.setArguments(bundle);

        return fragment;
    }

    private void switchFragment(int pos, String tag)
    {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragmentholder, fragments.get(pos), tag)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item)
                    {
                        switch (item.getItemId())
                        {
                            case R.id.bottombaritem_home:
                                switchFragment(0, TAG_FRAGMENT_HOME);
                                return true;
                            case R.id.bottombaritem_notifications:
                                switchFragment(1, TAG_FRAGMENT_NOTIFICATIONS);
                                return true;
                            case R.id.bottombaritem_add_request:
                                switchFragment(2, TAG_FRAGMENT_REQUEST);
                                return true;
                            case R.id.bottombaritem_schedule:
                                switchFragment(3, TAG_FRAGMENT_SCHEDULE);
                                return true;
                            case R.id.bottombaritem_profile:
                                switchFragment(4, TAG_FRAGMENT_PROFILE);
                                return true;
                        }
                        return false;
                    }
                });
        buildFragmentsList();
        switchFragment(0, TAG_FRAGMENT_HOME);
    }
}
