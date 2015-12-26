package com.adventuretron.timtris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen, InputProcessor {

    final TimtrisGame game;
    int field[][] = new int[10][16];
    private Color[] shapeColors;

    boolean gameRunning = true;
    Timtromino currentPiece;
    Timtromino nextPiece;
    Timtromino rotatedPiece;
    int curX = 4;
    int curY = 14;

    long gameTime;
    long gameTick;

    int score;
    int highScore;

    Sound blipSound = Gdx.audio.newSound(Gdx.files.internal("blip.wav"));
    Sound gameOverSound = Gdx.audio.newSound(Gdx.files.internal("gameover.wav"));
    Sound lineClearSound = Gdx.audio.newSound(Gdx.files.internal("lineclear.wav"));

    private final int T_WIDTH = 25;
    private final int T_XOFFSET = 50;
    private final int T_YOFFSET = 45;

    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont font;

    public GameScreen(TimtrisGame game) {
        gameTick = 1000;
        this.game = game;
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(this);
        shapeColors = new Color[8];
        setupColors();
        init();
    }

    private void setupColors() {
        // note to self; read up on arrays
        shapeColors[0] = Color.DARK_GRAY;
        shapeColors[1] = Color.CYAN;
        shapeColors[2] = Color.RED;
        shapeColors[3] = Color.YELLOW;
        shapeColors[4] = Color.GREEN;
        shapeColors[5] = Color.BLUE;
        shapeColors[6] = Color.PURPLE;
        shapeColors[7] = Color.GOLD;
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
        nextPiece = new Timtromino();
        // Set the game time
        gameTime = TimeUtils.millis();
        score = 0;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(Color.GRAY.r, Color.GRAY.g, Color.GRAY.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // TODO - Render the next Timtromino
        // TODO - Render game state, score & high score
        // TODO - Pause button

        // draw the field
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 10; x++) {
                shapeRenderer.setColor(shapeColors[field[x][y]]);
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

        // draw over any bits sticking out at the top
        for (int x = 0; x < 10; x++) {
            shapeRenderer.setColor(Color.GRAY);
            shapeRenderer.rect(x * T_WIDTH + T_XOFFSET, 16 * T_WIDTH + T_YOFFSET, T_WIDTH, T_WIDTH);
        }

        // draw the next shape coming
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                shapeRenderer.setColor(Color.DARK_GRAY);
                shapeRenderer.rect(x * T_WIDTH + T_XOFFSET + (T_WIDTH * 12), y * T_WIDTH + T_YOFFSET + (T_WIDTH * 10), T_WIDTH, T_WIDTH);
            }
        }
        if (nextPiece.getShape() != Timtromino.Shapes.NoShape) {
            for (int i = 0; i < 4; ++i) {
                int x = 14 + nextPiece.x(i);
                int y = 12 + nextPiece.y(i);
                shapeRenderer.setColor(nextPiece.getColor());
                shapeRenderer.rect(x * T_WIDTH + T_XOFFSET, y * T_WIDTH + T_YOFFSET, T_WIDTH, T_WIDTH);
            }
        }

        // draw score boxes
        for (int x = 0; x < 5; x++) {
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.rect(x * T_WIDTH + T_XOFFSET + (T_WIDTH * 12), T_WIDTH + T_YOFFSET + (T_WIDTH * 6), T_WIDTH, T_WIDTH);
            shapeRenderer.rect(x * T_WIDTH + T_XOFFSET + (T_WIDTH * 12), T_WIDTH + T_YOFFSET + (T_WIDTH * 5), T_WIDTH, T_WIDTH);
            shapeRenderer.rect(x * T_WIDTH + T_XOFFSET + (T_WIDTH * 12), T_WIDTH + T_YOFFSET + (T_WIDTH * 2), T_WIDTH, T_WIDTH);
            shapeRenderer.rect(x * T_WIDTH + T_XOFFSET + (T_WIDTH * 12), T_WIDTH + T_YOFFSET + (T_WIDTH * 1), T_WIDTH, T_WIDTH);
        }

        shapeRenderer.end();

        batch.begin();
        font.setColor(Color.WHITE);
        font.draw(batch, "NEXT PIECE", 12 * T_WIDTH + T_XOFFSET, 16 * T_WIDTH + T_YOFFSET);
        font.draw(batch, "HIGH SCORE", 12 * T_WIDTH + T_XOFFSET, 9 * T_WIDTH + T_YOFFSET);
        font.draw(batch, "SCORE", 12 * T_WIDTH + T_XOFFSET, 5 * T_WIDTH + T_YOFFSET);
        font.draw(batch, "" + score, 13 * T_WIDTH + T_XOFFSET, 3 * T_WIDTH + T_YOFFSET);
        font.draw(batch, "" + highScore, 13 * T_WIDTH + T_XOFFSET, 7 * T_WIDTH + T_YOFFSET);
        batch.end();

        // game logic, such as it is
        if (gameRunning) {
            if (TimeUtils.timeSinceMillis(gameTime) > gameTick) {
                gameTime = TimeUtils.millis();
                gameLogic();
            }
        }

    }

    private void gameLogic() {
        tryDrop();
    }

    private void tryDrop() {
        Gdx.app.log("Move", "Trying to drop");
        boolean blocked = false;
        for (int i = 0; i < 4; ++i) {
            if (collisionAt(curX + currentPiece.x(i), curY - 1 + currentPiece.y(i))) {
                blocked = true;
            }
        }
        if (blocked) {
            // something's blocking us! our piece becomes part of the field
            for (int i = 0; i < 4; ++i) {
                field[curX + currentPiece.x(i)][curY + currentPiece.y(i)] = currentPiece.getColorIndex();
            }
            // and we get a new piece!
            curX = 4;
            curY = 14;
            currentPiece = nextPiece;
            nextPiece = new Timtromino();
            // check to see if it has anywhere to go, otherwise it's game over dude
            boolean newpieceBlocked = false;
            for (int i = 0; i < 4; ++i) {
                if (collisionAt(curX + currentPiece.x(i), curY - 1 + currentPiece.y(i))) {
                    newpieceBlocked = true;
                }
            }
            if (newpieceBlocked) {
                gameRunning = false;
                gameOverSound.play();
            }
            checkRows();

        } else {
            // everything's cool, drop it like it's hot
            curY = curY - 1;
            blipSound.play();
        }


    }

    private void checkRows() {
        int rowsCleared = 0;
        for (int a = 0; a < 16; a++) {
            boolean rowComplete = true;
            for (int b = 0; b < 10; b++) {
                if (field[b][a] == 0) {
                    rowComplete = false;
                }
            }
            if (rowComplete) {
                // row a is complete!
                Gdx.app.log("CHECK", "Row " + a + " is complete!");
                int newfield[][] = new int[10][16];
                for (int newa = 0; newa < a; newa++) {
                    for (int newb = 0; newb < 10; newb++) {
                        newfield[newb][newa] = field[newb][newa];
                    }
                }
                for (int newa = a; newa < 15; newa++) {
                    for (int newb = 0; newb < 10; newb++) {
                        newfield[newb][newa] = field[newb][newa+1];
                    }
                }
                // blank top row
                for (int foo = 0; foo < 10; foo++) {
                    newfield[foo][15] = 0;
                }
                field = newfield;
                lineClearSound.play();
                rowsCleared++;
                // check the new field from the start again
                a = 0;
            }
        }
        if (rowsCleared > 0) {
            gameTick = gameTick - 2;
            rowsCleared = (rowsCleared * rowsCleared) * 10;
            score = score + rowsCleared;
        }
    }

    private void tryMoveLeft() {
        Gdx.app.log("Move", "Trying to move left");
        boolean blocked = false;
        for (int i = 0; i < 4; ++i) {
            if (collisionAt(curX - 1 + currentPiece.x(i), curY + currentPiece.y(i))) {
                blocked = true;
            }
        }
        if (!blocked) {
            curX = curX - 1;
        }
    }

    private void tryMoveRight() {
        Gdx.app.log("Move", "Trying to move right");
        boolean blocked = false;
        for (int i = 0; i < 4; ++i) {
            if (collisionAt(curX + 1 + currentPiece.x(i), curY + currentPiece.y(i))) {
                blocked = true;
            }
        }
        if (!blocked) {
            curX = curX + 1;
        }
    }

    private void tryRotateRight() {
        Gdx.app.log("Move", "Trying to rotate right");
        boolean blocked = false;
        rotatedPiece = currentPiece.rotateRight();
        for (int i = 0; i < 4; ++i) {
            if (collisionAt(curX + rotatedPiece.x(i), curY + rotatedPiece.y(i))) {
                blocked = true;
            }
        }
        if (!blocked) {
            currentPiece = rotatedPiece;
        }
    }

    private boolean collisionAt(int x, int y) {
        if (x < 0 | x > 9) {
            Gdx.app.log("Collision Check", "Checking x:" + x + " y:" + y + " BANG");
            return true;
        }
        if (y < 0 | y > 15) {
            Gdx.app.log("Collision Check", "Checking x:" + x + " y:" + y + " BANG");
            return true;
        }
        if (field[x][y] != 0) {
            Gdx.app.log("Collision Check", "Checking x:" + x + " y:" + y + " BANG");
            return true;
        }
        Gdx.app.log("Collision Check", "Checking x:" + x + " y:" + y + " Clear");
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean keyTyped (char character) {
        Gdx.app.log("KeyTyped", "Key:" + character);
        switch (character) {
            case 'a':
                tryMoveLeft();
                break;
            case 'd':
                tryMoveRight();
                break;
            case 's':
                tryDrop();
                break;
            case 'w':
                tryRotateRight();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
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
