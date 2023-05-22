/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project;

import java.util.List;
import javafx.geometry.Point2D;

/**
 *
 * @author Mark
 */
public class Line {
    private String points; //line points
    private String src; //line block source
    private String dst; //line dst source
    private List<Branch> branches; //branches

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

   public int get_Src_Id(){ //get SID of src block as int
       return Integer.valueOf(src.substring(0, src.indexOf("#")));
   }
   public int get_src_port(){ //get src port number  
       return Integer.valueOf(src.substring(src.length()-1));
   }
   public int get_dst_id(){ //get dst SID
       if(dst!=null)
       return Integer.valueOf(dst.substring(0, dst.indexOf("#")));
       return 0;
   }
   
   public int get_dst_port(){ //get dst port number
       return Integer.valueOf(dst.substring(dst.indexOf(":")+1,dst.length()));
   }
   public Point2D[] get_points(){ //return points as an array of 2D points 
      if(points!=null){
            
            String[] rows = points.substring(1, points.length() - 1).split(";");

            int[] array = new int[rows.length * rows[0].split(",").length];

            int index = 0;
            for (String row : rows) {
                String[] values = row.trim().split(",");
                for (String value : values) {
                    array[index++] = Integer.parseInt(value.trim());
                }
            }
            Point2D[] poi=new Point2D[array.length/2];
            for(int i=0;i<poi.length;i++){
                poi[i]=new Point2D(array[i*2],array[i*2+1]);
            }
            return poi;
      }
            return null;
   }

    @Override
    public String toString() {
        return "Line{" + "points=" + points + ", src=" + src + ", dst=" + dst +branches+"\n";
    }
    
}
