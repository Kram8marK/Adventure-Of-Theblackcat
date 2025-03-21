package objects;

import main.Game;

public class Cannon extends GameObject {

	private int tileY;

	//รับตำแหน่ง (x, y) และประเภทของวัตถุ (objType) แล้วทำการ
	public Cannon(int x, int y, int objType) {
		super(x, y, objType);
		tileY = y / Game.TILES_SIZE;//คำนวณค่า tileY โดยหารตำแหน่ง y ด้วยขนาดของไทล์ (Game.TILES_SIZE)
		initHitbox(40, 26);//กำหนด Hitbox ของปืนใหญ่ด้วยขนาด 40x26 พิกเซล
		hitbox.y += (int) (6 * Game.SCALE);
	}

	//อัปเดตสถานะของปืนใหญ่
	public void update() {
		if (doAnimation)
			updateAnimationTick();
	}

	public int getTileY() {
		return tileY;
	}

}
