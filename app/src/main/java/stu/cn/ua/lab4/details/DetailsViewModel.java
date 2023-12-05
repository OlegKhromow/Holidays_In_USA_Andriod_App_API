package stu.cn.ua.lab4.details;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import stu.cn.ua.lab4.BaseViewModel;
import stu.cn.ua.lab4.model.Callback;
import stu.cn.ua.lab4.model.Cancellable;
import stu.cn.ua.lab4.model.Holiday;
import stu.cn.ua.lab4.model.PublicHolidayService;
import stu.cn.ua.lab4.model.Result;

public class DetailsViewModel extends BaseViewModel {
    private MutableLiveData<Result<Holiday>> holidayLiveData = new MutableLiveData<>();
    private Cancellable cancellable;


    {
        holidayLiveData.postValue(Result.empty());
    }

    public DetailsViewModel(PublicHolidayService publicHolidayService) {
        super(publicHolidayService);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (cancellable != null)
            cancellable.cancel();
    }

    public void getHolidayDetails(long id){

        cancellable  = getPublicHolidayService().getHolidayById(id, new Callback<Holiday>() {
            @Override
            public void onError(Throwable error) {
                holidayLiveData.postValue(Result.error(error));
            }

            @Override
            public void onResult(Holiday data) {
                holidayLiveData.postValue(Result.success(data));
            }
        });
    }

    public LiveData<Result<Holiday>> getResult() {
        return holidayLiveData;
    }
}
