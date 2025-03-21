package objects;

import static utilz.Constants.ObjectConstants.*;

import main.Game;

public class GameContainer extends GameObject {

	//รับตำแหน่ง (x, y) และประเภทของวัตถุ (objType)
	public GameContainer(int x, int y, int objType) {
		super(x, y, objType);
		createHitbox();//สร้าง Hitbox สำหรับวัตถุ
	}

	//สร้าง Hitbox ของวัตถุ
	private void createHitbox() {
		if (objType == BOX) {
			initHitbox(25, 18);//หากวัตถุเป็นกล่อง (BOX) จะกำหนด Hitbox ขนาด 25x18 พิกเซล

			xDrawOffset = (int) (7 * Game.SCALE);
			yDrawOffset = (int) (12 * Game.SCALE);

		} else {
			initHitbox(23, 25);//หากวัตถุเป็นถัง (BARREL) จะกำหนด Hitbox ขนาด 23x25 พิกเซล
			xDrawOffset = (int) (8 * Game.SCALE);
			yDrawOffset = (int) (5 * Game.SCALE);
		}

		hitbox.y += yDrawOffset + (int) (Game.SCALE * 2);
		hitbox.x += xDrawOffset / 2;
	}

	//อัปเดตสถานะของวัตถุ
	public void update() {
		if (doAnimation)
			updateAnimationTick();
	}
}
