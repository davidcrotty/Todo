package com.david.todo.service;

/**
 * Created by David Crotty on 29/11/2015.
 */
public class TextEntryService implements ITextEntryService{

    @Override
    public boolean textHasBeenEntered(CharSequence text) {
        return text == null || text.toString().isEmpty() ? false : true;
    }
}
