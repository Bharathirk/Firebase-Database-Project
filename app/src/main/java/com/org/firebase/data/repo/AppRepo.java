package com.org.firebase.data.repo;


import com.org.firebase.data.api.ApiInterface;

public class AppRepo {

    private ApiInterface api;

    public AppRepo(ApiInterface api) {
        this.api = api;
    }


//    public Observable<OfferResponse> getOfferList(String accessToken) {
//        String authorization = AppConstants.HEADER_BEARER + accessToken;
//        return api.getOfferList(authorization).compose(RxJavaUtils.applyObserverSchedulers())
//                .map(offerResponse -> {
//                    if (offerResponse == null) {
//                        throw new RuntimeException(AppConstants.API_UNKNOWN_FAILURE_MSG);
//                    }
//                    return offerResponse;
//                });
//
//    }




}
