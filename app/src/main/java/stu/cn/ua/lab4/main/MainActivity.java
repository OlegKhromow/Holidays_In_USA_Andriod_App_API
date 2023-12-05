package stu.cn.ua.lab4.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import stu.cn.ua.lab4.App;
import stu.cn.ua.lab4.databinding.ActivityMainBinding;
import stu.cn.ua.lab4.details.DetailsActivity;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private HolidaysAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        App app = (App) getApplication();
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, app.getViewModelFactory());
        viewModel = viewModelProvider.get(MainViewModel.class);

        viewModel.getViewState().observe(this, viewState -> {
            binding.holidaysList.setVisibility(viewState.isShowList() ? View.VISIBLE : View.GONE);
            binding.progress.setVisibility(viewState.isShowProgress() ? View.VISIBLE : View.GONE);
            binding.errrormsg.setVisibility(viewState.isShowError() ? View.VISIBLE : View.GONE);
            adapter.setHolidays(viewState.getHolidays());
        });

        viewModel.getCountries();

        initHolidayList();
    }

    private void initHolidayList(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.holidaysList.setLayoutManager(layoutManager);
        adapter = new HolidaysAdapter(holiday -> {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(DetailsActivity.EXTRA_HOLIDAY_ID, holiday.getId());
            startActivity(intent);
        });
        binding.holidaysList.setAdapter(adapter);
    }
}