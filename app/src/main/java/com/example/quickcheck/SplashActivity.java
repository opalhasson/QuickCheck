package com.example.quickcheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;


public class SplashActivity extends AppCompatActivity {

    private ImageView gifImageView;
    private GifDrawable gifDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        gifImageView = findViewById(R.id.gif_image_view);

        gifImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });

        Glide.with(this)
                .asGif()
                .load(R.raw.splash)
                .into(gifImageView);

        gifDrawable = (GifDrawable) gifImageView.getDrawable();
    }

    public void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
