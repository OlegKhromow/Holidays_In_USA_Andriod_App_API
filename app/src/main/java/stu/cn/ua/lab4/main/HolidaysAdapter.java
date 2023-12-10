package stu.cn.ua.lab4.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import stu.cn.ua.lab4.R;
import stu.cn.ua.lab4.model.Holiday;

public class HolidaysAdapter extends RecyclerView.Adapter<HolidaysAdapter.HolidayViewHolder> implements View.OnClickListener{
    private List<Holiday> holidays = Collections.emptyList();
    private HolidayListener listener;

    public HolidaysAdapter(HolidayListener listener) {
        this.listener = listener;
    }

    public void setHolidays(List<Holiday> holidays) {
        this.holidays = holidays;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HolidayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.item_holiday, parent, false);
        return new HolidayViewHolder(root, this);
    }

    @Override
    public void onBindViewHolder(@NonNull HolidayViewHolder holder, int position) {
        Holiday holiday = holidays.get(position);
        holder.nameTextView.setText(holiday.getName());
        holder.itemView.setTag(holiday);
    }

    @Override
    public int getItemCount() {
        return holidays.size();
    }

    @Override
    public void onClick(View v) {
        Holiday holiday = (Holiday) v.getTag();
        listener.onHolidayChosen(holiday);
    }

    static class HolidayViewHolder extends RecyclerView.ViewHolder{
        private TextView nameTextView;
        public HolidayViewHolder(@NonNull View itemView, View.OnClickListener listener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            itemView.setOnClickListener(listener);
        }
    }

    public interface HolidayListener {
        void onHolidayChosen(Holiday holiday);
    }
}
