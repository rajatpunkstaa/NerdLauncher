package com.learningandroid.android.nerdlauncher;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by rajatpunkstaa on 4/11/16.
 */

public class NerdLauncherFragment extends Fragment {


    private static final String TAG = "NerdLauncherFragment";

    private RecyclerView mRecyclerView;
    public static NerdLauncherFragment newInstance(){
        return new NerdLauncherFragment();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_nerd_launcher, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_nerd_launcher_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupAdapter(); // Query the package manager
        return v;
    }

    private void setupAdapter(){
        // To query the package manager that how many main launcher activities are there in the android system


        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);
        //Sorting the activities according to their names
        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo a, ResolveInfo b) {
                PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(a.loadLabel(pm).toString(),b.loadLabel(pm).toString());
            }
        });
        Log.i(TAG, "Found" + activities.size() + "activities");
        mRecyclerView.setAdapter(new ActivityAdapter(activities));
    }

    //Implementing a ViewHolder
    private class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ResolveInfo mResolveInfo;
        private TextView mNameTextView;
        private ImageView mImageView;
        public ActivityHolder(View itemView){
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.app_name);
            mImageView = (ImageView) itemView.findViewById(R.id.app_icon);

        }
        private void bindActivity(ResolveInfo resolveInfo){
            mResolveInfo = resolveInfo;
            PackageManager pm = getActivity().getPackageManager();
            String appName = mResolveInfo.loadLabel(pm).toString();
            mNameTextView.setText(appName);
            mImageView.setImageDrawable(resolveInfo.loadIcon(pm));
            mNameTextView.setOnClickListener(this);
        }
        //OnClick method
        public void onClick(View v){

            ActivityInfo activityInfo = mResolveInfo.activityInfo;
            //start up the activity using an explicit intent
            Intent mStartNewActivity = new Intent(Intent.ACTION_MAIN).setClassName(activityInfo.applicationInfo.packageName,activityInfo.name).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mStartNewActivity);
        }


    }

    // An Adapter for the ViewHolder
    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder>{
        private final List<ResolveInfo> mActivities;
        public ActivityAdapter(List<ResolveInfo> activities){
            mActivities = activities;
        }
        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType){

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.nerd_launcher_list_item, parent, false);
            return new ActivityHolder(view);
        }
        @Override
        public void onBindViewHolder(ActivityHolder activityHolder, int position){
            ResolveInfo resolveInfo = mActivities.get(position);
            activityHolder.bindActivity(resolveInfo);
        }
        @Override
        public int getItemCount(){
            return mActivities.size();
        }






    }


}
