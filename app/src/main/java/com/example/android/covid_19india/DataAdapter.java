package com.example.android.covid_19india;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<StateReport> stateReportArrayList;

    public DataAdapter(Context context,ArrayList<StateReport> arrayList) {
        super();
        mContext=context;
        stateReportArrayList=arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        final ViewHolder holder=new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.state_list_item,parent,false));

        holder.getParentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StateReport stateReport=stateReportArrayList.get(holder.getAdapterPosition());
                if(stateReport.isDataVisible()){
                    holder.getStateData().setVisibility(View.GONE);
                    stateReport.setDataVisible(false);
                    holder.getDropDown().setImageResource(R.drawable.ic_drop_down);
                }else {
                    holder.getStateData().setVisibility(View.VISIBLE);
                    stateReport.setDataVisible(true);
                    holder.getDropDown().setImageResource(R.drawable.ic_drop_up);
                }
            }
        });

        holder.getDropDown().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StateReport stateReport=stateReportArrayList.get(holder.getAdapterPosition());
                if(stateReport.isDataVisible()){
                    holder.getStateData().setVisibility(View.GONE);
                    stateReport.setDataVisible(false);
                    holder.getDropDown().setImageResource(R.drawable.ic_drop_down);
                }else {
                    holder.getStateData().setVisibility(View.VISIBLE);
                    stateReport.setDataVisible(true);
                    holder.getDropDown().setImageResource(R.drawable.ic_drop_up);
                }
            }
        });

        return holder;
    }

    @Override
    public int getItemCount() {
        return stateReportArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView activeText,confirmedText,recoveredText,deathText,lastUpdatedTime,lastUpdatedDate;
        ImageView circle;
        LinearLayout stateData;
        TextView stateName;
        ImageButton dropDown;
        View parentView;


        public ViewHolder(@NonNull View convertView) {
            super(convertView);

            activeText=(TextView)convertView.findViewById(R.id.active_count);
            confirmedText=(TextView)convertView.findViewById(R.id.confirmed_count);
            deathText=(TextView)convertView.findViewById(R.id.death_count);
            recoveredText=(TextView)convertView.findViewById(R.id.recovered_count);
            lastUpdatedTime=(TextView)convertView.findViewById(R.id.last_updated_time);
            lastUpdatedDate=(TextView)convertView.findViewById(R.id.last_updated_date);
            circle=(ImageView)convertView.findViewById(R.id.circle);
            dropDown=(ImageButton)convertView.findViewById(R.id.dropdown);
            stateName=(TextView)convertView.findViewById(R.id.state_name);
            stateData=(LinearLayout) convertView.findViewById(R.id.state_data);
            parentView=convertView;
        }

        public TextView getLastUpdatedDate() {
            return lastUpdatedDate;
        }

        public TextView getLastUpdatedTime() {
            return lastUpdatedTime;
        }

        public TextView getActiveText() {
            return activeText;
        }

        public ImageButton getDropDown() {
            return dropDown;
        }

        public ImageView getCircle() {
            return circle;
        }

        public LinearLayout getStateData() {
            return stateData;
        }

        public TextView getConfirmedText() {
            return confirmedText;
        }

        public TextView getDeathText() {
            return deathText;
        }

        public TextView getRecoveredText() {
            return recoveredText;
        }

        public TextView getStateName() {
            return stateName;
        }

        public View getParentView() {
            return parentView;
        }
    }


    private int[] getDrawableColor(int active) {
        if (active <= 1000) {
            return new int[]{mContext.getColor(R.color.color1), mContext.getColor(R.color.color2)};
        } else if (active <= 10000) {
            return new int[]{mContext.getColor(R.color.color3), mContext.getColor(R.color.color4)};
        }
        return new int[]{mContext.getColor(R.color.color5), mContext.getColor(R.color.color6)};
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final StateReport stateReport=stateReportArrayList.get(position);
        ArrayList<Integer> integers=stateReport.getData();

        holder.getStateName().setText(stateReport.getStateName());
        holder.getActiveText().setText(String.valueOf(integers.get(0)));
        holder.getConfirmedText().setText(String.valueOf(integers.get(1)));
        holder.getDeathText().setText(String.valueOf(integers.get(2)));
        holder.getRecoveredText().setText(String.valueOf(integers.get(3)));

        GradientDrawable gradientDrawable=(GradientDrawable)holder.getCircle().getBackground();
        gradientDrawable.setColors(getDrawableColor(integers.get(0)));

        String[] updated=stateReport.getUpadtedTime().split(" ",-1);
        holder.getLastUpdatedDate().setText(updated[0]);
        holder.getLastUpdatedTime().setText(updated[1]);

        GradientDrawable gradientDrawable1=(GradientDrawable)holder.getStateData().getBackground();
        int[] colors=new int[]{mContext.getColor(R.color.color7),mContext.getColor(R.color.color8)};
        gradientDrawable1.setColors(colors);

        if(stateReport.isDataVisible()){
            holder.getStateData().setVisibility(View.VISIBLE);
        }else {
            holder.getStateData().setVisibility(View.GONE);
        }

//        holder.getParentView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(stateReport.isDataVisible()){
//                    holder.getStateData().setVisibility(View.GONE);
//                    stateReport.setDataVisible(false);
//                    holder.getDropDown().setImageResource(R.drawable.ic_drop_down);
//                }else {
//                    holder.getStateData().setVisibility(View.VISIBLE);
//                    stateReport.setDataVisible(true);
//                    holder.getDropDown().setImageResource(R.drawable.ic_drop_up);
//                }
//            }
//        });
//
//        holder.getDropDown().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(stateReport.isDataVisible()){
//                    holder.getStateData().setVisibility(View.GONE);
//                    stateReport.setDataVisible(false);
//                    holder.getDropDown().setImageResource(R.drawable.ic_drop_down);
//                }else {
//                    holder.getStateData().setVisibility(View.VISIBLE);
//                    stateReport.setDataVisible(true);
//                    holder.getDropDown().setImageResource(R.drawable.ic_drop_up);
//                }
//            }
//        });
    }
}