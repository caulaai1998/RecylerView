package com.example.recylerview;

public interface ItemTouchListenner {
    void onMove(int oldPosition, int newPosition);
    boolean onItemMove(int fromPosition, int toPosition);


}
