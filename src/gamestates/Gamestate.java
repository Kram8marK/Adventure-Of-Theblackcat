package gamestates;

public enum Gamestate {

	PLAYING, MENU, OPTIONS, QUIT, CREDITS, PLAYER_SELECTION;
	//PLAYING: สถานะเมื่อผู้เล่นกำลังเล่นเกม
	//MENU: สถานะเมื่อผู้เล่นอยู่ในเมนูหลัก
	//OPTIONS: สถานะเมื่อผู้เล่นอยู่ในหน้าจอตัวเลือก
	//QUIT: สถานะเมื่อผู้เล่นออกจากเกม
	//CREDITS: สถานะเมื่อผู้เล่นอยู่ในหน้าจอเครดิต
	//PLAYER_SELECTION: สถานะเมื่อผู้เล่นอยู่ในหน้าจอเลือกตัวละคร

	public static Gamestate state = MENU;

}
