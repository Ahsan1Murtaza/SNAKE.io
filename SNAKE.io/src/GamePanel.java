import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{

    static final int SCREEN_WIDTH = 550;
    static final int SCREEN_HEIGHT = 550;
    static final int UNIT_SIZE = 50;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    static final int delay = 175;
    char direction = 'R';
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 5;
    int applesEaten;
    int appleX;
    int appleY;
    Timer timer;
    Random random;
    boolean running = false;

    // Constructor
    public GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    // Start the game
    public void startGame(){
        newApple();
        running = true;
        x[0] = ((int)(SCREEN_WIDTH / UNIT_SIZE) / 2) * UNIT_SIZE; // Starts from Center
        y[0] = ((int)(SCREEN_HEIGHT / UNIT_SIZE) / 2) * UNIT_SIZE; // Starts from Center
        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public void paintComponent(Graphics g) {
       super.paintComponent(g);
        draw(g);
    }


    public void draw(Graphics g){
        if(running){
            for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }
            // Setting apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // Setting snake
            for (int i = 0; i < bodyParts; i++){
                if (i == 0){
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    g.setColor(new Color(166, 211, 162));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score : " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score : " + applesEaten)) / 2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
    }

    // Game Over interface
    public void gameOver(Graphics g){
        // Score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score : " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score : " + applesEaten)) / 2, g.getFont().getSize());

        // Game Over text
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over !", (SCREEN_WIDTH - metrics2.stringWidth("Game Over !")) / 2, SCREEN_HEIGHT / 2);

        // Restart Button
        JButton restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Arial", Font.PLAIN, 20));
        restartButton.setBounds((SCREEN_WIDTH / 2) - 50, SCREEN_HEIGHT / 2, 100, 40);
        restartButton.addActionListener(e -> restartGame());
        this.add(restartButton);
    }

    // generating new apple
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    // Moving Snake
    public void move(){
        for (int i = bodyParts; i > 0; i--){
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction){
            case 'U' :
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D' :
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'R' :
                x[0] = x[0] + UNIT_SIZE;
                break;
            case 'L' :
                x[0] = x[0] - UNIT_SIZE;
                break;
        }
    }

    // Detecting apple with snake
    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    // Checking Collisions
    public void checkCollisions(){
        for (int i = bodyParts; i > 0; i--){
            if (x[0] == x[i] && y[0] == y[i]){
                running = false;
            }
            if (x[0] < 0 || x[0] > SCREEN_WIDTH || y[0] < 0 || y[0] > SCREEN_HEIGHT){
                running = false;
            }
            if (!running){
                timer.stop();
            }
        }
    }

    // KeyAdapter
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_RIGHT :
                    if (direction != 'L'){
                        direction = 'R';
                    }
                    break;

                case KeyEvent.VK_LEFT :
                    if (direction != 'R'){
                        direction = 'L';
                    }
                    break;

                case KeyEvent.VK_UP :
                    if (direction != 'D'){
                        direction = 'U';
                    }
                    break;

                case KeyEvent.VK_DOWN :
                    if (direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }

    // Restart Game
    public void restartGame(){
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (parentFrame != null){
            parentFrame.dispose();
        }
        new GameFrame();
    }

}
