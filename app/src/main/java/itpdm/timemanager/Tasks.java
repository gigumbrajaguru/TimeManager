package itpdm.timemanager;


import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
    TaskTime addList;
    String email;
    public  String useremail="default@gmail.com";
    String[] taskname, tasktime, status,taskid;
    Object[] arraymakeone,arraymaketwo,arraymakethree,arraymaketfour;
    public final List<String> names = new ArrayList<String>();
    public final List<String> times = new ArrayList<String>();
    public final List<String> statuses = new ArrayList<String>();
    public final List<String> taskides = new ArrayList<String>();
    public final List<String> tasknote = new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_tasks, container, false);

        //String useremail = getArguments().getString("index");
        Handler handler=new Handler();
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm");
        try {
            Date dateone = sdf.parse(datesone);
            Date now = new Date(System.currentTimeMillis());
            long days = getDateDiff(dateone, now, TimeUnit.DAYS);
            if (days < 7)
                return days + "d";
            else
                return days / 7 + "w";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ERROR";
    }
    public String getDates(String datesone,String datetwo) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm");
        try {
            Date dateone = sdf.parse(datesone);
            Date datetwos = sdf.parse(datetwo);
            Date now = new Date(System.currentTimeMillis());
            long days = getDateDiff(dateone, datetwos, TimeUnit.DAYS);
            if (days < 7)
                return days + "d";
            else
                return days / 7 + "w";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ERROR";
    }
    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
    public void setArrays(final View rootView) {
        useremail="a@a";
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
                                t.times.add("time");
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
        }catch (NullPointerException e){
    }
    }
}

