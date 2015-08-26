package com.example.talkingtoy;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

public class Sounds {
    SoundPool soundPool;
    HashMap<Integer, Integer> soundPoolMap;
    AudioManager audioManager;


    public Sounds(Context context, int[] preloadResIds) {
        audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>();
        for( int i : preloadResIds){
            soundPoolMap.put(i, soundPool.load(context, i, 1));
        }
    }

    void sound(int resId){
//        float curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//        float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float leftVolume = 1;//curVolume / maxVolume;
        float rightVolume = 1;//curVolume / maxVolume;
        int priority = 1;
        int no_loop = 0;
        float normal_playback_rate = 1f;
        soundPool.play(soundPoolMap.get(resId), leftVolume, rightVolume, priority, no_loop, normal_playback_rate);
    }

}
