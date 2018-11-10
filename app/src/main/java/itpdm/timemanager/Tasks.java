package itpdm.timemanager;


import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    String validates="",validates2="",validates3="";
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
    public String getDateAgo(String datesone,String time,String name,String email) {
        String join=null;

        String now = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            validationdates = getDateDiff(datesone, now);
                if (validationdates[2] < 0) {
                    return "Date error";
                } else if ((validationdates[2] > 0) && (validationdates[2] == 0) && (validationdates[1] < 0)) {
                    return "Date error";
                } else if ((validationdates[2] > 0) && (validationdates[2] == 0) && (validationdates[1] > 0) && (validationdates[1] == 0) && validationdates[2] < 0) {
                    return "Date error";
                } else {
                    validate=1;
                }
        if(validate==1){
            int totalday=validationdates[0]+validationdates[1]*30+validationdates[2]*365;
            if(totalday==0) {
                return timedifference(time,name);
            }
            else{
                return "After "+String.valueOf(totalday)+" days";
            }
        }
        return "Errors in process";
    }
    public String getDates(String datesone,String datetwo,String timeone,String timetwo) {
        String join=null;
            validationdates = getDateDiff(datesone, datetwo);
        if (validationdates[2] < 0) {
            return "Date error";
        } else if ((validationdates[2] > 0) && (validationdates[2] == 0) && (validationdates[1] < 0)) {
            return "Date error";
        } else if ((validationdates[2] > 0) && (validationdates[2] == 0) && (validationdates[1] > 0) && (validationdates[1] == 0) && validationdates[2] < 0) {
            return "Date error";
        } else {
            validate=1;
        }
            if(validate==1){
                int totalday=validationdates[0]+validationdates[1]*30+validationdates[2]*365;
                if(totalday==0) {
                    return timedifferencetwo(timeone,timetwo);
                }
                else{
                    return "After "+String.valueOf(totalday)+" days";
                }
            }

        return "Errors in process";
    }
    public String timedifference(String time,String name){
        String[] timedef=new String[2];
        String[] timedef2=new String[2];
        int outtime;
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("hh : mm");
        String dateToStr = format.format(today);
        timedef=dateToStr.split(" : ");
        timedef2=time.split(" : ");
        if(Integer.parseInt(timedef[0])<Integer.parseInt(timedef2[0])) {
            outtime =Integer.parseInt(timedef2[0])-Integer.parseInt(timedef[0]);
            validates=String.valueOf(outtime)+" hrs";
        }
        else if(timedef[0]==timedef2[0]){
            outtime =Integer.parseInt(timedef2[1])-Integer.parseInt(timedef[1]);
            validates2=String.valueOf(outtime)+" minutes";
        }
        else{
            return "Expired";
        }
        addNotification(name,time,"Critical");
        validates3=validates+" "+validates2;
        return validates3;
    }
    public String timedifferencetwo(String time,String timetwo){
        String[] timedef=new String[2];
        String[] timedef2=new String[2];
        int outtime;
        timedef=timetwo.split(":");
        timedef2=time.split(":");
        if(Integer.parseInt(timedef[0])<Integer.parseInt(timedef2[0])) {
            outtime =Integer.parseInt(timedef2[0])-Integer.parseInt(timedef[0]);
        }
        else if(timedef[0]==timedef2[0]){
            outtime =Integer.parseInt(timedef2[1])-Integer.parseInt(timedef[1]);
        }
        else{
            return "Expired";
        }

        return String.valueOf(outtime);
    }


    private int[] getDateDiff(String  date1, String date2) {

        String[] datesone=new String[3];
        datesone=date1.split("/");
        int days1=Integer.parseInt(datesone[0]);
        int month1=Integer.parseInt(datesone[1]);
        int yeardate1=Integer.parseInt(datesone[2]);
        String[] datestwo=new String[3];
        datestwo=date2.split("/");
        int days2=Integer.parseInt(datestwo[0]);
        int month2=Integer.parseInt(datestwo[1]);
        int yeardate2=Integer.parseInt(datestwo[2]);

         int checkday=days2-days1;
         int checkmonth=month2-month1;
         int checkyear=yeardate2-yeardate1;
        checkeddated[0]=checkday;
        checkeddated[1]=checkmonth;
        checkeddated[2]=checkyear;
        return checkeddated;
    }
    public void addNotification(final String name,final String time,final String status) {
        mAuth = FirebaseAuth.getInstance();
        final DatabaseReference mDatabases;
        mDatabases = FirebaseDatabase.getInstance().getReference();
        mDatabases.child("notifications").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                        mDatabases.child("notifications").child(mAuth.getCurrentUser().getEmail().toString().split("@")[0]);
                        mDatabases.child("notifications").child(mAuth.getCurrentUser().getEmail().toString().split("@")[0]).child("email").setValue(mAuth.getCurrentUser().getEmail().toString());
                        mDatabases.child("notifications").child(mAuth.getCurrentUser().getEmail().toString().split("@")[0]).child("name").setValue(name);
                        mDatabases.child("notifications").child(mAuth.getCurrentUser().getEmail().toString().split("@")[0]).child("time").setValue(time);
                        mDatabases.child("notifications").child(mAuth.getCurrentUser().getEmail().toString().split("@")[0]).child("status").setValue(status);
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                                    if( child.child("note").getValue()!=null && child.child("email").getValue()!=null && child.child("status").getValue()!=null&&child.child("status").getValue()!=null) {
                                        t.taskides.add(child.child("email").getValue().toString());
                                        t.tasknote.add(child.child("note").getValue().toString());
                                        t.names.add(child.getKey().toString());
                                        t.statuses.add(child.child("status").getValue().toString());
                                        String startdate = child.child("startdate").getValue().toString();
                                        String enddate = child.child("enddate").getValue().toString();
                                        String starttime = child.child("starttime").getValue().toString();
                                        String endtime = child.child("endtime").getValue().toString();

                                        String time = getDateAgo(startdate, starttime, child.getKey().toString(), child.child("email").getValue().toString());
                                        t.times.add(time);
                                    }
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

