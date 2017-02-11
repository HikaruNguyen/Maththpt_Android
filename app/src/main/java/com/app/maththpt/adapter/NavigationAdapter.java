package com.app.maththpt.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.maththpt.R;
import com.app.maththpt.config.Configuaration;
import com.app.maththpt.eventbus.ClickSlideLeftEvent;
import com.app.maththpt.model.ItemMenu;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


/**
 * Created by manhi on 2/7/2016.
 */

public class NavigationAdapter extends BaseRecyclerAdapter<ItemMenu, NavigationAdapter.ViewHolder> {
    public NavigationAdapter(Context context, List<ItemMenu> list) {
        super(context, list);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.bindData(list.get(position), list.get(position).type);
        if (list.get(position).type != ItemMenu.TYPE_HEADER) {
            if (list.get(position).isSelected) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.selected));
                holder.tvName.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                holder.tvName.setTypeface(null, Typeface.BOLD);
            } else {
                holder.itemView.setBackgroundResource(R.drawable.background_slide);
                holder.tvName.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                holder.tvName.setTypeface(null, Typeface.NORMAL);
            }
        } else {
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ItemMenu.TYPE_HEADER) {
            view = layoutInflater.inflate(R.layout.nav_header_main, parent, false);
        } else if (viewType == ItemMenu.TYPE_HOME) {
            view = layoutInflater.inflate(R.layout.item_navigation, parent, false);
        } else if (viewType == ItemMenu.TYPE_BODY) {
            view = layoutInflater.inflate(R.layout.item_navigation, parent, false);
        } else {
            view = layoutInflater.inflate(R.layout.item_navigation, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgLogo;
        private TextView tvName, tv_play_game, tv_user_name;
//        private CircleImageView img_user;

        public ViewHolder(View view) {
            super(view);
            imgLogo = (ImageView) view.findViewById(R.id.imgLogo);
            tvName = (TextView) view.findViewById(R.id.tvName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(getAdapterPosition()).type == ItemMenu.TYPE_HEADER) {
                        EventBus.getDefault().post(new ClickSlideLeftEvent(getAdapterPosition(), true, list.get(getAdapterPosition()).type));
                    } else {
                        int position = getAdapterPosition();
                        if (Configuaration.oldPostionProvince < 0) {
                            list.get(position).isSelected = true;
                        } else if (position != Configuaration.oldPostionProvince) {
                            list.get(Configuaration.oldPostionProvince).isSelected = false;
                            list.get(position).isSelected = true;

                        } else {
                            EventBus.getDefault().post(new ClickSlideLeftEvent(getAdapterPosition(), false, list.get(getAdapterPosition()).type));
                        }
                        Configuaration.oldPostionProvince = getAdapterPosition();
                        notifyDataSetChanged();
                    }

                }
            });
        }

        public void bindData(ItemMenu itemMenu, int type) {
            if (type != ItemMenu.TYPE_HEADER) {
                tvName.setText(itemMenu.title);
                if (type != ItemMenu.TYPE_HOME) {
//                    Picasso.with(mContext).load(itemMenu.icon).placeholder(R.mipmap.ic_leagues_default).error(R.mipmap.ic_leagues_default).into(imgLogo);
                    imgLogo.setImageResource(R.mipmap.ic_home);
                } else {
                    imgLogo.setImageResource(R.mipmap.ic_home);
                }

            } else {
//                tv_user_name.setText(itemMenu.title);
            }
        }

    }

}
