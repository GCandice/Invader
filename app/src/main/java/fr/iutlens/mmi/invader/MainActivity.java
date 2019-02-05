package fr.iutlens.mmi.invader;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button bt;


    private GameView gameView;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameView = findViewById(R.id.gameView);
        TextView textViewScore = findViewById(R.id.textScore);
        gameView.setTextViewScore(textViewScore);
        gameView.setActivity(this);

        bt = (Button) findViewById(R.id.buttonFire);
        mp = MediaPlayer.create(this, R.raw.tir);


        //Bind music service

        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);

    }


        private boolean mIsBound = false;
        private MusicService mServ;
        private ServiceConnection Scon =new ServiceConnection(){

            public void onServiceConnected(ComponentName name, IBinder
                    binder) {
                mServ = ((MusicService.ServiceBinder)binder).getService();
            }

            public void onServiceDisconnected(ComponentName name) {
                mServ = null;
            }
        };

        void doBindService(){
            bindService(new Intent(this,MusicService.class),
                    Scon, Context.BIND_AUTO_CREATE);
            mIsBound = true;
        }

        void doUnbindService()
        {
            if(mIsBound)
            {
                unbindService(Scon);
                mIsBound = false;
            }


        }

    @Override
    protected void onPause() {
        super.onPause();
        if (mServ != null) {
            mServ.pauseMusic();
        }
    }

    @Override
        protected void onResume() {
            super.onResume();

            if (mServ != null) {
                mServ.resumeMusic();
            }

        }

        //unbind

        @Override
        protected void onDestroy() {
            super.onDestroy();

            doUnbindService();
            Intent music = new Intent();
            music.setClass(this,MusicService.class);
            stopService(music);

        }


    public void onLeft(View view) {
        gameView.onLeft();
    }

    public void onRight(View view) {
        gameView.onRight();
    }

    public void onFire(View view) {
//        mp.start();
        gameView.onFire();
    }



    //Résulats

}
