package com.aea2.appmanager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.aea2.permissiondemo.R;


public class AppListActivity extends AppCompatActivity {
    private ListView mListView;
    private AppInfoListAdapter mListAdapter;
    private List<AppInfo> mListData;
    private boolean mIsScanSysApp = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case 0:
                    mListData.clear();
                    mListData.addAll((List<AppInfo>) msg.obj);
                    mListAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applist);
        deleteApkUpdate();
        mListView = (ListView) findViewById(R.id.listview);
        mListData = new ArrayList();
        mListAdapter = new AppInfoListAdapter(AppListActivity.this, mListData);
        mListView.setAdapter(mListAdapter);
        new Thread(new InstalledAppInfoTask(mIsScanSysApp)).start();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switch_app:
                mIsScanSysApp = !mIsScanSysApp;
                new Thread(new InstalledAppInfoTask(mIsScanSysApp)).start();
                break;
        }
        return false;
    }

    public void deleteApkUpdate() {
        DeleteReceiver mDeleteReceiver = new DeleteReceiver();// 自定义的广播接收类，接收到结果后的操作
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        registerReceiver(mDeleteReceiver, filter);// 注册广播监听应用程序的增加、删除、更新等操作
    }

    public class DeleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "delete Package", Toast.LENGTH_SHORT)
                    .show();
            new Thread(new InstalledAppInfoTask(mIsScanSysApp)).start();
        }
    }

    private class InstalledAppInfoTask implements Runnable {

        private boolean mIsSysApp = false;

        public InstalledAppInfoTask(boolean isSysApp) {
            mIsSysApp = isSysApp;
        }

        @Override
        public void run() {
            List<AppInfo> AppInfoList = achieveAppInfoList();
            handler.sendMessage(handler.obtainMessage(0, AppInfoList));
        }

        private List<AppInfo> achieveAppInfoList() {
            List<AppInfo> result = new ArrayList<AppInfo>();
            int state = 0;
            List<PackageInfo> packageInfoList = getPackageManager()
                    .getInstalledPackages(0); // 返回已安装的包信息列表
            for (PackageInfo packageInfo : packageInfoList) {
                /*
                 * 判断是否为非系统应用,1是系统应用，0非系统应用
				 */
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == (mIsSysApp ? 1 : 0)) {
                    AppInfo appInfo = new AppInfo();
                    appInfo.setIcon(packageInfo.applicationInfo
                            .loadIcon(getPackageManager()));
                    appInfo.setName(packageInfo.applicationInfo.loadLabel(
                            getPackageManager()).toString());
                    appInfo.setPackageName(packageInfo.packageName);
                    appInfo.setVersionName(packageInfo.versionName);
                    appSize(AppListActivity.this, packageInfo.packageName, appInfo);
                    result.add(appInfo);
                }
            }
            return result;

        }

        public void appSize(final Context context, String pkgName, final AppInfo appInfo) {
            // getPackageSizeInfo是PackageManager中的一个private方法，所以需要通过反射的机制来调用
            Method method;
            try {
                method = PackageManager.class.getMethod("getPackageSizeInfo",
                        new Class[]{String.class, IPackageStatsObserver.class});
                // 调用 getPackageSizeInfo 方法，需要两个参数：1、需要检测的应用包名；2、回调
                method.invoke(context.getPackageManager(), pkgName,
                        new IPackageStatsObserver.Stub() {
                            @Override
                            public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
                                if (succeeded && pStats != null) {
                                    synchronized (AppInfo.class) {
                                        appInfo.cacheSize = pStats.cacheSize;//缓存大小
                                        appInfo.dataSize = (pStats.dataSize); //数据大小
                                        appInfo.codeSize = (pStats.codeSize); //应用大小
                                        appInfo.appSize = (pStats.cacheSize + pStats.codeSize + pStats.dataSize);//应用的总大小
                                    }
                                }
                            }
                        });
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

}
