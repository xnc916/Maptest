package com.yuluedu.maptest.fragment;

import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.yuluedu.maptest.commons.ActivityUtils;

import java.io.FileDescriptor;

public class MainMP4Fragment extends Fragment implements TextureView.SurfaceTextureListener{

    private TextureView mTextureView;
    private ActivityUtils mActivityUtils;
    private MediaPlayer mMediaPlayer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivityUtils = new ActivityUtils(this);


        // Fragment全屏显示播放视频的控件
        mTextureView = new TextureView(getContext());

        return mTextureView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 当我们所要展示的视频播放的控件已经准备好的时候，就可以播放视频
        // 什么时候准备好呢，我们可以设置一个监听，看有没有准备好或者是有没有变化
        mTextureView.setSurfaceTextureListener(this);
    }



    @Override
    public void onSurfaceTextureAvailable(final SurfaceTexture surface, int width, int height) {

        try {
            //打开资源文件
            AssetFileDescriptor openFd = getContext().getAssets().openFd("welcome.mp4");

            //拿到资源
            FileDescriptor fileDescriptor = openFd.getFileDescriptor();

            mMediaPlayer = new MediaPlayer();


            // 设置播放的资源给MediaPlayer
            mMediaPlayer.setDataSource(fileDescriptor,openFd.getStartOffset(),openFd.getLength());

            // 异步准备
            mMediaPlayer.prepareAsync();

            // 设置准备的监听：看一下有没有准备好，可不可以播放
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                // 准备好了，视频可以播放了
                @Override
                public void onPrepared(MediaPlayer mp) {

                    Surface mySurface = new Surface(surface);
                    mMediaPlayer.setSurface(mySurface);
                    mMediaPlayer.setLooping(true);// 循环播放
                    mMediaPlayer.start();// 开始播放
                }
            });




        } catch (Exception e) {
            mActivityUtils.showToast("媒体文件播放失败了");
        }

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}

