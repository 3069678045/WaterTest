package top.lhx.myapp.utils;

import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.List;
/**
 * 坐标以及电子围栏相关的工具类
 * @author
 *
 */
public class PointUtil {
    
    public static void main(String[] args) {
        String str="114.316587,30.671626#114.325921,30.683437#114.342122,30.689342#114.359009,30.695246#114.377066,30.692884#114.393304,30.678417#114.412289,30.650953#114.426107,30.625253#114.433703,30.595853#114.444368,30.560458#114.466705,30.527415#114.457264,30.517327#114.454002,30.503097#114.458465,30.474631#114.430828,30.466313#114.397697,30.460362#114.343281,30.456778#114.305086,30.457354#114.269101,30.468743#114.23449,30.489598#114.182713,30.522872#114.17296,30.535364#114.167328,30.55022#114.165783,30.575754#114.167155,30.591715#114.161492,30.606935#114.160461,30.619049#114.170589,30.627469#114.206123,30.623481#114.233932,30.629832#114.184837,30.704361#114.187927,30.776472#114.233589,30.780122#114.238567,30.717035#114.248609,30.66835#114.284744,30.659813#";

    }
     /**
      * 传入字符串类型的坐标组；经纬度“，”分隔；坐标之间“#”分隔；以“#”好结束
      * @return
      */
     public static boolean is(String str,double x,double y){
         List<xyPoint> list = getxyUtil(str);
         xyPoint[] ps2 = new xyPoint[list.size()];
         for(int i=0;i<list.size();i++){     
             ps2[i]=list.get(i);
         }     
         xyPoint n1 = new xyPoint(x,y);
         if(isPtInPoly(n1.getX() , n1.getY() , ps2)){     
             return true;
         }
         return false;
     }
     /**
      * 传入经度，维度；和需要判断的多边形坐标组
      * @param ALon
      * @param ALat
      * @param ps
      * @return
      */
        public static boolean isPtInPoly (double ALon , double ALat , xyPoint[] ps) {
            int iSum, iCount, iIndex;  
            double dLon1 = 0, dLon2 = 0, dLat1 = 0, dLat2 = 0, dLon;  
            if (ps.length < 3) {  
                return false;  
            }  
            iSum = 0;  
            iCount = ps.length;  
            for (iIndex = 0; iIndex<iCount;iIndex++) {  
                if (iIndex == iCount - 1) {  
                    dLon1 = ps[iIndex].getX();  
                    dLat1 = ps[iIndex].getY();  
                    dLon2 = ps[0].getX();  
                    dLat2 = ps[0].getY();  
                } else {  
                    dLon1 = ps[iIndex].getX();  
                    dLat1 = ps[iIndex].getY();  
                    dLon2 = ps[iIndex + 1].getX();  
                    dLat2 = ps[iIndex + 1].getY();  
                }  
                // 以下语句判断A点是否在边的两端点的水平平行线之间，在则可能有交点，开始判断交点是否在左射线上  
                if (((ALat >= dLat1) && (ALat < dLat2)) || ((ALat >= dLat2) && (ALat < dLat1))) {  
                    if (Math.abs(dLat1 - dLat2) > 0) {  
                        //得到 A点向左射线与边的交点的x坐标：  
                        dLon = dLon1 - ((dLon1 - dLon2) * (dLat1 - ALat) ) / (dLat1 - dLat2);  
                        // 如果交点在A点左侧（说明是做射线与 边的交点），则射线与边的全部交点数加一：  
                        if (dLon < ALon) {  
                            iSum++;  
                        }  
                    }  
                }  
            }  
            if ((iSum % 2) != 0) {  
                return true;  
            }  
            return false;  
        }  
        /**
         * 传入 坐标组；转为集合；114.288221,30.593268#114.321866,30.580559#114.311223,30.560015#
         * @param str
         * @return
         */
        public static List<xyPoint> getxyUtil(String str){
            List<xyPoint> list = new ArrayList<>();
            while (str.indexOf("#")!=-1) {
                String str1 = str.substring(0, str.indexOf("#"));//40.01,40.12
                xyPoint pd = new xyPoint(Double.valueOf(str1.substring(0, str1.indexOf(","))),Double.valueOf(str1.substring(str1.indexOf(",")+1)));
                list.add(pd);
                str=str.substring(str.indexOf("#")+1);
            }    
            while (str.indexOf(";")!=-1) {
                String str1 = str.substring(0, str.indexOf(";"));//40.01,40.12
                xyPoint pd = new xyPoint(Double.valueOf(str1.substring(0, str1.indexOf(","))),Double.valueOf(str1.substring(str1.indexOf(",")+1)));
                list.add(pd);
                str=str.substring(str.indexOf(";")+1);
            }    
            return list;
        }

    /**
     * 获取图像中多边形的中心像素点的位置
     * @param mPoints
     * @return
     */
    public static Point getCenterOfIMGPoint(List<Point> mPoints ) {


        double area = 0.0;//多边形面积
        double Gx = 0.0, Gy = 0.0;// 重心的x、y
        for (int i = 1; i <= mPoints.size(); i++) {
            double iLat = mPoints.get(i % mPoints.size()).x;
            double iLng = mPoints.get(i % mPoints.size()).y;
            double nextLat = mPoints.get(i - 1).x;
            double nextLng = mPoints.get(i - 1).y;
            double temp = (iLat * nextLng - iLng * nextLat) / 2.0;
            area += temp;
            Gx += temp * (iLat + nextLat) / 3.0;
            Gy += temp * (iLng + nextLng) / 3.0;
        }
        Gx = Gx / area;
        Gy = Gy / area;
        Point data = new Point(Gx,Gy);

        return data;
    }

        /**
         * 获取多边形的中心
         * @param str
         * @return
         */
        public static xyPoint getCenterOfGravityPoint(String str ) {

             List<xyPoint> mPoints = getxyUtil(str);
            double area = 0.0;//多边形面积
            double Gx = 0.0, Gy = 0.0;// 重心的x、y
            for (int i = 1; i <= mPoints.size(); i++) {
                double iLat = mPoints.get(i % mPoints.size()).getX();
                double iLng = mPoints.get(i % mPoints.size()).getY();
                double nextLat = mPoints.get(i - 1).getX();
                double nextLng = mPoints.get(i - 1).getY();
                double temp = (iLat * nextLng - iLng * nextLat) / 2.0;
                area += temp;
                Gx += temp * (iLat + nextLat) / 3.0;
                Gy += temp * (iLng + nextLng) / 3.0;
            }
            Gx = Gx / area;
            Gy = Gy / area;
            xyPoint data = new xyPoint(Gx,Gy);

            return data;
        }

    
}

class  xyPoint {
    private Double x;
    private Double y;
    public xyPoint (Double x , Double y) {
        this.x = x;
        this.y = y;
    }
    public Double getX() {
        return x;
    }
    public void setX(Double x) {
        this.x = x;
    }
    public Double getY() {
        return y;
    }
    public void setY(Double y) {
        this.y = y;
    }
}