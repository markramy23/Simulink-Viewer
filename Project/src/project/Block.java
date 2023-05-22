/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.*;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Mark
 */
public class Block {
    private String Name; //block name
    private String Position; //block position
    private String SID; //SID for lines
    private Text text; //tect to be shown under block rectangle
    private Rectangle rec; //rectangle representation for the block
    private String I_O; // input/output of the block
    private String Bmirror; //mirror property 

    public Block() {
    }

    public Block(String Name, String Position, String SID) {
        this.Name = Name;
        this.Position = Position;
        this.SID = SID;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setPosition(String Position) {
        this.Position = Position;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }

    public void setBmirror(String Bmirror) {
        this.Bmirror = Bmirror;
    }

    public void setI_O(String I_O) {
        this.I_O = I_O;
    }

    public String getName() {
        return Name;
    }

    public String getPosition() {
        return Position;
    }

    public int getSID() {
        return Integer.valueOf(SID);
    }

    public String getBmirror() {
        return Bmirror;
    }

    @Override
    public String toString() {
        return "Block{" + "Name=" + Name + ", Position=" + Position + ", SID=" + SID +" ,I_O="+I_O+" ,mirror="+Bmirror+ '}'+"\n";
    }
    private int[] ConvertPosition(){   //return the first two numbers in the position as integer
        int[] positions=new int[2];
        StringBuffer pos=new StringBuffer(Position);
        pos.deleteCharAt(0);
        pos.deleteCharAt(pos.length()-1);
        String[] pixels= pos.toString().split(", ");
        positions[0]=Integer.valueOf(pixels[0]);
        positions[1]=Integer.valueOf(pixels[1]);
        return positions;
    }

    public Rectangle getRec() {  //draw the rectangle of the block
        rec=new Rectangle(this.ConvertPosition()[0],this.ConvertPosition()[1],40,40);
         rec.setStroke(Color.BLACK);
             rec.setStrokeWidth(4);
             rec.setFill(null);
             rec.setArcWidth(5);
             rec.setArcHeight(5);             
        return rec;
    }

    public Text getText() {  //text to be displayed under the block
            Rectangle rect=this.getRec();
            text=new Text(Name);
            text.setStroke(Color.BLUE);
            text.setTranslateX(rect.getX() + 40 / 2 - text.getLayoutBounds().getWidth() / 2);
            text.setTranslateY(rect.getY() + 40+ 20);

        return text;
    }
    public Rectangle getBorder(){ //drawing the border of the rectangle
        Rectangle rec=this.getRec();
        Rectangle border = new Rectangle(rec.getX()-5, rec.getY()-5, 50, 50);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.AQUA);
        border.setStrokeWidth(3);
        border.setArcWidth(10);
        border.setArcHeight(10);
        return border;
    }
    private int[] getI_O(){ //return an array of integers containing number of input ports and number of output ports
        int[] input=new int[2]; //if no I_O given then return 1 input and one output only
        if(I_O==null||I_O.length()<4){
            input[0]=1;
            input[1]=1;
            return input;
        }
        else{   //else return inputs in index 0 and outputs in index 1
        StringBuffer i_o=new StringBuffer(I_O);
        i_o.deleteCharAt(0);
        i_o.deleteCharAt(i_o.length()-1);
        String[] pixels= i_o.toString().split(", ");
         input[0]=Integer.valueOf(pixels[0]);
         input[1]=Integer.valueOf(pixels[1]);
         return input;
        }
    }
    public Point2D[] getinput(){   //return all input points of block
        if(Bmirror==null){ //if block is mirrorred reverse the position of input ports
          
        Point2D[] inputs=new Point2D[this.getI_O()[0]];
        Rectangle rect=this.getRec();
        if(I_O==null){
           inputs[0]=new Point2D(rect.getX(), rect.getY()+20);
        }
        else{
         int input=this.getI_O()[0]; //get the number of inputs
         if(input==1)
             inputs[0]=new Point2D(rect.getX(), rect.getY()+20); //if 1 input then make in the middle of the block
         else{
         for(int i=0;i<inputs.length;i++){ //else distripute inputs along the side of the block
             double x=rect.getX();
             double inc =(double)40/input;
             double y=rect.getY()+inc*i+7;
             if(i==inputs.length-1)
                 y=rect.getY()+inc*i+5;
             inputs[i]=new Point2D(x,y);
         }
         }
        }
        return inputs;
        }
                Point2D[] inputs=new Point2D[this.getI_O()[1]]; //same for nonmirrorred blocks
        Rectangle rect=this.getRec();
        if(I_O==null){
           inputs[0]=new Point2D(rect.getX()+40, rect.getY()+20);
        }
        else{
         int input=this.getI_O()[0];
         for(int i=0;i<inputs.length;i++){
             double x=rect.getX()+40;
             double inc =(double)40/input;
             double y=rect.getY()+inc*i+7;
              if(i==inputs.length-1)
                 y=rect.getY()+inc*i+5;
             inputs[i]=new Point2D(x,y);
         }
        }
        return inputs;
    }
        public Point2D[] getoutput(){ //same for output ports
            if(Bmirror==null){
        Point2D[] outputs=new Point2D[this.getI_O()[1]];
        Rectangle rect=this.getRec();
        if(I_O==null){
           outputs[0]=new Point2D(rect.getX()+40, rect.getY()+20);
        }
        else{
         int output=this.getI_O()[1];
         if(output==1)
             outputs[0]=new Point2D(rect.getX()+40, rect.getY()+20);
         else{
         for(int i=0;i<outputs.length;i++){
             outputs[i]=new Point2D(rect.getX()+40, rect.getY()+(i/output));
         }
         }
        }
        return outputs;
            }
                
           Point2D[] outputs=new Point2D[this.getI_O()[0]];
        Rectangle rect=this.getRec();
        if(I_O==null){
           outputs[0]=new Point2D(rect.getX(), rect.getY()+20);
        }
        
        else{
            
         int output=this.getI_O()[1];
         for(int i=0;i<outputs.length;i++){
             outputs[i]=new Point2D(rect.getX(), rect.getY()+(40/output)*i);
         }
        }
        return outputs;
 
    }

}
