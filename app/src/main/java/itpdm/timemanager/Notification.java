package itpdm.timemanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import itpdm.timemanager.MainActivity.Login;

public class Notification extends Fragment {
    String[] taskname, tasktime, status, taskid;
    Object[] arraymakeone, arraymaketwo, arraymakethree, arraymaketfour;
    private FirebaseAuth mAuth;
    final List<String> names = new ArrayList<String>();
    final List<String> times = new ArrayList<String>();
    final List<String> statuses = new ArrayList<String>();
    final List<String> taskides = new ArrayList<String>();
    Notify_Time notify;
     View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_notification, container, false);
        mAuth = FirebaseAuth.getInstance();
        final DatabaseReference mDatabases;
        mDatabases = FirebaseDatabase.getInstance().getReference();
        mDatabases.child("notifications").orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail().toString()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.child("email").getValue()!=null&&child.child("name").getValue()!=null&&child.child("time").getValue()!=null&&child.child("status").getValue()!=null) {
                            taskides.add(child.child("email").getValue().toString());
                            names.add(child.child("name").getValue().toString());
                            times.add(child.child("time").getValue().toString());
                            statuses.add(child.child("status").getValue().toString());
                            insertlist(rootView, names, taskides, statuses, times);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return rootView;
    }
    public void insertlist(View rootView,List names,List taskides,List statuses,List times) {
        ListView lists = rootView.findViewById(R.id.notifylist);
        taskname = new String[names.size()];
        status = new String[statuses.size()];
        taskid = new String[taskides.size()];
        tasktime=new String[times.size()];
        arraymakeone = names.toArray();
        arraymaketwo = times.toArray();
        arraymakethree = statuses.toArray();
        arraymaketfour = taskides.toArray();
        for (int x = 0; x < arraymakeone.length; x++) {
            taskname[x] = (String) arraymakeone[x];
            tasktime[x] = (String) arraymaketwo[x];
            status[x] = (String) arraymakethree[x];
            taskid[x] = (String) arraymaketfour[x];
        }
        if (getActivity() != null) {
            notify = new Notify_Time(getActivity(), taskname, tasktime, status, taskid);
            lists.setAdapter(notify);
            names.clear();
            times.clear();
            statuses.clear();
            taskides.clear();
        }

    }
}
