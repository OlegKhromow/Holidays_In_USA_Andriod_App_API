package stu.cn.ua.lab4.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import stu.cn.ua.lab4.BaseViewModel;
import stu.cn.ua.lab4.model.Callback;
import stu.cn.ua.lab4.model.Cancellable;
import stu.cn.ua.lab4.model.Holiday;
import stu.cn.ua.lab4.model.Result;
import stu.cn.ua.lab4.model.PublicHolidayService;

public class MainViewModel extends BaseViewModel {
    private Result<List<Holiday>> holidayResult = Result.empty();
    private MutableLiveData<ViewState> stateLiveData = new MutableLiveData<>();
    private Cancellable cancellable;

    {
        updateViewState(Result.empty());
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

    public void getCountries(){
        updateViewState(Result.loading());
        cancellable  = getPublicHolidayService().getHolidays(new Callback<List<Holiday>>() {
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
