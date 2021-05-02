package com.yoonsample.extcontrol;

import androidx.appcompat.app.AppCompatActivity;
import com.yoonfactory.*;
import com.yoonfactory.camera.IReceiveImageEventListener;
import com.yoonfactory.camera.YoonFlir;
import com.yoonfactory.image.YoonImage;
import com.yoonfactory.log.IProcessLogEventListener;
import com.yoonfactory.log.YoonDisplayer;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements IProcessLogEventListener, IReceiveImageEventListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CommonClass.pDLM = new YoonDisplayer();
        CommonClass.pView = (ImageView)findViewById(R.id.imageView);
        initClient();
    }

    public void initClient() {
        try {
            CommonClass.pCamera = new YoonFlir();
            CommonClass.pCamera.open();
            if (!CommonClass.pCamera.isOpenCamera())
                CommonClass.pDLM.write("Camera Open Fail!");
        } catch (Exception e) {
            e.printStackTrace();
            CommonClass.pDLM.write("Camera Access Fail!");
        }
    }

    @Override
    public void onProcessLogEvent(String strMessage, Color pColor) {
        Toast pToast = Toast.makeText(this.getApplicationContext(), strMessage, Toast.LENGTH_LONG);
        pToast.show();
    }

    @Override
    public void onReceiveImageEvent(YoonImage pImage) {
        CommonClass.pImage = pImage;
        CommonClass.pView.setImageBitmap(pImage.copyImage());
    }
}