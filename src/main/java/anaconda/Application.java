package anaconda;

import anaconda.models.GameModel;
import anaconda.views.GameView;
import anaconda.views.MenuView;
import anaconda.controllers.GameController;
import anaconda.controllers.MenuController;
import anaconda.controllers.MenuController.MenuAction;
import com.googlecode.lanterna.screen.Screen;

public class Application {
    public static void main(String[] args) {
        Screen screen = null;

        try {
            // Configurações do jogo
            int maxWidth = 80;
            int maxHeight = 24;
            int startX = 0;
            int startY = 0;

            // Limites internos do quadrado
            int minX = 1;
            int maxX = 38;
            int minY = 1;
            int maxY = 21;

            // Criar GameView para obter o screen
            GameView view = new GameView(startX, startY, maxWidth, maxHeight);
            screen = view.getScreen();

            // Criar MenuController
            MenuController menuController = new MenuController(screen, maxWidth, maxHeight);

            boolean running = true;

            // Loop principal
            while (running) {
                // Processar menu
                MenuAction action = menuController.processMenu();

                if (action == MenuAction.START_GAME) {
                    // Limpar a tela do menu
                    screen.clear();
                    screen.refresh();

                    // Criar novo jogo e iniciar
                    GameModel model = new GameModel(minX, maxX, minY, maxY);
                    GameController controller = new GameController(model, view);
                    controller.run();

                    // Quando o jogo terminar, limpar tela e voltar ao menu
                    screen.clear();
                    screen.refresh();
                    menuController.reset();

                } else if (action == MenuAction.EXIT) {
                    running = false;
                }

                Thread.sleep(100);
            }

            // Fechar aplicação
            view.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}