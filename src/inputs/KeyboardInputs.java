package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gamestates.Gamestate;
import main.GamePanel;

public class KeyboardInputs implements KeyListener {

    private GamePanel gamePanel;

    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    //ถูกเรียกเมื่อผู้เล่นปล่อยปุ่มคีย์บอร์ด โดยจะตรวจสอบสถานะปัจจุบันของเกม
    @SuppressWarnings("incomplete-switch")
    @Override
    public void keyReleased(KeyEvent e) {
        switch (Gamestate.state) {
            case MENU -> gamePanel.getGame().getMenu().keyReleased(e);//ส่งเหตุการณ์ไปยังเมนู (Menu)
            case PLAYING -> gamePanel.getGame().getPlaying().keyReleased(e);//ส่งเหตุการณ์ไปยังส่วนเล่นเกม (Playing)
            case CREDITS -> gamePanel.getGame().getCredits().keyReleased(e);//ส่งเหตุการณ์ไปยังส่วนเครดิต (Credits)
        }
    }

    //ถูกเรียกเมื่อผู้เล่นกดปุ่มคีย์บอร์ด
    @SuppressWarnings("incomplete-switch")
    @Override
    public void keyPressed(KeyEvent e) {
        switch (Gamestate.state) {
            case MENU -> gamePanel.getGame().getMenu().keyPressed(e);//ส่งเหตุการณ์ไปยังเมนู (Menu)
            case PLAYER_SELECTION -> gamePanel.getGame().getPlayerSelection().keyPressed(e);//ส่งเหตุการณ์ไปยังส่วนเลือกผู้เล่น (PlayerSelection)
            case PLAYING -> gamePanel.getGame().getPlaying().keyPressed(e);//ส่งเหตุการณ์ไปยังส่วนเล่นเกม (Play)
            case OPTIONS -> gamePanel.getGame().getGameOptions().keyPressed(e);//ส่งเหตุการณ์ไปยังส่วนตัวเลือกเกม (GameOptions)
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not In Use
    }
}