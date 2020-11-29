package fcul.cm.g20.ecopack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    //VARIÁVEIS
    Animation topAnimation, boAnimation;
    ImageView packageImg;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Animações
        topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        boAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //
        packageImg = findViewById(R.id.imageView2);
        logo = findViewById(R.id.imgLog2);

        //Adiciona as animações
        packageImg.setAnimation(topAnimation);
        logo.setAnimation(boAnimation);

        // HIDE ACTION BAR
        getSupportActionBar().hide();

        // START THE CANVAS ACTIVITY AFTER A 2 SECOND WAIT
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent navToHome = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(navToHome);
                finish();
            }
        }, 2200);
    }
}