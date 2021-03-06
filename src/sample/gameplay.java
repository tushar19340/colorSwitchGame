package sample;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class gameplay implements java.io.Serializable {
    private gameOverScreen gameOverScreen;
    transient private pauseScreen pauseScreen;
    transient private Pane root;
    transient private Scene scene;
    transient public Stage primaryStage;
    transient public Scene gameMain;
    transient private AnimationTimer animation;
    transient private Text scoreBoard;
    Player player;
    private ArrayList<gameElement> allObstacles;
    private ArrayList<rainbowBall> colorSwitchers;
    private ArrayList<star> stars;
    private double[] obstaclesPositions;
    private double[] switchersPositions;
    private double[] starsPositions;
    private double ballY;

    public gameplay(Stage primaryStage, Scene gameMain){
        this.primaryStage=primaryStage;
        this.gameMain=gameMain;
        this.gameOverScreen = new gameOverScreen(this);
        this.root = new Pane();
        player=new Player();
        this.colorSwitchers = new ArrayList<>();
        this.allObstacles = new ArrayList<>();
        this.stars = new ArrayList<>();



        createGameElements();
        startGame();
        this.pauseScreen = new pauseScreen(this);
        addPauseButton(primaryStage);
        this.scene = new Scene(root, 500, 700);
        root.setStyle("-fx-background-color: #272327;");

        scene.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.W) {
                animation.start();
                startObstacleAnimation();
                player.ball.setV(4.5f);
            }
        });

    }

    public void deSerialize(Stage primaryStage,Scene gameMain){
        this.primaryStage=primaryStage;
        this.gameMain=gameMain;
        this.gameOverScreen = new gameOverScreen(this);
        this.root = new Pane();
        player.deSerialize();
        player.ball.setCentreY(ballY);
        for(int i = 0; i < allObstacles.size(); i++){
            allObstacles.get(i).deSerialize();
            allObstacles.get(i).setLayoutY(obstaclesPositions[i]);
        }
        for(int i = 0; i < stars.size(); i++){
            stars.get(i).deSerialize();
            stars.get(i).setLayoutY(starsPositions[i]);
        }
        for (int i = 0; i < colorSwitchers.size(); i++){
            colorSwitchers.get(i).deSerialize();
            colorSwitchers.get(i).setLayoutY(switchersPositions[i]);
        }
        addPauseButton(primaryStage);
        this.scene = new Scene(root, 500, 700);
        root.setStyle("-fx-background-color: #272327;");
        startGame();
        this.pauseScreen = new pauseScreen(this);

        for (int i = 0; i < allObstacles.size(); i++){
            root.getChildren().add(allObstacles.get(i).getNode());
        }

        for(int i = 0; i < allObstacles.size(); i++){
            root.getChildren().addAll(colorSwitchers.get(i).getNode(), stars.get(i).getNode());
        }
        root.getChildren().addAll(player.ball.getBall());

        scene.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.W) {
                animation.start();
                startObstacleAnimation();
                player.ball.setV(4.5f);
            }
        });

    }
    public void startGame(){

        animation = new AnimationTimer() {
            @Override
            public void handle(long l) {

//      ---------------------- Moving obstacles down ----------------------

                if(player.ball.getLayoutY() < 350){
                    for(int i = 0; i < allObstacles.size(); i++){
                        allObstacles.get(i).move(2);
                    }
                    for (int i = 0; i < stars.size(); i++){
                        stars.get(i).move(2);
                    }
                    for(int i = 0; i < colorSwitchers.size(); i++){
                        colorSwitchers.get(i).move(2);
                    }
                }

//      ---------------------- Ball Bounce ----------------------
                player.ball.setV(player.ball.getV() - player.ball.getBallGravity());;
                player.jump(player.ball.getV());

                if(player.ball.getLayoutY() >= 650){
                    player.ball.setCentreY(650);
                }

//      ---------------------- Below is the implementation of infinite obstacles ----------------------

                int least = 0;
                double min = 999999;
                for(int i = 0; i < allObstacles.size(); i++){
                    if(allObstacles.get(i).getLayoutY() <= min){
                        least = i;
                        min = allObstacles.get(i).getLayoutY();
                    }
                }

                for(int i = 0; i < allObstacles.size(); i++){
                    if(allObstacles.get(i).getLayoutY() > 750){
                        double h1 = allObstacles.get(least).getNode().getBoundsInLocal().getHeight() / 2;
                        double h2 = allObstacles.get(i).getNode().getBoundsInLocal().getHeight() / 2;

                        allObstacles.get(i).setLayoutY(allObstacles.get(least).getLayoutY() - h1 - h2 - 200);
//                        colorSwitchers.remove(0);
//                        stars.remove(0);
//                        rainbowBall newRainbow = new rainbowBall();
//                        newRainbow.setLayoutY(allObstacles.get(i).getLayoutY() + h2 + 100);
//                        colorSwitchers.add(newRainbow);
//                        root.getChildren().add(newRainbow.getNode());
//
//                        star newStar = new star();
//                        newStar.setLayoutY(allObstacles.get(i).getLayoutY() - newStar.getNode().getLayoutBounds().getHeight()/2);
//
//                        stars.add(newStar);
//                        root.getChildren().add(newStar.getNode());

                        colorSwitchers.get(i).setLayoutY(allObstacles.get(i).getLayoutY() + h2 + 100);
                        stars.get(i).setLayoutY(allObstacles.get(i).getLayoutY() - stars.get(i).getNode().getLayoutBounds().getHeight()/2);
                        allObstacles.get(i).levelUp();

                    }
                }

//      ---------------------- Ball and rainbowBall collision ----------------------

                for(int i = 0; i < colorSwitchers.size(); i++){
                    Shape intersect = Shape.intersect((Shape) colorSwitchers.get(i).getNode().getChildren().get(2), player.ball.getBall());
                    if(intersect.getBoundsInLocal().getWidth() != -1){
                        player.ball.changeColour();
//                        root.getChildren().remove(colorSwitchers.get(i).getNode());
                        colorSwitchers.get(i).setLayoutY(2000);
                        System.out.println("Its intersection with color switcher!!!");
                    }
                }

//      ---------------------- Ball and star collision ----------------------
                for(int i = 0; i < stars.size(); i++){
                    if(player.ball.getBall().intersects(stars.get(i).getNode().getBoundsInParent())){
                        System.out.println("Its intersection with star!!!");
                        player.increaseScore();
                        updateScoreboard(String.valueOf(player.getScore()));
                        System.out.println("Player Score = " + player.getScore());
//                        root.getChildren().remove(stars.get(i).getNode());
                        stars.get(i).setLayoutY(2000);
                    }
                }
//      ---------------------- Ball and obstacles collision ----------------------
                for(int i = 0; i < allObstacles.size(); i++){
                        if(allObstacles.get(i).getLayoutY() > 0 && allObstacles.get(i).getLayoutY() < 700){
                            for (int j = 0; j < allObstacles.get(i).getNode().getChildren().size(); j++){
                                Shape intersect = Shape.intersect((Shape) allObstacles.get(i).getNode().getChildren().get(j), player.ball.getBall());
                                if(intersect.getBoundsInLocal().getHeight() > 12 && !allObstacles.get(i).getNode().getChildren().get(j).getId().equals(player.ball.getColor()) && !player.ball.getColor().equals("#FFFFFF")){
                                    System.out.println("Yippee!!!");
                                    System.out.println(!player.ball.getColor().equals("#FFFFFF"));
                                    gameOverScreen.setFinalScore(String.valueOf(player.getScore()));
                                    pauseGame();

                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    primaryStage.setScene(gameOverScreen.scene);
                                }
                            }
                    }
                }
            }
        };
    }

    public void serialize(){
        for(int i = 0; i < allObstacles.size(); i++){
            obstaclesPositions[i] = allObstacles.get(i).getLayoutY();
        }
        for(int i = 0; i < stars.size(); i++){
            starsPositions[i] = stars.get(i).getLayoutY();
        }
        for (int i = 0; i < colorSwitchers.size(); i++){
            switchersPositions[i] = colorSwitchers.get(i).getLayoutY();
        }
        ballY=player.ball.getLayoutY();
    }
    private void createGameElements(){
        root.getChildren().addAll(player.ball.getBall());

        allObstacles.add(new ring(0, 85, 15, 1));
        allObstacles.add(new doubleRing());
        allObstacles.add(new rectangle(0, 15, 170));
        allObstacles.add(new verticalLine());
        allObstacles.add(new plusObstacle());
        allObstacles.add(new tripleRing());
        allObstacles.add(new line());
        allObstacles.add(new circleRing());


        allObstacles.get(0).setLayoutY(350);
        root.getChildren().add(allObstacles.get(0).getNode());
        for (int i = 1; i < allObstacles.size(); i++){
            double h1 = allObstacles.get(i-1).getNode().getBoundsInLocal().getHeight() / 2;
            double h2 = allObstacles.get(i).getNode().getBoundsInLocal().getHeight() / 2;
            allObstacles.get(i).setLayoutY(allObstacles.get(i-1).getLayoutY() - h1 - h2 - 200);
            root.getChildren().add(allObstacles.get(i).getNode());
        }

        for(int i = 0; i < allObstacles.size(); i++){
            colorSwitchers.add(new rainbowBall());
            stars.add(new star());
            double h = allObstacles.get(i).getNode().getBoundsInLocal().getHeight() / 2;
            colorSwitchers.get(i).getNode().setLayoutY(allObstacles.get(i).getLayoutY() + h + 100);
            stars.get(i).setLayoutY(allObstacles.get(i).getLayoutY() - stars.get(i).getNode().getLayoutBounds().getHeight()/2);
            root.getChildren().addAll(colorSwitchers.get(i).getNode(), stars.get(i).getNode());

        }


        this.obstaclesPositions = new double[allObstacles.size()];
        this.switchersPositions = new double[colorSwitchers.size()];
        this.starsPositions = new double[stars.size()];

    }
    public void pauseGame(){
        animation.stop();
//        pauseObstacleAnimation();
    }

    public void restartGame(){
        player.ball.setCentreY(650);
        player.ball.makeItWhite();
        player.setScore(0);
        updateScoreboard("0");
        allObstacles.get(0).setLayoutY(350);
        for (int i = 1; i < allObstacles.size(); i++){
            double h1 = allObstacles.get(i-1).getNode().getBoundsInLocal().getHeight() / 2;
            double h2 = allObstacles.get(i).getNode().getBoundsInLocal().getHeight() / 2;
            allObstacles.get(i).setLayoutY(allObstacles.get(i-1).getLayoutY() - h1 - h2 - 200);
        }

        int diff = allObstacles.size() - colorSwitchers.size();
        for(int i = 0; i < diff; i++){
            colorSwitchers.add(new rainbowBall());
            this.root.getChildren().add(colorSwitchers.get(colorSwitchers.size() - 1).getNode());

        }

        diff = allObstacles.size() - stars.size();

        for(int i = 0; i < diff; i++){
            stars.add(new star());
            this.root.getChildren().add(stars.get(stars.size() - 1).getNode());

        }
        for(int i = 0; i < allObstacles.size(); i++){
            double h = allObstacles.get(i).getNode().getBoundsInLocal().getHeight() / 2;
            colorSwitchers.get(i).setLayoutY(allObstacles.get(i).getLayoutY() + h + 100);
            stars.get(i).setLayoutY(allObstacles.get(i).getLayoutY() - stars.get(i).getNode().getLayoutBounds().getHeight()/2);
        }
//        root.getChildren().add(stars.get(0).getNode());
//        root.getChildren().add(colorSwitchers.get(0).getNode());
//        root.getChildren().add(colorSwitchers.get(1).getNode());


    }
    private void addPauseButton(Stage primaryStage){
        StackPane root = new StackPane();
        Button pauseButton = new Button();
        pauseButton.setStyle(
                "-fx-background-radius: 5em;"  +
                        "-fx-min-width: 70px;" +
                        "-fx-min-height: 70px;"+
                        "-fx-max-width: 70px;" +
                        "-fx-max-height: 70px;"+
                        "-fx-background-color: transparent"
        );
        EventHandler<ActionEvent> pauseGame =
                e -> {
                    primaryStage.setScene(this.pauseScreen.scene);
                    pauseGame();
                };
        pauseButton.setOnAction(pauseGame);

        Image imagePause = new Image("file:assets/images/pauseButton.png");
        ImageView pauseImage = new ImageView(imagePause);
        pauseImage.setPreserveRatio(true);
        pauseImage.setFitWidth(70);
        root.setLayoutX(400);
        root.setLayoutY(30);
        root.getChildren().addAll(pauseImage, pauseButton);
        this.root.getChildren().add(root);

//      ------------- Adding score test ---------------

        scoreBoard = new Text("0");
        scoreBoard.setFill(Color.WHITE);
        scoreBoard.setStyle("-fx-font-size: 70;");
        scoreBoard.setLayoutX(50);
        scoreBoard.setLayoutY(70);
        this.root.getChildren().add(scoreBoard);



    }
    public void updateScoreboard(String score){
        this.scoreBoard.setText(score);
    }

    public void startObstacleAnimation(){
        for(int i = 0; i < allObstacles.size(); i++){
            allObstacles.get(i).startAnimation();
        }
    }

    public Scene getScene() {
        return scene;
    }
    public void changeSceneColor(){
        Random rand = new Random();
        scene.setFill(Color.RED);
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        root.setStyle("-fx-background-color: rgb(" + r + "," + g + ", " + b + ");");
    }

}