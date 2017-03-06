package com.app.maththpt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.maththpt.R;
import com.app.maththpt.model.DetailPoint;

import java.util.List;


/**
 * Created by manhi on 2/7/2016.
 */

public class DetailPointAdapter extends
        BaseRecyclerAdapter<DetailPoint, DetailPointAdapter.ViewHolder> {
    public DetailPointAdapter(Context context, List<DetailPoint> list) {
        super(context, list);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = layoutInflater.inflate(R.layout.item_chi_tiet_diem, parent, false);
        return new ViewHolder(view);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvPoint;

        ViewHolder(View view) {
            super(view);
            tvPoint = (TextView) view.findViewById(R.id.tvPoint);
            tvName = (TextView) view.findViewById(R.id.tvName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void bindData(DetailPoint chiTietDiem) {
            tvName.setText(chiTietDiem.name);
            tvPoint.setText(chiTietDiem.trueQuestion + "/" + chiTietDiem.sumQuestion);
        }

    }

}
