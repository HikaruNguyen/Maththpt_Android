package com.app.maththpt.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.app.maththpt.BR;
import com.app.maththpt.R;
import com.app.maththpt.activity.MyApplication;
import com.app.maththpt.activity.QuestionActivity;
import com.app.maththpt.config.Configuaration;
import com.app.maththpt.model.Tests;
import com.app.maththpt.realm.TestsModule;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by manhi on 2/7/2016.
 */

public class TestsAdapter extends BaseRecyclerAdapter<Tests, TestsAdapter.ViewHolder> {
    private Realm realm;

    public TestsAdapter(Context context, List<Tests> list) {
        super(context, list);
        Realm.init(context);
        RealmConfiguration settingConfig = new RealmConfiguration.Builder()
                .name("tests.realm")
                .modules(Realm.getDefaultModule(), new TestsModule())
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(MyApplication.with(context).REALM_VERSION)
                .build();
        realm = Realm.getInstance(settingConfig);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ViewDataBinding viewDataBinding = holder.getViewDataBinding();
        viewDataBinding.setVariable(BR.test, list.get(position));

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_tests, parent, false);
        return new ViewHolder(binding);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding mViewDataBinding;

        ViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding.getRoot());
            mViewDataBinding = viewDataBinding;
            mViewDataBinding.executePendingBindings();
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, QuestionActivity.class);
                intent.putExtra("title", list.get(getAdapterPosition()).displayname);
                intent.putExtra("type", Configuaration.TYPE_TESTS);
                intent.putExtra("testID", list.get(getAdapterPosition()).id + "");
                if (!list.get(getAdapterPosition()).isSeen) {
                    realm.beginTransaction();
//                    Tests tests = realm.where(Tests.class).equalTo("id", list.get(getAdapterPosition()).id).findFirst();
//                    tests.isSeen = true;
                    list.get(getAdapterPosition()).isSeen = true;
                    list.get(getAdapterPosition()).isNew = false;
                    realm.copyToRealmOrUpdate(list.get(getAdapterPosition()));
                    realm.commitTransaction();

                }
                mContext.startActivity(intent);
                viewDataBinding.notifyChange();
                notifyDataSetChanged();
            });
        }

        ViewDataBinding getViewDataBinding() {
            return mViewDataBinding;

        }
    }
}
