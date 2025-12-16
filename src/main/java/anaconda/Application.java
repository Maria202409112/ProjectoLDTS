package anaconda;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.input.KeyStroke; //para a snake
import com.googlecode.lanterna.input.KeyType;

import java.util.LinkedList;
import java.util.Random;


public class Application {
    public static void main(String[] args) {
        try {
            // Cria o terminal e a tela
            Screen screen = new DefaultTerminalFactory().createScreen();
            screen.startScreen();
            screen.setCursorPosition(null);

            // ✅ criar TextGraphics
            TextGraphics tg = screen.newTextGraphics();


            // Obtém tamanho do terminal
            TerminalSize terminalSize = screen.getTerminalSize();
            int maxWidth = 80;
            int maxHeight = 24;



            // Define posição inicial e tamanho do quadrado
            int size = Math.min(maxWidth / 2 - 2, maxHeight - 2); // divide largura por 2 para compensar
            int startX = 0; // multiplicar por 2 para compensar largura dos blocos //distancia da borda esquerda
            int startY = 0; // distância da borda superior

            // Desenha quadrado oco
            for (int y = 0; y < 24-1; y++) {
                for (int x = 0; x <80-1; x++) {
                    if (y == 0 || y == (22) || x == 0 || x == (78)) {
                        tg.putString(startX + x, startY + y, "██");
                    }
                }
            }

            //screen.refresh();
            //screen.readInput();
            //screen.stopScreen();

            // Limites internos do quadrado
            int minX = 1;
            int maxX = 38;
            int minY = 1;
            int maxY = 21;

            // Inicializa a cobra (posição dentro do quadrado, multiplicando X por 2)
            LinkedList<int[]> snake = new LinkedList<>();

            // int initialX = startX + 2 * 1; // 1 célula dentro do quadrado
            //int initialY = startY + 1;     // 1 célula dentro do quadrado
            snake.add(new int[]{size / 2, size / 2}); // inicial no centro do quadrado

            // Direção inicial: direita
            int dx = 1; // cada passo no X deve ser 2 para acompanhar blocos duplos
            int dy = 0;

            // Inicializa a fruta
            Random rand = new Random();
            int fruitX = rand.nextInt(maxX - minX + 1) + minX;
            int fruitY = rand.nextInt(maxY - minY + 1) + minY;
            tg.putString(startX + fruitX * 2, startY + fruitY, "🍎"); // fruta


            // Desenha a cobra inicial
            for (int[] part : snake) {
                tg.putString(startX + part[0] * 2, startY + part[1], "██");
            }
            screen.refresh();

            /// /////////////
            LinkedList<int[]> obstacles = new LinkedList<>();// criar labirinto

            int numberOfObstacles = 6; // NÃO muito grande

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
            for (int[] obs : obstacles) {
                tg.putString(startX + obs[0] * 2, startY + obs[1], "██████");
            }

            ////////////////

            while (true) {
                Thread.sleep(200);

                // Lê input do teclado
                KeyStroke key = screen.pollInput();
                if (key != null) {
                    //KeyType type = key.getKeyType();
                    switch (key.getKeyType()) {
                        case ArrowUp -> { dx = 0; dy = -1; }
                        case ArrowDown -> { dx = 0; dy = 1; }
                        case ArrowLeft -> { dx = -1; dy = 0; }
                        case ArrowRight -> { dx = 1; dy = 0; }
                        case Escape -> { screen.stopScreen(); return; }
                    }
                }

                // Calcula nova cabeça
                int[] head = snake.getFirst();
                int newX = head[0] + dx;
                int newY = head[1] + dy;


                // Verifica colisão com paredes
                if ((newX < minX || newX > maxX )|| (newY < minY || newY > maxY)) {
                    screen.stopScreen();
                    System.out.println(newX+" # "+ minX);
                    System.out.println("Game Over! A cobra bateu na parede.");
                    return;
                }

                // Verifica colisão com a própria cobra

                for (int[] part : snake) {

                    if (part[0] == newX && part[1] == newY) {
                        screen.stopScreen();
                        System.out.println("Game Over! A cobra bateu nela mesma.");

                        return;
                    }

                }

                //vweifica colisao com obs
                for (int[] obs : obstacles) {
                    if (newX == obs[0] && newY == obs[1]) {
                        screen.stopScreen();
                        System.out.println("Game Over! A cobra bateu em um obstaculo.");
                        return;
                    }
                }
                /// ////

                // Remove o rabo da cobra
                int[] tail = snake.removeLast();
                //tg.putString(tail[0], tail[1], "  "); // apaga rabo
                tg.putString(startX + tail[0] * 2, startY + tail[1], "  ");


                // Adiciona nova cabeça
                // Adiciona nova cabeça
                snake.addFirst(new int[]{newX, newY});
                tg.putString(startX + newX * 2, startY + newY, "██");

                // Verifica se comeu a fruta
                if (newX == fruitX && newY == fruitY) {
                    // Cresce: NÃO remove o rabo (adiciona de volta)
                    snake.addLast(tail);

                    // Gera nova fruta
                    fruitX = rand.nextInt(maxX - minX + 1) + minX;
                    fruitY = rand.nextInt(maxY - minY + 1) + minY;
                    tg.putString(startX + fruitX * 2, startY + fruitY, "🍎");
                }
                // Se não comeu, o rabo já foi removido no início - não precisa fazer nada!

                ///   /////////////////////////////////////


                /////////////////////////////////////////////////////
                screen.refresh();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}