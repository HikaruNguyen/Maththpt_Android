package com.app.maththpt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.maththpt.R;
import com.app.maththpt.model.StatisticalPoint;
import com.app.maththpt.model.StatisticalPoint;

import java.util.List;


/**
 * Created by manhi on 2/7/2016.
 */

public class StatisticalPointAdapter extends BaseRecyclerAdapter<StatisticalPoint, StatisticalPointAdapter.ViewHolder> {
    public StatisticalPointAdapter(Context context, List<StatisticalPoint> list) {
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


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvPoint;

        public ViewHolder(View view) {
            super(view);
            tvPoint = (TextView) view.findViewById(R.id.tvPoint);
            tvName = (TextView) view.findViewById(R.id.tvName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void bindData(StatisticalPoint StatisticalPoint) {
            tvName.setText(StatisticalPoint.getCateName());
            tvPoint.setText(String.format("%.1f", StatisticalPoint.getRatio()) + "%");
//                    + "("+StatisticalPoint.trueQuestion/StatisticalPoint.sumQuestion+")" );
        }

    }

}