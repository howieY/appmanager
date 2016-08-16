package com.aea2.appmanager;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aea2.permissiondemo.R;


public class AppInfoListAdapter extends BaseAdapter {

    private Context mContext;
    private List<AppInfo> mDataList;
    private PackageUtils mPackageUtils;

    public AppInfoListAdapter(Context context, List<AppInfo> data) {
        this.mContext = context;
        this.mDataList = data;
        mPackageUtils = new PackageUtils();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listview_item, null);
        }
        AppInfo info = mDataList.get(position);
        ImageView icon = (ImageView) convertView
                .findViewById(R.id.item_IconImageView);
        TextView name = (TextView) convertView
                .findViewById(R.id.item_NameTextView);
        TextView packageName = (TextView) convertView
                .findViewById(R.id.item_PackageNameTextView);
        TextView versionName = (TextView) convertView
                .findViewById(R.id.item_VersionNameTextView);
        TextView appSize = (TextView) convertView
                .findViewById(R.id.item_AppSize);

        icon.setImageDrawable(info.getIcon());
        name.setText(info.getName());
        packageName.setText(info.getPackageName());
        versionName.setText(info.getVersionName());
        appSize.setText(Formatter.formatFileSize(mContext, info.codeSize));
        convertView.setOnClickListener(new OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                AppInfo info = (AppInfo) v.getTag();
                String signed = mPackageUtils.getPackageSignatures(mContext,
                        info.getPackageName());
                TextView text = new TextView(mContext);
                text.setText(signed);
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setView(text)
                        .setOnDismissListener(new OnDismissListener() {

                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }

                        }).setPositiveButton("Ok", null).create();
                dialog.show();
                dialog.setCancelable(false);
            }
        });

        convertView.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                AppInfo info = (AppInfo) v.getTag();
                deleteApk(info);
                return true;
            }
        });
        convertView.setTag(mDataList.get(position));

        return convertView;
    }

    public void deleteApk(final AppInfo info) {
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("UnInstall " + info.getName())
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri packageURI = Uri.parse("package:"
                                + info.getPackageName());
                        Intent intent = new Intent(Intent.ACTION_DELETE,
                                packageURI);
                        mContext.startActivity(intent);
                    }
                })
                .setPositiveButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).create();

        dialog.show();
    }
}
