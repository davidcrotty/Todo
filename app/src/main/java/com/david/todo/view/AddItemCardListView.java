package com.david.todo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.david.todo.R;
import com.david.todo.presenter.AddItemPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddItemCardListView extends RelativeLayout {
    private AddItemPresenter _addItemPresenter;

    @Bind(R.id.event_black)
    ImageView _imageView;

    public AddItemCardListView(Context context, AddItemPresenter addItemPresenter) {
        super(context);
        _addItemPresenter = addItemPresenter;
        init();
    }

    public AddItemCardListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.add_item_card_list_view, this);
        ButterKnife.bind(this);
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.event_black);
        _imageView.setImageBitmap(setColour(icon, 0x3F51B5));
    }

    public static Bitmap setColour(Bitmap src, int hexRgbColour) {
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        // color info
        int A, R, G, B;
        int pixelColor;
        // image size
        int height = src.getHeight();
        int width = src.getWidth();

        // scan through every pixel
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                // get one pixel
                pixelColor = src.getPixel(x, y);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = (hexRgbColour >> 16) & 0xFF;
                G = (hexRgbColour >> 8) & 0xFF;
                B = (hexRgbColour >> 0) & 0xFF;
                // set newly-inverted pixel to output image
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final bitmap
        return bmOut;
    }
}
