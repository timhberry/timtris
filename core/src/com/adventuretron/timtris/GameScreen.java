package com.adventuretron.timtris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {

    final TimtrisGame game;
    int field[][] = new int[10][16];

    boolean gameRunning = true;
    Timtromino currentPiece;
    int curX = 4;
    int curY = 13;

    long gameTime;
    long gameTick;

    private final int T_WIDTH = 25;
    private final int T_XOFFSET = 50;
    private final int T_YOFFSET = 45;

    private ShapeRenderer shapeRenderer;

    public GameScreen(TimtrisGame game) {
        gameTime = TimeUtils.millis();
        gameTick = 1000;
        this.game = game;
        shapeRenderer = new ShapeRenderer();
        init();
    }

    private void init() {
        // Create a blank Timtris field
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 10; x++) {
                field[x][y] = 0;
            }
        }
        // Set the first Timtomino to drop
        currentPiece = new Timtromino();
        // Set the game time

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(Color.GRAY.r, Color.GRAY.g, Color.GRAY.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // draw the field
        shapeRenderer.setColor(Color.DARK_GRAY);
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 10; x++) {
                shapeRenderer.rect(x * T_WIDTH + T_XOFFSET, y * T_WIDTH + T_YOFFSET, T_WIDTH, T_WIDTH);
            }
        }

        // draw the current piece
        if (currentPiece.getShape() != Timtromino.Shapes.NoShape) {
            for (int i = 0; i < 4; ++i) {
                int x = curX + currentPiece.x(i);
                int y = curY + currentPiece.y(i);
                shapeRenderer.setColor(currentPiece.getColor());
                shapeRenderer.rect(x * T_WIDTH + T_XOFFSET, y * T_WIDTH + T_YOFFSET, T_WIDTH, T_WIDTH);
            }
        }

        shapeRenderer.end();

        // game logic, such as it is
        if (gameRunning) {
            if (TimeUtils.timeSinceMillis(gameTime) > gameTick) {
                gameTime = TimeUtils.millis();
                gameLogic();
            }
        }

    }

    private void gameLogic() {
        for (int i = 0; i < 4; ++i) {
            Gdx.app.log("Timtronimo", "Part " + i + " at X:" + (curX + currentPiece.x(i)) + "Y:" + (curY + currentPiece.y(i)));
        }
        curY = curY - 1;
    }

    private void tryDrop() {

    }

    private void tryRotateLeft() {

    }

    private void tryRotateRight() {

    }


    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
