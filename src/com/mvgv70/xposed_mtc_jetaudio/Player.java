package com.mvgv70.xposed_mtc_jetaudio;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Player implements IXposedHookLoadPackage 
{
  private static MediaPlayer mPlayer;
  private static Context context;
  // mp3-tags
  private static String album = "";
  private static String title = "";
  private static String artist = "";
  private static boolean mPlaying = false;
  private final static String PACKAGE_NAME = "com.jetappfactory.jetaudio"; 
  private final static String TAG = "xposed-mtc-jetaudio";
  
  @Override
  public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable 
  {
    
    // MediaPlayer constructor
    XC_MethodHook playerCreate = new XC_MethodHook() {
        
      @Override
      protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        Log.d(TAG,"playerCreate");
        mPlayer = (MediaPlayer)param.thisObject;
        // TODO: создать контекст и показать версию модуля
        context = (Context)param.args[0];
      }
    };
    
    // MediaPlayer.setDataSource(String) 
    XC_MethodHook setDataSource = new XC_MethodHook() {
        
      @Override
      protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        String fileName = (String)param.args[0];
        Log.d(TAG,"setDataSource("+fileName+")");
      }
    };
        
    // MediaPlayer.prepare() & prepareAsync() 
    XC_MethodHook prepare = new XC_MethodHook() {
        
      @Override
      protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        Log.d(TAG,"prepare");
        // TODO: handler.postDelayed()
      }
    };
    
    // MediaPlayer.start() 
    XC_MethodHook start = new XC_MethodHook() {
        
      @Override
      protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        Log.d(TAG,"start");
        mPlaying = true;
      }
    };
    
    // MediaPlayer.stop() 
    XC_MethodHook stop = new XC_MethodHook() {
        
      @Override
      protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        Log.d(TAG,"stop");
        mPlaying = false;
      }
    };
    
    // MediaPlayer.pause() 
    XC_MethodHook pause = new XC_MethodHook() {
        
      @Override
      protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        Log.d(TAG,"pause");
        mPlaying = false;
      }
    };
    
    // MediaPlayer.release()
    XC_MethodHook release = new XC_MethodHook() {
        
      @Override
      protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        Log.d(TAG,"release");
        mPlaying = false;
        mPlayer = null;
      }
    };
    
    // MediaPlayer.scanInternalSubtitleTracks()
    XC_MethodHook scanInternalSubtitleTracks = new XC_MethodHook() {
        
      @Override
      protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        Log.d(TAG,"scanInternalSubtitleTracks");
      }
    };
    
    // start hooks
    if (!lpparam.packageName.equals(PACKAGE_NAME)) return;
    Log.d(TAG,PACKAGE_NAME);
    XposedHelpers.findAndHookConstructor("android.media.MediaPlayer", lpparam.classLoader, playerCreate);
    XposedHelpers.findAndHookMethod("android.media.MediaPlayer", lpparam.classLoader, "setDataSource", String.class, setDataSource);
    XposedHelpers.findAndHookMethod("android.media.MediaPlayer", lpparam.classLoader, "prepare", prepare);
    XposedHelpers.findAndHookMethod("android.media.MediaPlayer", lpparam.classLoader, "prepareAsync", prepare);
    XposedHelpers.findAndHookMethod("android.media.MediaPlayer", lpparam.classLoader, "start", start);
    XposedHelpers.findAndHookMethod("android.media.MediaPlayer", lpparam.classLoader, "stop", stop);
    XposedHelpers.findAndHookMethod("android.media.MediaPlayer", lpparam.classLoader, "pause", pause);
    XposedHelpers.findAndHookMethod("android.media.MediaPlayer", lpparam.classLoader, "release", release);
    XposedHelpers.findAndHookMethod("android.media.MediaPlayer", lpparam.classLoader, "scanInternalSubtitleTracks", scanInternalSubtitleTracks);
    Log.d(TAG,PACKAGE_NAME+" hook OK");
  }
  
}
