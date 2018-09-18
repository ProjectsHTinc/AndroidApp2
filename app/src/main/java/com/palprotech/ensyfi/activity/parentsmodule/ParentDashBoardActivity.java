package com.palprotech.ensyfi.activity.parentsmodule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.palprotech.ensyfi.R;
import com.palprotech.ensyfi.activity.general.CircularActivity;
import com.palprotech.ensyfi.activity.general.EventsActivity;
import com.palprotech.ensyfi.activity.general.LeaveCalendarActivity;
import com.palprotech.ensyfi.activity.general.OnDutyActivity;
import com.palprotech.ensyfi.activity.loginmodule.ChangePasswordActivity;
import com.palprotech.ensyfi.activity.loginmodule.ProfileActivity;
import com.palprotech.ensyfi.activity.loginmodule.SplashScreenActivity;
import com.palprotech.ensyfi.activity.studentmodule.AttendanceActivity;
import com.palprotech.ensyfi.activity.studentmodule.ClassTestHomeworkActivity;
import com.palprotech.ensyfi.activity.studentmodule.ExamsResultActivity;
import com.palprotech.ensyfi.activity.studentmodule.StudentInfoActivity;
import com.palprotech.ensyfi.activity.studentmodule.StudentTimeTableActivity;
import com.palprotech.ensyfi.adapter.NavDrawerAdapter;
import com.palprotech.ensyfi.bean.general.support.DeleteTableRecords;
import com.palprotech.ensyfi.interfaces.DialogClickListener;
import com.palprotech.ensyfi.utils.PreferenceStorage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ParentDashBoardActivity extends AppCompatActivity implements DialogClickListener {

    private static final String TAG = ParentDashBoardActivity.class.getName();
    Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView navDrawerList;
    boolean doubleBackToExitPressedOnce = false;
    private ImageView imgNavProfileImage;
    private ArrayAdapter<String> navListAdapter;
    private String[] values = {"Profile", "Attendance", "Class Test & Homework", "Exam & Result", "Time Table", "Events", "Circular", "Student Info", "On Duty", "Settings", "Sign Out"};
    TextView navUserProfileName = null;
    LinearLayout dashAttendance, dashTimeTable, dashClassTest, dashExam, dashEvent, dashCommunication;
    private String mCurrentUserProfileUrl = "";
    Context context;
    private DeleteTableRecords deleteTableRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dash_board);
        String userTypeString = PreferenceStorage.getUserType(getApplicationContext());
        int userType = Integer.parseInt(userTypeString);
        if (userType == 3) {
            setTitle("ENSYFI - Students");
        } else if (userType == 4) {
            setTitle("ENSYFI - Parents");
        }
        toolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.new_navi);

        initializeNavigationDrawer();
        initializeViews();
        context = getApplicationContext();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("LandingonPause", "LandingonPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("LandingonResume", "LandingonResume");
    }

    private void initializeViews() {
        Log.d(TAG, "initializin the views");
        Log.d(TAG, "initializing view pager");
        navUserProfileName = (TextView) findViewById(R.id.user_profile_name);

        dashAttendance = (LinearLayout) findViewById(R.id.attendance);
        dashClassTest = (LinearLayout) findViewById(R.id.class_test);
        dashExam = (LinearLayout) findViewById(R.id.exam);
        dashTimeTable = (LinearLayout) findViewById(R.id.time_table);
        dashEvent = (LinearLayout) findViewById(R.id.events);
        dashCommunication = (LinearLayout) findViewById(R.id.communication);
        deleteTableRecords = new DeleteTableRecords(this);

        dashAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AttendanceActivity.class);
                startActivity(intent);
            }
        });

        dashClassTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClassTestHomeworkActivity.class);
                startActivity(intent);
            }
        });

        dashExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExamsResultActivity.class);
                startActivity(intent);
            }
        });

        dashTimeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StudentTimeTableActivity.class);
                startActivity(intent);
            }
        });

        dashEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EventsActivity.class);
                startActivity(intent);
            }
        });

        dashCommunication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CircularActivity.class);
                startActivity(intent);
            }
        });


        String name = PreferenceStorage.getName(getApplicationContext());
        Log.d(TAG, "user name value" + name);
        if ((name != null) && !name.isEmpty()) {
            navUserProfileName.setText("Hi, " + name);
        }
        String url = PreferenceStorage.getUserPicture(this);
        mCurrentUserProfileUrl = url;

        if (((url != null) && !(url.isEmpty()))) {
            Log.d(TAG, "image url is " + url);
            Picasso.with(this).load(url).placeholder(R.drawable.ab_logo).error(R.drawable.ab_logo).into(imgNavProfileImage,
                    new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Image uploaded successfully using picasso");
                            try {

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }
        Log.d(TAG, "Set the selected page to 0");//default page
    }

    private void initializeNavigationDrawer() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {

            }

            public void onDrawerOpened(View drawerView) {
                String userProfileName = PreferenceStorage.getName(getApplicationContext());
                String url = PreferenceStorage.getUserPicture(ParentDashBoardActivity.this);

                Log.d(TAG, "user name value" + userProfileName);
                if ((userProfileName != null) && !userProfileName.isEmpty()) {
                    String[] splitStr = userProfileName.split("\\s+");
                    navUserProfileName.setText("Hi, " + splitStr[0]);
                }

                if (((url != null) && !(url.isEmpty())) && !(url.equalsIgnoreCase(mCurrentUserProfileUrl))) {
                    Log.d(TAG, "image url is " + url);
                    mCurrentUserProfileUrl = url;
                    Picasso.with(ParentDashBoardActivity.this).load(url).noPlaceholder().error(R.drawable.ab_logo).into(imgNavProfileImage);
                }
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        // enable ActionBar app icon to behave as action to toggle nav drawer
        navDrawerList = (ListView) findViewById(R.id.nav_drawer_options_list);

        NavDrawerAdapter navDrawerAdapter = new NavDrawerAdapter(getApplicationContext(), R.layout.nav_list_item, values);
        navListAdapter = new ArrayAdapter<String>(this, R.layout.nav_list_item, values);
        navDrawerList.setAdapter(navDrawerAdapter);

        imgNavProfileImage = (ImageView) findViewById(R.id.img_profile_image);
        navDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onNavigationMenuSelected(position);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
    }

    private void onNavigationMenuSelected(int position) {

        if (position == 0) {
            Intent navigationIntent = new Intent(this, ProfileActivity.class);
            navigationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(navigationIntent);
        } else if (position == 1) {
            Intent navigationIntent = new Intent(this, AttendanceActivity.class);
            navigationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(navigationIntent);
        } else if (position == 2) {
            Intent navigationIntent = new Intent(this, ClassTestHomeworkActivity.class);
            navigationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(navigationIntent);
        } else if (position == 3) {
            Intent navigationIntent = new Intent(this, ExamsResultActivity.class);
            navigationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(navigationIntent);
        } else if (position == 4) {
            Intent navigationIntent = new Intent(this, StudentTimeTableActivity.class);
            navigationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(navigationIntent);
        } else if (position == 5) {
            Intent navigationIntent = new Intent(this, EventsActivity.class);
            navigationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(navigationIntent);
        } else if (position == 6) {
            Intent navigationIntent = new Intent(this, CircularActivity.class);
            navigationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(navigationIntent);
        } else if (position == 7) {
            Intent navigationIntent = new Intent(this, StudentInfoActivity.class);
            navigationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(navigationIntent);
        } else if (position == 8) {
            Intent navigationIntent = new Intent(this, OnDutyActivity.class);
            navigationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(navigationIntent);
        }
//        else if (position == 9) {
//            Intent navigationIntent = new Intent(this, LeaveCalendarActivity.class);
//            navigationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(navigationIntent);
//        }
        else if (position == 9) {
            Intent navigationIntent = new Intent(this, ChangePasswordActivity.class);
            navigationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(navigationIntent);
        } else if (position == 10) {
            Log.d(TAG, "Perform Logout");
            doLogout();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void doLogout() {

        deleteTableRecords.deleteAllRecords();

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().clear().commit();

        Intent homeIntent = new Intent(this, SplashScreenActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        //Checking for fragment count on backstack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
            return;
        }
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }
}
