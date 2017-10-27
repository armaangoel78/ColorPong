package com.armaangoel.myapplication;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.bluetooth.BluetoothAdapter;
import android.widget.Toast;
import java.util.Random;

import static java.lang.Math.abs;

/**
 * Created by sunjaygoel* on 9/1/16.
 *            ^^^^^^
 *           *armaan
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread = new MainThread(getHolder(), this);
    private MainActivity myActivity;
    private final Display disp  = ((WindowManager)this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    private MediaPlayer mp, mp2;
    private GameMenu title, singlePlayer, localMulti, bluetoothMulti;
    private Toast toastBluetoothNowOn, toastBluetoothWasOn, toastNoBluetooth;
    private boolean toastShow = true;
    private int menuOrRetry = 1;
    private RectPlayer player, playerTwo;
    private int p2velX;
    private ball myball;

    public static boolean btMode = false;

    BluetoothAdapter btAdapter;

    private final Point singlePlayerMenuPoint = new Point(disp.getWidth()/2, disp.getHeight() * 3/4 - 400);
    private final Point localMultiplayerMenuPoint = new Point(disp.getWidth()/2, disp.getHeight() * 3/4  - 100);
    private final Point bluetoothMultiplayerMenuPoint = new Point (disp.getWidth()/2, disp.getHeight() * 3/4 + 200);
    private Point playerPoint, playerTwoPoint, ballPoint;
    private int moveSpeed = 10;
    private Random r = new Random();
    private int ballVelX = moveSpeed * (r.nextInt(6) - 3);
    private int ballVelY = moveSpeed * (r.nextInt(6) - 3);
    private ScoreManager gameScoreOne, gameScoreTwo;
    private int scoreOne = 0;
    private int scoreTwo = 0;

    private int gameModePong = 1;

    private int colorR = 255;
    private int colorG = 255;
    private int colorB = 255;

    private int paddleSize = disp.getWidth() /4;

    public static boolean restart = false;

    public GamePanel(Context context, MainActivity mainActivity) {
        super(context);
        getHolder().addCallback(this);

        myActivity = mainActivity;

        mp = MediaPlayer.create(context, R.raw.beep);
        mp2 = MediaPlayer.create(context, R.raw.beep);

        toastBluetoothWasOn = Toast.makeText(context, "Bluetooth already on", Toast.LENGTH_SHORT);
        toastBluetoothNowOn = Toast.makeText(context, "Bluetooth is now on", Toast.LENGTH_SHORT);
        toastNoBluetooth = Toast.makeText(context, "Bluetooth not available", Toast.LENGTH_SHORT);

        playerPoint = new Point(disp.getWidth()/2, disp.getHeight()/10);
        playerTwoPoint = new Point (disp.getWidth()/2, disp.getHeight() - 100);
        ballPoint = new Point(disp.getWidth()/2, disp.getHeight()/2);

        setFocusable(true);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        while (true) {
            try {
                thread.setRunning(false);
                thread.join();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gameModePong == 1){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    if ((int) event.getY() >= singlePlayerMenuPoint.y - 80 && event.getY() <= singlePlayerMenuPoint.y + 80) gameModePong = 2;
                    else if ((int) event.getY() >= localMultiplayerMenuPoint.y - 80 && event.getY() <= localMultiplayerMenuPoint.y + 80) gameModePong = 3;
                    else if ((int) event.getY() >= bluetoothMultiplayerMenuPoint.y - 80 && event.getY() <= bluetoothMultiplayerMenuPoint.y + 80) gameModePong = 4;
            }
        }

        if (scoreOne < 10 || scoreTwo < 10 && myActivity.gameMode == 2 || myActivity.gameMode == 3) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_DOWN:
                    if ((int) event.getX() <= playerTwoPoint.x + paddleSize && (int) event.getX() >= playerTwoPoint.x - paddleSize && (int) event.getY() > disp.getHeight()/2) {
                        playerTwoPoint.set((int) event.getX(), disp.getHeight() - 100);
                    }
                    if ((int) event.getX() <= playerPoint.x + paddleSize && (int) event.getX() >= playerPoint.x - paddleSize && (int) event.getY() < disp.getHeight()/2 && myActivity.gameMode == 3) {
                        playerPoint.set((int) event.getX(), disp.getHeight()/10);
                    }
                case MotionEvent.ACTION_POINTER_DOWN:
                    if ((int) event.getX() <= playerTwoPoint.x + paddleSize && (int) event.getX() >= playerTwoPoint.x - paddleSize && (int) event.getY() > disp.getHeight()/2) {
                        playerTwoPoint.set((int) event.getX(), disp.getHeight() - 100);
                    }
                    if ((int) event.getX() <= playerPoint.x + paddleSize && (int) event.getX() >= playerPoint.x - paddleSize && (int) event.getY() < disp.getHeight()/2 && myActivity.gameMode == 3) {
                        playerPoint.set((int) event.getX(), disp.getHeight()/10);
                    }
            }

            return true;
        }
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawColor(Color.BLACK);

        if (title != null) title.draw(canvas);
        if (singlePlayer != null) singlePlayer.draw(canvas);
        if (localMulti != null)  localMulti.draw(canvas);
        if (bluetoothMulti != null) bluetoothMulti.draw(canvas);

        //onePlayerBox.draw(canvas);
        //twoPlayerBox.draw(canvas);

        if (player != null) player.draw(canvas);
        if (playerTwo != null) playerTwo.draw(canvas);
        if (gameScoreOne != null) gameScoreOne.draw(canvas);
        if (gameScoreTwo != null) gameScoreTwo.draw(canvas);
        if (myball != null) myball.draw(canvas);

    }

    public void update() {
        if (myActivity.gameMode == 2 || myActivity.gameMode == 3) gameModePong();
        else if (myActivity.gameMode == 4) btMulitplayer();


        /*boolean gameMode = false;

        if (gameModePong == 1) {
            gameModeMenu();
            gameMode = false;
        }
        else if (gameModePong == 2 || gameModePong == 3){
            gameModePong();
            gameMode = true;
        }
        else if (gameModePong == 4) {
            btMulitplayer();
            gameMode = true;
        }

        title.blankGameMenu(gameMode);
        singlePlayer.blankGameMenu(gameMode);
        localMulti.blankGameMenu(gameMode);
        bluetoothMulti.blankGameMenu(gameMode);
        */
    }


    public void gameModeMenu() {
        //onePlayerBox = new RectPlayer(new Rect(onePlayerPoint.x - 400, onePlayerPoint.y - 80, onePlayerPoint.x + 400, onePlayerPoint.y + 80), Color.rgb(menuColor, menuColor, menuColor));

        title = new GameMenu(menuOrRetry, "Color Pong", disp.getWidth()/2 - 490, disp.getHeight()/2 - 200, Color.rgb(colorR, colorG, colorB), 200);
        singlePlayer = new GameMenu(3, "1 Player", disp.getWidth()/2 - 200, singlePlayerMenuPoint.y, Color.WHITE, 100);
        localMulti = new GameMenu(3, "2 Player", disp.getWidth()/2 - 200, localMultiplayerMenuPoint.y, Color.WHITE, 100);
        bluetoothMulti = new GameMenu(3, "Bluetooth", disp.getWidth()/2 - 230, bluetoothMultiplayerMenuPoint.y, Color.WHITE, 100);

        thread.sleepThread(300);

        randomizeColor();
        title.update();
        singlePlayer.update();
        localMulti.update();
        bluetoothMulti.update();
    }
    public void gameModePong() {
        player = new RectPlayer(new Rect(playerPoint.x - paddleSize, playerPoint.y - 50, playerPoint.x + paddleSize, playerPoint.y + 50), Color.rgb(colorR, colorG, colorB));
        playerTwo = new RectPlayer(new Rect(playerTwoPoint.x - paddleSize, playerTwoPoint.y - 50, playerTwoPoint.x + paddleSize, playerTwoPoint.y + 50), Color.rgb(colorR, colorG, colorB));
        myball = new ball(new Rect(0, 0, 100, 100), Color.rgb(colorR, colorG, colorB));
        gameScoreOne = new ScoreManager(Color.rgb(colorR, colorG, colorB), disp.getWidth() / 10, disp.getHeight() / 2 - 100);
        gameScoreTwo = new ScoreManager(Color.rgb(colorR, colorG, colorB), disp.getWidth() / 10, disp.getHeight() / 2 + 100);
        gameScoreOne.setScore(scoreOne);
        gameScoreTwo.setScore(scoreTwo);


        /*if (gameModePong == 2){
            aiPaddle();
            playerPoint.x += p2velX;

        }*/

        if (myActivity.gameMode == 2) {
            aiPaddle();
            playerPoint.x += p2velX;
        }

        paddleSideCollisionCheck();

        velocityCheck(true);
        velocityCheck(false);

        while (abs(ballVelX) > abs(ballVelY)) {
            if (ballVelY < 0) ballVelY--;
            else ballVelY++;
        }

        ballPoint.x += ballVelX;
        ballPoint.y += ballVelY;

        sideCollisionCheck();
        topBottomCollisionCheck();

        player.update(playerPoint);
        playerTwo.update(playerTwoPoint);
        myball.update(ballPoint);

        paddleCollisionCheck();

        System.gc();

        while (scoreOne >= 10 || scoreTwo >= 10) {
            scoreOne = 0;
            scoreTwo = 0;

            //gameModePong = 1;
            //menuOrRetry = 2;

            restart = true;
        }
    }

    public void btMulitplayer() {
        btMode = true;

        /*btAdapter = BluetoothAdapter.getDefaultAdapter();


        if (myActivity.btOn() == 1) {
            toastNoBluetooth.show();
        } else if (myActivity.btOn() == 2 && toastShow == true) {
            toastBluetoothWasOn.show();
        } else if (myActivity.btOn() == 3 && toastShow == true) {
            toastBluetoothNowOn.show();
        }

        toastShow = false;

        while (myActivity.btConnectionEstablished == false) {
            myActivity.establishBTConnection();
        }*/

    }

    public void aiPaddle() {
        if (ballPoint.y <= disp.getHeight() / 2 && ballVelY < 0) {
            if (ballVelX > 0) p2velX = 5;
            if (ballVelX < 0) p2velX = -5;
        } else p2velX = 0;
    }
    public void paddleSideCollisionCheck() {
        while (playerPoint.x <= paddleSize) playerPoint.x++;
        while (playerTwoPoint.x <= paddleSize) playerTwoPoint.x++;
        while (playerPoint.x >= disp.getWidth() - paddleSize) playerPoint.x--;
        while (playerTwoPoint.x >= disp.getWidth() - paddleSize) playerTwoPoint.x--;
    }
    public void velocityCheck(boolean velocityX){
        if (velocityX == true) {
            while (abs(ballVelX) <= 2) {
                if (ballVelX < 0) ballVelX--;
                else if (ballVelX > 0) ballVelX++;
                else {
                    if (r.nextInt(1) == 1) ballVelX += 3;
                    else ballVelX -= 3;

                }
            }
        } else if (velocityX == false) {
            while (abs(ballVelY) <= 2) {
                if (ballVelY < 0) ballVelY--;
                else if (ballVelY > 0) ballVelY++;
                else {
                    if (r.nextInt(1) == 1) ballVelY += 3;
                    else ballVelY -= 3;

                }
            }
        }
    }
    public void sideCollisionCheck() {
        if (ballPoint.x <= 50 || ballPoint.x >= disp.getWidth() - 50) {
            ballVelX = -(ballVelX);
            randomizeColor();

            mp.start();
            if (mp2.isPlaying()) mp2.pause();
        }
    }
    public void topBottomCollisionCheck() {
        if (ballPoint.y <= 50 || ballPoint.y >= disp.getHeight() - 50) {
            if (ballPoint.y <= 50) scoreTwo++;
            else scoreOne++;

            ballVelX = moveSpeed * (r.nextInt(6) - 3);
            ballVelY = moveSpeed * (r.nextInt(6) - 3);
            ballPoint.y = disp.getHeight() / 2 - 50;
            ballPoint.x = disp.getWidth() / 2 - 50;

            colorR = 255;
            colorG = 255;
            colorB = 255;
        }
    }
    public void paddleCollisionCheck() {
        if (ballPoint.y - 50 <= playerPoint.y + 50 && ballPoint.x - 50 >= playerPoint.x - paddleSize && ballPoint.x + 50 <= playerPoint.x + paddleSize) {
            if (ballVelY < 0) {
                if (ballPoint.x - 50 >= playerTwoPoint.x - paddleSize/2 && ballPoint.x + 50 <= playerTwoPoint.x + paddleSize/2) {
                    ballVelX --;
                    if (ballVelY < 0) ballVelY -= 2;
                    else if (ballVelY > 0) ballVelY += 2;
                }
                else {
                    ballVelX ++;
                    if (ballVelY < 0) ballVelY -= 2;
                    else if (ballVelY > 0) ballVelY += 2;
                }
                ballVelY = -(ballVelY);

                randomizeColor();
                mp2.start();
                if (mp.isPlaying()) mp.pause();
            }
        }

        if (ballPoint.y + 50 >= playerTwoPoint.y - 50 && ballPoint.x - 50 >= playerTwoPoint.x - paddleSize && ballPoint.x + 50 <= playerTwoPoint.x + paddleSize) {
            if (ballVelY > 0) {
                if (ballPoint.x - 50 >= playerTwoPoint.x - paddleSize/2 && ballPoint.x + 50 <= playerTwoPoint.x + paddleSize/2){
                    ballVelX--;
                    if (ballVelY < 0) ballVelY -= 2;
                    else if (ballVelY > 0) ballVelY += 2;
                }
                else {
                    ballVelX ++;
                    if (ballVelY < 0) ballVelY -= 2;
                    else if (ballVelY > 0) ballVelY += 2;
                }
                ballVelY = -(ballVelY);

                randomizeColor();
                mp2.start();
                if (mp.isPlaying()) mp.pause();
            }
        }
    }
    public void randomizeColor() {
        colorR = r.nextInt(250) + 5;
        colorG = r.nextInt(250) + 5;
        colorB = r.nextInt(250) + 5;
    }
}
