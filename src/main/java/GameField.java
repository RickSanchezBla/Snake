import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    private final int SIZE = 320;                   // Кол-во заданных точек
    private final int DOT_SIZE = 16;                // Размер одной нашей точки
    private final int ALL_DOTS = 400;               // Максимальное кол-во точек

    private Image dot;
    private Image apple;

    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];

    private int  appleX;                             // Координаты яблока
    private int  appleY;                             // Координаты яблока
    private int  appleX2;                            // Координаты яблока
    private int  appleY2;                            // Координаты яблока
    private int  appleX3;                            // Координаты яблока
    private int  appleY3;                            // Координаты яблока

    private int dots;                                // Кол-во звеньев нашей змеи
    private Timer timer;                             // fps

    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;

    private boolean inGame = true;                   // Выход из программы

    public void loadImage(){
        ImageIcon iia = new ImageIcon("apple.png");
        apple = iia.getImage();
        ImageIcon iid = new ImageIcon("dot.png");
        dot = iid.getImage();
    }

    public  void  createApple() {                        //Метод диапозона генерации координат "яблока"
        Random random = new Random();               //Сделайте так, чтобы метод create apple был ориентирован на появление сразу 3 яблок. ДЗ
        appleX = random.nextInt(20) * DOT_SIZE;
        appleY = random.nextInt(20) * DOT_SIZE;
    }
    public  void  createApple2() {
        Random random2 = new Random();
        appleX2 = random2.nextInt(20) * DOT_SIZE;
        appleY2 = random2.nextInt(20) * DOT_SIZE;
        }
    public  void  createApple3(){
        Random random3 = new Random();
        appleX3 = random3.nextInt(20) * DOT_SIZE;
        appleY3 = random3.nextInt(20) * DOT_SIZE;
    }



    public void initGame(){                             //Метод для начального расположения и движения змейки + ее увеличение при съедании яблока
        dots = 3;
        for (int i= 0; i<dots; i++){
            y[i] = 48;
            x[i] = 48 - i * DOT_SIZE;
        }
        timer = new Timer(150, this);
        timer.start();
        createApple();
        createApple2();
        createApple3();
    }

    public void checkApple(){                           //Перепишите метод check apple так, чтобы он был рассчитан на 3 яблока, которые появляются одновременно. ДЗ
        if (x[0] == appleX && y[0] == appleY) {
            dots++;
            createApple();
        }
        if (x[0] == appleX2 && y[0] == appleY2) {
            dots++;
            createApple2();
            }
        if (x[0] == appleX3 && y[0] == appleY3){
            dots++;
            createApple3();
        }
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if (inGame){
            g.drawImage(apple, appleX, appleY,this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot, x[i], y[i],this);
            }
            g.drawImage(apple, appleX2, appleY2,this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot, x[i], y[i],this);
            }
            g.drawImage(apple, appleX3, appleY3,this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot, x[i], y[i],this);
            }
        }
        else {
            String str = "Game over";
            g.setColor(Color.cyan);
            g.drawString(str,SIZE/6, SIZE/2);
        }
    }

    public void checkCollision(){
        for (int i = dots; i >0; i--) {
            if (x[0] == x[i] && y[0] == y[i]){
                inGame = false;
            }                                       //Сделайте так, чтобы при попадании в верхнюю или нижнюю грань, змейка умирала. ДЗ
        }                                           //Сделайте так, чтобы в методе checkCollision змейка при столкновении с верхней границей появлялась в нижней и наоборот. ДЗ
        if (x[0]>SIZE)                          //transparent walls
            x[0] = 0;
        if (x[0]<0)
            x[0] = SIZE;

//        if (x[0]> SIZE)                       //solid walls
//            inGame = false;
//        if (x[0]<0)
//            inGame = false;

        if (y[0]> SIZE)                         //transparent walls
            y[0] = 0;
        if (y[0]<0)
            y[0] = SIZE;

//        if (y[0]> SIZE)                       //solid walls
//            inGame = false;
//        if (y[0]<0)
//            inGame = false;
    }
    @Override
    public void actionPerformed(ActionEvent a){
        if (inGame){
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    public GameField(){
        setBackground(Color.BLACK);
        loadImage();
        initGame();
        addKeyListener(new FiledKeyListener());
        setFocusable(true);
    }

    public void move(){
        for (int i = dots; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if (left)
            x[0] -= DOT_SIZE;
        if (right)
            x[0] += DOT_SIZE;
        if (up)
            y[0] -= DOT_SIZE;
        if (down)
            y[0] += DOT_SIZE;
    }

    class FiledKeyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent k){
            super.keyPressed(k);
            int key = k.getKeyCode();

            if (key == KeyEvent.VK_LEFT && !right){
                left = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_RIGHT && !left){
                right = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_UP && !down){
                up = true;
                left = false;
                right = false;
            }
            if (key == KeyEvent.VK_DOWN && !up){
                down = true;
                left = false;
                right = false;
            }
        }
    }
}
