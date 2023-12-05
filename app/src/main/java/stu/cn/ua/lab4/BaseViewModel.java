package stu.cn.ua.lab4;

import androidx.lifecycle.ViewModel;

import stu.cn.ua.lab4.model.PublicHolidayService;

public class BaseViewModel extends ViewModel {
    private PublicHolidayService publicHolidayService;

    public BaseViewModel(PublicHolidayService publicHolidayService) {
        this.publicHolidayService = publicHolidayService;
    }

    protected final PublicHolidayService getPublicHolidayService() {
        return publicHolidayService;
    }
}
