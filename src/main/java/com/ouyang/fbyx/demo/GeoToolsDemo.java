package com.ouyang.fbyx.demo;


import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import com.vividsolutions.jts.util.GeometricShapeFactory;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.GeometryBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author ouyangxingjie
 * @Description
 * @Date 9:22 2021/12/24
 */
public class GeoToolsDemo {


    public static void main(String []args) throws Exception{
        String wktPoint = "POINT(103.83489981581 33.462715497945)";
        String wktLine = "LINESTRING(108.32803893589 41.306670233001,99.950999898452 25.84722546391)";
        String wktPolygon = "POLYGON((100.02715479879 32.168082192159,102.76873121104 37.194305614622,107.0334056301 34.909658604412,105.96723702534 30.949603786713,100.02715479879 32.168082192159))";
        String wktPolygon1 = "POLYGON((96.219409781775 32.777321394882,96.219409781775 40.240501628236,104.82491352023001 40.240501628236,104.82491352023001 32.777321394882,96.219409781775 32.777321394882))";
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );
        WKTReader reader = new WKTReader( geometryFactory );
        Point point = (Point) reader.read(wktPoint);
        LineString line = (LineString) reader.read(wktLine);
        Polygon polygon = (Polygon) reader.read(wktPolygon);
        Polygon polygon1 = (Polygon) reader.read(wktPolygon1);
        System.out.println("-------空间关系判断-------");
        System.out.println(polygon.contains(point));
        System.out.println(polygon.disjoint(point));
        System.out.println(polygon.intersects(line));
        System.out.println(polygon.overlaps(polygon1));
        System.out.println(polygon.touches(polygon1));


       /* System.out.println("\r\n-------空间计算-------");
        WKTWriter write = new WKTWriter();
        Geometry intersection = polygon.union( polygon1 );
        Geometry union = polygon.union( polygon1 );
        Geometry difference = polygon.difference( polygon1 );
        Geometry symdifference = polygon.symDifference( polygon1 );
        System.out.println("\t+++++++++++叠加分析+++++++++++");
        System.out.println(write.write(intersection));
        System.out.println("\t+++++++++++合并分析+++++++++++");
        System.out.println(write.write(union));
        System.out.println("\t+++++++++++差异分析+++++++++++");
        System.out.println(write.write(difference));
        System.out.println("\t+++++++++++sym差异分析+++++++++++");
        System.out.println(write.write(symdifference));*/


        // buffer宽度 1.5公里
       /* Double distance = 1.5 * 180 / Math.PI / 6371000;
        GeometryFactory geoFactory = new GeometryFactory();
        WKTReader reader = new WKTReader(geoFactory);
        // 两个线段
        String str = "LINESTRING(100 0,101 0,100 1,100 0)";

        // 字符串转Geometry类型
        Geometry inner = reader.read(str);
        // 线段扩充buffer成多边形
        Geometry outer =  inner.buffer(distance);
        System.out.println(outer);
        System.out.println(inner.within(outer));

        // 设置保留6位小数，否则GeometryJSON默认保留4位小数
        GeometryJSON geometryJson = new GeometryJSON(4);
        StringWriter writer = new StringWriter();
        geometryJson.write(outer, writer);
        System.out.println(writer.toString());
        StringWriter writer1 = new StringWriter();
        geometryJson.write(inner,writer1);
        System.out.println(writer1.toString());
        writer.close();
        System.out.println(inner.getBoundary());
        System.out.println(outer.getBoundary());*/

       /* Double distance = 1.5 * 180 / Math.PI / 6371000;
        GeometryBuilder geometryBuilder = new GeometryBuilder();
        Polygon circle = geometryBuilder.circle(116.411743,39.954548,1,50);
        GeometryJSON geometryJson = new GeometryJSON(20);
        StringWriter writer = new StringWriter();
        geometryJson.write(circle, writer);
        System.out.println(writer.toString());
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );
        WKTReader reader = new WKTReader( geometryFactory );
        String wktPolygon = "POLYGON((100.02715479879 32.168082192159,102.76873121104 37.194305614622,107.0334056301 34.909658604412,105.96723702534 30.949603786713,100.02715479879 32.168082192159))";
        Polygon polygon = (Polygon) reader.read(wktPolygon);
        StringWriter writer1 = new StringWriter();
        geometryJson.write(polygon, writer1);
        System.out.println(writer1);
        System.out.println(polygon.contains(circle));

        String str1 = "POINT(112.57 50.91)";
        String str2 = "POINT(113.40 50.69)";
        Point point1 = (Point) reader.read(str1);
        Point point2 = (Point) reader.read(str2);
        System.out.println(point1.distance(point2));
        // 84坐标系构造GeodeticCalculator
        GeodeticCalculator geodeticCalculator = new GeodeticCalculator(DefaultGeographicCRS.WGS84);
        // 起点经纬度
        geodeticCalculator.setStartingGeographicPoint(112.57,50.91);
        // 末点经纬度
        geodeticCalculator.setDestinationGeographicPoint(113.40,50.69);
        // 计算距离，单位：米
        double orthodromicDistance = geodeticCalculator.getOrthodromicDistance();
        System.out.println(orthodromicDistance);


        //点的缓冲区
        Point point3 = (Point) reader.read("POINT(116.411743 39.954548)");
        Geometry pointBuffer = point3.buffer(distance);
        StringWriter writer2 = new StringWriter();
        geometryJson.write(pointBuffer, writer2);
        System.out.println(writer2);*/

        /*GeometryFactory geoFactory = new GeometryFactory();
        WKTReader reader = new WKTReader(geoFactory);
        Circle c = new Circle();
        //第一个圆
        String cStr =c.createWktCircle(116.411743,39.954548,100,50);
        Geometry geometry = reader.read(cStr);
        StringWriter writer = new StringWriter();
        GeometryJSON geometryJson = new GeometryJSON(20);
        geometryJson.write(geometry, writer);
        System.out.println(writer);
        //第二个圆
        String cStr1 =c.createWktCircle(116.511743,39.944548,1,50);
        Geometry geometry1 = reader.read(cStr1);
        StringWriter writer1 = new StringWriter();
        GeometryJSON geometryJson1 = new GeometryJSON(20);
        geometryJson1.write(geometry1, writer1);
        System.out.println(writer1);
        //两个圆的关系
        //相等(Equals)：几何形状拓扑上相等。
        System.out.println("两个圆是否相等："+geometry.equals(geometry1));
        //脱节(Disjoint)：几何形状没有共有的点。
        System.out.println("两个圆是否脱节："+geometry.disjoint(geometry1));
        //相交(Intersects)：几何形状至少有一个共有点（区别于脱节）
        System.out.println("两个圆是否相交："+geometry.intersects(geometry1));
        //接触(Touches)：几何形状有至少一个公共的边界点，但是没有内部点。
        System.out.println("两个圆是否相触："+geometry.touches(geometry1));
        //交叉(Crosses)：几何形状共享一些但不是所有的内部点。
        System.out.println("两个圆是否交叉："+geometry.crosses(geometry1));
        //内含(Within)：几何形状A的线都在几何形状B内部。
        System.out.println("两个圆是否内含："+geometry.within(geometry1));
        //包含(Contains)：几何形状B的线都在几何形状A内部（区别于内含）。
        System.out.println("两个圆是否包含："+geometry.contains(geometry1));
        //重叠(Overlaps)：几何形状共享一部分但不是所有的公共点，而且相交处有他们自己相同的区域。
        System.out.println("两个圆是否重叠："+geometry.overlaps(geometry1));
*/

        int arr [] = new int[]{2,4,5,1,6,7,9,10,3,8};
        int max = arr[0];
        int min = arr[0];
        int sum = 0;
        for(int i = 0;i<arr.length;i++){
            if(arr[i]>max){
                max = arr[i];
            }
            if(arr[i]<min){
                min = arr[i];
            }
            sum += arr[i];
        }
        System.out.println("max = "+max);
        System.out.println("min = "+min);
        System.out.println("sum = "+sum);
        double avg = new BigDecimal(sum - max - min).divide(new BigDecimal(arr.length-2),2, RoundingMode.HALF_UP).doubleValue();
        System.out.println("avg = "+avg);
        ThreadPoolTaskExecutor e = new ThreadPoolTaskExecutor();

    }

/*
    public  GeoJsonPolygon createRadiusPolygon(  Point point,double RADIUS) {

        GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
        shapeFactory.setNumPoints(32);
        shapeFactory.setCentre(new com.vividsolutions.jts.geom.Coordinate(point.getX(), point.getY()));
        shapeFactory.setSize(RADIUS * 2);
        com.vividsolutions.jts.geom.Geometry circle = shapeFactory.createCircle();
        List<Point> points = new ArrayList<Point>();
        for (com.vividsolutions.jts.geom.Coordinate coordinate : circle.getCoordinates()) {
            Point lngLatAtl = new Point(coordinate.x, coordinate.y);
            points.add(lngLatAtl);
        }
        Collections.reverse(points);
        return new GeoJsonPolygon(points);
    }*/
}
