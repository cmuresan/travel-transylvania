package com.example.android.networkmodule.network;

public interface CallbackInterface<T> {
    void success(T response);
    void failure(String errorMessage, String errorCode);
}
