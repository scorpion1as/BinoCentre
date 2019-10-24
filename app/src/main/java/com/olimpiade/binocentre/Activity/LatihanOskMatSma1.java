package com.olimpiade.binocentre.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.olimpiade.binocentre.Model.Pertanyaan;
import com.olimpiade.binocentre.R;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class LatihanOskMatSma1 extends AppCompatActivity {
    Button b1, b2, b3, b4;
    TextView timerTxt, SkorTxt;
    int total = 0;
    int correct = 0;
    DatabaseReference reference;
    TimerClass timerClass;
    ProgressDialog progressDialog;
    ImageView imageView, imageViewSoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latihan_osk_mat_sma1);
        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.button3);
        b4 = findViewById(R.id.button4);
        imageViewSoal = findViewById(R.id.imageView1);
        timerTxt = findViewById(R.id.timerTxt);
        SkorTxt = findViewById(R.id.SkorTxt);
        imageView = findViewById(R.id.imageView);

        progressDialog = new ProgressDialog(LatihanOskMatSma1.this);
        progressDialog.show();
        progressDialog.setCancelable(false);

        timerClass = new TimerClass(60000 * 3, 1000);
        timerClass.start();

        updateQuestion();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("OSK SMA");
        getSupportActionBar().setSubtitle("      2018");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void updateQuestion() {
        total++;
        if (total > 2) {
            timerClass.onFinish();
        } else {
            reference = FirebaseDatabase.getInstance().getReference().child("OskMatSmaA1").child(String.valueOf(total));
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressDialog.cancel();
                    final Pertanyaan pertanyaan = dataSnapshot.getValue(Pertanyaan.class);
                    Picasso.with(getBaseContext())
                            .load(pertanyaan.getPertanyaan())
                            .into(imageViewSoal);
                    b1.setText(pertanyaan.getPilihan1());
                    b2.setText(pertanyaan.getPilihan2());
                    b3.setText(pertanyaan.getPilihan3());
                    b4.setText(pertanyaan.getPilihan4());
                    Picasso.with(getBaseContext())
                            .load(pertanyaan.getPembahasan())
                            .into(imageView);
                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (b1.getText().toString().equals(pertanyaan.getJawaban())) {
                                correct++;
                                updateQuestion();
                                SkorTxt.setText("Skor : " + String.valueOf(correct * 10));
                            } else updateQuestion();
                        }
                    });
                    b2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (b2.getText().toString().equals(pertanyaan.getJawaban())) {
                                correct++;
                                SkorTxt.setText("Skor : " + String.valueOf(correct * 10));
                                updateQuestion();
                            } else updateQuestion();
                        }
                    });
                    b3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (b3.getText().toString().equals(pertanyaan.getJawaban())) {
                                correct++;
                                SkorTxt.setText("Skor : " + String.valueOf(correct * 10));
                                updateQuestion();
                            } else updateQuestion();
                        }
                    });
                    b4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (b4.getText().toString().equals(pertanyaan.getJawaban())) {
                                correct++;
                                SkorTxt.setText("Skor : " + String.valueOf(correct * 10));
                                updateQuestion();
                            } else updateQuestion();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public class TimerClass extends CountDownTimer {

        TimerClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            String waktu = String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
            timerTxt.setText(waktu);
        }

        @Override
        public void onFinish() {
            showDialog();
            timerClass.cancel();
        }

        private void showDialog() {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LatihanOskMatSma1.this);
            alertDialogBuilder.setTitle("SKOR ANDA");
            alertDialogBuilder
                    .setMessage("                    "+correct * 10)
                    .setIcon(R.mipmap.ic_launcher)
                    .setCancelable(false)
                    .setPositiveButton("Ulangi", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(LatihanOskMatSma1.this,LatihanOskMatSma1.class));
                            progressDialog.show();
                            finish();
                        }
                    })
                    .setNegativeButton("Halaman Utama", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(LatihanOskMatSma1.this,MainActivity.class));
                            finish();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}

