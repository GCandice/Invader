package fr.iutlens.mmi.linvasion;

import android.graphics.Canvas;
import android.graphics.RectF;

import fr.iutlens.mmi.linvasion.utils.SpriteSheet;

/**
 * Created by dubois on 05/12/2018.
 */

 class Sprite {

    protected final SpriteSheet sprite;
    protected int state;

    protected float x;
    protected float y;
    public boolean hit;

    public boolean act(){
         return hit;
     }


    Sprite(int id, float x, float y){
        this.sprite = SpriteSheet.get(id);
        this.x = x;
        this.y = y;
        state = 0;
        hit = false;
    }


    public void paint(Canvas canvas){
        sprite.paint(canvas,state,x,y);
    }

    public RectF getBoundingBox() {
        return new RectF(x,y,x+sprite.w,y+sprite.h);
    }
}
