package com.org.firebase.data.di.componants;

import android.app.Application;

import com.org.firebase.data.bus.MainThreadBus;
import com.org.firebase.data.di.modules.ApplicationModule;
import com.org.firebase.data.navigator.Navigator;
import com.org.firebase.helper.filecompress.FileCompress;

import dagger.Component;

@Component(modules = {ApplicationModule.class})
public interface ApplicationComponant {

    Application getApplication();

    Navigator getNavigator();

    MainThreadBus getBus();

    FileCompress getFileCompress();


}
