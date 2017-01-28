package eu.epitech.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import eu.epitech.todolist.TaskClass;

/**
 * Created by theo on 1/24/17.
 */

public class CustomAdapter extends ArrayAdapter<TaskClass> {

    private Context mContext;

    private static class ViewHolder{
        TextView taskName;
        TextView taskDescription;
    }

    CustomAdapter(ArrayList<TaskClass> data, Context context){
        super(context, R.layout.item_todo, data);
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        TaskClass newData = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_todo, parent, false);
            viewHolder.taskName = (TextView) convertView.findViewById(R.id.task_title);
            viewHolder.taskDescription = (TextView) convertView.findViewById(R.id.description);
            result = convertView;
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.taskName.setText(newData.getTask());
        viewHolder.taskDescription.setText(newData.getDescription());
        return convertView;
    }

}
