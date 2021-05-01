package com.yoonfactory.camera;

import android.graphics.Bitmap;

import com.flir.thermalsdk.ErrorCode;
import com.flir.thermalsdk.androidsdk.image.BitmapAndroid;
import com.flir.thermalsdk.image.ImageBase;
import com.flir.thermalsdk.image.ImageFactory;
import com.flir.thermalsdk.image.JavaImageBuffer;
import com.flir.thermalsdk.live.Camera;
import com.flir.thermalsdk.live.CommunicationInterface;
import com.flir.thermalsdk.live.ConnectParameters;
import com.flir.thermalsdk.live.Identity;
import com.flir.thermalsdk.live.connectivity.ConnectionStatusListener;
import com.flir.thermalsdk.live.discovery.DiscoveryEventListener;
import com.flir.thermalsdk.live.discovery.DiscoveryFactory;
import com.flir.thermalsdk.live.streaming.ThermalImageStreamListener;
import com.yoonfactory.image.YoonImage;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class YoonFlir implements IYoonCamera, DiscoveryEventListener, ConnectionStatusListener,
        ThermalImageStreamListener {
    Camera m_pCamera = null;
    ConnectParameters m_pParamConnect;
    boolean m_bOpenCamera = false;
    boolean m_bLive = false;
    int m_nWidth = 0;
    int m_nHeight = 0;

    @Override
    public void onCameraFound(Identity identity) {
        try {
            m_pCamera = new Camera();
            m_pCamera.connect(identity, this, new ConnectParameters(1000));
            m_bOpenCamera = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDiscoveryError(CommunicationInterface communicationInterface, ErrorCode errorCode) {
        //
    }


    @Override
    public void onDisconnected(@Nullable ErrorCode errorCode) {
        m_pCamera = null;
    }

    @Override
    public void onImageReceived() {
        if (m_pCamera != null) {
            m_pCamera.withImage(thermalImage -> {
                m_nWidth = thermalImage.getWidth();
                m_nHeight = thermalImage.getHeight();
                JavaImageBuffer pBuffer = thermalImage.getImage();
                Bitmap pBitmap = BitmapAndroid.createBitmap(pBuffer).getBitMap();
                CameraEventHandler.callReceiveImageEvent(YoonFlir.class, new YoonImage(pBitmap));
            });
        }
    }

    @Override
    public boolean isOpenCamera() {
        return m_bOpenCamera;
    }

    @Override
    public boolean isStartCamera() {
        return true;
    }

    @Override
    public boolean isLiveOn() {
        return m_bLive;
    }

    @Override
    public int getImageWidth() {
        return 0;
    }

    @Override
    public void setImageWidth(int nWidth) {

    }

    @Override
    public int getImageHeight() {
        return 0;
    }

    @Override
    public void setImageHeight(int nHeight) {

    }

    @Override
    public void open() {
        if (m_bOpenCamera) return;
        try {
            DiscoveryFactory.getInstance().scan(this, CommunicationInterface.USB);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void liveOn() {
        if (m_bLive) return;
        m_pCamera.subscribeStream(this);
        m_bLive = true;
    }

    @Override
    public void liveOff() {
        if (!m_bLive) return;
        m_pCamera.unsubscribeStream(this);
        m_bLive = false;
    }

    @Override
    public boolean getImage(int nTimeout) {
        return false;
    }
}
