package audio;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {

	//ค่า(ดัชนี)ของเพลงที่เล่นอยู่
	public static int MENU_1 = 0;
	public static int LEVEL_1 = 1;
	public static int LEVEL_2 = 2;

	public static int DIE = 0;
	public static int JUMP = 1;
	public static int GAMEOVER = 2;
	public static int LVL_COMPLETED = 3;
	public static int ATTACK_ONE = 4;
	public static int ATTACK_TWO = 5;
	public static int ATTACK_THREE = 6;
	//ถึงตรงนี้

	private Clip[] songs, effects;//array ของคลิปเสียง(เพลง/เอฟเฟกต์)
	private int currentSongId;//เก็บค่าของเพลงที่กำลังเล่นอยู่
	private float volume = 0.5f;//เสียงเริ่มต้นตั้งค่าเป็น50%
	private boolean songMute, effectMute;//สถานะของเปิดปิด(เสียง/เอฟดฟกต์)
	private Random rand = new Random();//เล่นเสียงโจมตีแบบสุ่ม

	//โหลดเพลงและเสียงเอฟเฟกต์ทั้งหมด จากนั้นเริ่มเล่นเพลงเมนูหลัก 
	public AudioPlayer() {
		loadSongs();// โหลดเพลงทั้งหมด
		loadEffects();// โหลดเสียงเอฟเฟกต์ทั้งหมด
		playSong(MENU_1);// เล่นเพลงเมนูหลักเมื่อเริ่มต้น
	}

	private void loadSongs() {
		String[] names = { "menu", "level1", "level2" };// ชื่อไฟล์เสียงเพลง
		songs = new Clip[names.length];//arrayสำหรับเก็บเสียงเพลง
		for (int i = 0; i < songs.length; i++)
			songs[i] = getClip(names[i]);// โหลดเพลงแต่ละอัน
	}

	private void loadEffects() {
		String[] effectNames = { "die", "jump", "gameover", "lvlcompleted", "attack1", "attack2", "attack3" };// ชื่อไฟล์เสียงเอฟเฟกต์
		effects = new Clip[effectNames.length];//arrayสำหรับเก็บเสียงเอฟเฟกต์
		for (int i = 0; i < effects.length; i++)
			effects[i] = getClip(effectNames[i]);// โหลดเสียงเอฟเฟกต์แต่ละอัน

		updateEffectsVolume();// อัปเดตระดับเสียงของเสียงเอฟเฟกต์

	}

	private Clip getClip(String name) {
		URL url = getClass().getResource("/audio/" + name + ".wav");//หาไฟล์เสียงจากres
		AudioInputStream audio;

		try {
			audio = AudioSystem.getAudioInputStream(url);//อ่านข้อมูลเสียง
			Clip c = AudioSystem.getClip();
			c.open(audio);
			return c;

		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {

			e.printStackTrace();
		}

		return null;

	}

	//ตั้งค่าระดับเสียงทั้งเพลงและเสียงเอฟเฟกต์
	public void setVolume(float volume) {
		this.volume = volume;// ตั้งค่าระดับเสียง
		updateSongVolume();// อัปเดตระดับเสียงของเพลง
		updateEffectsVolume();// อัปเดตระดับเสียงของเสียงเอฟเฟกต์
	}

	//หยุดเพลงที่เล่นอยู่
	public void stopSong() {
		if (songs[currentSongId].isActive())
			songs[currentSongId].stop();
	}

	//เลือกเพลงสำหรับด่านต่าง ๆ
	public void setLevelSong(int lvlIndex) {
		if (lvlIndex % 2 == 0)
			playSong(LEVEL_1);// เล่นเพลงของด่าน 1
		else
			playSong(LEVEL_2);// เล่นเพลงของด่าน 2ขึ้นไป
	}

	//เล่นเพลงผ่านด่าน
	public void lvlCompleted() {
		stopSong();//หยุดเพลงที่เล่นอยู่
		playEffect(LVL_COMPLETED);//เล่นเพลงผ่านด่าน
	}

	//สุ่มเล่นเสียงโจมตี
	public void playAttackSound() {
		int start = 4;
		start += rand.nextInt(3);// สุ่มเลือก
		playEffect(start);// เล่นเสียงโจมตี
	}

	//เล่นเสียงเอฟเฟกต์ตามค่าที่กำหนด
	public void playEffect(int effect) {
		if (effects[effect].getMicrosecondPosition() > 0)
			effects[effect].setMicrosecondPosition(0);// เริ่มเล่นตั้งแต่ต้น
		effects[effect].start();// เล่นเสียงเอฟเฟกต์
	}

	//เล่นเพลงตามค่าที่กำหนด และลูปเพลงนั้น
	public void playSong(int song) {
		stopSong();// หยุดเพลงที่กำลังเล่นอยู่เพื่อจะเล่นเพลงต่อไป

		currentSongId = song;// ตั้งค่าเพลงปัจจุบัน
		updateSongVolume();// อัปเดตระดับเสียงของเพลง
		songs[currentSongId].setMicrosecondPosition(0);// เริ่มเล่นเพลงตั้งแต่ต้น
		songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);// ลูปเพลง
	}

	//ปิดหรือเปิดเสียงเพลง
	public void toggleSongMute() {
		this.songMute = !songMute;// สลับสถานะการปิดเสียง
		for (Clip c : songs) {
			BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
			booleanControl.setValue(songMute);// ตั้งค่าปิดเสียง
		}
	}

	//ปิดหรือเปิดเสียงเอฟเฟกต์(รวมเสียงกระโดดในนี้ด้วย)
	public void toggleEffectMute() {
		this.effectMute = !effectMute;// สลับสถานะการปิดเสียง
		for (Clip c : effects) {
			BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
			booleanControl.setValue(effectMute);// ตั้งค่าปิดเสียง
		}
		if (!effectMute)
			playEffect(JUMP);// เล่นเสียงกระโดดเมื่อเปิดเสียง
	}

	//ปรับระดับเสียงของเพลงที่กำลังเล่นอยู่
	private void updateSongVolume() {

		FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
		float range = gainControl.getMaximum() - gainControl.getMinimum();//ดูว่าเสียงตอนนี้กี่%
		float gain = (range * volume) + gainControl.getMinimum();
		gainControl.setValue(gain);// ตั้งค่าระดับเสียง

	}

	//รับระดับเสียงของเสียงเอฟเฟกต์ทั้งหมด
	private void updateEffectsVolume() {
		for (Clip c : effects) {
			FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
			float range = gainControl.getMaximum() - gainControl.getMinimum();//ดูว่าเสียงตอนนี้กี่%
			float gain = (range * volume) + gainControl.getMinimum();
			gainControl.setValue(gain);// ตั้งค่าระดับเสียง
		}
	}

}
