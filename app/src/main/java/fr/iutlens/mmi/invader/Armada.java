package fr.iutlens.mmi.invader;

import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dubois on 05/12/2018.
 */

class Armada extends Sprite{

    private final List<Alien> alien;

    private final List<Projectile> missile;
    private final int missileDx;
    private final int missileDy;
    private int speed_x;
    private int speed_y;
    public int deltaScore;


    public Armada(int id, List<Projectile> missile) {
        super(id,0,0);
        this.missile = missile;
        alien = new ArrayList<>();

        for(int i = 0; i <6; ++i){
            for(int j= 0; j< 5; ++j){
                alien.add(new Alien(id,i*200,j*140));
            }
        }

        this.speed_x =8;
        this.speed_y =0;

        missileDx = sprite.w/2;
        missileDy = sprite.h;
    }

    @Override
    public void paint(Canvas canvas) {
        for (Sprite s: alien
             ) {
            s.paint(canvas);
        }
    }

    @Override
    public boolean act() {

        RectF bounds = getBoundingBox();
        ++state;

        if (speed_y != 0){
            y += speed_y;
            if (state >= 20){
                speed_x = -speed_x;
                speed_y = 0;
            }
        } else if (speed_x +bounds.right >= GameView.SIZE_X|| bounds.left+speed_x < 0){
            speed_y = 8;
            state = 0;
        }

        Iterator<Alien> it = alien.iterator();
        while(it.hasNext()){
            Sprite s = it.next();
            s.state = (state/10)%2;
            if (speed_y != 0) s.y+= speed_y;
            else s.x+=speed_x;
            if (s.act()) it.remove();
            else if (Math.random()<0.005f){
                missile.add(new Projectile(R.mipmap.missile,s.x+missileDx,s.y+missileDy,+15));
            }
        }

        return false;
    }

    public RectF getBoundingBox() {
        RectF result = null;
        for (Alien s : alien
                ) {
            final RectF boundingBox = s.getBoundingBox();
            if (result == null) result = boundingBox;
            else result.union(boundingBox);
        }

        return result;
    }

    public void testIntersection(List<Projectile> laser) {
        deltaScore = 0;
        for(Projectile p : laser){
            RectF bbox = p.getBoundingBox();
            for(Alien a: alien){
                if (bbox.intersect(a.getBoundingBox())){
                    a.hit = true;
                    p.hit = true;
                    deltaScore += 100;
                }
            }
        }

    }
}
