package objects;

import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.ObjectConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

public class GameObject {

	protected int x, y, objType;
	protected Rectangle2D.Float hitbox;
	protected boolean doAnimation, active = true;
	protected int aniTick, aniIndex;
	protected int xDrawOffset, yDrawOffset;

	//รับตำแหน่ง (x, y) และประเภทของวัตถุ (objType) แล้วทำการกำหนดค่าให้กับตัวแปรอินสแตนซ์
	public GameObject(int x, int y, int objType) {
		this.x = x;
		this.y = y;
		this.objType = objType;
	}

	//อัปเดตการเคลื่อนไหวของวัตถุ
	protected void updateAnimationTick() {
		aniTick++;//เพิ่มค่า aniTick ทุกครั้งที่อัปเดต
		if (aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;//หาก aniTick ถึง ANI_SPEED จะรีเซ็ต aniTick และเพิ่ม aniIndex
			if (aniIndex >= GetSpriteAmount(objType)) {
				aniIndex = 0;
				if (objType == BARREL || objType == BOX) {
					doAnimation = false;
					active = false;
				} else if (objType == CANNON_LEFT || objType == CANNON_RIGHT)
					doAnimation = false;
			}
		}
	}

	//สำหรับรีเซ็ตวัตถุ
	public void reset() {
		aniIndex = 0;
		aniTick = 0;
		active = true;
		//รีเซ็ต aniIndex และ aniTick เป็น 0

		if (objType == BARREL || objType == BOX || objType == CANNON_LEFT || objType == CANNON_RIGHT)
			doAnimation = false;//หยุดการเคลื่อนไหวหากวัตถุเป็นถัง, กล่อง, หรือปืนใหญ่
		else
			doAnimation = true;
	}

	//กำหนด Hitbox ของวัตถุ โดยคำนวณขนาด Hitbox
	protected void initHitbox(int width, int height) {
		hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
	}

	//วาด Hitbox ของวัตถุบนหน้าจอ
	public void drawHitbox(Graphics g, int xLvlOffset) {
		g.setColor(Color.PINK);
		g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
	}

	public int getObjType() {
		return objType;
	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setAnimation(boolean doAnimation) {
		this.doAnimation = doAnimation;
	}

	public int getxDrawOffset() {
		return xDrawOffset;
	}

	public int getyDrawOffset() {
		return yDrawOffset;
	}

	public int getAniIndex() {
		return aniIndex;
	}

	public int getAniTick() {
		return aniTick;
	}

}
