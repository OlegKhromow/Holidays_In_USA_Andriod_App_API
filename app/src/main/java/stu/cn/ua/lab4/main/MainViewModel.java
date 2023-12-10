package stu.cn.ua.lab4.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import stu.cn.ua.lab4.BaseViewModel;
import stu.cn.ua.lab4.model.Callback;
import stu.cn.ua.lab4.model.Cancellable;
import stu.cn.ua.lab4.model.Holiday;
import stu.cn.ua.lab4.model.Result;
import stu.cn.ua.lab4.model.PublicHolidayService;
import stu.cn.ua.lab4.model.network.CountryNetworkEntity;

public class MainViewModel extends BaseViewModel {
    private Result<List<Holiday>> holidayResult = Result.empty();
    private MutableLiveData<ViewState> stateLiveData = new MutableLiveData<>();
    private Cancellable cancellable;

    {
        updateViewState(Result.firstEntry());
    }

    public MainViewModel(PublicHolidayService publicHolidayService) {
        super(publicHolidayService);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (cancellable != null)
            cancellable.cancel();
    }

    public LiveData<ViewState> getViewState() {
        return stateLiveData;
    }

    private void getHolidays(CountryNetworkEntity country){
        updateViewState(Result.loading());
        cancellable  = getPublicHolidayService().getHolidays(country, new Callback<List<Holiday>>() {
            @Override
            public void onError(Throwable error) {
                if(holidayResult.getStatus() != Result.Status.SUCCESS)
                    updateViewState(Result.error(error));
            }

            @Override
            public void onResult(List<Holiday> data) {
                updateViewState(Result.success(data));
            }
        });
    }

    public void getCountryCode(String countryName){
        cancellable = getPublicHolidayService().getCountry(countryName, new Callback<CountryNetworkEntity>() {
            @Override
            public void onError(Throwable error) {
                updateViewState(Result.error(error));
            }

            @Override
            public void onResult(CountryNetworkEntity data) {
                getHolidays(data);
            }
        });
    }

    private void updateViewState(Result<List<Holiday>> holidayResult){
        this.holidayResult = holidayResult;
        ViewState viewState = new ViewState();
        viewState.showList = holidayResult.getStatus() == Result.Status.SUCCESS;
        viewState.showProgress = holidayResult.getStatus() == Result.Status.LOADING;
        viewState.showError = holidayResult.getStatus() == Result.Status.EMPTY;
        viewState.holidays = holidayResult.getData();
        stateLiveData.postValue(viewState);
    }

    static class ViewState{
        private boolean showList;
        private boolean showError;
        private boolean showProgress;
        private List<Holiday> holidays;

        public boolean isShowList() {
            return showList;
        }

        public boolean isShowError() {
            return showError;
        }

        public boolean isShowProgress() {
            return showProgress;
        }

        public List<Holiday> getHolidays() {
            return holidays;
        }
    }
}
