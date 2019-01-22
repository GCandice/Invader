package fr.iutlens.mmi.invader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import fr.iutlens.mmi.invader.utils.RefreshHandler;
import fr.iutlens.mmi.invader.utils.SpriteSheet;
import fr.iutlens.mmi.invader.utils.TimerAction;


public class GameView extends View implements TimerAction {
    private RefreshHandler timer;

    // taille de l'écran virtuel
    public final static int SIZE_X = 2000;
    public final static int SIZE_Y = 2400;

    // transformation (et son inverse)
    private Matrix transform;
    private Matrix reverse;

    //liste des sprites à afficher


    private Armada armada;
    private Canon canon;
    private List<Projectile> missile;
    private List<Projectile> laser;
    private TextView textViewScore;
    private int score;
    private MainActivity activity;


    public GameView(Context context) {
        super(context);
        init(null, 0);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    /**
     * Initialisation de la vue
     *
     * Tous les constructeurs (au-dessus) renvoient ici.
     *
     * @param attrs
     * @param defStyle
     */
    private void init(AttributeSet attrs, int defStyle) {

        // Chargement des feuilles de sprites
        SpriteSheet.register(R.mipmap.alien,2,1,this.getContext());
        SpriteSheet.register(R.mipmap.missile,4,1,this.getContext());
        SpriteSheet.register(R.mipmap.laser,1,1,this.getContext());
        SpriteSheet.register(R.mipmap.canon,1,1,this.getContext());
        SpriteSheet.register(R.mipmap.vie,1,1,this.getContext());

        transform = new Matrix();
        reverse = new Matrix();

        missile = new ArrayList<>();
        laser = new ArrayList<>();

        armada = new Armada(R.mipmap.alien,missile);
        canon = new Canon(R.mipmap.canon,800, 2200,laser);
        // x: position horizontale y : position verticale


//        hero = new Hero(R.drawable.running_rabbit,SPEED);



        // Gestion du rafraichissement de la vue. La méthode update (juste en dessous)
        // sera appelée toutes les 30 ms
        timer = new RefreshHandler(this);

        // Un clic sur la vue lance (ou relance) l'animation
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!timer.isRunning()) timer.scheduleRefresh(30);
            }
        });
    }


    public static void act(List list){
        Iterator it = list.iterator();
        while (it.hasNext()) if (((Sprite) it.next()).act()) it.remove();
    }
    /**
     * Mise à jour (faite toutes les 30 ms)
     */
    @Override
    public void update() {
        if (this.isShown() && canon.vie >= 0) { // Si la vue est visible
            timer.scheduleRefresh(30); // programme le prochain rafraichissement

            armada.testIntersection(laser);
            canon.testIntersection(missile);
            armada.act();
            canon.act();

            act(missile);
            act(laser);
            score += armada.deltaScore;


            if (textViewScore != null) textViewScore.setText(""+score);
            invalidate(); // demande à rafraichir la vue
        } else if (canon.vie <0){

            Intent intent = new Intent(activity, result.class);
            intent.putExtra("SCORE", score);
            activity.startActivity(intent);
        }
    }

    /**
     * Méthode appelée (automatiquement) pour afficher la vue
     * C'est là que l'on dessine le décor et les sprites
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // On met une couleur de fond
        //canvas.drawColor(0xff000077);

        // On choisit la transformation à appliquer à la vue i.e. la position
        // de la "camera"

        for( int i = 0 ; i<=canon.vie-1 ; i++){
            SpriteSheet.get(R.mipmap.vie).paint(canvas,0,i*90,20);
        }

        canvas.concat(transform);

        for(Sprite s : missile){
            s.paint(canvas);
        }
        for(Sprite s : laser){
            s.paint(canvas);
        }
        canon.paint(canvas);
        armada.paint(canvas);


        // Dessin des différents éléments
/*        level.paint(canvas,current_pos);

        float x = 1;
        float y = hero.getY();
        hero.paint(canvas,level.getX(x),level.getY(y));
*/
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        setZoom(w, h);
    }

    /***
     * Calcul du centrage du contenu de la vue
     * @param w
     * @param h
     */
    private void setZoom(int w, int h) {
        if (w<=0 ||h <=0) return;

        // Dimensions dans lesquelles ont souhaite dessiner
        RectF src = new RectF(0,0,SIZE_X,SIZE_Y);

        // Dimensions à notre disposition
        RectF dst = new RectF(0,0,w,h);

        // Calcul de la transformation désirée (et de son inverse)
        transform.setRectToRect(src,dst, Matrix.ScaleToFit.CENTER);
        transform.invert(reverse);
    }

    public void onLeft() {
        canon.setDirection(-1);
    }

    public void onRight(){
        canon.setDirection(+1);
    }

    public void onFire(){
        canon.fire();

    }

    public void setTextViewScore(TextView textViewScore) {
        this.textViewScore = textViewScore;
    }

    public TextView getTextViewScore() {
        return textViewScore;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public MainActivity getActivity() {
        return activity;
    }
}
