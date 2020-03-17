package com.org.firebase.base;

public interface LoadBaseView extends BaseView {

    /**
     * This method is used to initiate the progress loading view while calling an api or time taking logics. this will
     * * restrict the user to interact the ui
     */
    void showLoading();

    /**
     * This method is used to hide the progress view which is currently loading
     */
    void hideLoading();

    /**
     * This method is used to send an error message after processing an api or background tasks.
     *
     * @param message is an error message to notify to the user.
     */
    void showError(String message);
}
