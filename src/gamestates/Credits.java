package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Game;
import utilz.LoadSave;

public class Credits extends State implements Statemethods {
    private BufferedImage backgroundImg, creditsImg;//ภาพพื้นหลังของหน้าจอเครดิต/ภาพเครดิตที่แสดงรายชื่อ
    private int bgX, bgY, bgW, bgH;// ตำแหน่ง X และ Y ของภาพเครดิต/ความกว้างและความสูงของภาพเครดิต
    private float bgYFloat;//ค่าที่ใช้ในการเลื่อนภาพเครดิตขึ้นด้านบน

    private ArrayList<ShowEntity> entitiesList;//รายการของ entity ที่แสดงในหน้าจอเครดิต (เช่น ตัวละคร, ศัตรู)

    //กำหนดค่าเริ่มต้นให้กับตัวแปรต่าง ๆ เช่น ภาพพื้นหลัง, ภาพเครดิต, และโหลด entity ที่จะแสดงในหน้าจอเครดิต
    public Credits(Game game) {
        super(game);
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
        creditsImg = LoadSave.GetSpriteAtlas(LoadSave.CREDITS);
        bgW = (int) (creditsImg.getWidth() * Game.SCALE);
        bgH = (int) (creditsImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = Game.GAME_HEIGHT;
        loadEntities();
    }

    // โหลด entity ที่จะแสดงในหน้าจอเครดิต
    private void loadEntities() {
        entitiesList = new ArrayList<>();
    }

    //แยกภาพเคลื่อนไหวจากไฟล์ภาพใหญ่
    private BufferedImage[] getIdleAni(BufferedImage atlas, int spritesAmount, int width, int height) {
        BufferedImage[] arr = new BufferedImage[spritesAmount];
        for (int i = 0; i < spritesAmount; i++)
            arr[i] = atlas.getSubimage(width * i, 0, width, height);
        return arr;
    }

    //อัปเดตตำแหน่งของภาพเครดิต
    @Override
    public void update() {
        bgYFloat -= 0.2f;
        for (ShowEntity se : entitiesList)
            se.update();
    }

    //วาดภาพพื้นหลัง, ภาพเครดิต, และ entity
    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(creditsImg, bgX, (int) (bgY + bgYFloat), bgW, bgH, null);

        for (ShowEntity se : entitiesList)
            se.draw(g);
    }

    //หากผู้เล่นกดปุ่ม ESC จะรีเซ็ตตำแหน่งของภาพเครดิตและเปลี่ยนสถานะเป็นเมนูหลัก
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            bgYFloat = 0;
            setGamestate(Gamestate.MENU);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    private class ShowEntity {
        private BufferedImage[] idleAnimation;
        private int x, y, aniIndex, aniTick;

        public ShowEntity(BufferedImage[] idleAnimation, int x, int y) {
            this.idleAnimation = idleAnimation;
            this.x = x;
            this.y = y;
        }

        public void draw(Graphics g) {
            g.drawImage(idleAnimation[aniIndex], x, y, (int) (idleAnimation[aniIndex].getWidth() * 4), (int) (idleAnimation[aniIndex].getHeight() * 4), null);
        }

        public void update() {
            aniTick++;
            if (aniTick >= 25) {
                aniTick = 0;
                aniIndex++;
                if (aniIndex >= idleAnimation.length)
                    aniIndex = 0;
            }

        }
    }

}
