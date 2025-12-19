package anaconda.views;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;
import java.util.LinkedList;

public class GameView {
    private Screen screen;
    private TextGraphics tg;
    private final int startX, startY;
    private final int maxWidth, maxHeight;

    public GameView(int startX, int startY, int maxWidth, int maxHeight) throws IOException {
        this.startX = startX;
        this.startY = startY;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;

        initializeScreen();
    }

    private void initializeScreen() throws IOException {
        screen = new DefaultTerminalFactory().createScreen();
        screen.startScreen();
        screen.setCursorPosition(null);
        tg = screen.newTextGraphics();
    }

    public void clearScreen() {
        screen.clear();
    }

    public void drawBorder() {
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        for (int y = 0; y < maxHeight - 1; y++) {
            for (int x = 0; x < maxWidth - 1; x++) {
                if (y == 0 || y == maxHeight - 2 || x == 0 || x == maxWidth - 2) {
                    tg.putString(startX + x, startY + y, "██");
                }
            }
        }
    }

    public void drawSnake(LinkedList<int[]> snake) {
        tg.setForegroundColor(TextColor.ANSI.GREEN);
        for (int[] part : snake) {
            tg.putString(startX + part[0] * 2, startY + part[1], "██");
        }
    }

    public void drawObstacles(LinkedList<int[]> obstacles) {
        tg.setForegroundColor(TextColor.ANSI.RED);
        for (int[] obs : obstacles) {
            tg.putString(startX + obs[0] * 2, startY + obs[1], "██████");
        }
    }

    public void drawFruit(int[] fruit) {
        tg.setForegroundColor(TextColor.ANSI.YELLOW);
        tg.putString(startX + fruit[0] * 2, startY + fruit[1], "🍎");
    }

    public void clearPosition(int x, int y) {
        tg.putString(startX + x * 2, startY + y, "  ");
    }

    public void drawPauseScreen() {
        int centerX = maxWidth / 2 - 10;
        int centerY = maxHeight / 2;

        // Desenhar fundo da pausa
        tg.setForegroundColor(TextColor.ANSI.BLACK);
        tg.setBackgroundColor(TextColor.ANSI.WHITE);
        for (int y = centerY - 3; y < centerY + 4; y++) {
            for (int x = centerX - 5; x < centerX + 25; x++) {
                tg.putString(x, y, " ");
            }
        }

        // Desenhar texto de pausa
        tg.setForegroundColor(TextColor.ANSI.YELLOW);
        tg.setBackgroundColor(TextColor.ANSI.DEFAULT);

        String[] pauseText = {
                "╔═══════════════════╗",
                "║   JOGO PAUSADO    ║",
                "╚═══════════════════╝",
                "",
                "Pressione P para continuar",
                "Pressione ESC para sair"
        };

        for (int i = 0; i < pauseText.length; i++) {
            tg.putString(centerX, centerY - 2 + i, pauseText[i]);
        }
    }

    public void refresh() throws IOException {
        screen.refresh();
    }

    public Screen getScreen() {
        return screen;
    }

    public void close() throws IOException {
        screen.stopScreen();
    }
}