package anaconda.views;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

public class MenuView {
    private Screen screen;
    private TextGraphics tg;
    private final int maxWidth;
    private final int maxHeight;

    public MenuView(Screen screen, int maxWidth, int maxHeight) {
        this.screen = screen;
        this.tg = screen.newTextGraphics();
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    /**
     * Renderiza o menu principal
     */
    public void renderMenu(String[] options, int selectedOption) {
        screen.clear();

        int centerX = maxWidth / 2 - 15;
        int centerY = maxHeight / 4;

        drawTitle(centerX, centerY);
        drawOptions(options, selectedOption, centerX, centerY + 5);
        drawNavigationHelp(centerX);
    }

    /**
     * Renderiza a tela de instruções
     */
    public void renderInstructions() {
        screen.clear();

        tg.setForegroundColor(TextColor.ANSI.YELLOW);

        String title = "═══ INSTRUÇÕES ═══";
        int centerX = maxWidth / 2 - 10;
        int startLine = 5;

        tg.putString(centerX, startLine, title);

        tg.setForegroundColor(TextColor.ANSI.WHITE);
        String[] instructions = {
                "",
                "▪ Use as setas ↑ ↓ ← → para mover",
                "▪ Coma as frutas 🍎 para crescer",
                "▪ Evite as paredes e obstáculos",
                "▪ Pressione P para pausar",
                "▪ Pressione ESC para sair",
                "",
                "",
                "Pressione ENTER para voltar..."
        };

        for (int i = 0; i < instructions.length; i++) {
            tg.putString(centerX - 10, startLine + 2 + i, instructions[i]);
        }
    }

    /**
     * Desenha o título do jogo
     */
    private void drawTitle(int x, int y) {
        tg.setForegroundColor(TextColor.ANSI.GREEN);

        String[] titleLines = {
                "╔═══════════════════════════╗",
                "║     JOGO DA ANACONDA      ║",
                "╚═══════════════════════════╝"
        };

        for (int i = 0; i < titleLines.length; i++) {
            tg.putString(x, y + i, titleLines[i]);
        }
    }

    /**
     * Desenha as opções do menu
     */
    private void drawOptions(String[] options, int selectedOption, int x, int y) {
        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) {
                tg.setForegroundColor(TextColor.ANSI.YELLOW);
                tg.putString(x + 5, y + i * 2, "► " + options[i]);
            } else {
                tg.setForegroundColor(TextColor.ANSI.WHITE);
                tg.putString(x + 5, y + i * 2, "  " + options[i]);
            }
        }
    }

    /**
     * Desenha as instruções de navegação
     */
    private void drawNavigationHelp(int x) {
        tg.setForegroundColor(TextColor.ANSI.CYAN);
        String help = "Use ↑↓ para navegar, ENTER para selecionar";
        tg.putString(x - 5, maxHeight - 5, help);
    }

    /**
     * Atualiza a tela
     */
    public void refresh() {
        try {
            screen.refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}