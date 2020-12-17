package sample;
import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class rectangle extends gameElement {
    private Line line1;
    private Line line2;
    private Line line3;
    private Line line4;
    private int height;
    Group square;
    private RotateTransition rotate;

    public rectangle(int height,int width ,int length , int direction) {
        this.height = height;
        line1 = new Line();
        line1.setStartX(165);
        line1.setStartY(height - length/2);
        line1.setEndX(165);
        line1.setEndY(height - length/2+length);
        line1.setFill(Color.TRANSPARENT);
        line1.setStrokeWidth(width);
        line1.setStyle("-fx-stroke-line-cap: round");
        line1.setStroke(Color.web("#FAE100"));
        line1.setId("#FAE100");

        line2 = new Line();
        line2.setStartX(165);
        line2.setStartY(height - length/2+length);
        line2.setEndX(165+length);
        line2.setEndY(height - length/2+length);
        line2.setFill(Color.TRANSPARENT);
        line2.setStrokeWidth(width);
        line2.setStyle("-fx-stroke-line-cap: round");
        line2.setStroke(Color.web("#900DFF"));
        line2.setId("#900DFF");

        line3 = new Line();
        line3.setStartX(165);
        line3.setStartY(height - length/2);
        line3.setEndX(165+length);
        line3.setEndY(height - length/2);
        line3.setFill(Color.TRANSPARENT);
        line3.setStrokeWidth(width);
        line3.setStyle("-fx-stroke-line-cap: round");
        line3.setStroke(Color.web("#FF0181"));
        line3.setId("#FF0181");

        line4 = new Line();
        line4.setStartX(165+length);
        line4.setStartY(height - length/2);
        line4.setEndX(165+length);
        line4.setEndY(height - length/2+length);
        line4.setFill(Color.TRANSPARENT);
        line4.setStrokeWidth(width);
        line4.setStyle("-fx-stroke-line-cap: round");
        line4.setStroke(Color.web("#32DBF0"));
        line4.setId("#32DBF0");

        square = new Group();
        square.getChildren().addAll(line1,line2,line3,line4);
        rotate = new RotateTransition();
        rotate.setNode(square);
        rotate.setDuration(Duration.seconds(3));

        if(direction == 0){
            rotate.setByAngle(360);
        }else{
            rotate.setByAngle(-360);
        }

        rotate.setCycleCount(Timeline.INDEFINITE);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.play();
    }

    public int getHeight() {
        return height;
    }

    public void moveDownBy(int x){

    }

    @Override
    void move(double y) {
        square.setLayoutY(square.getLayoutY() + y);
    }

    @Override
    Group getNode() {
        return square;
    }

    @Override
    void setLayoutY(double y) {
        square.setLayoutY(y);
    }

    @Override
    double getLayoutY() {
        return square.getLayoutY();
    }

    @Override
    void levelUp() {

    }

    @Override
    void startAnimation() {
        rotate.play();
    }

    @Override
    void pauseAnimation() {
        rotate.pause();
    }
}






