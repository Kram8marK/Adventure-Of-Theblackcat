package entities;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.EnemyConstants.GetSpriteAmount;
import static utilz.HelpMethods.IsFloor;
import static utilz.Constants.Dialogue.*;

import gamestates.Playing;

public class Crabby extends Enemy {

	//กำหนดขนาดและประเภทของน้องปู
	public Crabby(float x, float y) {
		super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
		initHitbox(22, 19);//ขนาดhitbox
		initAttackBox(82, 19, 30);//ระยะโจมตี
	}

	//อัปเดตพฤติกรรม, การเคลื่อนไหว ของน้องปู
	public void update(int[][] lvlData, Playing playing) {
		updateBehavior(lvlData, playing);
		updateAnimationTick();
		updateAttackBox();
	}

	//พฤติกรรมของน้องปู
	private void updateBehavior(int[][] lvlData, Playing playing) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir) {
			inAirChecks(lvlData, playing);
		} else {
			switch (state) {
			case IDLE://ถ้าอยู่บนพื้นเกลี่ยนเป็นเดิน
				if (IsFloor(hitbox, lvlData))
					newState(RUNNING);
				else
					inAir = true;
				break;
			case RUNNING://ถ้ากำลังเดินเจอผู้เล่นจะหันไปและโจมตี
				if (canSeePlayer(lvlData, playing.getPlayer())) {
					turnTowardsPlayer(playing.getPlayer());//ถ้าไม่ถึงระยะเดินไปหา
					if (isPlayerCloseForAttack(playing.getPlayer()))
						newState(ATTACK);//เข้าระยะโจมตี
				}
				move(lvlData);

				if (inAir)
					playing.addDialogue((int) hitbox.x, (int) hitbox.y, EXCLAMATION);

				break;
			case ATTACK:
				if (aniIndex == 0)
					attackChecked = false;
				if (aniIndex == 3 && !attackChecked)
					checkPlayerHit(attackBox, playing.getPlayer());
				break;
			case HIT://หากโดนโจมตีจะกระเด็น
				if (aniIndex <= GetSpriteAmount(enemyType, state) - 2)
					pushBack(pushBackDir, lvlData, 2f);
				updatePushBackDrawOffset();
				break;
			}
		}
	}

}