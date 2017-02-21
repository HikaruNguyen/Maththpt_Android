package com.app.maththpt.fragment;


import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.maththpt.R;
import com.app.maththpt.activity.MainActivity;
import com.app.maththpt.activity.QuestionActivity;
import com.app.maththpt.adapter.CategoryCheckAdapter;
import com.app.maththpt.config.Configuaration;
import com.app.maththpt.database.CategoryDBHelper;
import com.app.maththpt.model.Category;
import com.app.maththpt.widget.DisableScrollRecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class KiemTraFragment extends Fragment {
    private static final int CODE_CHAM_DIEM = 12;
    private DisableScrollRecyclerView rvCategory;
    private CategoryCheckAdapter adapter;
    private List<Category> list;
    private LinearLayout lnSoCau, lnThoiGian;
    private TextView tvSoCau, tvThoiGian;
    private Button btnLamBai;
    private int soCau = 5;
    private long time;

    public KiemTraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kiem_tra, container, false);
        initUI(view);
        bindData();
        event();
        return view;
    }

    private void event() {
        btnLamBai.setOnClickListener(view -> {

            List<Category> categories = new ArrayList<>();
            for (int i = 0; i < adapter.getItemCount(); i++) {
                if (adapter.getItembyPostion(i).isChecked) {
                    categories.add(adapter.getItembyPostion(i));
                }
            }
            if (categories.size() == 0) {
                Toast.makeText(getActivity(), getString(R.string.banChuaChonChuDe),
                        Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), QuestionActivity.class);
                intent.putParcelableArrayListExtra(
                        "listCate", (ArrayList<? extends Parcelable>) categories);
                intent.putExtra("type", Configuaration.TYPE_KIEMTRA);
                intent.putExtra("soCau", soCau);
                intent.putExtra("time", time);
                startActivityForResult(intent, CODE_CHAM_DIEM);
            }

        });
        lnSoCau.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.soCauKT));
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_socau, null);
            builder.setView(dialogView);

            builder.setPositiveButton(
                    getString(R.string.ok), (dialogInterface, i) -> dialogInterface.dismiss());
            builder.setNegativeButton(
                    getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
            NumberPicker np = (NumberPicker) dialog.findViewById(R.id.numberPicker);
            np.setMinValue(5);
            np.setMaxValue(100);
            np.setOnValueChangedListener((picker, oldVal, newVal) -> {
                tvSoCau.setText(newVal + " " + getString(R.string.question));
                soCau = newVal;
            });
        });

        lnThoiGian.setOnClickListener(view -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getActivity(), (view1, hourOfDay, minute1) -> {
                tvThoiGian.setText(hourOfDay + " Giờ " + minute1 + " Phút");
                time = (hourOfDay * 60 * 60 + minute1 * 60) * 1000;
            }, hour, minute, true);
            mTimePicker.setTitle(getString(R.string.thoiGianLamBai));
            mTimePicker.show();
        });
    }

    private void bindData() {
        time = (long) (0.5 * 60 * 1000);
        list = CategoryDBHelper.getAllListCategory(getActivity());
        adapter.addAll(list);
    }

    private void initUI(View view) {
        rvCategory = (DisableScrollRecyclerView) view.findViewById(R.id.rvCategory);
        adapter = new CategoryCheckAdapter(getActivity(), new ArrayList<Category>());
        rvCategory.setAdapter(adapter);

        lnSoCau = (LinearLayout) view.findViewById(R.id.lnSoCau);
        lnThoiGian = (LinearLayout) view.findViewById(R.id.lnThoiGian);
        tvSoCau = (TextView) view.findViewById(R.id.tvSoCau);
        tvThoiGian = (TextView) view.findViewById(R.id.tvThoiGian);

        btnLamBai = (Button) view.findViewById(R.id.btnLamBai);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CODE_CHAM_DIEM) {
                ((MainActivity) getActivity()).clearBackStack(getActivity().getSupportFragmentManager());
                ((MainActivity) getActivity()).changeFragment(new HistoryFragment());
                ((MainActivity) getActivity()).setMenuSelect(R.id.nav_history);
            }
        }

    }
}
