package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import gamestates.Gamestate;
import main.GamePanel;

public class MouseInputs implements MouseListener, MouseMotionListener {

    private GamePanel gamePanel;

    public MouseInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    //ถูกเรียกเมื่อผู้เล่นลากเมาส์ (กดปุ่มเมาส์ค้างไว้และเคลื่อนที่)
    @SuppressWarnings("incomplete-switch")
    @Override
    public void mouseDragged(MouseEvent e) {
        switch (Gamestate.state) {
            case PLAYING -> gamePanel.getGame().getPlaying().mouseDragged(e);//ส่งเหตุการณ์ไปยังส่วนเล่นเกม (Playing)
            case OPTIONS -> gamePanel.getGame().getGameOptions().mouseDragged(e);//ส่งเหตุการณ์ไปยังส่วนตัวเลือกเกม (GameOptions)
        }
    }

    //ถูกเรียกเมื่อผู้เล่นเคลื่อนที่เมาส์
    @SuppressWarnings("incomplete-switch")
    @Override
    public void mouseMoved(MouseEvent e) {
        switch (Gamestate.state) {
            case MENU -> gamePanel.getGame().getMenu().mouseMoved(e);//ส่งเหตุการณ์ไปยังเมนู (Menu)
            case PLAYER_SELECTION -> gamePanel.getGame().getPlayerSelection().mouseMoved(e);//ส่งเหตุการณ์ไปยังส่วนเลือกผู้เล่น (PlayerSelection)
            case PLAYING -> gamePanel.getGame().getPlaying().mouseMoved(e);//ส่งเหตุการณ์ไปยังส่วนเล่นเกม (Playing)
            case OPTIONS -> gamePanel.getGame().getGameOptions().mouseMoved(e);//ส่งเหตุการณ์ไปยังส่วนตัวเลือกเกม (GameOptions)
        }
    }

    //ถูกเรียกเมื่อผู้เล่นคลิกเมาส์
    @SuppressWarnings("incomplete-switch")
    @Override
    public void mouseClicked(MouseEvent e) {
        switch (Gamestate.state) {
            case PLAYING -> gamePanel.getGame().getPlaying().mouseClicked(e);//ส่งเหตุการณ์ไปยังส่วนเล่นเกม (Playing)
        }
    }

    //ถูกเรียกเมื่อผู้เล่นกดปุ่มเมาส์
    @SuppressWarnings("incomplete-switch")
    @Override
    public void mousePressed(MouseEvent e) {
        switch (Gamestate.state) {
            case MENU -> gamePanel.getGame().getMenu().mousePressed(e);//ส่งเหตุการณ์ไปยังเมนู (Menu)
            case PLAYER_SELECTION -> gamePanel.getGame().getPlayerSelection().mousePressed(e);//ส่งเหตุการณ์ไปยังส่วนเลือกผู้เล่น (PlayerSelection)
            case PLAYING -> gamePanel.getGame().getPlaying().mousePressed(e);//ส่งเหตุการณ์ไปยังส่วนเล่นเกม (Playing)
            case OPTIONS -> gamePanel.getGame().getGameOptions().mousePressed(e);//ส่งเหตุการณ์ไปยังส่วนตัวเลือกเกม (GameOptions)
        }
    }

    //ถูกเรียกเมื่อผู้เล่นปล่อยปุ่มเมาส์
    @SuppressWarnings("incomplete-switch")
    @Override
    public void mouseReleased(MouseEvent e) {
        switch (Gamestate.state) {
            case MENU -> gamePanel.getGame().getMenu().mouseReleased(e);//ส่งเหตุการณ์ไปยังเมนู (Menu)
            case PLAYER_SELECTION -> gamePanel.getGame().getPlayerSelection().mouseReleased(e);//ส่งเหตุการณ์ไปยังส่วนเลือกผู้เล่น (PlayerSelection)
            case PLAYING -> gamePanel.getGame().getPlaying().mouseReleased(e);//ส่งเหตุการณ์ไปยังส่วนเล่นเกม (Playing)
            case OPTIONS -> gamePanel.getGame().getGameOptions().mouseReleased(e);//ส่งเหตุการณ์ไปยังส่วนตัวเลือกเกม (GameOptions)
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Not In use
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Not In use
    }

}