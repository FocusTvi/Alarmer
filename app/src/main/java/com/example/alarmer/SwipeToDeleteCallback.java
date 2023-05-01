package com.example.alarmer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private AlarmAdapter adapter;

    public SwipeToDeleteCallback(AlarmAdapter adapter) {
        super(0, ItemTouchHelper.LEFT);
//        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;

    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        // Don't allow items to be moved
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (this.adapter != null) {
            this.adapter.deleteAlarm(position);
        }
    }


    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {

        View itemView = viewHolder.itemView;

        Paint p = new Paint();
        p.setColor(Color.parseColor("#ff0000"));

        if (dX > 0) {
            // Swiping right
            RectF background = new RectF(itemView.getLeft(), itemView.getTop(), dX, itemView.getBottom());
            c.drawRoundRect(background,20,20,p);
        } else {
            // Swiping left
            RectF background = new RectF(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            c.drawRoundRect(background,20,20,p);



            Drawable trashIcon = ContextCompat.getDrawable(viewHolder.itemView.getContext(), R.drawable.ic_baseline_delete_24);

            trashIcon.setBounds(
                    itemView.getRight() - 125,
                    itemView.getTop() + itemView.getHeight() / 4 +25,
                    itemView.getRight()-45 ,
                    itemView.getBottom() - itemView.getHeight() / 4-25
            );
            trashIcon.setColorFilter(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.white), PorterDuff.Mode.SRC_IN);
            trashIcon.draw(c);

            Paint textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(50f);
            textPaint.setTextAlign(Paint.Align.CENTER);
            Rect bounds = new Rect();
            textPaint.getTextBounds("Delete", 0, 4, bounds);

            c.drawText("Delete", itemView.getRight() - bounds.width() / 2 - 150f, itemView.getTop() + itemView.getHeight() / 2 + bounds.height() / 2, textPaint);

        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}