package fcul.cm.g20.ecopack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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
                SharedPreferences preferences = getSharedPreferences("userCredentials", Context.MODE_PRIVATE);
                if (preferences.contains("username")) startActivity(new Intent(getApplicationContext(), MainActivity.class));
                else startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        }, 2200);
    }
}