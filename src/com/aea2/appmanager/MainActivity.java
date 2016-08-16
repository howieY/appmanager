package com.aea2.appmanager;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aea2.permissiondemo.R;


public class MainActivity extends Activity implements OnClickListener {

    protected static final String TAG = MainActivity.class.getSimpleName();
    private TextView mTextView;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnButton = (Button) findViewById(R.id.testStringBtn);
        btnButton.setOnClickListener(this);

        Button btnButton1 = (Button) findViewById(R.id.testObjectBtn);
        btnButton1.setOnClickListener(this);

        Button btnButton2 = (Button) findViewById(R.id.testMixtureBtn);
        btnButton2.setOnClickListener(this);
        mTextView = (TextView) findViewById(R.id.textView);

        mEditText = (EditText) findViewById(R.id.signaturesEdit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void getPackageSignatures(String packageName) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);
            Log.d(TAG, packageName + " " + info.versionCode + " "
                    + info.versionName);
            for (Signature signature : info.signatures) {
                Log.d(TAG, "" + signature.toCharsString());
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        float totalTime = 0;
        switch (v.getId()) {
            case R.id.testStringBtn:
                getPackageSignatures(mEditText.getText().toString());
                break;

            case R.id.testObjectBtn:

                for (int j = 0; j < 100; j++) {
                    long t1 = System.currentTimeMillis();
                    for (int i = 0; i < 1000; i++) {
                        Log.v("a" + TAG,
                                " currentTimeMillis isCheck:"
                                        + this
                                        + " mTextView:"
                                        + mTextView
                                        + " v:"
                                        + v
                                        + ".action=ACTION_UP, id[0]=0, x[0]=535.5, y[0]=857.0,");
                    }
                    totalTime += System.currentTimeMillis() - t1;

                }

                mTextView.setText(mTextView.getText()
                        + "\nObject 1000  elapse time:" + (totalTime / 100));
                break;

            case R.id.testMixtureBtn:
                for (int j = 0; j < 100; j++) {
                    long t1 = System.currentTimeMillis();
                    for (int i = 0; i < 1000; i++) {
                        Log.v("a" + TAG,
                                " currentTimeMillis isCheck:"
                                        + this
                                        + " mTextView:"
                                        + mTextView
                                        + " v:"
                                        + v
                                        + " i:"
                                        + i
                                        + " j:"
                                        + j
                                        + " t1:"
                                        + t1
                                        + ".action=ACTION_UP, id[0]=0, x[0]=535.5, y[0]=857.0,");
                    }
                    totalTime += System.currentTimeMillis() - t1;
                }

                mTextView.setText(mTextView.getText()
                        + "\nMixture  1000 elapse time:" + (totalTime / 100));
                break;
        }
    }
}
