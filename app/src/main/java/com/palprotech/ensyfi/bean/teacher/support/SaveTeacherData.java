package com.palprotech.ensyfi.bean.teacher.support;

import android.content.Context;

import com.palprotech.ensyfi.bean.database.SQLiteHelper;
import com.palprotech.ensyfi.utils.PreferenceStorage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Admin on 04-07-2017.
 */

public class SaveTeacherData {

    private Context context;
    SQLiteHelper database;

    public SaveTeacherData(Context context) {
        this.context = context;
    }

    public void saveTeacherProfile(JSONObject teacherProfile) {
        try {
            JSONObject getTeacherProfile = teacherProfile.getJSONObject("0");

            //Student Details
            String TeacherId = "";
            String TeacherName = "";
            String TeacherGender = "";
            String TeacherAge = "";
            String TeacherNationality = "";
            String TeacherReligion = "";
            String TeacherCaste = "";
            String TeacherCommunity = "";
            String TeacherAddress = "";
            String TeacherSubject = "";
            String ClassTeacher = "";
            String TeacherMobile = "";
            String TeacherSecondaryMobile = "";
            String TeacherMail = "";
            String TeacherSecondaryMail = "";
            String TeacherPic = "";
            String TeacherSectionName = "";
            String TeacherClassName = "";
            String TeacherClassTaken = "";

            TeacherId = getTeacherProfile.getString("admission_id");
            TeacherName = getTeacherProfile.getString("admisn_year");
            TeacherGender = getTeacherProfile.getString("admisn_no");
            TeacherAge = getTeacherProfile.getString("emsi_num");
            TeacherNationality = getTeacherProfile.getString("admisn_date");
            TeacherReligion = getTeacherProfile.getString("name");
            TeacherCaste = getTeacherProfile.getString("sex");
            TeacherCommunity = getTeacherProfile.getString("dob");
            TeacherAddress = getTeacherProfile.getString("age");
            TeacherSubject = getTeacherProfile.getString("nationality");
            ClassTeacher = getTeacherProfile.getString("religion");
            TeacherMobile = getTeacherProfile.getString("community_class");
            TeacherSecondaryMobile = getTeacherProfile.getString("community");
            TeacherMail = getTeacherProfile.getString("parnt_guardn");
            TeacherSecondaryMail = getTeacherProfile.getString("parnt_guardn_id");
            TeacherPic = getTeacherProfile.getString("mother_tongue");
            TeacherSectionName = getTeacherProfile.getString("language");
            TeacherClassName = getTeacherProfile.getString("mobile");
            TeacherClassTaken = getTeacherProfile.getString("sec_mobile");


            // Parents Preference - Student Admission Id
            if ((TeacherId != null) && !(TeacherId.isEmpty()) && !TeacherId.equalsIgnoreCase("null")) {
                PreferenceStorage.saveTeacherId(context, TeacherId);
            }

            // Parents Preference - Student Admission Year
            if ((TeacherName != null) && !(TeacherName.isEmpty()) && !TeacherName.equalsIgnoreCase("null")) {
                PreferenceStorage.saveTeacherName(context, TeacherName);
            }

            // Parents Preference - Student Admission Number
            if ((TeacherGender != null) && !(TeacherGender.isEmpty()) && !TeacherGender.equalsIgnoreCase("null")) {
                PreferenceStorage.saveTeacherGender(context, TeacherGender);
            }

            // Parents Preference - Student Emsi Number
            if ((TeacherAge != null) && !(TeacherAge.isEmpty()) && !TeacherAge.equalsIgnoreCase("null")) {
                PreferenceStorage.saveTeacherAge(context, TeacherAge);
            }

            // Parents Preference - Student Admission Date
            if ((TeacherNationality != null) && !(TeacherNationality.isEmpty()) && !TeacherNationality.equalsIgnoreCase("null")) {
                PreferenceStorage.saveTeacherNationality(context, TeacherNationality);
            }

            // Parents Preference - Student Name
            if ((TeacherReligion != null) && !(TeacherReligion.isEmpty()) && !TeacherReligion.equalsIgnoreCase("null")) {
                PreferenceStorage.saveTeacherReligion(context, TeacherReligion);
            }

            // Parents Preference - Student Gender
            if ((TeacherCaste != null) && !(TeacherCaste.isEmpty()) && !TeacherCaste.equalsIgnoreCase("null")) {
                PreferenceStorage.saveTeacherCaste(context, TeacherCaste);
            }

            // Parents Preference - Student Date Of Birth
            if ((TeacherCommunity != null) && !(TeacherCommunity.isEmpty()) && !TeacherCommunity.equalsIgnoreCase("null")) {
                PreferenceStorage.saveTeacherCommunity(context, TeacherCommunity);
            }

            // Parents Preference - Student Age
            if ((TeacherAddress != null) && !(TeacherAddress.isEmpty()) && !TeacherAddress.equalsIgnoreCase("null")) {
                PreferenceStorage.saveTeacherAddress(context, TeacherAddress);
            }

            // Parents Preference - Student Nationality
            if ((TeacherSubject != null) && !(TeacherSubject.isEmpty()) && !TeacherSubject.equalsIgnoreCase("null")) {
                PreferenceStorage.saveTeacherSubject(context, TeacherSubject);
            }

            // Parents Preference - Student Religion
            if ((ClassTeacher != null) && !(ClassTeacher.isEmpty()) && !ClassTeacher.equalsIgnoreCase("null")) {
                PreferenceStorage.saveClassTeacher(context, ClassTeacher);
            }

            // Parents Preference - Student Caste
            if ((TeacherMobile != null) && !(TeacherMobile.isEmpty()) && !TeacherMobile.equalsIgnoreCase("null")) {
                PreferenceStorage.saveTeacherMobile(context, TeacherMobile);
            }

            // Parents Preference - Student Community
            if ((TeacherSecondaryMobile != null) && !(TeacherSecondaryMobile.isEmpty()) && !TeacherSecondaryMobile.equalsIgnoreCase("null")) {
                PreferenceStorage.saveTeacherSecondaryMobile(context, TeacherSecondaryMobile);
            }

            // Parents Preference - Student Parent Or Guardian
            if ((TeacherMail != null) && !(TeacherMail.isEmpty()) && !TeacherMail.equalsIgnoreCase("null")) {
                PreferenceStorage.saveTeacherMail(context, TeacherMail);
            }

            // Parents Preference - Student Parent Or Guardian Id
            if ((TeacherSecondaryMail != null) && !(TeacherSecondaryMail.isEmpty()) && !TeacherSecondaryMail.equalsIgnoreCase("null")) {
                PreferenceStorage.saveTeacherSecondaryMail(context, TeacherSecondaryMail);
            }

            // Parents Preference - Student Mother Tongue
            if ((TeacherPic != null) && !(TeacherPic.isEmpty()) && !TeacherPic.equalsIgnoreCase("null")) {
                PreferenceStorage.saveTeacherPic(context, TeacherPic);
            }

            // Parents Preference - Student Language
            if ((TeacherSectionName != null) && !(TeacherSectionName.isEmpty()) && !TeacherSectionName.equalsIgnoreCase("null")) {
                PreferenceStorage.saveTeacherSectionName(context, TeacherSectionName);
            }

            // Parents Preference - Student Mobile
            if ((TeacherClassName != null) && !(TeacherClassName.isEmpty()) && !TeacherClassName.equalsIgnoreCase("null")) {
                PreferenceStorage.saveTeacherClassName(context, TeacherClassName);
            }

            // Parents Preference - Student Secondary Mobile
            if ((TeacherClassTaken != null) && !(TeacherClassTaken.isEmpty()) && !TeacherClassTaken.equalsIgnoreCase("null")) {
                PreferenceStorage.saveTeacherClassTaken(context, TeacherClassTaken);
            }


        } catch (Exception ex) {
        }
    }

    public void saveTeacherTimeTable(JSONArray timeTable) {
        database = new SQLiteHelper(context);
        try {
            database.deleteTeacherTimeTable();

            for (int i = 0; i < timeTable.length(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                JSONObject jsonobj = timeTable.getJSONObject(i);

                String table_id = "";
                String class_id = "";
                String subject_id = "";
                String subject_name = "";
                String teacher_id = "";
                String name = "";
                String day = "";
                String period = "";
                String sec_name = "";
                String class_name = "";

                table_id = jsonobj.getString("table_id");
                class_id = jsonobj.getString("class_id");
                subject_id = jsonobj.getString("subject_id");
                subject_name = jsonobj.getString("subject_name");
                teacher_id = jsonobj.getString("teacher_id");
                name = jsonobj.getString("name");
                day = jsonobj.getString("day");
                period = jsonobj.getString("period");
                sec_name = jsonobj.getString("sec_name");
                class_name = jsonobj.getString("class_name");

                System.out.println("table_id : " + i + " = " + table_id);
                System.out.println("class_id : " + i + " = " + class_id);
                System.out.println("subject_id : " + i + " = " + subject_id);
                System.out.println("subject_name : " + i + " = " + subject_name);
                System.out.println("teacher_id : " + i + " = " + teacher_id);
                System.out.println("name : " + i + " = " + name);
                System.out.println("day : " + i + " = " + day);
                System.out.println("period : " + i + " = " + period);
                System.out.println("sec_name : " + i + " = " + sec_name);
                System.out.println("class_name : " + i + " = " + class_name);

                for (int f = 1; f <= 6; f++) {
                    for (int c1 = 1; c1 <= 8; c1++) {

                    }
                }

                String v1 = table_id,
                        v2 = class_id,
                        v3 = subject_id,
                        v4 = subject_name,
                        v5 = teacher_id,
                        v6 = name,
                        v7 = day,
                        v8 = period,
                        v9 = sec_name,
                        v10 = class_name;

                database.teacher_timetable_insert(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

