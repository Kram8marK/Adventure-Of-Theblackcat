package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface Statemethods {
	public void update();//เมธอดนี้ใช้สำหรับอัปเดตสถานะของเกม

	public void draw(Graphics g);//เมธอดนี้ใช้สำหรับวาดกราฟิกของเกมบนหน้าจอ

	public void mouseClicked(MouseEvent e);//เรียกเมื่อเมาส์ถูกคลิก

	public void mousePressed(MouseEvent e);//เรียกเมื่อปุ่มเมาส์ถูกกด

	public void mouseReleased(MouseEvent e);//เรียกเมื่อปุ่มเมาส์ถูกปล่อย

	public void mouseMoved(MouseEvent e);//เรียกเมื่อเมาส์เคลื่อนที่

	public void keyPressed(KeyEvent e);//เรียกเมื่อปุ่มคีย์บอร์ดถูกกด

	public void keyReleased(KeyEvent e);//เรียกเมื่อปุ่มคีย์บอร์ดถูกปล่อย

}
