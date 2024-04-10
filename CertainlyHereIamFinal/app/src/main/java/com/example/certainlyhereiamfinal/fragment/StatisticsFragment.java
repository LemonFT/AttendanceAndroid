package com.example.certainlyhereiamfinal.fragment;

import static com.example.certainlyhereiamfinal.Global.COLOR_CUSTOM;
import static com.example.certainlyhereiamfinal.Global.REQUIRED_PERMISSIONS_STORAGE;
import static com.example.certainlyhereiamfinal.Global.permissionStorageGranted;
import static com.example.certainlyhereiamfinal.Global.requestPermissionStorage;
import static com.example.certainlyhereiamfinal.Global.showAlert;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.security.identity.CredentialDataResult;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import com.example.certainlyhereiamfinal.R;
import com.example.certainlyhereiamfinal.model.Attendances;
import com.example.certainlyhereiamfinal.model.ExcelExport;
import com.example.certainlyhereiamfinal.model.Statistics;
import com.example.certainlyhereiamfinal.store.DataLocalManager;
import com.example.certainlyhereiamfinal.viewmodel.AttendanceViewModel;
import com.example.certainlyhereiamfinal.viewmodel.MemberViewModel;
import com.example.certainlyhereiamfinal.viewmodel.SessionViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatisticsFragment extends Fragment {
    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final int NOTI_ID = 99;
    private Button button;
    private AttendanceViewModel attendanceViewModel;
    private View view;
    private List<Statistics> statistics;
    private int focusSession;
    private int total_member;
    private TextView room, time, checked, nochecked, number_member, total_session, participation_rate;
    private ImageView back, next;
    private MemberViewModel memberViewModel;
    private SessionViewModel sessionViewModel;
    private int awaitApi = 0;
    private boolean pieClear = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.statistic_fragment, container, false);
        attendanceViewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);
        memberViewModel = new ViewModelProvider(this).get(MemberViewModel.class);
        sessionViewModel = new ViewModelProvider(this).get(SessionViewModel.class);
        view = root;

        button = root.findViewById(R.id.button);
        room = root.findViewById(R.id.sta_room);
        time = root.findViewById(R.id.sta_time);
        checked = root.findViewById(R.id.sta_checked);
        nochecked = root.findViewById(R.id.sta_nochecked);
        back = root.findViewById(R.id.sta_back);
        next = root.findViewById(R.id.sta_next);
        number_member = root.findViewById(R.id.sta_number_member);
        total_session = root.findViewById(R.id.sta_total_session);
        participation_rate = root.findViewById(R.id.participation_rate);

        Long classId = DataLocalManager.getClassId();
        loadData(classId);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statistics != null && focusSession > 0) {
                    focusSession--;
                    statisticSession(focusSession);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statistics != null && focusSession < statistics.size() - 1) {
                    focusSession++;
                    statisticSession(focusSession);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!permissionStorageGranted(view.getContext())){
                    requestPermissionStorage(StatisticsFragment.this.getActivity());
                }else{
                    exportToExcel("hereiam_"+new Date().getTime()+".xlsx");
                }
            }
        });
        return root;
    }

    private void loadData(Long classId) {
        attendanceViewModel.findAttendanceByClassroomId(classId)
                .observe((LifecycleOwner) view.getContext(), data -> {
                    if (data != null) {
                        statistics = data;
                        awaitApi++;
                        if (awaitApi == 3 && statistics != null) {
                            focusSession = 0;
                            statisticSession(0);
                        }
                    } else {

                    }
                });
        memberViewModel.getNumberMember(DataLocalManager.getClassId()).observe((LifecycleOwner) view.getContext(), data -> {
            number_member.setText("Members: " + data);
            total_member = data;
            awaitApi++;
            if (awaitApi == 3 && statistics != null) {
                focusSession = 0;
                statisticSession(0);
            }
        });
        sessionViewModel.findAllSessionClass(DataLocalManager.getClassId()).observe((LifecycleOwner) requireContext(), data -> {
            if (data != null) {
                total_session.setText("Total sessions: " + data.size());
                awaitApi++;
                if (awaitApi == 3 && statistics != null) {
                    focusSession = 0;
                    statisticSession(0);
                }
            } else {
                total_session.setText("Total sessions: " + 0);
            }
        });

    }

    private void statisticSession(int index) {
        if(statistics == null || statistics.isEmpty()){
            return;
        }
        Statistics statistics1 = statistics.get(index);
        room.setText(statistics1.getRoom());
        time.setText("Time: " + formatDate(statistics1.getInit_session(), "HH:mm:ss dd-MM-yyyy"));
        Long att = statistics1.getCheckedInCount();
        Long noatt = total_member - att;
        float rateJoin = (float) (att * 100.0 / total_member);
        loadPieChart(att, noatt);
        checked.setText("Checked in: " + att);
        nochecked.setText("No checked in: " + noatt);
        participation_rate.setText("Participation rate of session: " + String.format("%.2f", rateJoin) + "%");
    }

    public void loadPieChart(Long check, Long nocheck) {
        PieChart pieChart = view.findViewById(R.id.pie_chart);
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(check,"Check in"));
        entries.add(new PieEntry(nocheck, "No Check in"));
        PieDataSet pieDataSet = new PieDataSet(entries, "Attendance Statistics");
        pieDataSet.setColors(COLOR_CUSTOM);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(13);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1000);
        pieChart.invalidate();
    }

    public static String formatDate(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }


    private void exportToExcel(String filename) {
        List<ExcelExport> excelExports = new ArrayList<>();
        attendanceViewModel.statisticAttendanceAllSession(DataLocalManager.getClassId()).observe((LifecycleOwner) StatisticsFragment.this, data -> {
            if (data == null || data.isEmpty()) {
                showAlert("No data available for export", view.getContext());
                return;
            }
            File file;
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Statistic");
            Row headerRow = sheet.createRow(1);
            int columnHeaderStart = 3;
            for (ExcelExport excelExport : data) {
                headerRow.createCell(columnHeaderStart).setCellValue(excelExport.getSession().getRoom());
                columnHeaderStart++;
            }

            for (int i = 0; i < 3; i++) {
                sheet.setColumnWidth(i, 5000);
                sheet.setHorizontallyCenter(true);
                sheet.setVerticallyCenter(true);
            }

            for (int i = 3; i < columnHeaderStart; i++) {
                sheet.setColumnWidth(i, 2000);
                sheet.setHorizontallyCenter(true);
                sheet.setVerticallyCenter(true);
            }


            List<Attendances> attendances = data.get(0).getAttendances();
            int rowIf = 2;
            for (int i = 0; i < attendances.size(); i++) {
                Row row = sheet.createRow(rowIf);
                row.createCell(0).setCellValue(attendances.get(i).getEmail());
                row.createCell(1).setCellValue(attendances.get(i).getName());
                row.createCell(2).setCellValue(attendances.get(i).getIdentifier());
                rowIf++;
            }


            int rowCount = 2;
            for (int j = 0; j < data.get(0).getAttendances().size(); j++) {
                Row containerRow = sheet.getRow(rowCount);
                int column = 3;
                for (int i = 0; i < data.size(); i++) {
                    String check = data.get(i).getAttendances().get(j).isChecked() ? "X" : "";
                    containerRow.createCell(column).setCellValue(check);
                    column++;
                }
                rowCount++;
            }

            try {
                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                file = new File(directory, filename);
                FileOutputStream fileOut = new FileOutputStream(file);
                workbook.write(fileOut);
                fileOut.close();
                sendNotification();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void sendNotification(){
        Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
        PendingIntent pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifi)
                .setContentTitle("File download successful")
                .setContentText("Click to open file")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
        if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(NOTI_ID, builder.build());
    }
}
