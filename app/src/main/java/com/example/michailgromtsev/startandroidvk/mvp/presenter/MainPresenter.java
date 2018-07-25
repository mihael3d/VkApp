package com.example.michailgromtsev.startandroidvk.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.michailgromtsev.startandroidvk.CurrentUser;
import com.example.michailgromtsev.startandroidvk.mvp.view.MainView;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    public void checkAuth() {
        if (!CurrentUser.isAutorized()) {
           getViewState().startSigIn();
        } else {
           getViewState().signedId();
        }
    }
}
