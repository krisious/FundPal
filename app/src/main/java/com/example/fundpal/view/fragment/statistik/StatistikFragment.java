package com.example.fundpal.view.fragment.statistik;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fundpal.R;
import com.example.fundpal.view.fragment.pemasukan.PemasukanViewModel;
import com.example.fundpal.view.fragment.pengeluaran.PengeluaranViewModel;

public class StatistikFragment extends Fragment {

    private PemasukanViewModel pemasukanViewModel;
    private PengeluaranViewModel pengeluaranViewModel;

    private TextView tvTotalKeuangan, tvTotalPemasukan, tvTotalPengeluaran;

    public StatistikFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pemasukanViewModel = new ViewModelProvider(this).get(PemasukanViewModel.class);
        pengeluaranViewModel = new ViewModelProvider(this).get(PengeluaranViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistik, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTotalKeuangan = view.findViewById(R.id.tvTotalKeuangan);
        tvTotalPemasukan = view.findViewById(R.id.tvTotalPemasukan);
        tvTotalPengeluaran = view.findViewById(R.id.tvTotalPengeluaran);

        // Observe total pemasukan changes
        pemasukanViewModel.getTotalPemasukan().observe(getViewLifecycleOwner(), totalPemasukan -> {
            // Update the UI with the total pemasukan
            tvTotalPemasukan.setText(String.format("Rp %,d", totalPemasukan));

            // Recalculate total keuangan
            calculateTotalKeuangan();
        });

        // Observe total pengeluaran changes
        pengeluaranViewModel.getTotalPengeluaran().observe(getViewLifecycleOwner(), totalPengeluaran -> {
            // Update the UI with the total pengeluaran
            tvTotalPengeluaran.setText(String.format("Rp %,d", totalPengeluaran));

            // Recalculate total keuangan
            calculateTotalKeuangan();
        });
    }

    private void calculateTotalKeuangan() {
        // Get the values from the TextViews
        long totalPemasukan = getLongValue(tvTotalPemasukan);
        long totalPengeluaran = getLongValue(tvTotalPengeluaran);

        // Calculate total keuangan
        long totalKeuangan = totalPemasukan - totalPengeluaran;
        tvTotalKeuangan.setText(String.format("Rp %,d", totalKeuangan));
    }

    private long getLongValue(TextView textView) {
        try {
            String text = textView.getText().toString().replace("Rp", "").replace(",", "").trim();
            return Long.parseLong(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
