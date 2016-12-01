package me.zjc.zlib.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * Created by ChuanZhangjiang on 2016/8/4.
 * 界面助手，用于协助Activity和Fragment完成一些界面显示
 */
class UiHelper {

    private ProgressDialog mProgressDialog;
    private Toast mToast;
    private Context mContext;

    private UiHelper(Context context) {
        this.mContext = context;
    }

    static UiHelper newInstance(@NonNull Context context) {
        return new UiHelper(context);
    }

    void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
        }
        mProgressDialog.setMessage(null);
        mProgressDialog.show();
    }

    void showProgressDialog(String content) {
        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(content);
        mProgressDialog.show();
    }

    void dismissProgressDialog() {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            return;
        }
        mProgressDialog.dismiss();
    }

    void showToast(@NonNull String content, int duration) {
        if (mToast == null) {
            mToast = new Toast(mContext);
        }

        mToast.setText(content);
        mToast.setDuration(duration);
        mToast.show();
    }

    void showToast(String content) {
        showToast(content, Toast.LENGTH_SHORT);
    }

    void showSnackBar(@NonNull View view, @NonNull String content, int duration) {
        Snackbar.make(view, content, duration).show();
    }

    void showSnackBar(@NonNull View view,@NonNull String content) {
        showSnackBar(view, content, Snackbar.LENGTH_SHORT);
    }

    void onDestroy(){
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }
}
