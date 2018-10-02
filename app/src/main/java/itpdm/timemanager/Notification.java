package itpdm.timemanager;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import itpdm.timemanager.MainActivity.Login;

public class Notification extends Fragment {
    private final Handler handler = new Handler();
    String[] taskname, tasktime, status, taskid;
    Object[] arraymakeone, arraymaketwo, arraymakethree, arraymaketfour;
    public final List<String> names = new ArrayList<String>();
    public final List<String> times = new ArrayList<String>();
    public final List<String> statuses = new ArrayList<String>();
    public final List<String> taskides = new ArrayList<String>();
    Notify_Time notify;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_notification, container, false);

        return rootView;
    }

    public void insertlist(View rootView, List names, List tasknote, List taskides, List statuses, List times) {
        ListView lists = (ListView) rootView.findViewById(R.id.notifylist);
        taskname = new String[names.size()];
        status = new String[statuses.size()];
        taskid = new String[taskides.size()];
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
            tasknote.clear();
        }
    }
}
