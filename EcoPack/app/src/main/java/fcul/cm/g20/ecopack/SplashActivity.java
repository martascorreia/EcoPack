package fcul.cm.g20.ecopack;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findViewById(R.id.image_package).setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_animation));
        findViewById(R.id.image_logo).setAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_animation));

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }, 2200);
    }
}