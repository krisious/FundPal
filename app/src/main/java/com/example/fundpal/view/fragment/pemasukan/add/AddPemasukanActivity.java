package com.example.fundpal.view.fragment.pemasukan.add;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.fundpal.R;
import com.example.fundpal.model.ModelDatabase;
import com.example.fundpal.view.MainActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddPemasukanActivity extends AppCompatActivity {
    private static final String KEY_IS_EDIT = "key_is_edit";
    private static final String KEY_DATA = "key_data";

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private ImageView ivGambar;
    private String imageFilePath = null;

    public static void startActivity(Context context, boolean isEdit, ModelDatabase pemasukan) {
        Intent intent = new Intent(new Intent(context, AddPemasukanActivity.class));
        intent.putExtra(KEY_IS_EDIT, isEdit);
        intent.putExtra(KEY_DATA, pemasukan);
        context.startActivity(intent);
    }

    private AddPemasukanViewModel addPemasukanViewModel;

    private boolean mIsEdit = false;
    private int strId = 0;

    private Toolbar toolbar;
    private TextInputEditText etKeterangan, etTanggal, etJmlUang;
    private Button btnSimpan, btnCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data);

        toolbar = findViewById(R.id.toolbar);
        etKeterangan = findViewById(R.id.etKeterangan);
        etTanggal = findViewById(R.id.etTanggal);
        etJmlUang = findViewById(R.id.etJmlUang);
        btnSimpan = findViewById(R.id.btnSimpan);
        ivGambar = findViewById(R.id.ivGambar);
        btnCapture = findViewById(R.id.btnCapture);

        btnCapture.setOnClickListener(v -> dispatchTakePictureIntent());

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        addPemasukanViewModel = ViewModelProviders.of(this).get(AddPemasukanViewModel.class);

        loadData();
        initAction();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.fundpal.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (imageFilePath != null) {
                Glide.with(this).load(imageFilePath).into(ivGambar);
            }
        }
    }

    private void loadData() {
        mIsEdit = getIntent().getBooleanExtra(KEY_IS_EDIT, false);
        if (mIsEdit) {
            ModelDatabase pemasukan = getIntent().getParcelableExtra(KEY_DATA);
            if (pemasukan != null) {
                strId = pemasukan.uid;
                String keterangan = pemasukan.keterangan;
                String tanggal = pemasukan.tanggal;
                int uang = pemasukan.jmlUang;

                etKeterangan.setText(keterangan);
                etTanggal.setText(tanggal);
                etJmlUang.setText(String.valueOf(uang));

                // Load gambar dari path pada ModelDatabase
                if (pemasukan.imagePath != null) {
                    Glide.with(this).load(pemasukan.imagePath).into(ivGambar);
                    imageFilePath = pemasukan.imagePath;
                }
            }
        }
    }

    private void initAction() {
        etTanggal.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String strFormatDefault = "d MMMM yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strFormatDefault, Locale.getDefault());
                etTanggal.setText(simpleDateFormat.format(calendar.getTime()));
            };

            new DatePickerDialog(AddPemasukanActivity.this, date,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnSimpan.setOnClickListener(v -> {
            String strTipe = "pemasukan";
            String strKeterangan = etKeterangan.getText().toString();
            String strTanggal = etTanggal.getText().toString();
            String strJmlUang = etJmlUang.getText().toString();

            if (strKeterangan.isEmpty() || strTanggal.isEmpty() || strJmlUang.isEmpty()) {
                Toast.makeText(AddPemasukanActivity.this, "Ups, form tidak boleh kosong!",
                        Toast.LENGTH_SHORT).show();
            } else {
                if (mIsEdit) {
                    addPemasukanViewModel.updatePemasukan(strId, strKeterangan, strTanggal,
                            Integer.parseInt(strJmlUang), imageFilePath);
                } else {
                    addPemasukanViewModel.addPemasukan(strTipe, strKeterangan, strTanggal,
                            Integer.parseInt(strJmlUang), imageFilePath);
                }
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(AddPemasukanActivity.this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
