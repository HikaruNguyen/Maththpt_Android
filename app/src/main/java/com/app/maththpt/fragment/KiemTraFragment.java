package com.app.maththpt.fragment;


import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import com.app.maththpt.activity.QuestionActivity;
import com.app.maththpt.adapter.CategoryCheckAdapter;
import com.app.maththpt.config.Configuaration;
import com.app.maththpt.database.CategoryDBHelper;
import com.app.maththpt.model.Category;
import com.app.maththpt.widget.DisableScrollRecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class KiemTraFragment extends Fragment {

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
        btnLamBai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Category> categories = new ArrayList<Category>();
                for (int i = 0; i < adapter.getItemCount(); i++) {
                    if (adapter.getItembyPostion(i).isChecked) {
                        categories.add(adapter.getItembyPostion(i));
                    }
                }
                if (categories.size() == 0) {
                    Toast.makeText(getActivity(), getString(R.string.banChuaChonChuDe), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), QuestionActivity.class);
                    intent.putParcelableArrayListExtra("listCate", (ArrayList<? extends Parcelable>) categories);
                    intent.putExtra("type", Configuaration.TYPE_KIEMTRA);
                    intent.putExtra("soCau", soCau);
                    intent.putExtra("time", time);
                    startActivity(intent);
                }

            }
        });
        lnSoCau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.soCauKT));
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_socau, null);
                builder.setView(dialogView);

                builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                NumberPicker np = (NumberPicker) dialog.findViewById(R.id.numberPicker);
                np.setMinValue(5);
                np.setMaxValue(100);
                np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                        tvSoCau.setText(newVal + " " + getString(R.string.question));
                    }
                });
            }
        });

        lnThoiGian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tvThoiGian.setText(selectedHour + " Giờ " + selectedMinute + " Phút");
                    }
                }, hour, minute, true);//Yes 24 hour time

                mTimePicker.setTitle(getString(R.string.thoiGianLamBai));
                mTimePicker.show();
            }
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

}
