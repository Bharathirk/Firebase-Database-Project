package com.org.firebase.base;


import com.org.firebase.app.AppController;
import com.org.firebase.data.di.componants.RepositoryComponant;

public abstract class AbstractBasePresenter<V extends BaseView> implements BasePresenter<V> {

    protected V view;

    protected abstract void inject(RepositoryComponant repositoryComponant);

    @Override
    public void setView(V view) {
        inject(AppController.getInstance().getRepositoryComponant());
        this.view = view;
    }

    @Override
    public void destroyView() {
        view = null;
    }

    @Override
    public void destroy() {
        destroyView();
    }
}
