package effects;

import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.Dialogue.*;

public class DialogueEffect {

	private int x, y, type;
	//(x,y)ตำแหน่งบนหน้าจอที่เอฟเฟกต์จะถูกแสดง
	//(type)ประเภทของเอฟเฟกต์
	private int aniIndex, aniTick;
	//(aniIndex)ของเฟรมปัจจุบันในภาพเคลื่อนไหว
	//(aniTick)ตัวนับเวลาเพื่อควบคุมความเร็วของภาพเคลื่อนไหว
	private boolean active = true;//ทำงานอยู่หรือไม่

	//กำหนดค่าเริ่มต้นว่าDialougeอยู่ตรงไหน
	public DialogueEffect(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}

	public void update() {
		aniTick++;//เพิ่มค่าตัวนับเวลา (aniTick) ในทุกการอัปเดต
		if (aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(type)) {
				active = false;
				aniIndex = 0;//หาก aniIndex เกินจำนวนเฟรมทั้งหมดของเอฟเฟกต์จะหยุดการทำงานและเว็ตค่าเป็น0
			}
		}
	}

	//หยุดการทำงานของเอฟเฟกต์
	public void deactive() {
		active = false;
	}

	//ตั้งค่าpositionใหม่และเปิดการทำงาน
	public void reset(int x, int y) {
		this.x = x;
		this.y = y;
		active = true;
	}

	//ส่งคืนค่าเฟรมปัจจุบัน
	public int getAniIndex() {
		return aniIndex;
	}

	//คืนตำแหน่ง x ของเอฟเฟกต์
	public int getX() {
		return x;
	}

	//คืนตำแหน่ง y ของเอฟเฟกต์
	public int getY() {
		return y;
	}

	//ส่งคืนประเภทของเอฟเฟกต์
	public int getType() {
		return type;
	}

	//ส่งคืนสถานะการทำงานของเอฟเฟกต์
	public boolean isActive() {
		return active;
	}
}
