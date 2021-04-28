package com.yoonfactory.camera;

import com.intel.realsense.librealsense.Align;
import com.intel.realsense.librealsense.Colorizer;
import com.intel.realsense.librealsense.Config;
import com.intel.realsense.librealsense.DepthFrame;
import com.intel.realsense.librealsense.Device;
import com.intel.realsense.librealsense.DeviceList;
import com.intel.realsense.librealsense.Extension;
import com.intel.realsense.librealsense.Frame;
import com.intel.realsense.librealsense.FrameSet;
import com.intel.realsense.librealsense.Option;
import com.intel.realsense.librealsense.Pipeline;
import com.intel.realsense.librealsense.PipelineProfile;
import com.intel.realsense.librealsense.RsContext;
import com.intel.realsense.librealsense.Sensor;
import com.intel.realsense.librealsense.StreamFormat;
import com.intel.realsense.librealsense.StreamType;
import com.intel.realsense.librealsense.VideoFrame;
import com.yoonfactory.image.YoonImage;

import java.util.List;

public class YoonRealsense implements IYoonCamera{
    private static final int MAX_IMAGE_WIDTH = 1920;
    private static final int MAX_IMAGE_HEIGHT = 1080;
    private static final int MAX_DEPTH_WIDTH = 1280;
    private static final int MAX_DEPTH_HEIGHT = 720;
    private static final double MAX_DEPTH_DIST = 1.5;
    private Pipeline m_pPipeline;
    private RsContext m_pContext;
    private Device m_pCurrentCamera;
    private Sensor m_pCurrentRGBSensor;
    private Sensor m_pCurrentDepthSensor;
    private Colorizer m_pColorizer;
    private Align m_pAlign;
    private DeviceList m_pListCamera;
    private List<eYoonCaptureMode> m_pListCaptureMode;
    private Thread m_pThreadLive = null;
    private boolean m_bFlagUseRGBStream = false;
    private boolean m_bFlagUseDepthStream = false;
    private boolean m_bOpenCamera = false;
    private boolean m_bStartCamera = false;
    private boolean m_bLiveOn = false;
    private int m_nImageWidth;
    private int m_nImageHeight;
    private int m_nDepthWidth;
    private int m_nDepthHeight;

    @Override
    public boolean isOpenCamera() {
        return m_bOpenCamera;
    }

    @Override
    public boolean isStartCamera() {
        return m_bStartCamera;
    }

    @Override
    public boolean isLiveOn() {
        return m_bLiveOn;
    }

    @Override
    public int getImageWidth() {
        return m_nImageWidth;
    }

    @Override
    public void setImageWidth(int nWidth) {
        m_nImageWidth = nWidth;
    }

    @Override
    public int getImageHeight() {
        return m_nImageHeight;
    }

    @Override
    public void setImageHeight(int nHeight) {
        m_nImageHeight = nHeight;
    }

    public int getDepthWidth() {
        return m_nDepthWidth;
    }

    public void setDepthWidth(int nWidth) {
        m_nDepthWidth = nWidth;
    }

    public int getDepthHeight() {
        return m_nDepthHeight;
    }

    public void setDepthHeight(int nHeight) {
        m_nDepthHeight = nHeight;
    }

    @Override
    public int open(int nNo) {
        if (m_bOpenCamera) return -1;
        if (m_nImageWidth > MAX_IMAGE_WIDTH || m_nImageHeight > MAX_IMAGE_HEIGHT) return -1;
        if (m_nDepthWidth > MAX_DEPTH_WIDTH || m_nDepthHeight > MAX_DEPTH_HEIGHT) return -1;
        try {
            m_pContext = new RsContext();
            m_pListCamera = m_pContext.queryDevices();
            if (m_pListCamera.getDeviceCount() == 0 || nNo >= m_pListCamera.getDeviceCount())
                throw new IllegalAccessException("Open Realsense Failure : Camera Lack");
            m_pCurrentCamera = m_pListCamera.createDevice(nNo);
            for (Sensor pSensor : m_pCurrentCamera.querySensors()) {
                switch (pSensor.getDescription(Option.SENSOR_MODE)) {
                    case "Stereo Module":
                        m_pCurrentDepthSensor = pSensor;
                        break;
                    case "RGB Camera":
                        m_pCurrentRGBSensor = pSensor;
                        break;
                    default:
                        throw new IllegalStateException("Open Realsense Failure : Sensor mode Unknown");
                }
            }
            m_pPipeline = new Pipeline(m_pContext);
            m_pColorizer = new Colorizer();
            m_pAlign = new Align(StreamType.ANY);
        } catch (Exception ex) {
            throw new IllegalStateException("Open Realsense Failure : Invaild Error");
        }
        m_bOpenCamera = true;
        return m_pListCamera.getDeviceCount();
    }

    @Override
    public void start() {
        if (m_bStartCamera) return;
        if (m_nImageWidth > MAX_IMAGE_WIDTH || m_nImageHeight > MAX_IMAGE_HEIGHT) return;
        if (m_nDepthWidth > MAX_DEPTH_WIDTH || m_nDepthHeight > MAX_DEPTH_HEIGHT) return;
        try {
            Config pConfig = new Config();
            for (eYoonCaptureMode nMode : m_pListCaptureMode) {
                switch (nMode) {
                    case RGBMono:
                        if (m_bFlagUseRGBStream == true) continue;
                        pConfig.enableStream(StreamType.COLOR, m_nImageWidth, m_nImageHeight, StreamFormat.Y8);
                        m_bFlagUseRGBStream = true;
                        break;
                    case RGBColor:
                        if (m_bFlagUseRGBStream == true) continue;
                        pConfig.enableStream(StreamType.COLOR, m_nImageWidth, m_nImageHeight, StreamFormat.RGB8);
                        m_bFlagUseRGBStream = true;
                        break;
                    case Depth:
                        if (m_bFlagUseDepthStream == true) continue;
                        pConfig.enableStream(StreamType.DEPTH, m_nDepthWidth, m_nDepthHeight, StreamFormat.ANY);
                        m_bFlagUseDepthStream = true;
                        break;
                }
            }
            PipelineProfile pProfile = m_pPipeline.start(pConfig);
        } catch (Exception ex) {
            throw new IllegalStateException("Start Realsense Failure : Invaild Error");
        }
        m_bStartCamera = true;
    }

    @Override
    public void liveOn() {
        if (!m_bStartCamera) return;
        if (m_pThreadLive != null && m_pThreadLive.isAlive()) return;
        m_pThreadLive = new Thread(new Runnable() {
            @Override
            public void run() {
                processLive();
            }
        });
        m_pThreadLive.start();
    }

    @Override
    public void liveOff() {
        if (m_pThreadLive == null) return;
        if (m_pThreadLive.isAlive())
            m_pThreadLive.interrupt();
        m_pThreadLive = null;
    }

    @Override
    public boolean getImage(int nTimeout) {
        return false;
    }

    public void processLive() {
        while (!m_pThreadLive.isInterrupted()) {
            try (FrameSet pFrameSet = m_pPipeline.waitForFrames()) {
                try (FrameSet pFrameSetAlign = m_pAlign.process(pFrameSet)) {
                    if (m_bFlagUseRGBStream) {
                        Frame pFrame = pFrameSetAlign.first(StreamType.COLOR);
                        try (VideoFrame pFrameColor = pFrame.as(Extension.VIDEO_FRAME)) {
                            int nBpp = pFrameColor.getBitsPerPixel();
                            int nWidth = pFrameColor.getWidth();
                            int nHeight = pFrameColor.getHeight();
                            byte[] pBuffer = new byte[nBpp * nWidth * nHeight];
                            pFrameColor.getData(pBuffer);
                            YoonImage pImage = new YoonImage(pBuffer, nWidth, nHeight, nBpp);
                            CameraEventHandler.callReceiveImageEvent(YoonRealsense.class, pImage);
                        }
                    }
                    if (m_bFlagUseDepthStream) {
                        Frame pFrame = pFrameSetAlign.first(StreamType.DEPTH);
                        try (DepthFrame pFrameDepth = pFrame.as(Extension.DEPTH_FRAME)) {
                            int nBpp = pFrameDepth.getBitsPerPixel();
                            int nWidth = pFrameDepth.getWidth();
                            int nHeight = pFrameDepth.getHeight();
                            byte[] pBuffer = new byte[nBpp * nWidth * nHeight];
                            pFrameDepth.getData(pBuffer);
                            YoonImage pImage = new YoonImage(pBuffer, nWidth, nHeight, nBpp);
                            CameraEventHandler.callReceiveImageEvent(YoonRealsense.class, pImage);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
