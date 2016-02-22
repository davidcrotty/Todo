package com.david.todo.model;

import java.io.Serializable;

/**
 * Created by DavidHome on 11/01/2016.
 */
public class AnimateLocationCoordinatesModel implements Serializable {

    public final int _x;
    public final int _y;
    public final int _width;
    public final int _height;
    public final float _finalRadius;

    public AnimateLocationCoordinatesModel(int x, int y, int width, int height, float finalRadius) {
        _x = x;
        _y = y;
        _width = width;
        _height = height;
        _finalRadius = finalRadius;
    }
}
