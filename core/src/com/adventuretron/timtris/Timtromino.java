package com.adventuretron.timtris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class Timtromino {

    enum Shapes { NoShape, ZShape, SShape, LineShape, TShape, SquareShape, LShape, MirroredLShape };
    Array<Color> Colors[];

    private Color[] shapeColors;
    private Shapes pieceShape;
    private int coords[][];
    private int[][][] coordsTable;

    public Timtromino() {
        // might be a neater way to do this
        shapeColors = new Color[8];
        setupColors();
        // create shapes
        coords = new int[4][2];
        // set a random shape for this instance
        setRandomShape();
        Gdx.app.log("Timtromino", "Created new piece with shape " + this.getShape());
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

    public void setShape(Shapes shape) {
        coordsTable = new int[][][] {
                { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },
                { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } },
                { { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 } },
                { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } },
                { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } },
                { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },
                { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } },
                { { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 } }
        };
        for (int i = 0; i < 4 ; i++) {
            for (int j = 0; j < 2; ++j) {
                coords[i][j] = coordsTable[shape.ordinal()][i][j];
            }
        }
        pieceShape = shape;
    }

    private void setX(int index, int x) {
        coords[index][0] = x;
    }

    private void setY(int index, int y) {
        coords[index][1] = y;
    }

    public int x(int index) {
        return coords[index][0];
    }

    public int y(int index) {
        return coords[index][1];
    }

    public Shapes getShape()  {
        return pieceShape;
    }

    public Color getColor() {
        return shapeColors[pieceShape.ordinal()];
    }

    public void setRandomShape()
    {
        Random r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;
        Shapes[] values = Shapes.values();
        setShape(values[x]);
    }

    public int minX()
    {
        int m = coords[0][0];
        for (int i=0; i < 4; i++) {
            m = Math.min(m, coords[i][0]);
        }
        return m;
    }

    public int minY()
    {
        int m = coords[0][1];
        for (int i=0; i < 4; i++) {
            m = Math.min(m, coords[i][1]);
        }
        return m;
    }

    public Timtromino rotateLeft()
    {
        if (pieceShape == Shapes.SquareShape)
            return this;

        Timtromino result = new Timtromino();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; ++i) {
            result.setX(i, y(i));
            result.setY(i, -x(i));
        }
        return result;
    }

    public Timtromino rotateRight()
    {
        if (pieceShape == Shapes.SquareShape)
            return this;

        Timtromino result = new Timtromino();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; ++i) {
            result.setX(i, -y(i));
            result.setY(i, x(i));
        }
        return result;
    }

}
