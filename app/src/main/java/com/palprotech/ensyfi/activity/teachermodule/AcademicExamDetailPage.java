package com.palprotech.ensyfi.activity.teachermodule;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.palprotech.ensyfi.R;
import com.palprotech.ensyfi.adapter.teachermodule.AcademicExamDetailsListBaseAdapter;
import com.palprotech.ensyfi.bean.database.SQLiteHelper;
import com.palprotech.ensyfi.bean.teacher.viewlist.AcademicExamDetails;
import com.palprotech.ensyfi.helper.ProgressDialogHelper;
import com.palprotech.ensyfi.interfaces.DialogClickListener;
import com.palprotech.ensyfi.serviceinterfaces.IServiceListener;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 15-07-2017.
 */

public class AcademicExamDetailPage extends AppCompatActivity implements IServiceListener, AdapterView.OnItemClickListener, DialogClickListener, View.OnClickListener {

    long id;
    SQLiteHelper db;
    private static final String TAG = "AcademicExamView";
    protected ProgressDialogHelper progressDialogHelper;
    ArrayList<AcademicExamDetails> myList = new ArrayList<AcademicExamDetails>();
    AcademicExamDetailsListBaseAdapter cadapter;
    ListView loadMoreListView;
    String examId, examName, classMasterId, sectionName, className, fromDate, toDate, markStatus;
    ImageView addExamMark, viewExamMark;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic_exam_details);
        Intent intent = getIntent();
        id = getIntent().getExtras().getLong("id");
        db = new SQLiteHelper(getApplicationContext());
        String localExamId = String.valueOf(id);
        loadMoreListView = (ListView) findViewById(R.id.listView_events);
        addExamMark = (ImageView) findViewById(R.id.addExamMarks);
        addExamMark.setOnClickListener(this);
        viewExamMark = (ImageView) findViewById(R.id.viewExamMarks);
        viewExamMark.setOnClickListener(this);

        GetAcademicExamInfo(localExamId);
        loadAcademicExamDetails(classMasterId, examId);
        int checkMarkStatus = Integer.parseInt(markStatus);
        if (checkMarkStatus == 0) {
            addExamMark.setVisibility(View.VISIBLE);
            viewExamMark.setVisibility(View.GONE);
        } else {
            addExamMark.setVisibility(View.GONE);
            viewExamMark.setVisibility(View.VISIBLE);
        }

        ImageView bckbtn = (ImageView) findViewById(R.id.back_res);
        bckbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void GetAcademicExamInfo(String examIdLocal) {
        try {
            Cursor c = db.getAcademicExamInfo(examIdLocal);
            if (c.getCount() > 0) {
                if (c.moveToFirst()) {
                    do {
                        examId = c.getString(1);
                        examName = c.getString(2);
                        classMasterId = c.getString(3);
                        sectionName = c.getString(4);
                        className = c.getString(5);
                        fromDate = c.getString(6);
                        toDate = c.getString(7);
                        markStatus = c.getString(8);
                    } while (c.moveToNext());
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void loadAcademicExamDetails(String classSectionId, String ExamId) {
        myList.clear();
        try {
            Cursor c = db.getAcademicExamDetailsList(classSectionId, ExamId);
            if (c.getCount() > 0) {
                if (c.moveToFirst()) {
                    do {
                        AcademicExamDetails lde = new AcademicExamDetails();
                        lde.setId(Integer.parseInt(c.getString(0)));
                        lde.setExamDate(c.getString(4));
                        lde.setSubjectName(c.getString(3));
                        lde.setTimes(c.getString(5));

                        // Add this object into the ArrayList myList
                        myList.add(lde);

                    } while (c.moveToNext());
                }
            } else {
                Toast.makeText(getApplicationContext(), "No records", Toast.LENGTH_LONG).show();
            }

            db.close();

            cadapter = new AcademicExamDetailsListBaseAdapter(AcademicExamDetailPage.this, myList);
            loadMoreListView.setAdapter(cadapter);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == addExamMark) {
            Intent intent = new Intent(getApplicationContext(), AddAcademicExamMarksActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
        if (v == viewExamMark) {
            Intent intent = new Intent(getApplicationContext(), AcademicExamResultView.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }

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

    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public void onError(String error) {

    }
}
