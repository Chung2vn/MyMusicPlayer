package com.android.mymusicplayer;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TextView txtSong, txtTime, txtTimeTotal;
    SeekBar skSong;
    ImageView imgDisc;
    ImageButton btnRewind, btnPlay, btnForward;

    ArrayList<Song> arraySong;
    int position = 0;
    MediaPlayer mediaPlayer;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);

        Mapping();
        AddSong();

        animation = AnimationUtils.loadAnimation(this, R.anim.disc_rotate);

        PlaySong();

        btnRewind.setOnClickListener(v -> {
            position--;
            if (position < 0){
                position = arraySong.size() - 1;
            }
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            PlaySong();
            mediaPlayer.start();
            btnPlay.setImageResource(R.drawable.pause);
            SetTimeTotal();
            UpdateTime();
        });

        btnForward.setOnClickListener(v -> {
            position++;
            if (position > arraySong.size() -1){
                position = 0;
            }
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            PlaySong();
            mediaPlayer.start();
            btnPlay.setImageResource(R.drawable.pause);
            SetTimeTotal();
            UpdateTime();
        });



        btnPlay.setOnClickListener(v -> {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            btnPlay.setImageResource(R.drawable.play);
        }else {
            mediaPlayer.start();
            btnPlay.setImageResource(R.drawable.pause);
        }
            SetTimeTotal();
            UpdateTime();
            imgDisc.startAnimation(animation);
        });

        skSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(skSong.getProgress());
            }
        });
    }

    private void UpdateTime(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
                txtTime.setText(timeFormat.format(mediaPlayer.getCurrentPosition()));
                // update progress seekbar
                skSong.setProgress(mediaPlayer.getCurrentPosition());

                // forward to next if song has ended
                mediaPlayer.setOnCompletionListener(mp -> {
                    position++;
                    if (position > arraySong.size() -1){
                        position = 0;
                    }
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                    }
                    PlaySong();
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.pause);
                    SetTimeTotal();
                    UpdateTime();
                });

                handler.postDelayed(this, 500);
            }
        }, 100);
    }

    private void SetTimeTotal(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
        txtTimeTotal.setText(timeFormat.format(mediaPlayer.getDuration()));
        // set seekbar equal mediaPlayer.getDuration()
        skSong.setMax(mediaPlayer.getDuration());
    }

    private void PlaySong(){
        mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(position).getFile());
        txtSong.setText(arraySong.get(position).getTitle());
    }

    private void AddSong() {
        arraySong = new ArrayList<>();
        arraySong.add(new Song("Anh Đếch Cần Gì Nhiều Ngoài Em", R.raw.anh_dech_can_gi_nhieu_ngoai_em));
        arraySong.add(new Song("Bài Này Chill Phết", R.raw.bai_nay_chill_phet));
        arraySong.add(new Song("Đố Em Biết Anh Đang Nghĩ Gì", R.raw.do_em_biet_anh_dang_nghi_gi));
        arraySong.add(new Song("Hai Triệu Năm", R.raw.hai_trieu_nam));
        arraySong.add(new Song("Lối Nhỏ", R.raw.loi_nho));
        arraySong.add(new Song("Mang Tiền Về Cho Mẹ", R.raw.mang_tien_ve_cho_me));
        arraySong.add(new Song("Một Triệu Like", R.raw.mot_trieu_like));
        arraySong.add(new Song("Ngày Khác Lạ", R.raw.ngay_khac_la));
        arraySong.add(new Song("Ta Và Nàng", R.raw.ta_va_nang));
        arraySong.add(new Song("Trốn Tìm", R.raw.tron_tim));
    }

    private void Mapping() {
        txtTime = findViewById(R.id.textViewTime);
        txtTimeTotal = findViewById(R.id.textViewTimeTotal);
        txtSong = findViewById(R.id.textViewSong);
        skSong = findViewById(R.id.seekBar);
        btnRewind = findViewById(R.id.imageButtonRewind);
        btnPlay = findViewById(R.id.imageButtonPlay);
        btnForward = findViewById(R.id.imageButtonForward);
        imgDisc = findViewById(R.id.imageViewDisc);
    }
}