package com.example.firebaseapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class EnrollmentActivity extends AppCompatActivity {
    private ArrayList<String> selectedSubjects;
    private ArrayList<Subject> subjects; // Daftar mata kuliah
    private int totalCredits;
    private final int MAX_CREDITS = 24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);

        // Cek apakah pengguna sudah login
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show();
            finish(); // Tutup aktivitas jika belum login
            return;
        }

        selectedSubjects = new ArrayList<>();
        totalCredits = 0;

        // Inisialisasi daftar mata kuliah
        subjects = new ArrayList<>();
        subjects.add(new Subject("Matematika Diskrit", 3));
        subjects.add(new Subject("Pemrograman Java", 4));
        subjects.add(new Subject("Struktur Data", 3));
        subjects.add(new Subject("Basis Data", 3));
        subjects.add(new Subject("Mobile Programming", 5));
        subjects.add(new Subject("Data Struktur dan Algoritma", 6));
        subjects.add(new Subject("Bahasa Inggris", 0));
        subjects.add(new Subject("Pemrograman C++", 3));
        subjects.add(new Subject("3D Graphics", 4));

        // Temukan RecyclerView dan atur tata letaknya
        RecyclerView subjectList = findViewById(R.id.subjectList);
        subjectList.setLayoutManager(new LinearLayoutManager(this));

        // Inisialisasi adapter dan hubungkan ke RecyclerView
        SubjectAdapter adapter = new SubjectAdapter(subjects, (subject, isSelected) -> {
            if (isSelected) {
                selectedSubjects.add(subject.name);
                totalCredits += subject.credits;
            } else {
                selectedSubjects.remove(subject.name);
                totalCredits -= subject.credits;
            }

            // Perbarui teks ringkasan
            TextView summaryText = findViewById(R.id.summary);
            summaryText.setText("Selected Credits: " + totalCredits);
        });
        subjectList.setAdapter(adapter);

        // Komponen lainnya, seperti tombol submit
        TextView summaryText = findViewById(R.id.summary);
        Button submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(view -> {
            if (totalCredits > MAX_CREDITS) {
                Toast.makeText(this, "Total credits cannot exceed " + MAX_CREDITS, Toast.LENGTH_SHORT).show();
            } else {
                // Ambil User ID dari Firebase Authentication
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                // Buat objek Enrollment untuk disimpan di Firestore
                Enrollment enrollment = new Enrollment(selectedSubjects, totalCredits);

                // Simpan data ke Firestore di koleksi "enrollments"
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                firestore.collection("enrollments").document(userId).set(enrollment)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Enrollment Successful!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Enrollment Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }

    public static class Enrollment {
        public ArrayList<String> subjects;
        public int totalCredits;

        // Konstruktor kosong diperlukan oleh Firestore
        public Enrollment() {
        }

        public Enrollment(ArrayList<String> subjects, int totalCredits) {
            this.subjects = subjects;
            this.totalCredits = totalCredits;
        }
    }

    // Kelas Subject
    public static class Subject {
        public String name;
        public int credits;

        public Subject(String name, int credits) {
            this.name = name;
            this.credits = credits;
        }
    }
}
