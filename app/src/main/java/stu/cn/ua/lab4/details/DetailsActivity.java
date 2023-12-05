package stu.cn.ua.lab4.details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import stu.cn.ua.lab4.App;
import stu.cn.ua.lab4.databinding.ActivityDetailsBinding;
import stu.cn.ua.lab4.model.Holiday;

public class DetailsActivity extends AppCompatActivity {
    public static final String EXTRA_HOLIDAY_ID = "HOLIDAY_ID";

    private ActivityDetailsBinding binding;
    private DetailsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        App app = (App) getApplication();
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, app.getViewModelFactory());
        viewModel = viewModelProvider.get(DetailsViewModel.class);

        viewModel.getHolidayDetails(getIntent().getLongExtra(EXTRA_HOLIDAY_ID, 0));

        viewModel.getResult().observe(this, holidayResult -> {
            switch (holidayResult.getStatus()){
                case SUCCESS:
                    Holiday holiday = holidayResult.getData();
                    binding.holidayName.setText(holiday.getName());
                    binding.localName.setText(holiday.getLocalName());
                    binding.date.setText(holiday.getDate());
                    binding.type.setText(holiday.getType());
                    break;
                case EMPTY:
                    binding.holidayName.setVisibility(View.GONE);
                    binding.localName.setVisibility(View.GONE);
                    binding.localNameTitle.setVisibility(View.GONE);
                    binding.date.setVisibility(View.GONE);
                    binding.dateTitle.setVisibility(View.GONE);
                    binding.type.setVisibility(View.GONE);
                    binding.typeTitle.setVisibility(View.GONE);
                    binding.errorMassage.setVisibility(View.VISIBLE);
            }
        });
    }
}