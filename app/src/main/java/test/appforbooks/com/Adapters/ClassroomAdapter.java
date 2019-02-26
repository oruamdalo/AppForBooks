package test.appforbooks.com.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
        TextView view = new TextView(c);
        view.setText(classRoomList.get(position).getNumber()
                +classRoomList.get(position).getLetter()
                +classRoomList.get(position).getDesc());
        return view;
    }
}
