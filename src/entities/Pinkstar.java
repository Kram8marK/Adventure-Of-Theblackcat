package entities;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.EnemyConstants.GetSpriteAmount;
import static utilz.Constants.Dialogue.*;
import static utilz.HelpMethods.CanMoveHere;
import static utilz.HelpMethods.IsFloor;
import static utilz.Constants.Directions.*;

import gamestates.Playing;

public class Pinkstar extends Enemy {

	private boolean preRoll = true;//ตรวจสอบว่า น้องดาว กำลังเตรียมตัวกลิ้งหรือไม่
	private int tickSinceLastDmgToPlayer;
	private int tickAfterRollInIdle;
	private int rollDurationTick, rollDuration = 300;//ระยะเวลาการกลิ้ง (300 ticks)

	//กำหนดค่าเริ่มต้นให้กับตัวแปร x, y, width, height, และ enemyType เมื่อสร้างอ็อบเจกต์ Pinkstar
	public Pinkstar(float x, float y) {
		super(x, y, PINKSTAR_WIDTH, PINKSTAR_HEIGHT, PINKSTAR);
		initHitbox(17, 21);
	}

	//อัปเดตพฤติกรรมและการเคลื่อนไหวของ Pinkstar
	public void update(int[][] lvlData, Playing playing) {
		updateBehavior(lvlData, playing);
		updateAnimationTick();
	}

	private void updateBehavior(int[][] lvlData, Playing playing) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir)
			inAirChecks(lvlData, playing);
		else {
			switch (state) {
			case IDLE://หาก Pinkstar อยู่ในสถานะ IDLE เป็นเวลา 120 ticks จะเปลี่ยนสถานะเป็น RUNNING
				preRoll = true;
				if (tickAfterRollInIdle >= 120) {
					if (IsFloor(hitbox, lvlData))
						newState(RUNNING);
					else
						inAir = true;
					tickAfterRollInIdle = 0;
					tickSinceLastDmgToPlayer = 60;
				} else
					tickAfterRollInIdle++;
				break;
			case RUNNING://หาก Pinkstar มองเห็นผู้เล่น จะเปลี่ยนสถานะเป็น ATTACK และกำหนดทิศทางการเดิน
				if (canSeePlayer(lvlData, playing.getPlayer())) {
					newState(ATTACK);
					setWalkDir(playing.getPlayer());
				}
				move(lvlData, playing);
				break;
			case ATTACK://หาก Pinkstar อยู่ในสถานะ ATTACK จะเคลื่อนที่และโจมตีผู้เล่น
				if (preRoll) {
					if (aniIndex >= 3)
						preRoll = false;
				} else {
					move(lvlData, playing);
					checkDmgToPlayer(playing.getPlayer());
					checkRollOver(playing);
				}
				break;
			case HIT://หาก Pinkstar ถูกโจมตี จะถูกพลักกลับและอัปเดตการเคลื่อนไหว
				if (aniIndex <= GetSpriteAmount(enemyType, state) - 2)
					pushBack(pushBackDir, lvlData, 2f);
				updatePushBackDrawOffset();
				tickAfterRollInIdle = 120;

				break;
			}
		}
	}

	//ตรวจสอบการชนกับผู้เล่นและลดเลือดของผู้เล่นหากถูกโจมตี
	private void checkDmgToPlayer(Player player) {
		if (hitbox.intersects(player.getHitbox()))
			if (tickSinceLastDmgToPlayer >= 60) {
				tickSinceLastDmgToPlayer = 0;
				player.changeHealth(-GetEnemyDmg(enemyType), this);
			} else
				tickSinceLastDmgToPlayer++;
	}

	//กำหนดทิศทางการเดินของ น้องปู ตามตำแหน่งของผู้เล่น
	private void setWalkDir(Player player) {
		if (player.getHitbox().x > hitbox.x)
			walkDir = RIGHT;
		else
			walkDir = LEFT;

	}

	//เคลื่อนที่ Pinkstar ตามทิศทางที่กำหนด
	protected void move(int[][] lvlData, Playing playing) {
		float xSpeed = 0;

		if (walkDir == LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (state == ATTACK)//เมื่อโจมตีเคลื่อนที่เร็ว2เท่า
			xSpeed *= 2;

		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
			if (IsFloor(hitbox, xSpeed, lvlData)) {
				hitbox.x += xSpeed;
				return;
			}

		if (state == ATTACK) {
			rollOver(playing);
			rollDurationTick = 0;
		}

		changeWalkDir();

	}

	//ตรวจสอบระยะเวลาการกลิ้งและเปลี่ยนสถานะเป็น IDLE หากครบกำหนด
	private void checkRollOver(Playing playing) {
		rollDurationTick++;
		if (rollDurationTick >= rollDuration) {
			rollOver(playing);
			rollDurationTick = 0;
		}
	}

	//เปลี่ยนสถานะของ Pinkstar เป็น IDLE และแสดง(Question Dialouge)
	private void rollOver(Playing playing) {
		newState(IDLE);
		playing.addDialogue((int) hitbox.x, (int) hitbox.y, QUESTION);
	}

}
