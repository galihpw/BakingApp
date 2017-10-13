package com.galihpw.bakingapp.base;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by hidayatasep43 on 6/6/2017.
 */

public abstract class BaseFragment extends Fragment {

    protected ProgressDialog mProgressDialog;

    protected abstract View onBaseCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return onBaseCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);
    }

    protected void showProgress(int stringResId) {
        showProgress(getString(stringResId));
    }

    protected void showProgress(String message) {
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    protected void dismissProgress() {
        mProgressDialog.dismiss();
    }

    protected boolean isProgressShowing() {
        return mProgressDialog.isShowing();
    }

    protected void showToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


}
