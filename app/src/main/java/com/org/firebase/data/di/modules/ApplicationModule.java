package com.org.firebase.data.di.modules;

import android.app.Application;

import com.org.firebase.data.bus.MainThreadBus;
import com.org.firebase.data.navigator.Navigator;
import com.org.firebase.helper.filecompress.FileCompress;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    Application providesApplication() {
        return application;
    }

    @Provides
    Navigator providesNavigator() {
        return new Navigator();
    }

    @Provides
    MainThreadBus providesBus() {
        return new MainThreadBus();
    }

    @Provides
    FileCompress provideFileCompress() {
        return new FileCompress();
    }

}
