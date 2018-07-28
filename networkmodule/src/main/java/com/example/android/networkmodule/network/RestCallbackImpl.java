package com.example.android.networkmodule.network;

import android.support.annotation.Nullable;

public class RestCallbackImpl<T> extends RestCallback<T> {

    private final CallbackInterface<T> callbackInterface;

    RestCallbackImpl(CallbackInterface<T> callbackInterface) {
        this.callbackInterface = callbackInterface;
    }

    @Override
    public void success(T response) {
        callbackInterface.success(response);
    }

    @Override
    public void failure(String errorMessage, @Nullable String errorCode) {
        callbackInterface.failure(errorMessage, errorCode);
    }
}
