package com.example.myfirstapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet; // 必须导入这个
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class GobangView extends View {

    private Paint paint;
    private int gridSize = 15;
    private float cellWidth;
    private int[][] board = new int[15][15];
    private boolean isBlack = true;
    private boolean isGameOver = false;

    // 1. 第一个构造函数：给代码里直接 new 用的
    public GobangView(Context context) {
        super(context);
        init();
    }

    // 🚨🚨🚨 2. 第二个构造函数：给 XML 布局用的（之前就是缺这个导致崩溃！）
    public GobangView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);
    }

    // --- 下面是之前的画图和逻辑代码，保持不变 ---

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth();
        cellWidth = width / gridSize;

        // 画线
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < gridSize; i++) {
            float pos = i * cellWidth + cellWidth / 2;
            canvas.drawLine(cellWidth / 2, pos, width - cellWidth / 2, pos, paint);
            canvas.drawLine(pos, cellWidth / 2, pos, width - cellWidth / 2, paint);
        }

        // 画棋子
        paint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (board[i][j] != 0) {
                    paint.setColor(board[i][j] == 1 ? Color.BLACK : Color.WHITE);
                    float cx = i * cellWidth + cellWidth / 2;
                    float cy = j * cellWidth + cellWidth / 2;
                    canvas.drawCircle(cx, cy, cellWidth / 2 * 0.8f, paint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isGameOver) return true;

        if (event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) (event.getX() / cellWidth);
            int y = (int) (event.getY() / cellWidth);

            if (x >= 0 && x < gridSize && y >= 0 && y < gridSize && board[x][y] == 0) {
                board[x][y] = isBlack ? 1 : 2;
                if (checkWin(x, y)) {
                    isGameOver = true;
                    String winner = isBlack ? "江靖浩获胜！" : "江靖浩大获全胜！";
                    Toast.makeText(getContext(), winner, Toast.LENGTH_LONG).show();
                }
                isBlack = !isBlack;
                invalidate();
            }
        }
        return true;
    }

    private boolean checkWin(int x, int y) {
        int color = board[x][y];
        int[][] directions = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};
        for (int[] dir : directions) {
            int count = 1;
            for (int i = 1; i < 5; i++) {
                int nextX = x + dir[0] * i;
                int nextY = y + dir[1] * i;
                if (isValid(nextX, nextY) && board[nextX][nextY] == color) count++;
                else break;
            }
            for (int i = 1; i < 5; i++) {
                int nextX = x - dir[0] * i;
                int nextY = y - dir[1] * i;
                if (isValid(nextX, nextY) && board[nextX][nextY] == color) count++;
                else break;
            }
            if (count >= 5) return true;
        }
        return false;
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < gridSize && y >= 0 && y < gridSize;
    }

    // 重新开始游戏
    public void restartGame() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                board[i][j] = 0;
            }
        }
        isBlack = true;
        isGameOver = false;
        invalidate();
        Toast.makeText(getContext(), "游戏已重置", Toast.LENGTH_SHORT).show();
    }
}