package com.org.firebase.data.di.componants;


import com.org.firebase.app.AppPreference;
import com.org.firebase.data.di.modules.AppPreferenceModule;

import dagger.Component;

@Component(modules = {AppPreferenceModule.class})
public interface AppPreferenceComponant {

    AppPreference getAppPreference();

}
