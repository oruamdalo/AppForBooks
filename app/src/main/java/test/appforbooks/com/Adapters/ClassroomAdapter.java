package test.appforbooks.com.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textservice.TextInfo;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import test.appforbooks.com.R;
import test.appforbooks.com.SchoolUtils.ClassRoom;

public class ClassroomAdapter extends BaseAdapter {
    ArrayList<ClassRoom> classRoomList;
    Context c;
    public ClassroomAdapter(Context c, ArrayList<ClassRoom> classRoomList){
        this.classRoomList = classRoomList;
        this.c = c;
    }

    @Override
    public int getCount() {
        return classRoomList.size();
    }

    @Override
    public Object getItem(int position) {
        return classRoomList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view;
        if (convertView == null) {

            view = new View(c);
            view = inflater.inflate(R.layout.grid_view_item, null);
            TextView className = view.findViewById(R.id.class_name);
            TextView classAddress = view.findViewById(R.id.class_address);
            className.setText(classRoomList.get(position).getNumber()+classRoomList.get(position).getLetter());
            String text = classRoomList.get(position).getDesc();
            int index;
            if(text.indexOf(',')!=-1){
                index = text.indexOf(',');
            }else if(text.indexOf(' ') != -1){
                index = text.indexOf(' ');
            }else{
                index = 0;
            }
            String classAddressText = text.substring(0, index);
            classAddress.setText(classAddressText);
        } else {
            view = (View) convertView;
        }

        return view;
    }
}
