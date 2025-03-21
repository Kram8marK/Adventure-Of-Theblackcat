package gamestates;

import java.awt.event.MouseEvent;

import audio.AudioPlayer;
import main.Game;
import ui.MenuButton;

public class State {

	protected Game game;

	//กำหนดค่าเริ่มต้นให้กับคลาส State โดยรับออบเจ็กต์ Game มาเก็บไว้
	public State(Game game) {
		this.game = game;
	}

	//ตรวจสอบว่าเหตุการณ์เมาส์เกิดขึ้นภายในขอบเขตของปุ่ม MenuButton หรือไม่ โดยจะคืนค่า true หากตำแหน่งเมาส์อยู่ภายในขอบเขตของปุ่ม
	public boolean isIn(MouseEvent e, MenuButton mb) {
		return mb.getBounds().contains(e.getX(), e.getY());
	}

	//คืนค่าออบเจ็กต์ Game
	public Game getGame() {
		return game;
	}

	//กำหนดสถานะปัจจุบันของเกมและจัดการกับการกระทำที่เกี่ยวข้อง
	@SuppressWarnings("incomplete-switch")
	public void setGamestate(Gamestate state) {
		switch (state) {
		case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
		case PLAYING -> game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
		}

		Gamestate.state = state;
	}

}