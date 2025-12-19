package anaconda.controllers;

import anaconda.models.MenuModel;
import anaconda.views.MenuView;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;

public class MenuController {
    private MenuModel model;
    private MenuView view;
    private Screen screen;
    private MenuState currentState;

    public enum MenuState {
        MAIN_MENU,
        INSTRUCTIONS
    }

    public MenuController(Screen screen, int maxWidth, int maxHeight) {
        this.screen = screen;
        this.model = new MenuModel();
        this.view = new MenuView(screen, maxWidth, maxHeight);
        this.currentState = MenuState.MAIN_MENU;
    }

    /**
     * Processa o menu e retorna a ação a ser tomada
     * @return MenuAction com a ação selecionada
     */
    public MenuAction processMenu() throws IOException {
        if (currentState == MenuState.MAIN_MENU) {
            return handleMainMenu();
        } else {
            return handleInstructions();
        }
    }

    /**
     * Processa o menu principal
     */
    private MenuAction handleMainMenu() throws IOException {
        view.renderMenu(model.getOptions(), model.getSelectedOption());
        view.refresh();

        KeyStroke key = screen.pollInput();

        if (key != null) {
            if (key.getKeyType() == KeyType.ArrowUp) {
                model.moveUp();
            } else if (key.getKeyType() == KeyType.ArrowDown) {
                model.moveDown();
            } else if (key.getKeyType() == KeyType.Enter) {
                int selectedOption = model.getSelectedOption();

                switch (selectedOption) {
                    case 0: // INICIAR JOGO
                        return MenuAction.START_GAME;

                    case 1: // INSTRUÇÕES
                        currentState = MenuState.INSTRUCTIONS;
                        return MenuAction.NONE;

                    case 2: // SAIR
                        return MenuAction.EXIT;
                }
            }
        }

        return MenuAction.NONE;
    }

    /**
     * Processa a tela de instruções
     */
    private MenuAction handleInstructions() throws IOException {
        view.renderInstructions();
        view.refresh();

        KeyStroke key = screen.pollInput();

        if (key != null && key.getKeyType() == KeyType.Enter) {
            currentState = MenuState.MAIN_MENU;
        }

        return MenuAction.NONE;
    }

    /**
     * Reseta o estado do menu
     */
    public void reset() {
        model.resetSelection();
        currentState = MenuState.MAIN_MENU;
    }

    /**
     * Retorna a opção atualmente selecionada
     */
    public int getSelectedOption() {
        return model.getSelectedOption();
    }

    /**
     * Enum com as possíveis ações do menu
     */
    public enum MenuAction {
        NONE,           // Nenhuma ação
        START_GAME,     // Iniciar o jogo
        EXIT            // Sair do programa
    }
}