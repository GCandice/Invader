package fr.iutlens.mmi.invader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class result extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView ScoreLabel = (TextView) findViewById(R.id.ScoreLabel);
        TextView MeilleurScoreLabel = (TextView) findViewById(R.id.MeilleurScoreLabel);

        int score = getIntent().getIntExtra("SCORE", 0);
        ScoreLabel.setText(score + "");

        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int MeilleurScore = settings.getInt("MEILLEUR_SCORE", 0);

        if (score > MeilleurScore) {
            MeilleurScoreLabel.setText("Meilleur score : " + score);

            //enregistrer

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("MEILLEUR_SCORE", score);
            editor.commit();

        } else {
            MeilleurScoreLabel.setText("Meilleur Score : " + MeilleurScore);


        }

    }

        public void tryAgain(View view) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }
}