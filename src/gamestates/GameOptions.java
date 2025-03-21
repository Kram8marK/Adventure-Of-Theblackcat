package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.AudioOptions;
import ui.PauseButton;
import ui.UrmButton;
import utilz.LoadSave;
import static utilz.Constants.UI.URMButtons.*;

public class GameOptions extends State implements Statemethods {

	private AudioOptions audioOptions;//ออบเจกต์ที่ใช้จัดการตัวเลือกเสียงในเกม
	private BufferedImage backgroundImg, optionsBackgroundImg;//ภาพพื้นหลังของหน้าจอตัวเลือก/ภาพพื้นหลังของเมนูตัวเลือก
	private int bgX, bgY, bgW, bgH;//ตำแหน่ง X และ Y ของภาพเมนูตัวเลือก/ความกว้างและความสูงของภาพเมนูตัวเลือก
	private UrmButton menuB;//ปุ่มที่ใช้กลับไปยังเมนูหลัก

	//กำหนดค่าเริ่มต้นให้กับตัวแปรต่าง ๆ
	public GameOptions(Game game) {
		super(game);
		loadImgs();
		loadButton();
		audioOptions = game.getAudioOptions();
	}

	//โหลดปุ่มเมนูและกำหนดตำแหน่งของปุ่ม
	private void loadButton() {
		int menuX = (int) (387 * Game.SCALE);
		int menuY = (int) (325 * Game.SCALE);

		menuB = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
	}

	//โหลดภาพพื้นหลังและภาพเมนูตัวเลือก และกำหนดขนาดและตำแหน่งของภาพเมนูตัวเลือก
	private void loadImgs() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
		optionsBackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_MENU);

		bgW = (int) (optionsBackgroundImg.getWidth() * Game.SCALE);
		bgH = (int) (optionsBackgroundImg.getHeight() * Game.SCALE);
		bgX = Game.GAME_WIDTH / 2 - bgW / 2;
		bgY = (int) (33 * Game.SCALE);
	}

	//อัปเดตสถานะของปุ่มเมนูและตัวเลือกเสียง
	@Override
	public void update() {
		menuB.update();
		audioOptions.update();
	}

	//วาดภาพพื้นหลัง, ภาพเมนูตัวเลือก, ปุ่มเมนู, และตัวเลือกเสียงบนหน้าจอ
	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(optionsBackgroundImg, bgX, bgY, bgW, bgH, null);

		menuB.draw(g);
		audioOptions.draw(g);
	}

	//จัดการการลากเมาส์บนตัวเลือกเสียง
	public void mouseDragged(MouseEvent e) {
		audioOptions.mouseDragged(e);
	}

	//จัดการการกดเมาส์บนปุ่มเมนูหรือตัวเลือกเสียง
	@Override
	public void mousePressed(MouseEvent e) {
		if (isIn(e, menuB)) {
			menuB.setMousePressed(true);
		} else
			audioOptions.mousePressed(e);
	}

	//จัดการการปล่อยเมาส์บนปุ่มเมนูหรือตัวเลือกเสียง และเปลี่ยนสถานะเป็นเมนูหลักหากผู้เล่นคลิกที่ปุ่มเมนู
	@Override
	public void mouseReleased(MouseEvent e) {
		if (isIn(e, menuB)) {
			if (menuB.isMousePressed())
				Gamestate.state = Gamestate.MENU;
		} else
			audioOptions.mouseReleased(e);
		menuB.resetBools();
	}

	//จัดการการเคลื่อนเมาส์บนปุ่มเมนูหรือตัวเลือกเสียง
	@Override
	public void mouseMoved(MouseEvent e) {
		menuB.setMouseOver(false);

		if (isIn(e, menuB))
			menuB.setMouseOver(true);
		else
			audioOptions.mouseMoved(e);
	}

	//หากผู้เล่นกดปุ่ม ESC จะเปลี่ยนสถานะเป็นเมนูหลัก
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			Gamestate.state = Gamestate.MENU;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	//ตรวจสอบว่าเมาส์อยู่ภายในขอบเขตของปุ่มหรือไม่
	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

}
