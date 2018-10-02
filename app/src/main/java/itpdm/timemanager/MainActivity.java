package itpdm.timemanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.DatePicker;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button submitbtn;
    EditText taskname, tasknot;
    TextView strtdate, strttime, enddate, endtime;
    int day, mon, yr;
    public static String index;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private final Handler handler = new Handler();
    String[] tasknames, tasktime, status,taskid;
    Object[] arraymakeone,arraymaketwo,arraymakethree,arraymaketfour;
    public final List<String> names = new ArrayList<String>();
    public final List<String> times = new ArrayList<String>();
    public final List<String> statuses = new ArrayList<String>();
    public final List<String> taskides = new ArrayList<String>();
    public final List<String> tasknote = new ArrayList<String>();
    private static DatabaseReference mDatabases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDatabases = FirebaseDatabase.getInstance().getReference();
        mDatabases.keepSynced(true);
        logcheck();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton fabtwo=(FloatingActionButton)findViewById(R.id.fabtwo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add new task to database", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.addupdatetask, null);
                submitbtn = (Button) mView.findViewById(R.id.submitTask);
                taskname = (EditText) mView.findViewById(R.id.taskname);
                tasknot = (EditText) mView.findViewById(R.id.tasknote);
                strtdate = mView.findViewById(R.id.strtdate);
                strttime = mView.findViewById(R.id.strttime);
                enddate = mView.findViewById(R.id.enddate);
                endtime = mView.findViewById(R.id.endtime);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                final SharedPreferences.Editor editor = preferences.edit();
                dialog.show();
                strtdate.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        periodtaker(strtdate);
                    }
                });
                enddate.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        periodtaker(enddate);
                    }
                });
                strttime.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timetaker(strttime);
                    }
                });
                endtime.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timetaker(endtime);
                    }
                });
                if (index != null) {
                    submitbtn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDatabases.child("TaskDetails").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        editor.putString("email", MainActivity.index);
                                        mDatabases.child("TaskDetails").child(MainActivity.index.split("@")[0]).child(taskname.getText().toString()).child("email").setValue(MainActivity.index);
                                        mDatabases.child("TaskDetails").child(MainActivity.index.split("@")[0]).child(taskname.getText().toString()).child("note").setValue(tasknot.getText().toString());
                                        mDatabases.child("TaskDetails").child(MainActivity.index.split("@")[0]).child(taskname.getText().toString()).child("startdate").setValue(strtdate.getText().toString());
                                        mDatabases.child("TaskDetails").child(MainActivity.index.split("@")[0]).child(taskname.getText().toString()).child("starttime").setValue(strttime.getText().toString());
                                        mDatabases.child("TaskDetails").child(MainActivity.index.split("@")[0]).child(taskname.getText().toString()).child("enddate").setValue(enddate.getText().toString());
                                        mDatabases.child("TaskDetails").child(MainActivity.index.split("@")[0]).child(taskname.getText().toString()).child("endtime").setValue(endtime.getText().toString());
                                        mDatabases.child("TaskDetails").child(MainActivity.index.split("@")[0]).child(taskname.getText().toString()).child("status").setValue("Good");
                                    }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                }
                editor.apply();
            }

    });
        fabtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Snackbar.make(view, "Add new task to database", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.search, null);
                final TextView selecteddate;
                submitbtn = (Button) mView.findViewById(R.id.search);
                selecteddate= (TextView) mView.findViewById(R.id.selecteddate);
                tasknot = (EditText) mView.findViewById(R.id.tasknote);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                selecteddate.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick( View v) {
                        periodtaker(selecteddate);
                    }
                });
                if (index != null && selecteddate.getText().toString()!=null) {
                    submitbtn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDatabases.child("TaskDetails").child(index.split("@")[0]).orderByChild("startdate").equalTo(selecteddate.getText().toString()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange( DataSnapshot dataSnapshot) {
                                    TaskTime addList;
                                    if(dataSnapshot.hasChildren()){
                                        for(DataSnapshot child:dataSnapshot.getChildren()){
                                                taskides.add(child.child("email").getValue().toString());
                                                tasknote.add(child.child("note").getValue().toString());
                                                names.add(child.getKey().toString());
                                                statuses.add(child.child("status").getValue().toString());
                                                times.add("time");
                                        }
                                        ListView lists=(ListView)mView.findViewById(R.id.selectedlist);
                                        tasknames = new String[names.size()];
                                        tasktime = new String[times.size()];
                                        status = new String[statuses.size()];
                                        taskid = new String[taskides.size()];
                                        arraymakeone = names.toArray();
                                        arraymaketwo = times.toArray();
                                        arraymakethree =statuses.toArray();
                                        arraymaketfour=taskides.toArray();
                                        for(int x=0;x<arraymakeone.length;x++){
                                            tasknames[x]=(String)arraymakeone[x];
                                            tasktime[x]=(String)arraymaketwo[x];
                                            status[x]=(String)arraymakethree[x];
                                            taskid[x]=(String)arraymaketfour[x];
                                        }
                                        if(MainActivity.this!=null) {
                                            addList = new TaskTime(MainActivity.this, tasknames, tasktime, status, taskid);
                                            lists.setAdapter(addList);
                                            names.clear();
                                            times.clear();
                                            statuses.clear();
                                            taskides.clear();
                                            tasknote.clear();
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled( DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                }

            }

        });




    }

    public void periodtaker(final TextView textchange) {
        Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        mon = c.get(Calendar.MONTH);
        yr = c.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                textchange.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        }
                , yr, mon, day);
        datePickerDialog.show();
    }

    public void timetaker(final TextView textchanges) {
        final Calendar myCalender = Calendar.getInstance();
        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        int minute = myCalender.get(Calendar.MINUTE);
        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
                    myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalender.set(Calendar.MINUTE, minute);
                }
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
        timePickerDialog.setTitle("Choose hour:");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
        textchanges.setText(hour + " : " + minute);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            MainActivity.index=null;
            logcheck();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void logcheck(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (index == null) {
                    AlertDialog.Builder mBuilders = new AlertDialog.Builder(MainActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.activity_login, null);
                    mBuilders.setView(mView);
                    final AlertDialog dialog = mBuilders.create();
                    Login.Login(mView,dialog);
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);
                }
            }
        }, 50);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch (position) {
                case 0:
                    Profile profile = new Profile();
                    return profile;
                case 1:
                    Tasks tasks = new Tasks();
                    Bundle bundle = new Bundle();
                    bundle.putString("index", MainActivity.index);
                    Tasks fragobj = new Tasks();
                    fragobj.setArguments(bundle);
                    return tasks;
                case 2:
                    Notification noty = new Notification();
                    return noty;
                default:
                    return null;
            }
            // Return a PlaceholderFragment (defined as a static inner class below).
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }


    public static class Login {
        public static String Login(View v, final AlertDialog dialog) {
            final Button btnregisters, loginbtns;
            final EditText emails, passwords;
            final DatabaseReference mDatabases;
            mDatabases = FirebaseDatabase.getInstance().getReference();
            mDatabases.keepSynced(true);
            loginbtns = v.findViewById(R.id.loginbtn);
            btnregisters = v.findViewById(R.id.btnregister);
            emails = v.findViewById(R.id.email);
            passwords = v.findViewById(R.id.password);
            loginbtns.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatabases.child("UserInfo").orderByChild("email").equalTo(emails.getText().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    if (child.child("password").getValue().toString().equals(passwords.getText().toString())) {
                                        MainActivity.index = emails.getText().toString();
                                        dialog.dismiss();

                                    }

                                }
                            }
                            else{
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
            btnregisters.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    mDatabases.child("UserInfo").orderByChild("userId").equalTo(emails.getText().toString()).addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.hasChildren()) {
                                mDatabases.child("UserInfo").child(emails.getText().toString().split("@")[0]);
                                mDatabases.child("UserInfo").child(emails.getText().toString().split("@")[0]).child("email").setValue(emails.getText().toString());
                                mDatabases.child("UserInfo").child(emails.getText().toString().split("@")[0]).child("password").setValue(passwords.getText().toString());
                                Snackbar.make(v, "You are now registered !", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                MainActivity.index = emails.getText().toString();
                            } else {
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
            return MainActivity.index;

        }



    }


}
