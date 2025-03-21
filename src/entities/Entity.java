package entities;

import static utilz.Constants.Directions.DOWN;
import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.Directions.UP;
import static utilz.HelpMethods.CanMoveHere;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

public abstract class Entity {

	//ตำแหน่งและขนาด
	protected float x, y;
	protected int width, height;
	//Hitbox และ Attack Box:
	protected Rectangle2D.Float hitbox;
	protected Rectangle2D.Float attackBox;
	//animation
	protected int aniTick, aniIndex;//ตัวนับเวลาanimation,เฟรมanimation
	protected int state;//สถานะปัจจุบันของ entity
	//การเดิน(เคลื่อนที่)
	protected float airSpeed;//ความเร็วในการตกเมื่อบนอากาศ
	protected boolean inAir = false;//เช็คว่าอยู่บนอากาศไหม
	protected float walkSpeed;//ความเร็วในการเดิน
	//ค่าเลือด
	protected int maxHealth;//เลือดทั้งหมด
	protected int currentHealth;//เลือดปัจจุบัน
	
	//การกระเด็นเมื่อโดนตี
	protected int pushBackDir;//ทิศทางที่ถูกพลัก
	protected float pushDrawOffset;
	protected int pushBackOffsetDir = UP;

	//กำหนดค่าเริ่มต้นให้กับตัวแปร x,y,width,height เมื่อสร้างอ็อบเจกต์entity
	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	//อัปเดตค่าการถูกพลัก เพื่อให้entityคลื่อนที่ขึ้นและลงเมื่อถูกโจมตี
	protected void updatePushBackDrawOffset() {
		float speed = 0.95f;
		float limit = -30f;

		if (pushBackOffsetDir == UP) {
			pushDrawOffset -= speed;
			if (pushDrawOffset <= limit)
				pushBackOffsetDir = DOWN;
		} else {
			pushDrawOffset += speed;
			if (pushDrawOffset >= 0)
				pushDrawOffset = 0;
		}
	}

	//entity กระเด็นเมื่อถูกโจมตี โดยตรวจสอบว่าสามารถเคลื่อนที่ไปยังตำแหน่งใหม่ได้หรือไม่
	protected void pushBack(int pushBackDir, int[][] lvlData, float speedMulti) {
		float xSpeed = 0;
		if (pushBackDir == LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (CanMoveHere(hitbox.x + xSpeed * speedMulti, hitbox.y, hitbox.width, hitbox.height, lvlData))
			hitbox.x += xSpeed * speedMulti;
	}

	//วาด Attack Box บนหน้าจอ (ใช้สำหรับ debugging)
	protected void drawAttackBox(Graphics g, int xLvlOffset) {
		g.setColor(Color.red);
		g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
	}

	//วาด Hitbox บนหน้าจอ (ใช้สำหรับ debugging)
	protected void drawHitbox(Graphics g, int xLvlOffset) {
		g.setColor(Color.BLUE);
		g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
	}

	//กำหนด Hitbox ของentityโดยคำนวณขนาดตามสเกลของเกม
	protected void initHitbox(int width, int height) {
		hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
	}

	//ส่งคืน Hitbox ของ entity
	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	//ส่งคืนสถานะปัจจุบันของ entity
	public int getState() {
		return state;
	}

	//ส่งคืนดัชนีเฟรมปัจจุบันใน animation
	public int getAniIndex() {
		return aniIndex;
	}

	//เปลี่ยนสถานะของ entity และรีเซ็ตการเคลื่อนไหว
	protected void newState(int state) {
		this.state = state;
		aniTick = 0;
		aniIndex = 0;
	}
}