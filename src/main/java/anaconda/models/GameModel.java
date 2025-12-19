package anaconda.models;

import java.util.LinkedList;
import java.util.Random;

public class GameModel {
    private LinkedList<int[]> snake;
    private LinkedList<int[]> obstacles;
    private int[] fruit;
    private int dx, dy;
    private final int minX, maxX, minY, maxY;
    private final Random rand;
    private boolean gameOver;
    private String gameOverMessage;

    public GameModel(int minX, int maxX, int minY, int maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.rand = new Random();
        this.gameOver = false;

        initializeGame();
    }

    private void initializeGame() {
        // Inicializa cobra
        snake = new LinkedList<>();
        int size = Math.min(maxX - minX, maxY - minY);
        snake.add(new int[]{size / 2, size / 2});

        // Direção inicial: direita
        dx = 1;
        dy = 0;

        // Gera fruta inicial
        generateFruit();

        // Gera obstáculos
        generateObstacles();
    }

    private void generateObstacles() {
        obstacles = new LinkedList<>();
        int numberOfObstacles = 6;
        int size = Math.min(maxX - minX, maxY - minY);

        for (int i = 0; i < numberOfObstacles; i++) {
            int ox = rand.nextInt(maxX - minX + 1) + minX;
            int oy = rand.nextInt(maxY - minY + 1) + minY;

            // Evita nascer em cima da cobra inicial
            if (ox == size / 2 && oy == size / 2) {
                i--;
                continue;
            }

            // Evita obstáculo duplicado
            boolean alreadyExists = false;
            for (int[] obs : obstacles) {
                if (obs[0] == ox && obs[1] == oy) {
                    alreadyExists = true;
                    break;
                }
            }

            if (alreadyExists) {
                i--;
                continue;
            }

            obstacles.add(new int[]{ox, oy});
        }
    }

    private void generateFruit() {
        int fruitX = rand.nextInt(maxX - minX + 1) + minX;
        int fruitY = rand.nextInt(maxY - minY + 1) + minY;
        fruit = new int[]{fruitX, fruitY};
    }

    public void setDirection(int dx, int dy) {
        // Impede reverter direção 180°
        if (this.dx + dx != 0 || this.dy + dy != 0) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    public boolean update() {
        if (gameOver) return false;

        // Calcula nova cabeça
        int[] head = snake.getFirst();
        int newX = head[0] + dx;
        int newY = head[1] + dy;

        // Verifica colisão com paredes
        if (newX < minX || newX > maxX || newY < minY || newY > maxY) {
            gameOver = true;
            gameOverMessage = "Game Over! A cobra bateu na parede.";
            return false;
        }

        // Verifica colisão com própria cobra
        for (int[] part : snake) {
            if (part[0] == newX && part[1] == newY) {
                gameOver = true;
                gameOverMessage = "Game Over! A cobra bateu nela mesma.";
                return false;
            }
        }

        // Verifica colisão com obstáculos
        for (int[] obs : obstacles) {
            if (newX == obs[0] && newY == obs[1]) {
                gameOver = true;
                gameOverMessage = "Game Over! A cobra bateu em um obstaculo.";
                return false;
            }
        }

        // Remove rabo
        int[] tail = snake.removeLast();

        // Adiciona nova cabeça
        snake.addFirst(new int[]{newX, newY});

        // Verifica se comeu fruta
        if (newX == fruit[0] && newY == fruit[1]) {
            snake.addLast(tail); // Cresce
            generateFruit();
        }

        return true;
    }

    // Getters
    public LinkedList<int[]> getSnake() { return snake; }
    public LinkedList<int[]> getObstacles() { return obstacles; }
    public int[] getFruit() { return fruit; }
    public boolean isGameOver() { return gameOver; }
    public String getGameOverMessage() { return gameOverMessage; }
    public int[] getLastTail() {
        return snake.size() > 1 ? snake.getLast() : null;
    }
}