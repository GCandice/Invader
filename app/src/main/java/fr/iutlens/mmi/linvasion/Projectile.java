package fr.iutlens.mmi.linvasion;

/**
 * Created by dubois on 06/12/2018.
 */

class Projectile extends Sprite {
    private final int vy;
    private int frame;

    public Projectile(int id, float x, float y, int vy) {
        super(id,x,y);
        this.vy = vy;
    }

    @Override
    public boolean act() {
        y += vy;
        frame++;
        if (sprite.n>1) state = (frame/3)%sprite.n;
        return y < 0 || y> GameView.SIZE_Y || hit;
    }
}
