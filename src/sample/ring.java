package sample;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.util.Duration;

public class ring extends gameElement {
    transient private Group ring;
    private int height;
    private int radius;
    private int width;
    private int direction;
    transient private RotateTransition rotate;

    public ring(int height, int radius, int width, int direction){
        this.height = height;
        this.radius = radius;
        this.width = width;
        this.direction = direction;
        this.speed = 3000;
        deSerialize();
    }
    @Override
    public void deSerialize(){
        this.rotate = new RotateTransition();

        Arc arc1 = new Arc(250, height, radius, radius, 0, 90);
        Arc arc2 = new Arc(250, height, radius, radius, 90, 90);
        Arc arc3 = new Arc(250, height, radius, radius, 180, 90);
        Arc arc4 = new Arc(250, height, radius, radius, 270, 90);

        arc1.setType(ArcType.OPEN);
        arc1.setFill(Color.TRANSPARENT);
        arc1.setStrokeWidth(width);
        arc1.setStroke(Color.web("#FAE100"));
        arc1.setId("#FAE100");

        arc2.setType(ArcType.OPEN);
        arc2.setFill(Color.TRANSPARENT);
        arc2.setStrokeWidth(width);
        arc2.setStroke(Color.web("#900DFF"));
        arc2.setId("#900DFF");

        arc3.setType(ArcType.OPEN);
        arc3.setFill(Color.TRANSPARENT);
        arc3.setStrokeWidth(width);
        arc3.setStroke(Color.web("#FF0181"));
        arc3.setId("#FF0181");

        arc4.setType(ArcType.OPEN);
        arc4.setFill(Color.TRANSPARENT);
        arc4.setStrokeWidth(width);
        arc4.setStroke(Color.web("#32DBF0"));
        arc4.setId("#32DBF0");

        ring = new Group();
        ring.getChildren().addAll(arc1, arc2, arc3, arc4);

        rotate = new RotateTransition();
        rotate.setNode(ring);

        rotate.setDuration(Duration.millis(speed));

        if(direction == 0){
            rotate.setByAngle(360);
        }else{
            rotate.setByAngle(-360);
        }
        rotate.setCycleCount(Timeline.INDEFINITE);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.play();


    }

    @Override
    public void setLayoutY(double y){
        ring.setLayoutY(y);
    }
    @Override
    public double getLayoutY() {
        return ring.getLayoutY();
    }

    @Override
    public void startAnimation(){

    }
    @Override
    public void pauseAnimation(){
        rotate.pause();
    }

    @Override
    public Group getNode(){
        return ring;
    }

    @Override
    public void move(double y){
        ring.setLayoutY(ring.getLayoutY() + y);
    }

    @Override
    public void levelUp(){
        rotate.stop();
        rotate.setNode(ring);
        speed -= 100;
        rotate.setDuration(Duration.millis(speed));
        rotate.play();
    }

}