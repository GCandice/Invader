package fr.iutlens.mmi.linvasion;

import android.graphics.RectF;

import java.util.List;

import fr.iutlens.mmi.linvasion.utils.SpriteSheet;

/**
 * Created by dubois on 05/12/2018.
 */

class Canon extends Sprite {
    public static final int SPEED = 12;
    private final List<Projectile> laser;
    private final int dxLaser;
    private final int dyLaser;
    int vx = 0;
    public int vie = 3;

    Canon(int id, float x, float y, List<Projectile> laser) {
        super(id, x, y);
        this.laser = laser;
        final SpriteSheet laserSprite = SpriteSheet.get(R.mipmap.laser);
        dxLaser = sprite.w/2- laserSprite.w/2;
        dyLaser = -laserSprite.h;
    }

    @Override
    public boolean act() {
        RectF bounds = getBoundingBox();
        int dx = vx * SPEED;
        if (bounds.left+dx>0 && bounds.right+dx< GameView.SIZE_X){
            x += dx;
        } else {
            vx = 0;
        }
        if (hit == true) {
            hit = false;
            vie = vie-1;
        }
        return false;
    }

    public void setDirection(int i) {
        vx += i;
        if (vx<-2) vx = -2;
        else if (vx>2) vx = 2;
    }


    public boolean fire() {
        if (laser.size() >2 ) {
            return false;

        } else {

            laser.add(new Projectile(R.mipmap.laser,x+dxLaser,y+dyLaser,-20));
        }
         return true;

    }

    public void testIntersection(List<Projectile> missile) {
        for(Projectile p : missile){
            RectF bbox = p.getBoundingBox();
                if (bbox.intersect(getBoundingBox())){
                    hit = true;
                    p.hit = true;
                }
        }
    }
}
