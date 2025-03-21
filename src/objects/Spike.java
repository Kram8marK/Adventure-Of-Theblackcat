package objects;

import main.Game;

public class Spike extends GameObject{

	//รับตำแหน่ง (x, y) และประเภทของวัตถุ (objType)
	public Spike(int x, int y, int objType) {
		super(x, y, objType);
		initHitbox(32, 16);//Hitbox ของหนามด้วยขนาด 32x16 พิกเซล
		xDrawOffset = 0;
		yDrawOffset = (int)(Game.SCALE * 16);
		hitbox.y += yDrawOffset;
	}
}
