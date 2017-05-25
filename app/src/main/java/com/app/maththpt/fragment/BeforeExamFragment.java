package com.app.maththpt.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.app.maththpt.R;
import com.app.maththpt.activity.LoginActivity;
import com.app.maththpt.activity.MainActivity;
import com.app.maththpt.activity.MyApplication;
import com.app.maththpt.activity.QuestionActivity;
import com.app.maththpt.adapter.CategoryCheckAdapter;
import com.app.maththpt.config.Configuaration;
import com.app.maththpt.databinding.FragmentBeforeExamBinding;
import com.app.maththpt.model.Category;
import com.app.maththpt.realm.CategoryModule;
import com.app.maththpt.utils.FacebookUtils;
import com.app.maththpt.viewmodel.BeforeExamViewModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class BeforeExamFragment extends Fragment {
    private static final int CODE_CHAM_DIEM = 12;
    private CategoryCheckAdapter adapter;
    private int soCau = 50;
    private long time = 90;
    private FragmentBeforeExamBinding beforeExamBinding;
    private BeforeExamViewModel beforeExamViewModel;

    public BeforeExamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        beforeExamBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_before_exam, container, false);
        beforeExamViewModel = new BeforeExamViewModel(getActivity(), soCau, time * 60 * 1000);
        beforeExamBinding.setBeforeExamViewModel(beforeExamViewModel);
        View view = beforeExamBinding.getRoot();
        initUI();
        bindData();
        event();
        return view;
    }

    private void event() {
        beforeExamBinding.btnLamBai.setOnClickListener(view -> {

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
                SharedPreferences sharedPreferences = getActivity().
                        getSharedPreferences(Configuaration.Pref, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String token = sharedPreferences.getString(Configuaration.KEY_TOKEN, "");
                if (!FacebookUtils.getFacebookID().isEmpty() || !token.isEmpty()) {
                    Intent intent = new Intent(getActivity(), QuestionActivity.class);
                    intent.putExtra("soCau", soCau);
                    intent.putExtra("time", time * 60 * 1000);
                    intent.putExtra("type", Configuaration.TYPE_EXAM);
                    intent.putParcelableArrayListExtra(
                            "listCate", (ArrayList<? extends Parcelable>) categories);
                    startActivityForResult(intent, CODE_CHAM_DIEM);

                } else {
                    int num = sharedPreferences.getInt(Configuaration.KEY_FREE_TESTS_NUM, 0);
                    if (num < Configuaration.FREE_TESTS_NUM) {
                        num++;
                        editor.putInt(Configuaration.KEY_FREE_TESTS_NUM, num);
                        editor.apply();
                        Intent intent = new Intent(getActivity(), QuestionActivity.class);
                        intent.putExtra("type", Configuaration.TYPE_EXAM);
                        intent.putExtra("soCau", soCau);
                        intent.putExtra("time", time * 60 * 1000);
                        intent.putParcelableArrayListExtra(
                                "listCate", (ArrayList<? extends Parcelable>) categories);
                        startActivityForResult(intent, CODE_CHAM_DIEM);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(getString(R.string.loginToExam));
                        builder.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        });
                        builder.setNegativeButton(
                                getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
                        builder.show();
                    }
                }

            }

        });
        beforeExamBinding.lnSoCau.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.soCauKT));
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_socau, null);
            builder.setView(dialogView);

            builder.setPositiveButton(
                    getString(R.string.ok), (dialogInterface, i) -> dialogInterface.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
            NumberPicker np = (NumberPicker) dialog.findViewById(R.id.numberPicker);
            if (np != null) {
                np.setMinValue(5);
                np.setMaxValue(100);
                np.setValue(soCau);
                np.setOnValueChangedListener((picker, oldVal, newVal) -> {
                    beforeExamViewModel.setNumber(newVal);
                    soCau = newVal;
                });
            }
            EditText input = findInput(np);
            TextWatcher tw = new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().length() != 0) {
                        Integer value = Integer.parseInt(s.toString());
                        if (value >= np.getMinValue()) {
                            np.setValue(value);
                            beforeExamViewModel.setNumber(value);
                            soCau = value;
                        }
                    }
                }
            };
            input.addTextChangedListener(tw);
        });

        beforeExamBinding.lnThoiGian.setOnClickListener(view -> {
//            Calendar mcurrentTime = Calendar.getInstance();
//            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//            int minute = mcurrentTime.get(Calendar.MINUTE);
//            TimePickerDialog mTimePicker;
//            mTimePicker = new TimePickerDialog(getActivity(), (view1, hourOfDay, minute1) -> {
//                beforeExamViewModel.setTime((hourOfDay * 60 * 60 + minute1 * 60) * 1000);
//                time = (hourOfDay * 60 * 60 + minute1 * 60) * 1000;
//            }, hour, minute, true);
//            mTimePicker.setTitle(getString(R.string.thoiGianLamBai));
//            mTimePicker.updateTime(0, 15);
//            mTimePicker.show();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.thoiGianLamBai));
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_thoigian, null);
            builder.setView(dialogView);

            builder.setPositiveButton(
                    getString(R.string.ok), (dialogInterface, i) -> dialogInterface.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
            NumberPicker np = (NumberPicker) dialog.findViewById(R.id.numberPicker);
            if (np != null) {
                np.setMinValue(15);
                np.setMaxValue(180);
                np.setValue((int) time);
                np.setOnValueChangedListener((picker, oldVal, newVal) -> {
                    beforeExamViewModel.setTime(newVal * 60 * 1000);
                    time = newVal;
                });
            }
            EditText input = findInput(np);
            TextWatcher tw = new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().length() != 0) {
                        Integer value = Integer.parseInt(s.toString());
                        if (value >= np.getMinValue()) {
                            np.setValue(value);
                            beforeExamViewModel.setTime(value * 60 * 1000);
                            time = value;
                        }
                    }
                }
            };
            input.addTextChangedListener(tw);
        });
    }

    private EditText findInput(ViewGroup np) {
        int count = np.getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = np.getChildAt(i);
            if (child instanceof ViewGroup) {
                findInput((ViewGroup) child);
            } else if (child instanceof EditText) {
                return (EditText) child;
            }
        }
        return null;
    }

    private void bindData() {
        Realm.init(getActivity());
        RealmConfiguration settingConfig = new RealmConfiguration.Builder()
                .name("category.realm")
                .modules(Realm.getDefaultModule(), new CategoryModule())
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(MyApplication.with(getActivity()).REALM_VERSION)
                .build();
        Realm realm = Realm.getInstance(settingConfig);
        List<Category> list = realm.where(Category.class).findAll();
        adapter.addAll(list);
    }

    private void initUI() {
        adapter = new CategoryCheckAdapter(getActivity(), new ArrayList<>());
        beforeExamBinding.rvCategory.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CODE_CHAM_DIEM) {
                ((MainActivity) getActivity()).clearBackStack(getActivity()
                        .getSupportFragmentManager());
                ((MainActivity) getActivity()).changeFragment(new HistoryFragment());
                ((MainActivity) getActivity()).setMenuSelect(R.id.nav_history);
            }
        }

    }
}
