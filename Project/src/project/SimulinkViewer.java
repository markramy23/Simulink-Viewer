package project;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.InputSource;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SimulinkViewer extends Application {

   public void start(Stage primaryStage){ 
       Button button = new Button();
        FileChooser fileChooser = new FileChooser(); //file chooser to make user choose the desired file
        fileChooser.setTitle("Please choose your .mdl file");
         FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Simulink Model (MDL format)", "*.mdl"); //make only .mdl files allowed to be selected
         fileChooser.getExtensionFilters().add(filter);
        button.setOnAction(e -> { //setting the action of the button
        File selectedFile = fileChooser.showOpenDialog(primaryStage); //open file chooser
         if (selectedFile != null) { //make sure there is a file choosen
             try {
                  Stage newStage = new Stage(); //create new stage
                   List<Block> Blocks = new ArrayList<Block>(); //create list of blocks
                    List<Line> Lines = new ArrayList<Line>(); //create list of lines
                    String xml=ExtractBlock(selectedFile.getAbsolutePath()); //getting the desired part from the file choosen
                    Blocks=ParseXml(xml); //getting all blocks 
                    Lines=ParseLine(xml); //getting all lines
                    //make the window window size accou=rding to the user screen resolution
                     Screen screen = Screen.getPrimary();
                        Rectangle2D bounds = screen.getVisualBounds();
                        double screenWidth = bounds.getWidth();
                        double screenHeight = bounds.getHeight();
                        Pane pane = new Pane(); //create new pane for second window
                          pane.setPrefSize(screenWidth, screenHeight);
                          //loop throgh all blocks
                            for(Block b:Blocks){
                          Rectangle border=b.getBorder(); //get border of bloack    
                          Rectangle r=b.getRec(); //get the rectangle shape of the block
                          Text t=b.getText(); //get text to be written under the blocks

                          pane.getChildren().addAll(r,t,border); //add all to the pane
                      }
                   for(Line l:Lines){ //loop through all lines 
                       Block b=Block_get(l.get_Src_Id(), Blocks); //get the source block of the line from the block list
                       Point2D[] points=b.getoutput(); //get output ports the block
                       //specify which port and get it's coordinates
                       double x=points[(l.get_src_port())-1].getX(); 
                       double y=points[(l.get_src_port())-1].getY();
                       Polyline pline=new Polyline(x,y);//make new polyline with this start point
                       if(l.get_points()!=null){ //get line points if exists
                           points=l.get_points();
                           //addp points to the start point and make it the next point in the polyline
                           for(int i=0;i<points.length;i++){
                               x+=points[i].getX();
                               y+=points[i].getY();
                               pline.getPoints().addAll(x,y); 
                           }
                       }
                       if(l.getDst()!=null){ //get dst if exists and add the dst point to the polyline
                           b=Block_get(l.get_dst_id(), Blocks);
                           points=b.getinput();
                           x=points[l.get_dst_port()-1].getX();
                           y=points[l.get_dst_port()-1].getY();
                           pline.getPoints().addAll(x,y);
                       }
                       if(l.getBranches()!=null){ //get line branches
                           List<Branch> branches=l.getBranches();
                           for(Branch br:branches){ //loop through all branches
                                Polygon arrow=new Polygon(); //to draw the arrow at the end of each branch
                               Polyline brline=new Polyline(); //to draw the line of the branch
                               double bx;
                               double by;
                               if(br.getPoints()!=null){ //get points of the branch
                                Point2D[] brpoints=br.get_points();
                                //add the branch points to the end point of the line 
                           for(int i=0;i<brpoints.length;i++){
                                bx=x+brpoints[i].getX();
                                by=y+brpoints[i].getY();

                               brline.getPoints().addAll(x,y,bx,by); //branch line with starting from the end of the line and ending with the branch point
                           }

                               }
                               if(br.getDst()!=null){ //get branch destination
                               b=Block_get(br.get_dst_id(), Blocks); //distnation block of the branch
                              Point2D[] dstp=b.getinput(); //input ports of the branch
                              //get the point of distnation
                               bx=dstp[br.get_dst_port()-1].getX(); 
                               by=dstp[br.get_dst_port()-1].getY();
                               //if branch has points draw the arrow at the dst point after adding the points of the branch to polyline
                               if(br.getPoints()!=null){
                                    
                               brline.getPoints().addAll(bx,by);
                               if(b.getBmirror()==null) //mirror the arrow if block is mirrorred
                               arrow.getPoints().addAll(bx-7,by-7,bx,by,bx-7,by+7);
                               else
                                   arrow.getPoints().addAll(bx+7,by-7,bx,by,bx+7,by+7);
                               }
                               //if no points then add arrow to dst position and add the end point of the line as the start point of the branch
                               else{
                                   brline.getPoints().addAll(x,y,bx,by);
                                    if(b.getBmirror()==null)
                               arrow.getPoints().addAll(bx-7,by-7,bx,by,bx-7,by+7);
                               else
                                   arrow.getPoints().addAll(bx+7,by-7,bx,by,bx+7,by+7);
                                   
                                       }
                               }
                                brline.setStroke(Color.BLUE);
                                brline.setStrokeWidth(2);
                              
                               pane.getChildren().addAll(arrow,brline);
                                arrow.toFront(); //make arrow appear on front of line
                           }
                       }
                       pline.setStroke(Color.BLUE);
                       pline.setStrokeWidth(2);
                       if(l.getBranches()==null){ //if there is no branches add the arrow at the end of the line 
                       Polygon arrow=new Polygon(x-7,y-7,x,y,x-7,y+7);
                       pane.getChildren().addAll(arrow,pline);
                       arrow.toFront();
                       }
                       else
                            pane.getChildren().addAll(pline); //add line to pane
                   }
                             Scene scene = new Scene(pane); //make new scene for the pane
                            newStage.setTitle("mdl Reader"); // Set the stage title
                            newStage.getIcons().add(new Image(getClass().getResourceAsStream("/Images/icon.png")));
                            newStage.setScene(scene); // Place the scene in the stage
                            newStage.show(); 
                 
             } catch (Exception ex) {
                 System.out.println(ex.getMessage());
             }
            
             
         }

        });
         Pane pane = new Pane(); //create new pane for the primary window
         //change button style
         button.setLayoutX(170);
         button.setLayoutY(300);
         button.setStyle("-fx-min-width: 50px; -fx-min-height: 60px; -fx-max-width: 50px; -fx-max-height: 50px;");
         
         pane.getChildren().addAll(button); //add buttons and images 
          Image logo = new Image(getClass().getResourceAsStream("/Images/logo.png"));
          ImageView imageView = new ImageView(logo);
          imageView.setLayoutX(50);
          imageView.setLayoutY(30);
          imageView.setFitHeight(200);
          imageView.setFitWidth(300);
          pane.getChildren().add(imageView);
          Image icon = new Image(getClass().getResourceAsStream("/Images/icon.png"));
          Image buttonpic=new Image(getClass().getResourceAsStream("/Images/button.png"));
          ImageView buttonview=new ImageView(buttonpic);
          buttonview.setLayoutX(170);
          buttonview.setLayoutY(300);
          buttonview.setFitHeight(60);
          buttonview.setFitWidth(50);
          pane.getChildren().add(buttonview);
        Scene scene = new Scene(pane, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulink Viewer"); 
        primaryStage.getIcons().add(icon);
        primaryStage.setResizable(false);
        primaryStage.show();
   }
    public static void main(String[] args) {
        launch(args);
    }
            public static List<Block> ParseXml(String xml) throws Exception { //method for parsing Block from String given
    List<Block> Blocks = new ArrayList<Block>(); //create list of blocks
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    InputSource is = new InputSource(new StringReader(xml));
    Document document = builder.parse(is);
    document.getDocumentElement().normalize();
    NodeList nList = document.getElementsByTagName("Block"); // making a node list of the blocks
    for (int temp = 0; temp < nList.getLength(); temp++) { //loop through the list to extract data
     org.w3c.dom.Node node = nList.item(temp);
      if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
        Element eElement = (Element) node;
        Block Block = new Block();
        Block.setName(eElement.getAttribute("Name")); //getting block name
        Block.setSID(eElement.getAttribute("SID")); //getting block SID
        NodeList taglist=eElement.getElementsByTagName("P"); //making list for subelemets in each block
        String tagname;
        for(int j=0; j<taglist.getLength();j++){
          org.w3c.dom.Node tagnode=taglist.item(j);
           if(tagnode.getNodeType()==org.w3c.dom.Node.ELEMENT_NODE){
               Element tagElement = (Element) tagnode;
               tagname=tagElement.getAttribute("Name");
             if(tagname.equals("Position")) //getting position
                Block.setPosition(tagElement.getTextContent());
             if(tagname.equals("Ports")) //getting ports
                Block.setI_O(tagElement.getTextContent());
             if(tagname.equals("BlockMirror"))  //getting BlockMirrir property
                 Block.setBmirror(tagElement.getTextContent());
           }
             
        }
        //Add Block to list
        Blocks.add(Block);
      }
    }
    return Blocks;
}
     public static String ExtractBlock(String filepath) throws Exception{ //To extract only the needed part of the mdl file and return it as a string
         String file=Files.readString(Paths.get(filepath));
         StringBuffer filee=new StringBuffer(file);
         file=filee.substring(filee.lastIndexOf("system_root.xml"),filee.lastIndexOf("__MWOPC_PART_BEGIN__ /simulink/windowsInfo.xml")); //start and end of the needed partition
         file=file.substring(16);
         return file;
     }
     
    public static List<Line> ParseLine(String xml) throws Exception { //to parse line 
        List<Line> Lines = new ArrayList<Line>(); //creating list of lines 
                   

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        Document document = builder.parse(is);
        document.getDocumentElement().normalize();
        NodeList nList = document.getElementsByTagName("Line"); //making list of line elements
        for (int temp = 0; temp < nList.getLength(); temp++) {
         org.w3c.dom.Node node = nList.item(temp);
          if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
              List<Branch> b=new ArrayList<Branch>(); //create a list of branches for each element
            Element eElement = (Element) node;
            Line l = new Line(); //create new line object
            NodeList taglist=eElement.getElementsByTagName("P");//getting elements "p" of the line
          NodeList branchlist=eElement.getElementsByTagName("Branch"); //getting branches of the line
            String tagname;
            for(int j=0; j<4;j++){ //loop through the "p" elements to get the src,Dst,and points and setting them to the line object
                if(taglist.item(j)!=null){
              org.w3c.dom.Node tagnode=taglist.item(j);
               if(tagnode.getNodeType()==org.w3c.dom.Node.ELEMENT_NODE){
                   Element tagElement = (Element) tagnode;
                   tagname=tagElement.getAttribute("Name");
                 if(tagname.equals("Src"))
                    l.setSrc(tagElement.getTextContent());
                 if(tagname.equals("Dst"))
                    l.setDst(tagElement.getTextContent());
                 if(tagname.equals("Points"))
                     l.setPoints(tagElement.getTextContent());
               }
                }
            }
            for(int k=0; k<branchlist.getLength();k++){ //loop through all branches 
              org.w3c.dom.Node branchnode=branchlist.item(k);
               if(branchnode.getNodeType()==org.w3c.dom.Node.ELEMENT_NODE){
                   Element branchElement = (Element) branchnode;
                   Branch branch=new Branch(); //new branch object
                   NodeList branchtag =branchElement.getElementsByTagName("P"); //get "p" elements of the branch
                   for(int i=0;i<3;i++){ //loop through the p elements list 
                       if(branchtag.item(i)!=null){ //get p elements to get pounts and dst of branch
                org.w3c.dom.Node tagnode=branchtag.item(i);
                if(tagnode.getNodeType()==org.w3c.dom.Node.ELEMENT_NODE){
                    Element tagElement = (Element) tagnode;
                   tagname=tagElement.getAttribute("Name");
                 if(tagname.equals("Points"))
                    branch.setPoints(tagElement.getTextContent());
                 if(tagname.equals("Dst"))
                    branch.setDst(tagElement.getTextContent());
                   
                
                }
                }
                 
                   }
                  b.add(branch); //add branch object to the list of branches
               }
               l.setBranches(b); //set the list to the current line
            }
                           
            Lines.add(l); //add the line to the list of lines
               }
            
            
          }
          return Lines;  
        }
     
    public static Block Block_get(int SID,List<Block> blocks){ //to get the block of the line from a list of block according to the SID
        Block found=new Block();
        for(Block b:blocks){
            if(b.getSID()==SID){
                found=b;
            break;
            }
        }
        
        return found;
    } 

    
}
