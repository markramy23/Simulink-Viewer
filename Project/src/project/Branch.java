/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project;

import javafx.geometry.Point2D;

/**
 *
 * @author Mark
 */
public class Branch {
    private String points; //branch points
    private String dst; //branch dst

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    @Override
    public String toString() {
        return "Branch{" + "points=" + points + ", dst=" + dst + '}';
    }
    
       public int get_dst_id(){ //get dst block SId
       if(dst!=null)
       return Integer.valueOf(dst.substring(0, dst.indexOf("#")));
       return 0;
   }
   
   public int get_dst_port(){ //get dst BLock port number
       return Integer.valueOf(dst.substring(dst.indexOf(":")+1,dst.length()));
   }

       public Point2D[] get_points(){ //get branch points as 2D points
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

}
