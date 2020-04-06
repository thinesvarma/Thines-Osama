package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.animation.AnimationTimer;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;





public class Main extends Application {

    static int speed = 2; //to declare speed
    static int foodColor = 0; //to declare food color
    static int width = 40; // to declare width
    static int height = 27; //to declare height
    static int foodX = 0; //to declare 1st type of food
    static int foodY = 0; //to declare 2nd type of food
    static int cornersize = 25;
    static List<Corner> snake = new ArrayList<>();
    static Dir dir = Dir.left;
    static boolean gameOver = false;
    static Random rand = new Random();


    public static class Corner {
        int x; //initializing corner x
        int y; //initializing corner y

        public Corner(int x, int y) {
            this.x = x; //arguments
            this.y = y; //arguments
        }
    }

    public enum Dir {
        left, right, up, down
    }

    public void start(Stage primaryStage) {

        primaryStage.setTitle("CATERPILLAR");

        try {
            newFood();
            VBox root = new VBox(); // layout component which positions all its child nodes (components) in a vertical row.
            Canvas canvas = new Canvas(width*cornersize, height*cornersize );
            //Canvas is an image that can be drawn on using a set of
            // graphics commands provided by a GraphicsContext .
            GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
            root.getChildren().add(canvas);

            new AnimationTimer() {
                long lastTick = 0;

                public void handle(long now) {
                    if (lastTick == 0) {
                        lastTick = now;
                        tick(graphicsContext);
                        return;
                    }

                    if (now - lastTick > 1000000000 / speed) {
                        lastTick = now;
                        tick(graphicsContext);
                    }
                }

            }.start();



            Scene scene = new Scene(root, width*cornersize, height*cornersize);

            // control the snake
            scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
                if (key.getCode() == KeyCode.UP) {
                    dir = Dir.up;
                }
                if (key.getCode() == KeyCode.DOWN) {
                    dir= Dir.down;
                }
                if (key.getCode() == KeyCode.LEFT) {
                    dir = Dir.left;
                }
                if (key.getCode() == KeyCode.RIGHT) {
                    dir = Dir.right;
                }

            });

            // add start snake parts
            //static List<Corner> snake = new ArrayList<>();
            snake.add(new Corner(width / 2, height / 2));
            snake.add(new Corner(width / 2, height / 2));
            snake.add(new Corner(width / 2, height / 2));

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            //prints stacktrace for this Throwable Object
            e.printStackTrace();
        }
    }



    // tick
    public static void tick(GraphicsContext graphicsContext) {
        if (gameOver) {
            graphicsContext.setFill(Color.RED);
            graphicsContext.setFont(new Font("", 50));
            graphicsContext.fillText("GAME OVER", 100, 250);
            return;
        }

        for (int i = snake.size() - 1; i >= 1; i--) {
            snake.get(i).x = snake.get(i - 1).x;
            snake.get(i).y = snake.get(i - 1).y;
        }

        switch (dir) {
            case up:
                snake.get(0).y--;
                if (snake.get(0).y < 0) {
                    gameOver = true;
                }
                break;
            case down:
                snake.get(0).y++;
                if (snake.get(0).y > height) {
                    gameOver = true;
                }
                break;
            case left:
                snake.get(0).x--;
                if (snake.get(0).x < 0) {
                    gameOver = true;
                }
                break;
            case right:
                snake.get(0).x++;
                if (snake.get(0).x > width) {
                    gameOver = true;
                }
                break;

        }

        // eat food and get points
        if (foodX == snake.get(0).x && foodY == snake.get(0).y) {
            snake.add(new Corner(-1, -1));
            newFood();
        }

        // self destroy after banging any corner of the game
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
                gameOver = true;
            }
        }

        // fill
        // background
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, width * cornersize, height * cornersize);

        // score
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setFont(new Font("", 30));
        graphicsContext.fillText("Score: " + (speed - 3), 10, 30);

        // random foodcolor
        Color color = Color.WHITE;

        switch (foodColor) {
            case 0:
                color = Color.YELLOW;
                break;
            case 1:
                color = Color.BLUE;
                break;
            case 2:
                color = Color.ORANGE;
                break;
            case 3:
                color = Color.GREEN;
                break;
            case 4:
                color = Color.BROWN;
                break;
        }
        graphicsContext.setFill(color);
        graphicsContext.fillOval(foodX * cornersize, foodY * cornersize, cornersize, cornersize);

        // snake
        //static List<Corner> snake = new ArrayList<>();
        for (Corner c : snake) {
            graphicsContext.setFill(Color.YELLOW);
            graphicsContext.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 1, cornersize - 1);
            graphicsContext.setFill(Color.LAVENDER);
            graphicsContext.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 2, cornersize - 2);

        }

    }

    // food
    public static void newFood() {
        start: while (true) {
            foodX = rand.nextInt(width);
            foodY = rand.nextInt(height);

            for (Corner c : snake) {
                if (c.x == foodX && c.y == foodY) {
                    continue start;
                }
            }
            foodColor = rand.nextInt(5);
            speed++;
            break;

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}