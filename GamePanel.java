import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final int UNIT_SIZE = 20;
    private static final int NUMBER_OF_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);

    private final int[] x = new int[NUMBER_OF_UNITS];
    private final int[] y = new int[NUMBER_OF_UNITS];
    private int length = 5;
    private int foodEaten;
    private int foodX;
    private int foodY;
    private char direction = 'D';
    private boolean running = false;
    private Random random;
    private Timer timer;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(new Color(34, 50, 34)); // Dark green background
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        play();
    }

    public void play() {
        addFood();
        running = true;

        timer = new Timer(150, this); // Slowed down the snake speed to 150 milliseconds
        timer.start();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void move() {
        for (int i = length; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (direction == 'L') {
            x[0] = x[0] - UNIT_SIZE;
        } else if (direction == 'R') {
            x[0] = x[0] + UNIT_SIZE;
        } else if (direction == 'U') {
            y[0] = y[0] - UNIT_SIZE;
        } else {
            y[0] = y[0] + UNIT_SIZE;
        }
    }

    public void checkFood() {
        if (x[0] == foodX && y[0] == foodY) {
            length++;
            foodEaten++;
            addFood();
        }
    }

    public void draw(Graphics graphics) {
        if (running) {
            graphics.setColor(new Color(210, 115, 90)); // Light brown food color
            graphics.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            graphics.setColor(new Color(40, 200, 150)); // Light green snake color
            for (int i = 0; i < length; i++) {
                graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            graphics.setColor(Color.white);
            graphics.setFont(new Font("Sans serif", Font.PLAIN, 20));
            graphics.drawString("Score: " + foodEaten, 10, 20); // Display score

        } else {
            gameOver(graphics);
        }
    }

    public void addFood() {
        foodX = random.nextInt((int) (WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        foodY = random.nextInt((int) (HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void checkHit() {
        for (int i = length; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }

        if (x[0] < 0 || x[0] > WIDTH || y[0] < 0 || y[0] > HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics graphics) {
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Sans serif", Font.BOLD, 50));
        FontMetrics metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);

        graphics.setColor(Color.white);
        graphics.setFont(new Font("Sans serif", Font.PLAIN, 25));
        metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + foodEaten, (WIDTH - metrics.stringWidth("Score: " + foodEaten)) / 2, HEIGHT / 2 + 50);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (running) {
            move();
            checkFood();
            checkHit();
        }
        repaint();
    }



    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;

                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;

                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;

            }
        }
    }
}
