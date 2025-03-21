package entities;

import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;

import java.awt.geom.Rectangle2D;

import gamestates.Playing;

import static utilz.Constants.Directions.*;
import static utilz.Constants.*;

import main.Game;

public abstract class Enemy extends Entity {

	protected int enemyType;//ประเภทศัตรู
	protected boolean firstUpdate = true;
	protected int walkDir = LEFT;//ทิศทางการเดินของศัตรู
	protected int tileY;
	protected float attackDistance = Game.TILES_SIZE;//ระยะทางที่ศัตรูสามารถโจมตีผู้เล่นได้
	protected boolean active = true;//ตรวจสอบว่าศัตรูยังทำงานอยู่หรือไม่
	protected boolean attackChecked;
	protected int attackBoxOffsetX;//ตำแหน่ง X ของ Attack Box

	//กำหนดค่าเริ่มต้นให้กับตัวแปร x, y, width, height, และ enemyType เมื่อสร้างอ็อบเจกต์ Enemy
	public Enemy(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;

		maxHealth = GetMaxHealth(enemyType);
		currentHealth = maxHealth;//เลือดเมื่อเริ่มจะเต็ม
		walkSpeed = Game.SCALE * 0.35f;//กำหนดความเร็วในการเดิน
	}

	//อัปเดตตำแหน่งของ Attack Box ตามทิศทางการเดินของศัตรู
	protected void updateAttackBox() {
		attackBox.x = hitbox.x - attackBoxOffsetX;
		attackBox.y = hitbox.y;
	}

	//อัปเดตตำแหน่งของ Attack Box ตามทิศทางการเดินของศัตรู
	protected void updateAttackBoxFlip() {
		if (walkDir == RIGHT)
			attackBox.x = hitbox.x + hitbox.width;
		else
			attackBox.x = hitbox.x - attackBoxOffsetX;

		attackBox.y = hitbox.y;
	}

	//กำหนด Attack Box ของศัตรูโดยคำนวณขนาดตามสเกลของเกม
	protected void initAttackBox(int w, int h, int attackBoxOffsetX) {
		attackBox = new Rectangle2D.Float(x, y, (int) (w * Game.SCALE), (int) (h * Game.SCALE));
		this.attackBoxOffsetX = (int) (Game.SCALE * attackBoxOffsetX);
	}

	//ตรวจสอบว่าศัตรูอยู่บนพื้นหรือไม่
	protected void firstUpdateCheck(int[][] lvlData) {
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
		firstUpdate = false;
	}

	//ตรวจสอบการอยู่บนอากาศของศัตรู และจัดการเมื่อศัตรูสัมผัสกับวัตถุที่เป็นหนามและน้ำ
	protected void inAirChecks(int[][] lvlData, Playing playing) {
		if (state != HIT && state != DEAD) {
			updateInAir(lvlData);
			playing.getObjectManager().checkSpikesTouched(this);
			if (IsEntityInWater(hitbox, lvlData))
				hurt(maxHealth);
		}
	}

	//อัปเดตตำแหน่งของศัตรูเมื่ออยู่บนอากาศ
	protected void updateInAir(int[][] lvlData) {
		if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
			hitbox.y += airSpeed;
			airSpeed += GRAVITY;
		} else {
			inAir = false;
			hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
			tileY = (int) (hitbox.y / Game.TILES_SIZE);
		}
	}

	//ลื่อนที่ศัตรูตามทิศทางที่กำหนด และเปลี่ยนทิศทางหากไม่สามารถเคลื่อนที่ต่อไปได้
	protected void move(int[][] lvlData) {
		float xSpeed = 0;

		if (walkDir == LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
			if (IsFloor(hitbox, xSpeed, lvlData)) {
				hitbox.x += xSpeed;
				return;
			}

		changeWalkDir();
	}

	//หันศัตรูไปทางผู้เล่นเมื่อมองเห็น
	protected void turnTowardsPlayer(Player player) {
		if (player.hitbox.x > hitbox.x)
			walkDir = RIGHT;
		else
			walkDir = LEFT;
	}

	//ตรวจสอบว่าผู้เล่นอยู่ในระยะที่ศัตรูมองเห็น
	protected boolean canSeePlayer(int[][] lvlData, Player player) {
		int playerTileY = (int) (player.getHitbox().y / Game.TILES_SIZE);
		if (playerTileY == tileY)
			if (isPlayerInRange(player)) {
				if (IsSightClear(lvlData, hitbox, player.hitbox, tileY))
					return true;
			}
		return false;
	}

	//ตรวจสอบว่าผู้เล่นอยู่ในระยะที่ศัตรูโจมตีได้
	protected boolean isPlayerInRange(Player player) {
		int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		return absValue <= attackDistance * 5;
	}

	//ตรวจสอบว่าผู้เล่นอยู่ในระยะที่ศัตรูโจมตีได้
	protected boolean isPlayerCloseForAttack(Player player) {
		int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		switch (enemyType) {
		case CRABBY -> {
			return absValue <= attackDistance;
		}
		case SHARK -> {
			return absValue <= attackDistance * 2;
		}
		}
		return false;
	}

	//ดเลือดของศัตรูเมื่อถูกโจมตี และเปลี่ยนสถานะเป็น HIT หรือ DEAD
	public void hurt(int amount) {
		currentHealth -= amount;
		if (currentHealth <= 0)
			newState(DEAD);
		else {
			newState(HIT);
			if (walkDir == LEFT)
				pushBackDir = RIGHT;
			else
				pushBackDir = LEFT;
			pushBackOffsetDir = UP;
			pushDrawOffset = 0;
		}
	}

	//ตรวจสอบการโจมตีผู้เล่นและลดเลือดของผู้เล่นหากถูกโจมตี
	protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
		if (attackBox.intersects(player.hitbox))
			player.changeHealth(-GetEnemyDmg(enemyType), this);
		else {
			if (enemyType == SHARK)
				return;
		}
		attackChecked = true;
	}

	//อัปเดตการเคลื่อนไหวของศัตรู
	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(enemyType, state)) {
				if (enemyType == CRABBY || enemyType == SHARK) {
					aniIndex = 0;

					switch (state) {
					case ATTACK, HIT -> state = IDLE;
					case DEAD -> active = false;
					}
				} else if (enemyType == PINKSTAR) {
					if (state == ATTACK)
						aniIndex = 3;
					else {
						aniIndex = 0;
						if (state == HIT) {
							state = IDLE;

						} else if (state == DEAD)
							active = false;
					}
				}
			}
		}
	}

	//เปลี่ยนการเดิน
	protected void changeWalkDir() {
		if (walkDir == LEFT)
			walkDir = RIGHT;
		else
			walkDir = LEFT;
	}

	//รีเซ็ตศัตรูเมื่อเริ่มเกมใหม่หรือ respawn
	public void resetEnemy() {
		hitbox.x = x;
		hitbox.y = y;
		firstUpdate = true;
		currentHealth = maxHealth;
		newState(IDLE);
		active = true;
		airSpeed = 0;

		pushDrawOffset = 0;

	}

	public int flipX() {
		if (walkDir == RIGHT)
			return width;
		else
			return 0;
	}

	public int flipW() {
		if (walkDir == RIGHT)
			return -1;
		else
			return 1;
	}

	public boolean isActive() {
		return active;
	}

	public float getPushDrawOffset() {
		return pushDrawOffset;
	}

}