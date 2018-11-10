package itpdm.timemanager;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Notify_Time extends ArrayAdapter<String> {
    private final Activity context;
    private final String[]notifyids;
    private final String[]notify;
    private final String[]notifytimes;
    private final String[]notifystatus;
    public Notify_Time(Activity context, String[]notifies, String[]notifytimes, String[] notifystatus, String[]notifyids){
        super(context, R.layout.notify_time, notifies);
        this.context=context;
        this.notify=notifies;
        this.notifytimes=notifytimes;
        this.notifystatus=notifystatus;
        this.notifyids=notifyids;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View v = inflater.inflate(R.layout.notify_time, null, true);
        TextView taskid =  v.findViewById(R.id.taskid);
        TextView taskname = v.findViewById(R.id.taskname);
        TextView tasktime =  v.findViewById(R.id.tasktime);
        ImageView holder    =v.findViewById(R.id.holder);
        taskid.setVisibility(TextView.INVISIBLE);
        taskid.setText(notifyids[position]);
        taskname.setText(notify[position]);
        tasktime.setText(notifytimes[position]);
        if(notifystatus[position]=="true") {
            taskname.setTextColor(Color.RED);
        }
        holder.setVisibility(ImageView.INVISIBLE);
        return v;
    }

}
