package anaconda.controllers;

import anaconda.models.GameModel;
import anaconda.views.GameView;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import java.io.IOException;

public class GameController {
    private GameModel model;
    private GameView view;
    private int[] lastTail;
    private boolean paused;

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
        this.paused = false;
    }

    public void initialize() throws IOException {
        view.drawBorder();
        view.drawObstacles(model.getObstacles());
        view.drawSnake(model.getSnake());
        view.drawFruit(model.getFruit());
        view.refresh();
    }

    public void run() {
        try {
            initialize();

            while (!model.isGameOver()) {
                if (paused) {
                    handlePause();
                    continue;
                }

                Thread.sleep(200);

                // Processa input
                handleInput();

                // Guarda rabo antes de atualizar
                lastTail = model.getSnake().getLast().clone();

                // Atualiza modelo
                if (!model.update()) {
                    break;
                }

                // Atualiza view
                updateView();
            }

            // Game over
            view.close();
            System.out.println(model.getGameOverMessage());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleInput() throws IOException {
        KeyStroke key = view.getScreen().pollInput();
        if (key != null) {
            // Verificar se é tecla de caractere (P ou p)
            if (key.getCharacter() != null &&
                    (key.getCharacter() == 'p' || key.getCharacter() == 'P')) {
                paused = true;
                view.drawPauseScreen();
                view.refresh();
                return;
            }

            // Processar setas de direção
            switch (key.getKeyType()) {
                case ArrowUp -> model.setDirection(0, -1);
                case ArrowDown -> model.setDirection(0, 1);
                case ArrowLeft -> model.setDirection(-1, 0);
                case ArrowRight -> model.setDirection(1, 0);
                case Escape -> {
                    view.close();
                    System.exit(0);
                }
            }
        }
    }

    private void handlePause() throws IOException {
        KeyStroke key = view.getScreen().readInput();

        if (key != null) {
            // Verificar P para despausar
            if (key.getCharacter() != null &&
                    (key.getCharacter() == 'p' || key.getCharacter() == 'P')) {
                paused = false;
                // Redesenhar o jogo
                view.clearScreen();
                view.drawBorder();
                view.drawObstacles(model.getObstacles());
                view.drawSnake(model.getSnake());
                view.drawFruit(model.getFruit());
                view.refresh();
            }
            // Verificar ESC para sair
            else if (key.getKeyType() == KeyType.Escape) {
                view.close();
                System.exit(0);
            }
        }
    }

    private void updateView() throws IOException {
        // Limpa posição do rabo anterior (só se não cresceu)
        int[] currentTail = model.getSnake().getLast();
        if (lastTail[0] != currentTail[0] || lastTail[1] != currentTail[1]) {
            view.clearPosition(lastTail[0], lastTail[1]);
        }

        // Desenha nova cabeça
        int[] head = model.getSnake().getFirst();
        view.drawSnake(model.getSnake());

        // Redesenha fruta (caso tenha mudado)
        view.drawFruit(model.getFruit());

        view.refresh();
    }
}