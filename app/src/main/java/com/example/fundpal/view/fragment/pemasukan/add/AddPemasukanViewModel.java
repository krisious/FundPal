package com.example.fundpal.view.fragment.pemasukan.add;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.fundpal.database.DatabaseClient;
import com.example.fundpal.database.dao.DatabaseDao;
import com.example.fundpal.model.ModelDatabase;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddPemasukanViewModel extends AndroidViewModel {
    private DatabaseDao databaseDao;

    public AddPemasukanViewModel(@NonNull Application application) {
        super(application);

        databaseDao = DatabaseClient.getInstance(application).getAppDatabase().databaseDao();
    }

    public void addPemasukan(final String type, final String note, final String date, final int price, final String imagePath) {
        Completable.fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        ModelDatabase pemasukan = new ModelDatabase();
                        pemasukan.tipe = type;
                        pemasukan.keterangan = note;
                        pemasukan.tanggal = date;
                        pemasukan.jmlUang = price;
                        pemasukan.imagePath = imagePath; // Set imagePath to the model
                        databaseDao.insertPemasukan(pemasukan);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void updatePemasukan(final int uid, final String note, final String date, final int price, final String imagePath) {
        Completable.fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        databaseDao.updateDataPemasukan(note, date, price, imagePath, uid);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
