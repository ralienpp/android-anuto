package ch.logixisland.anuto.entity.shot;

import android.graphics.Canvas;

import ch.logixisland.anuto.R;
import ch.logixisland.anuto.engine.render.Layers;
import ch.logixisland.anuto.engine.render.sprite.SpriteInstance;
import ch.logixisland.anuto.engine.render.sprite.SpriteTemplate;
import ch.logixisland.anuto.engine.render.sprite.StaticSprite;
import ch.logixisland.anuto.entity.Entity;
import ch.logixisland.anuto.entity.Types;
import ch.logixisland.anuto.entity.enemy.Enemy;
import ch.logixisland.anuto.util.RandomUtils;
import ch.logixisland.anuto.util.math.vector.Vector2;

public class CanonShotMG extends Shot {

    private final static float HIT_RANGE = 0.5f;
    private final static float MOVEMENT_SPEED = 8.0f;

    private class StaticData {
        public SpriteTemplate mSpriteTemplate;
    }

    private float mAngle;
    private float mDamage;

    private StaticSprite mSprite;

    public CanonShotMG(Entity origin, Vector2 position, Vector2 direction, float damage) {
        super(origin);
        setPosition(position);
        setSpeed(MOVEMENT_SPEED);
        setDirection(direction);

        mAngle = direction.angle();
        mDamage = damage;

        StaticData s = (StaticData) getStaticData();

        mSprite = getSpriteFactory().createStatic(Layers.SHOT, s.mSpriteTemplate);
        mSprite.setListener(this);
        mSprite.setIndex(RandomUtils.next(4));
    }

    @Override
    public Object initStatic() {
        StaticData s = new StaticData();

        s.mSpriteTemplate = getSpriteFactory().createTemplate(R.drawable.canon_mg_shot, 4);
        s.mSpriteTemplate.setMatrix(0.2f, null, null, -90f);

        return s;
    }

    @Override
    public void init() {
        super.init();

        getGameEngine().add(mSprite);
    }

    @Override
    public void clean() {
        super.clean();

        getGameEngine().remove(mSprite);
    }

    @Override
    public void draw(SpriteInstance sprite, Canvas canvas) {
        super.draw(sprite, canvas);

        canvas.rotate(mAngle);
    }

    @Override
    public void tick() {
        super.tick();

        Enemy enemy = (Enemy) getGameEngine().get(Types.ENEMY)
                .filter(inRange(getPosition(), HIT_RANGE))
                .first();

        if (enemy != null) {
            enemy.damage(mDamage, getOrigin());
            this.remove();
        }

        if (!isInGame()) {
            this.remove();
        }
    }
}
