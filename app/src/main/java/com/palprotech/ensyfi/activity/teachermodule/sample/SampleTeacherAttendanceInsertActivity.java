package com.palprotech.ensyfi.activity.teachermodule.sample;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.palprotech.ensyfi.R;
import com.palprotech.ensyfi.activity.teachermodule.TeacherAttendanceInsertActivity;
import com.palprotech.ensyfi.adapter.teachermodule.StudentListBaseAdapter;
import com.palprotech.ensyfi.bean.database.SQLiteHelper;
import com.palprotech.ensyfi.bean.teacher.viewlist.Students;
import com.palprotech.ensyfi.interfaces.DialogClickListener;
import com.palprotech.ensyfi.utils.PreferenceStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

/**
 * Created by Admin on 12-08-2017.
 */

public class SampleTeacherAttendanceInsertActivity extends AppCompatActivity implements DialogClickListener {

    private static final String TAG = TeacherAttendanceInsertActivity.class.getName();
    private Spinner spnClassList;
    SQLiteHelper db;
    Vector<String> vecClassList, vecClassSectionList;
    List<String> lsClassList = new ArrayList<String>();
    ArrayList<Students> myList = new ArrayList<Students>();
    ArrayAdapter<String> adptClassList;
    String set1, set2, set3, AM_PM;
    //    ListView lvStudent;
    ImageView btnSave;
    TextView txtDateTime;
    private String storeClassId;
    String formattedServerDate;
    int valPresent = 0, valAbsent = 0, valLeave = 0, valOD = 0, setAM_PM;
    String lastInsertedId;
    Calendar c = Calendar.getInstance();
    LinearLayout layout_all;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_attendance_insert_sample);
        db = new SQLiteHelper(getApplicationContext());
        vecClassList = new Vector<String>();
        vecClassSectionList = new Vector<String>();
        spnClassList = (Spinner) findViewById(R.id.class_list_spinner);
//        lvStudent = (ListView) findViewById(R.id.listView_students);
        layout_all = (LinearLayout) findViewById(R.id.layout_timetable);
        btnSave = (ImageView) findViewById(R.id.btnSave);
        txtDateTime = (TextView) findViewById(R.id.txtDateTime);
        btnSave.setVisibility(View.VISIBLE);
        getClassList();

        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat serverDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formattedServerDate = serverDF.format(c.getTime());
        SimpleDateFormat slocalDF = new SimpleDateFormat("dd-MM-yyyy");
        String formattedLocalDate = slocalDF.format(c.getTime());
        txtDateTime.setText(formattedLocalDate);

        Calendar now = Calendar.getInstance();
        int a = now.get(Calendar.AM_PM);
        if (a == Calendar.AM) {
            setAM_PM = a;
            AM_PM = String.valueOf(setAM_PM);
        } else {
            setAM_PM = a;
            AM_PM = String.valueOf(setAM_PM);
        }

        spnClassList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String className = parent.getItemAtPosition(position).toString();
                layout_all.removeAllViews();
                GetStudentsList(className);
                btnSave.setVisibility(View.VISIBLE);
//                lvStudent.setAdapter(new StudentListClassTestMarkBaseAdapter(TeacherAttendanceInsertActivity.this, myList));
//                StudentListBaseAdapter cadapter = new StudentListBaseAdapter(SampleTeacherAttendanceInsertActivity.this, myList);
//                lvStudent.setAdapter(cadapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {

                try {

//                    * get all values of the EditText-Fields

                    int getCount = 0;
                    getCount = layout_all.getChildCount();

//                    View view;
                    ArrayList<String> mannschaftsnamen = new ArrayList<String>();
                    TextView et, et1;
                    Spinner spinner;
                    SimpleDateFormat slocalDF = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedLocalInsertDate = slocalDF.format(c.getTime());
                    String checkFlag = "";
                    try {
                        checkFlag = db.isAttendanceFlag(storeClassId, formattedLocalInsertDate, AM_PM);
                    } catch (Exception ex) {
                    }

                    int isAttendanceFlag = Integer.parseInt(checkFlag);

                    if (isAttendanceFlag == 0) {

                        StoreStudentAttendance();

                        int nViews = layout_all.getChildCount();

                        for (int i = 0; i < nViews; i++) {

                            View view = layout_all.getChildAt(i);

                            et = (TextView) view.findViewById(R.id.my_text_1);
                            et1 = (TextView) view.findViewById(R.id.my_text_2);
                            spinner = (Spinner) view.findViewById(R.id.my_edit_text_1);
                            if (et != null) {
                                mannschaftsnamen.add(String.valueOf(et.getText()));
                                String enrollId = String.valueOf(et.getText());
                                String studentName = String.valueOf(et1.getText());
                                String attendanceStatus = String.valueOf(spinner.getSelectedItem());
                                if (attendanceStatus.equalsIgnoreCase("Leave")) {
                                    valLeave = valLeave + 1;
                                    attendanceStatus = "L";
                                } else if (attendanceStatus.equalsIgnoreCase("Absent")) {
                                    valAbsent = valAbsent + 1;
                                    attendanceStatus = "A";
                                } else if (attendanceStatus.equalsIgnoreCase("OD")) {
                                    valOD = valOD + 1;
                                    attendanceStatus = "OD";
                                } else {
                                    valPresent = valPresent + 1;
                                    attendanceStatus = "P";
                                }

                                long c = db.student_attendance_history_insert(lastInsertedId, storeClassId, enrollId, formattedLocalInsertDate, attendanceStatus, AM_PM, "", PreferenceStorage.getTeacherId(getApplicationContext()), formattedServerDate, PreferenceStorage.getUserId(getApplicationContext()), formattedServerDate, "Active", "NS");
                                if (c == -1) {
                                    Toast.makeText(getApplicationContext(), "Error while attendance insert...",
                                            Toast.LENGTH_LONG).show();
                                }
//                                * you can try to log your values EditText
                                Log.v("ypgs", String.valueOf(et.getText()));
                            }
                        }
//                        String totalStudentsCount = String.valueOf(lvStudent.getCount());

                        UpdateLastInsertedStudentAttendance(valLeave, valAbsent, valOD, valPresent, lastInsertedId);
                        SetAttendanceFlag(storeClassId, formattedLocalInsertDate, AM_PM);

                        Toast.makeText(getApplicationContext(), "Attendance Updated Successfully...",
                                Toast.LENGTH_LONG).show();

                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "Attendance taken for this period...",
                                Toast.LENGTH_LONG).show();
                    }
//                btnSave.setVisibility(View.GONE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Try again...", Toast.LENGTH_LONG).show();
                }
            }


        });

        ImageView bckbtn = (ImageView) findViewById(R.id.back_res);

        bckbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void SetAttendanceFlag(String classId, String attendanceDate, String attendancePeriod) {
        try {

            db.student_attendance_flag_insert(classId, attendanceDate, attendancePeriod, "Active");

        } catch (Exception ex) {
        }
    }

    private void UpdateLastInsertedStudentAttendance(int valLeave, int valAbsent, int valOD, int valPresent, String lastInsertedLocalId) {
        try {
            int combinePOD = valOD + valPresent;
            int combineAL = valLeave + valAbsent;

            String noOfPresentOD = String.valueOf(combinePOD);
            String noOfLeaveAbsent = String.valueOf(combineAL);

            db.updateAttendance(noOfPresentOD, noOfLeaveAbsent, lastInsertedLocalId);

        } catch (Exception ex) {
        }
    }

    private void StoreStudentAttendance() {
        try {
            int totalNoOfStudents = layout_all.getChildCount();
            String convertTotalNoOfStudents = String.valueOf(totalNoOfStudents);
            String convertAM_PM = String.valueOf(setAM_PM);

            long c = db.student_attendance_insert(PreferenceStorage.getAcademicYearId(this), storeClassId, convertTotalNoOfStudents, "", "", convertAM_PM, PreferenceStorage.getUserId(this), formattedServerDate, PreferenceStorage.getUserId(this), formattedServerDate, "A", "NS");
            if (c == -1) {
                Toast.makeText(this, "Error while attendance insert...",
                        Toast.LENGTH_LONG).show();
            } else {
                lastInsertedId = String.valueOf(c);
            }
        } catch (Exception ex) {
            Log.println(Log.VERBOSE, TAG, ex.toString());
        }
    }

    private void GetStudentsList(String className) {

        myList.clear();
        try {

            TableLayout layout = new TableLayout(this);
            layout.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            layout_all.setScrollbarFadingEnabled(false);
            layout.setPadding(0, 50, 0, 50);

            TableLayout.LayoutParams rowLp = new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            cellLp.setMargins(2, 2, 2, 2);

            Cursor c = db.getStudentsOfClass(className);
            if (c.getCount() > 0) {
                if (c.moveToFirst()) {
                    int i = 1;
                    do {

                        Students lde = new Students();
                        lde.setId(Integer.parseInt(c.getString(0)));
                        lde.setEnrollId(c.getString(1));
                        lde.setAdmissionId(c.getString(2));
                        lde.setClassId(c.getString(3));
                        storeClassId = c.getString(3);
                        lde.setStudentName(c.getString(4));
                        lde.setClassSection(c.getString(5));

                        // Add this object into the ArrayList myList
//                        myList.add(lde);

                        for (int c1 = 0; c1 <= 0; c1++) {

                            LinearLayout cell = new LinearLayout(this);
                            cell.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
//                            cell.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            cell.setOrientation(LinearLayout.HORIZONTAL);
                            cell.setPadding(20, 5, 20, 5);
                            cell.setBackgroundColor(Color.parseColor("#FFFFFF"));


                            TextView t1 = new TextView(this);
                            t1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT, 0.10f));
                            t1.setGravity(Gravity.CENTER);

                            TextView t2 = new TextView(this);
                            t2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT, 0.50f));

                            Spinner b1 = new Spinner(this);
                            b1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT, 0.30f));
                            b1.setGravity(Gravity.CENTER);
                            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.attendance, android.R.layout.simple_spinner_item);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            b1.setAdapter(adapter);
                            b1.setId(R.id.my_edit_text_1);

                            /*EditText b = new EditText(this);
                            b.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT, 0.30f));
                            b.setGravity(Gravity.CENTER);

                            String name = "";

                            b.setText(name);
                            b.setId(R.id.my_edit_text_1);
                            b.requestFocusFromTouch();
                            b.setTextSize(13.0f);
                            b.setTypeface(null, Typeface.BOLD);
                            b.setKeyListener(DigitsKeyListener.getInstance("0123456789A"));
                            b.setInputType(InputType.TYPE_CLASS_TEXT);
                            b.setAllCaps(true);
                            b.setSingleLine(true);
                            b.setTextColor(Color.parseColor("#FF68358E"));
                            b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub

                                }
                            });
                            b.setPressed(true);
                            b.setHeight(120);
                            b.setWidth(100);
                            b.setPadding(1, 0, 2, 0);*/

                            t1.setText(c.getString(1));
                            t1.setTextColor(Color.parseColor("#FF68358E"));
                            t1.setHeight(120);
                            t1.setWidth(100);
                            t1.setPadding(1, 0, 2, 0);
                            t1.setId(R.id.my_text_1);

                            t2.setText(c.getString(4));
                            t2.setTextColor(Color.parseColor("#FF68358E"));
                            t2.setHeight(120);
                            t2.setWidth(100);
                            t2.setPadding(1, 0, 2, 0);
                            t2.setId(R.id.my_text_2);


                            cell.addView(t1);
                            cell.addView(t2);
                            cell.addView(b1);

                            layout_all.addView(cell);
                        }
                        i++;

                    } while (c.moveToNext());
                }
            }

            db.close();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG)
                    .show();
            e.printStackTrace();
        }
    }

    private void getClassList() {

        try {
            Cursor c = db.getTeachersClass();
            if (c.getCount() > 0) {
                if (c.moveToFirst()) {
                    do {
                        vecClassList.add(c.getString(1));
                        set3 = c.getString(0);
//                        vecClassSectionList.add(c.getString(1));

                    } while (c.moveToNext());
                }
            }
            for (int i = 0; i < vecClassList.size(); i++) {
                lsClassList.add(vecClassList.get(i));
            }
            HashSet hs = new HashSet();
            TreeSet ts = new TreeSet(hs);
            ts.addAll(lsClassList);
            lsClassList.clear();
//            lsStudent.add("Select");
            lsClassList.addAll(ts);
            db.close();
            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this, R.layout.spinner_item_ns, lsClassList);
//            adptStudent = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, lsStudent);
//            adptStudent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnClassList.setAdapter(dataAdapter3);
            spnClassList.setWillNotDraw(false);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }
}