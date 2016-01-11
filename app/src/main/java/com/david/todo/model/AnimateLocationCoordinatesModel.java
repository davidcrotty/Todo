package com.david.todo.model;

import java.io.Serializable;

/**
 * Created by DavidHome on 11/01/2016.
 */
public class AnimateLocationCoordinatesModel implements Serializable {

    private final int _x;
    private final int _y;
    private final int _width;
    private final int _height;

    public AnimateLocationCoordinatesModel(int x, int y, int width, int height) {
        _x = x;
        _y = y;
        _width = width;
        _height = height;
    }

    public int getX() {
        return _x;
    }

    public int getY(){
        return _y;
    }

    public int getWidth() {
        return _width;
    }

    public int getHeight() {
        return _height;
    }
}
