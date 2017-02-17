package cn.utsoft.cd.utupdater.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import cn.utsoft.cd.utupdater.R;

/**
 * Created by 李波 on 2017/2/16.
 * Function:
 * Desc:
 */

public class ProgressDialog extends Dialog {

    public ProgressDialog(Context context) {
        super(context, android.support.v7.appcompat.R.style.Base_Theme_AppCompat_Dialog_Alert);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_download);
    }
}
