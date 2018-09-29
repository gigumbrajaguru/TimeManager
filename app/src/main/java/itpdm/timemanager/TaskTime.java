package itpdm.timemanager;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TaskTime extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] taskids;
    private final String[]tasknames;
    private final String[]tasktimes;
    private final String[]status;
    public TaskTime(Activity context, String[]taskname, String[]tasktime,String[] status,String[]taskid){
        super(context, R.layout.task_time, taskname);
        this.context=context;
        this.tasknames=taskname;
        this.tasktimes=tasktime;
        this.status=status;
        this.taskids=taskid;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View v = inflater.inflate(R.layout.task_time, parent, true);
        TextView taskid =  v.findViewById(R.id.taskid);
        TextView taskname = v.findViewById(R.id.taskname);
        TextView tasktime =  v.findViewById(R.id.tasktime);
        taskid.setVisibility(TextView.INVISIBLE);
        taskid.setText(taskids[position]);
        taskname.setText(tasknames[position]);
        tasktime.setText(tasktimes[position]);
        if(status[position]=="true") {
        taskname.setTextColor(16);
        }
        return v;
    }

}
