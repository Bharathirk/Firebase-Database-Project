package com.org.firebase.app;

import android.app.Application;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.org.firebase.data.bus.MainThreadBus;
import com.org.firebase.data.di.componants.AppPreferenceComponant;
import com.org.firebase.data.di.componants.ApplicationComponant;
import com.org.firebase.data.di.componants.DaggerAppPreferenceComponant;
import com.org.firebase.data.di.componants.DaggerApplicationComponant;
import com.org.firebase.data.di.componants.DaggerRepositoryComponant;
import com.org.firebase.data.di.componants.RepositoryComponant;
import com.org.firebase.data.di.modules.AppPreferenceModule;
import com.org.firebase.data.di.modules.ApplicationModule;
import com.org.firebase.data.di.modules.RepositoryModule;
import com.org.firebase.data.navigator.Navigator;
import com.org.firebase.helper.filecompress.FileCompress;


public class AppController extends Application {

    private static AppController appController;

    private ApplicationComponant applicationComponant;
    private AppPreferenceComponant appPreferenceComponant;
    private RepositoryComponant repositoryComponant;
    private MainThreadBus bus;
    private Gson gson;
    private FileCompress fileCompress;


    public static AppController getInstance() {
        return appController;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appController = this;
        applicationComponant = DaggerApplicationComponant.builder().applicationModule(new ApplicationModule(this)).build();
        appPreferenceComponant = DaggerAppPreferenceComponant.builder().appPreferenceModule(new AppPreferenceModule(this)).build();
        repositoryComponant = DaggerRepositoryComponant.builder().repositoryModule(new RepositoryModule()).build();

        gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

    }

    public FileCompress getFileCompressor() {
        if (fileCompress == null) {
            return fileCompress = applicationComponant.getFileCompress();
        } else
            return fileCompress;
    }

    public RepositoryComponant getRepositoryComponant() {
        return repositoryComponant;
    }

    public Navigator getNavigator() {
        return applicationComponant.getNavigator();
    }

    public AppPreference getAppPreference() {
        return appPreferenceComponant.getAppPreference();
    }

    public MainThreadBus getBus() {
        if (bus == null) {
            return bus = applicationComponant.getBus();
        } else return bus;
    }

}
