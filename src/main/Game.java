package main;

import java.awt.Graphics;

import audio.AudioPlayer;
import gamestates.*;
import ui.AudioOptions;

public class Game implements Runnable {

    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;//กำหนดค่า FPS (เฟรมต่อวินาที)
    private final int UPS_SET = 200;//กำหนดค่า UPS (อัปเดตต่อวินาที)

    private Playing playing;
    private Menu menu;
    private Credits credits;
    private PlayerSelection playerSelection;
    private GameOptions gameOptions;
    private AudioOptions audioOptions;
    private AudioPlayer audioPlayer;

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 2f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    private final boolean SHOW_FPS_UPS = true;

    public Game() {
        System.out.println("size: " + GAME_WIDTH + " : " + GAME_HEIGHT);//แสดงขนาดของเกม
        initClasses();//เริ่มต้นคลาสต่าง ๆ
        gamePanel = new GamePanel(this);
        new GameWindow(gamePanel);//สร้าง GamePanel และ GameWindow
        gamePanel.requestFocusInWindow();
        startGameLoop();//เริ่มลูปเกม
    }

    //เริ่มต้นคลาสต่าง ๆ ที่เกี่ยวข้องกับเกม เช่น เมนู, การเล่นเกม, การเลือกผู้เล่น, เครดิต, และตัวเลือกเกม
    private void initClasses() {
        audioOptions = new AudioOptions(this);
        audioPlayer = new AudioPlayer();
        menu = new Menu(this);
        playing = new Playing(this);
        playerSelection = new PlayerSelection(this);
        credits = new Credits(this);
        gameOptions = new GameOptions(this);
    }

    //เริ่มลูปเกมโดยสร้างเธรดใหม่และเริ่มรันเกม
    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    //อัปเดตสถานะของเกมตามสถานะปัจจุบัน (Gamestate.state)
    public void update() {
        switch (Gamestate.state) {
            case MENU -> menu.update();
            case PLAYER_SELECTION -> playerSelection.update();
            case PLAYING -> playing.update();
            case OPTIONS -> gameOptions.update();
            case CREDITS -> credits.update();
            case QUIT -> System.exit(0);
        }
    }

    //วาดสถานะของเกมตามสถานะปัจจุบัน (Gamestate.state)
    @SuppressWarnings("incomplete-switch")
    public void render(Graphics g) {
        switch (Gamestate.state) {
            case MENU -> menu.draw(g);
            case PLAYER_SELECTION -> playerSelection.draw(g);
            case PLAYING -> playing.draw(g);
            case OPTIONS -> gameOptions.draw(g);
            case CREDITS -> credits.draw(g);
        }
    }

    //ลูปหลักของเกมที่ทำการอัปเดตและวาดเกมตามค่า FPS และ UPS ที่กำหนด
    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {

            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {

                update();
                updates++;
                deltaU--;

            }

            if (deltaF >= 1) {

                gamePanel.repaint();
                frames++;
                deltaF--;

            }

            if (SHOW_FPS_UPS)
                if (System.currentTimeMillis() - lastCheck >= 1000) {

                    lastCheck = System.currentTimeMillis();
                    System.out.println("FPS: " + frames + " | UPS: " + updates);
                    frames = 0;
                    updates = 0;

                }

        }
    }

    public void windowFocusLost() {
        if (Gamestate.state == Gamestate.PLAYING)
            playing.getPlayer().resetDirBooleans();
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }

    public Credits getCredits() {
        return credits;
    }

    public PlayerSelection getPlayerSelection() {
        return playerSelection;
    }

    public GameOptions getGameOptions() {
        return gameOptions;
    }

    public AudioOptions getAudioOptions() {
        return audioOptions;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }
}