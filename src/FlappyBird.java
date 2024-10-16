import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class FlappyBird extends JPanel implements ActionListener , KeyListener {
    int boardHeight = 640;
    int boardWidth = 360 ;

    //Images
    Image backgroundImg ;
    Image birdImg ;
    Image topPipeImg ;
    Image bottomPipeImg;

    //Bird
    int birdX = boardWidth/8 ;
    int birdY = boardHeight/2 ;
    int birdWidth = 34 ;
    int birdHeight = 24 ;

    class Bird {
        int x = birdX;
        int y = birdY ;
        int width = birdWidth ;
        int height = birdHeight ;
        Image img ; 

        public Bird(Image img) {
            this.img = img ; 
        }

        
    }
    Bird bird ;

    //pipes
    int pipeX = boardWidth ;
    int pipeY = 0 ;
    int pipeWidth =64 ;
    int pipeHeight = 512 ;

    class Pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight ;
        Image img ;
        boolean passed =false ;

        public Pipe(Image img) {
            this.img = img ;
        }

        

    }
    
    //game logic 
    int velocityY = 0 ;
    int velocityX = -4 ;
    int gravity = 1 ;

    ArrayList<Pipe> pipes ;

    Timer gameloop ;
    Timer placepipTimer ;
    Random random = new Random();

    boolean gameOver = false ;


    public FlappyBird() {
        setPreferredSize( new Dimension(boardWidth , boardHeight) );
        //setBackground(Color.BLUE);
        setFocusable(true);
        addKeyListener(this);

        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        bird =new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        placepipTimer = new Timer(1500 ,new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                placePipe();
            }
        });
        placepipTimer.start();

        gameloop = new Timer(1000/60 , this);
        gameloop.start();
    }
    public void placePipe(){

        int randomPipeY = (int)(pipeY -pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight/4 ;
        Pipe toPipe = new Pipe(topPipeImg);
        toPipe.y = randomPipeY ;
        pipes.add(toPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = toPipe.y + pipeHeight + openingSpace ;
        pipes.add(bottomPipe);
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //draw the background
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);
        // draw the image
        g.drawImage(birdImg, bird.x, bird.y, bird.width, bird.height,null);

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img,pipe.x,pipe.y,pipe.width,pipe.height, null);
        }
    }

    public void move (){
        //bird
        velocityY += gravity ;
        bird.y += velocityY ;
        bird.y = Math.max(bird.y , 0);

        //moving the pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX ;

            if (collision(bird, pipe)) {
                gameOver = true;
            }
        }  
        if (bird.y > boardHeight){
            gameOver = true ;
        }  
       

    }
    boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
               a.x + a.width > b.x &&   //a's top right corner passes b's top left corner
               a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
               a.y + a.height > b.y;    //a's bottom left corner passes b's top left corner
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        move();
        repaint();

        if (gameOver){
            placepipTimer.stop();
            gameloop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
       
    
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -9 ;
        }
       
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
    }
    
}
