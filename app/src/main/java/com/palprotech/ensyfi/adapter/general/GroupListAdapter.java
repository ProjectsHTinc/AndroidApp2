package com.palprotech.ensyfi.adapter.general;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.palprotech.ensyfi.R;
import com.palprotech.ensyfi.app.AppController;
import com.palprotech.ensyfi.bean.general.viewlist.GroupList;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

/**
 * Created by Narendar on 17/05/17.
 */

public class GroupListAdapter extends BaseAdapter {

    private static final String TAG = GroupListAdapter.class.getName();
    private final Transformation transformation;
    private Context context;
    private ArrayList<GroupList> groupListses;
    private boolean mSearching = false;
    private boolean mAnimateSearch = false;
    private ArrayList<Integer> mValidSearchIndices =new ArrayList<Integer>();
    private ImageLoader imageLoader = AppController.getInstance().getUniversalImageLoader();

    public GroupListAdapter(Context context, ArrayList<GroupList> groupListses) {
        this.context = context;
        this.groupListses = groupListses;

        transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(0)
                .oval(false)
                .build();
        mSearching = false;
    }

    @Override
    public int getCount() {
        if(mSearching){
            // Log.d("GroupList List Adapter","Search count"+mValidSearchIndices.size());
            if(!mAnimateSearch){
                mAnimateSearch = true;
            }
            return mValidSearchIndices.size();

        }else{
            // Log.d(TAG,"Normal count size");
            return groupListses.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if(mSearching){
            return groupListses.get(mValidSearchIndices.get(position));
        }else {
            return groupListses.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.group_notification_item, parent, false);

            holder = new GroupListAdapter.ViewHolder();
//            holder.txtClassTestTitle = (TextView) convertView.findViewById(R.id.txtClassTestTitle);
            holder.txtGroupName = (TextView) convertView.findViewById(R.id.group_title);
            holder.txtnotes = (TextView) convertView.findViewById(R.id.mini_notes);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(mSearching){
            // Log.d("GroupList List Adapter","actual position"+ position);
            position = mValidSearchIndices.get(position);
            //Log.d("GroupList List Adapter", "position is"+ position);

        }else{
            Log.d("GroupList List Adapter","getview pos called"+ position);
        }

        GroupList groupList = groupListses.get(position);

//        holder.txtClassTestTitle.setText(classTests.get(position).getHwTitle());
        holder.txtGroupName.setText(groupList.getGroup_title());
        holder.txtnotes.setText(groupList.getNotes());

        return convertView;
    }

    public void startSearch(String groupName){
        mSearching = true;
        mAnimateSearch = false;
        Log.d("GroupListAdapter","serach for group"+groupName);
        mValidSearchIndices.clear();
        for(int i = 0; i< groupListses.size(); i++){
            String groupTitle = groupListses.get(i).getGroup_title();
            if((groupTitle != null) && !(groupTitle.isEmpty())){
                if( groupTitle.toLowerCase().contains(groupName.toLowerCase())){
                    mValidSearchIndices.add(i);
                }

            }

        }
        Log.d("GroupList List Adapter","notify"+ mValidSearchIndices.size());
        //notifyDataSetChanged();
    }

    public void exitSearch(){
        mSearching = false;
        mValidSearchIndices.clear();
        mAnimateSearch = false;
        // notifyDataSetChanged();
    }

    public void clearSearchFlag(){
        mSearching = false;
    }

    public class ViewHolder {
        public TextView txtGroupName, txtnotes;
    }

    public boolean ismSearching() {
        return mSearching;
    }

    public int getActualGroupPos(int selectedSearchpos){
        if(selectedSearchpos < mValidSearchIndices.size()) {
            return mValidSearchIndices.get(selectedSearchpos);
        }else{
            return 0;
        }
    }
}
