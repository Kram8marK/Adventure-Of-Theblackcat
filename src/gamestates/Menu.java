package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.MenuButton;
import utilz.LoadSave;

public class Menu extends State implements Statemethods {

    private MenuButton[] buttons = new MenuButton[4];//อาร์เรย์ของปุ่มเมนูที่ใช้ในเมนูหลัก
    private BufferedImage backgroundImg, backgroundImgPink;//ภาพพื้นหลังของเมนูหลัก/ภาพพื้นหลังสีชมพูที่ใช้เป็นพื้นหลังหลัก
    private int menuX, menuY, menuWidth, menuHeight;//ตำแหน่ง X และ Y ของเมนู/ความกว้างและความสูงของเมนู

    //กำหนดค่าเริ่มต้นให้กับตัวแปรต่าง ๆ
    public Menu(Game game) {
        super(game);
        loadButtons();
        loadBackground();
        backgroundImgPink = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);

    }

    //โหลดภาพพื้นหลังของเมนูและกำหนดขนาดและตำแหน่งของเมนู
    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
        menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (25 * Game.SCALE);
    }

    //โหลดปุ่มเมนูและกำหนดตำแหน่ง, รูปแบบ, และสถานะที่เกี่ยวข้องกับแต่ละปุ่ม
    private void loadButtons() {
        buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int) (130 * Game.SCALE), 0, Gamestate.PLAYER_SELECTION);
        buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int) (200 * Game.SCALE), 1, Gamestate.OPTIONS);
        buttons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int) (270 * Game.SCALE), 3, Gamestate.CREDITS);
        buttons[3] = new MenuButton(Game.GAME_WIDTH / 2, (int) (340 * Game.SCALE), 2, Gamestate.QUIT);
    }

    //อัปเดตสถานะของปุ่มเมนูทุกปุ่ม
    @Override
    public void update() {
        for (MenuButton mb : buttons)
            mb.update();
    }

    //วาดภาพพื้นหลังและปุ่มเมนูบนหน้าจอ
    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImgPink, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);

        for (MenuButton mb : buttons)
            mb.draw(g);
    }

    //จัดการการกดเมาส์บนปุ่มเมนู
    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                mb.setMousePressed(true);
            }
        }
    }

    //จัดการการปล่อยเมาส์บนปุ่มเมนูและเปลี่ยนสถานะของเกมตามปุ่มที่คลิก
    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                if (mb.isMousePressed())
                    mb.applyGamestate();
                if (mb.getState() == Gamestate.PLAYING)
                    game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
                break;
            }
        }
        resetButtons();
    }

    // รีเซ็ตสถานะของปุ่มเมนูทั้งหมด
    private void resetButtons() {
        for (MenuButton mb : buttons)
            mb.resetBools();

    }

    //จัดการการเคลื่อนเมาส์บนปุ่มเมนู
    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton mb : buttons)
            mb.setMouseOver(false);

        for (MenuButton mb : buttons)
            if (isIn(e, mb)) {
                mb.setMouseOver(true);
                break;
            }

    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

}