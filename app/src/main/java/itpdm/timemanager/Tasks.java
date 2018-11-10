package itpdm.timemanager;


import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Tasks extends Fragment {
    int checkeddated[]=new int[3];
    int validationdates[]=new int[3];
    String validated[]=new String[3];
    int validate=0;
    TaskTime addList;
    String email;
    private FirebaseAuth mAuth;
    String[] taskname, tasktime, status,taskid;
    Object[] arraymakeone,arraymaketwo,arraymakethree,arraymaketfour;
    public final List<String> names = new ArrayList<String>();
    public final List<String> times = new ArrayList<String>();
    public final List<String> statuses = new ArrayList<String>();
    public final List<String> taskides = new ArrayList<String>();
    public final List<String> tasknote = new ArrayList<String>();
    Handler handler=new Handler();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_tasks, container, false);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               setArrays(rootView);
            }
        }, 1);
        return rootView;
    }
    public void insertlist(View rootView,List names,List tasknote,List taskides,List statuses,List times){
        ListView lists=(ListView)rootView.findViewById(R.id.lists);
        taskname = new String[names.size()];
        tasktime = new String[times.size()];
        status = new String[statuses.size()];
        taskid = new String[taskides.size()];
        arraymakeone = names.toArray();
        arraymaketwo = times.toArray();
        arraymakethree =statuses.toArray();
        arraymaketfour=taskides.toArray();
        for(int x=0;x<arraymakeone.length;x++){
            taskname[x]=(String)arraymakeone[x];
            tasktime[x]=(String)arraymaketwo[x];
            status[x]=(String)arraymakethree[x];
            taskid[x]=(String)arraymaketfour[x];
        }
        if(getActivity()!=null) {
            addList = new TaskTime(getActivity(), taskname, tasktime, status, taskid);
            lists.setAdapter(addList);
            names.clear();
            times.clear();
            statuses.clear();
            taskides.clear();
            tasknote.clear();
        }
    }
    public String getDateAgo(String datesone) {
        String join=null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm");
        try {
            Date dateone = sdf.parse(datesone);
            Date now = new Date(System.currentTimeMillis());
            validationdates = getDateDiff(dateone, now);
            for(int x:validationdates) {
                if (x > 0) {
                    validate=1;
                    join=join+String.valueOf(x)+"/";
                } else {
                    return "Date error";
                }
            }
            if(validate==1){

            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return "Errors in process";
    }
    public String getDates(String datesone,String datetwo) {
        String join=null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm");
        try {
            Date dateone = sdf.parse(datesone);
            Date datetwos = sdf.parse(datetwo);
            validationdates = getDateDiff(dateone, datetwos);
            for(int x:validationdates) {
                if (x > 0) {
                    validate=1;
                    join=join+String.valueOf(x)+"/";
                } else {
                    return "Date error";
                }
            }
            if(validate==1){

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Errors in process";
    }
    private int[] getDateDiff(Date date1, Date date2) {

        String dayOfTheWeek = (String) DateFormat.format("EEEE", date1);
        String day          = (String) DateFormat.format("dd",   date1);
        String monthString  = (String) DateFormat.format("MMM",  date1);
        String monthNumber  = (String) DateFormat.format("MM",   date1);
        String year         = (String) DateFormat.format("yyyy", date1);

        int days1=Integer.parseInt(day);
        int month1=Integer.parseInt(monthNumber);
        int yeardate1=Integer.parseInt(year);

        String dayOfTheWeek2 = (String) DateFormat.format("EEEE", date2);
        String day2       = (String) DateFormat.format("dd",   date2);
        String monthString2  = (String) DateFormat.format("MMM",  date2);
        String monthNumber2  = (String) DateFormat.format("MM",   date2);
        String year2         = (String) DateFormat.format("yyyy", date2);

        int days2=Integer.parseInt(day2);
        int month2=Integer.parseInt(monthNumber2);
        int yeardate2=Integer.parseInt(year2);

         int checkday=days1-days2;
         int checkmonth=month1-month2;
         int checkyear=yeardate1-yeardate2;

        checkeddated[0]=checkday;
        checkeddated[1]=checkmonth;
        checkeddated[2]=checkyear;

        return checkeddated;
    }
    public void setArrays(final View rootView) {
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null) {
            String useremail = mAuth.getCurrentUser().getEmail().toString();
            try {
                final DatabaseReference mDatabases;
                mDatabases = FirebaseDatabase.getInstance().getReference();
                if (MainActivity.index == null) {
                    mDatabases.child("TaskDetails").child(useremail.split("@")[0]).orderByChild("email").equalTo(useremail).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()) {
                                Tasks t = new Tasks();
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    t.taskides.add(child.child("email").getValue().toString());
                                    t.tasknote.add(child.child("note").getValue().toString());
                                    t.names.add(child.getKey().toString());
                                    t.statuses.add(child.child("status").getValue().toString());
                                    String startdate = child.child("startdate").getValue().toString();
                                    String enddate = child.child("enddate").getValue().toString();
                                    String starttime = child.child("starttime").getValue().toString();
                                    String endtime = child.child("endtime").getValue().toString();
                                    String time = getDates(startdate, enddate);
                                    t.times.add(time);
                                }
                                insertlist(rootView, t.names, t.tasknote, t.taskides, t.statuses, t.times);
                            } else {
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }

            } catch (NullPointerException e) {
            }
        }
    }
}

