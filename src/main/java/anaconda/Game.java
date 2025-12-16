import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

public class Game {
    public static void main(String[] args) {
        DefaultTerminalFactory factory = new DefaultTerminalFactory();
        Screen screen = null;
        try {
            screen = factory.createScreen();
            screen.startScreen();

            TextGraphics tg = screen.newTextGraphics();
            TerminalSize termSize = screen.getTerminalSize();

            // tamanho do lado do quadrado: metade do menor eixo do terminal
            int side = Math.min(termSize.getColumns(), termSize.getRows()) / 2;
            if (side < 2) side = 2; // garante tamanho mínimo visível

            // centraliza o quadrado
            int left = (termSize.getColumns() - side) / 2;
            int top  = (termSize.getRows() - side) / 2;

            // desenha apenas o contorno do quadrado com '*'
            tg.drawRectangle(new TerminalPosition(left, top),
                    new TerminalSize(side, side),
                    '*');

            screen.refresh();

            // espera por qualquer tecla para encerrar
            screen.readInput();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (screen != null) {
                try {
                    screen.stopScreen();
                } catch (Exception ignore) {}
            }
        }
    }
}
