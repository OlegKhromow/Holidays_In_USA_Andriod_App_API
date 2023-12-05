package stu.cn.ua.lab4;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.Constructor;

import stu.cn.ua.lab4.model.PublicHolidayService;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private PublicHolidayService publicHolidayService;

    public ViewModelFactory(PublicHolidayService publicHolidayService) {
        this.publicHolidayService = publicHolidayService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            Constructor<T> constructor = modelClass.getConstructor(PublicHolidayService.class);
            return constructor.newInstance(publicHolidayService);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
