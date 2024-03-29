package com.palprotech.ensyfi.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.palprotech.ensyfi.R;
import com.palprotech.ensyfi.adapter.studentmodule.StudentTimeTableListAdapter;
import com.palprotech.ensyfi.adapter.teachermodule.TeacherTimetableListAdapter;
import com.palprotech.ensyfi.bean.database.SQLiteHelper;
import com.palprotech.ensyfi.bean.student.viewlist.StudentTimeTable;
import com.palprotech.ensyfi.bean.student.viewlist.StudentTimeTableList;
import com.palprotech.ensyfi.bean.teacher.viewlist.TTDays;
import com.palprotech.ensyfi.bean.teacher.viewlist.TimeTable;
import com.palprotech.ensyfi.helper.AlertDialogHelper;
import com.palprotech.ensyfi.helper.ProgressDialogHelper;
import com.palprotech.ensyfi.interfaces.DialogClickListener;
import com.palprotech.ensyfi.servicehelpers.ServiceHelper;
import com.palprotech.ensyfi.serviceinterfaces.IServiceListener;
import com.palprotech.ensyfi.utils.EnsyfiConstants;
import com.palprotech.ensyfi.utils.PreferenceStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.util.Log.d;
import static com.android.volley.VolleyLog.TAG;

public class DynamicTTFragment extends Fragment implements IServiceListener, AdapterView.OnItemClickListener, DialogClickListener {
    Context context;
    private View view;
    private static ArrayList<TTDays> ttDays;
    ArrayList<TimeTable> ttArrayList = new ArrayList<>();
    ArrayList<StudentTimeTable> studentTTArrayList = new ArrayList<>();
    private int val;
    private StudentTimeTableListAdapter studentTimeTableListAdapter;
    private TeacherTimetableListAdapter teacherTimetableListAdapter;
    private static final String TAG = DynamicTTFragment.class.getName();
    private String subCatId = "";
    private ServiceHelper serviceHelper;
    private int totalCount = 0, checkrun = 0;
    private  boolean isLoadingForFirstTime = true;
    private ProgressDialogHelper progressDialogHelper;
    private ListView loadMoreListView;
    private Boolean msgErr = false;
    private Boolean noService = false;
    private String res = "";
    private String id = "";
    SQLiteHelper db;
    List<String> list = new ArrayList<String>();
    List<String> list1 = new ArrayList<String>();
    int dayCount = 0;

    private static boolean _hasLoadedOnce = false; // your boolean field

    public static DynamicTTFragment newInstance(int val, ArrayList<TTDays> ttDaysArrayList) {
        DynamicTTFragment fragment = new DynamicTTFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", val);
        fragment.setArguments(args);
        ttDays = ttDaysArrayList;
        if (String.valueOf(val).equalsIgnoreCase("1")) {
            _hasLoadedOnce = true;
        } else {
            _hasLoadedOnce = false;
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_timetable, container, false);
        db = new SQLiteHelper(view.getContext());
        serviceHelper = new ServiceHelper(view.getContext());
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(view.getContext());
        val = getArguments().getInt("someInt", 0);
//        categories = subCategoryList.getCategoryArrayList();
//        categories = subCategoryList.getCategoryArrayList();
        subCatId = ttDays.get(val).getDayId();
//        PreferenceStorage.saveSubCatClick(view.getContext(), subCatId);
//        rateCount = (TextView) view.findViewById(R.id.service_count);
//        summary = (TextView) view.findViewById(R.id.view_summary);
//        summary.setOnClickListener(this);
//        c = view.findViewById(R.id.c);
//        c.setText("" + subCatId);
        if (PreferenceStorage.getUserType(getActivity()).equalsIgnoreCase("2") ||
                PreferenceStorage.getUserType(getActivity()).equalsIgnoreCase("1")) {
            loadTimeTable();

            if (teacherTimetableListAdapter == null) {
                teacherTimetableListAdapter = new TeacherTimetableListAdapter(getActivity(), this.ttArrayList);
                loadMoreListView.setAdapter(teacherTimetableListAdapter);
            } else {
                teacherTimetableListAdapter.notifyDataSetChanged();
            }
        } else {
            getHolsList();
        }
        loadMoreListView = view.findViewById(R.id.time_table_list);
        loadMoreListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();

        if (validateSignInResponse(response)) {
            LoadListView(response);
        }
    }

    private void LoadListView(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
//        loadMoreListView.onLoadMoreComplete();
        Gson gson = new Gson();
        StudentTimeTableList studentTimeTableList = gson.fromJson(response.toString(), StudentTimeTableList.class);
        if (studentTimeTableList != null) {
            Log.d(TAG, "fetched all event list count" + studentTimeTableList.getCount());
        }
//        updateListAdapter(eventsList.getEvents());
        int totalNearbyCount = 0;
        if (studentTimeTableList.getStudentTT() != null && studentTimeTableList.getStudentTT().size() > 0) {


            isLoadingForFirstTime = false;
            totalCount = studentTimeTableList.getCount();
            updateListAdapter(studentTimeTableList.getStudentTT());
        }
    }

    protected void updateListAdapter(ArrayList<StudentTimeTable> studentTimeTableList) {
        this.studentTTArrayList.addAll(studentTimeTableList);
       /* if (mNoEventsFound != null)
            mNoEventsFound.setVisibility(View.GONE);*/

        if (studentTimeTableListAdapter == null) {
            studentTimeTableListAdapter = new StudentTimeTableListAdapter(getActivity(), this.studentTTArrayList);
            loadMoreListView.setAdapter(studentTimeTableListAdapter);
        } else {
            studentTimeTableListAdapter.notifyDataSetChanged();
        }
    }

    private boolean validateSignInResponse(JSONObject response) {
        boolean signInSuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(EnsyfiConstants.PARAM_MESSAGE);
                d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (((status.equalsIgnoreCase("activationError")) || (status.equalsIgnoreCase("alreadyRegistered")) ||
                            (status.equalsIgnoreCase("notRegistered")) || (status.equalsIgnoreCase("error")))) {
                        signInSuccess = false;
                        d(TAG, "Show error dialog");
//                        if (msg.equalsIgnoreCase("Services not found")) {
//                            msgErr = true;
//                        }
//                        AlertDialogHelper.showSimpleAlertDialog(view.getContext(), msg);
                        if (msg.equalsIgnoreCase("Service not found")){
                            noService = true;
                        }

                    } else {
                        signInSuccess = true;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInSuccess;
    }

    @Override
    public void onError(String error) {

    }

    private void loadTimeTable() {
        ttArrayList.clear();
        try {
            Cursor c = db.getTeacherTimeTableValueNew("1");
            if (c.getCount() > 0) {
                if (c.moveToFirst()) {
                    do {

                        TimeTable lde = new TimeTable();
                        lde.setClassName(c.getString(0));
                        lde.setSecName(c.getString(1));
                        lde.setSubjectName(c.getString(2));
                        lde.setClassId(c.getString(3));
                        lde.setSubjectId(c.getString(4));
                        lde.setName(c.getString(5));
                        lde.setPeriod(c.getString(6));
                        lde.setFromTime(c.getString(7));
                        lde.setToTime(c.getString(8));
                        lde.setIsBreak(c.getString(9));
                        lde.setBreakName(c.getString(10));

                        // Add this object into the ArrayList myList
                        ttArrayList.add(lde);
                    } while (c.moveToNext());
                }
            } else {
                Toast.makeText(getActivity(), "No records found", Toast.LENGTH_LONG).show();
            }
            db.close();

        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
//        setval = true;
    }

    public void getHolsList() {
        JSONObject jsonObject = new JSONObject();
        String id = "";
        id = PreferenceStorage.getStudentClassIdPreference(getActivity());
        try {
            jsonObject.put(EnsyfiConstants.PARAMS_CLASS_ID, id);
            jsonObject.put(EnsyfiConstants.PARAMS_DAY_ID, subCatId);
            jsonObject.put(EnsyfiConstants.KEY_USER_DYNAMIC_DB, PreferenceStorage.getUserDynamicDB(getActivity()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = EnsyfiConstants.BASE_URL + EnsyfiConstants.GET_TIME_TABLE_API;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }
}