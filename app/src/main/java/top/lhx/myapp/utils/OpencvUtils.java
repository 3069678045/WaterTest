package top.lhx.myapp.utils;

import android.os.Environment;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.DescriptorMatcher;

import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.xfeatures2d.SURF;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class OpencvUtils {

    // 模板匹配
    public void templateMatch(){

    }

    /**
     * SURF特征匹配
     * @param templateImage     模板图
     * @param originalImage     原图
     */
    public Point surfMatch(Mat templateImage, Mat originalImage){

        Mat imgObject = templateImage;
        Mat imgScene = originalImage;

        if (imgObject.empty() || imgScene.empty()) {
            System.err.println("Cannot read images!");
            System.exit(0);
        }

        //-- Step 1: Detect the keypoints using SURF Detector, compute the descriptors
        double hessianThreshold = 400;
        int nOctaves = 4, nOctaveLayers = 3;
        boolean extended = false, upright = false;
        org.opencv.xfeatures2d.SURF detector = org.opencv.xfeatures2d.SURF.create(hessianThreshold, nOctaves, nOctaveLayers, extended, upright);
        MatOfKeyPoint keypointsObject = new MatOfKeyPoint(), keypointsScene = new MatOfKeyPoint();
        Mat descriptorsObject = new Mat(), descriptorsScene = new Mat();
        detector.detectAndCompute(imgObject, new Mat(), keypointsObject, descriptorsObject);
        detector.detectAndCompute(imgScene, new Mat(), keypointsScene, descriptorsScene);

        //-- Step 2: Matching descriptor vectors with a FLANN based matcher
        // Since SURF is a floating-point descriptor NORM_L2 is used
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
        List<MatOfDMatch> knnMatches = new ArrayList<MatOfDMatch>();
        matcher.knnMatch(descriptorsObject, descriptorsScene, knnMatches, 2);

        //-- Filter matches using the Lowe's ratio test
        float ratioThresh = 0.75f;
        List<DMatch> listOfGoodMatches = new ArrayList<DMatch>();
        for (int i = 0; i < knnMatches.size(); i++) {
            if (knnMatches.get(i).rows() > 1) {
                DMatch[] matches = knnMatches.get(i).toArray();
                if (matches[0].distance < ratioThresh * matches[1].distance) {
                    listOfGoodMatches.add(matches[0]);
                }
            }
        }
        if(listOfGoodMatches==null || listOfGoodMatches.size()==0 || listOfGoodMatches.size() < 100){
            System.out.println("没有命中...");
            return null;
        }
        MatOfDMatch goodMatches = new MatOfDMatch();
        goodMatches.fromList(listOfGoodMatches);

        //-- Draw matches
        Mat imgMatches = new Mat();
        Features2d.drawMatches(imgObject, keypointsObject, imgScene, keypointsScene, goodMatches, imgMatches, Scalar.all(-1),
                Scalar.all(-1), new MatOfByte(), Features2d.DrawMatchesFlags_NOT_DRAW_SINGLE_POINTS);

        //-- Localize the object
        List<Point> obj = new ArrayList<Point>();
        List<Point> scene = new ArrayList<Point>();

        List<KeyPoint> listOfKeypointsObject = keypointsObject.toList();
        List<KeyPoint> listOfKeypointsScene = keypointsScene.toList();
        for (int i = 0; i < listOfGoodMatches.size(); i++) {
            //-- Get the keypoints from the good matches
            obj.add(listOfKeypointsObject.get(listOfGoodMatches.get(i).queryIdx).pt);
            scene.add(listOfKeypointsScene.get(listOfGoodMatches.get(i).trainIdx).pt);
        }

        MatOfPoint2f objMat = new MatOfPoint2f(), sceneMat = new MatOfPoint2f();
        objMat.fromList(obj);
        sceneMat.fromList(scene);
        double ransacReprojThreshold = 3.0;
        Mat H = Calib3d.findHomography( objMat, sceneMat, Calib3d.RANSAC, ransacReprojThreshold );

        //-- Get the corners from the image_1 ( the object to be "detected" )
        Mat objCorners = new Mat(4, 1, CvType.CV_32FC2), sceneCorners = new Mat();
        float[] objCornersData = new float[(int) (objCorners.total() * objCorners.channels())];
        objCorners.get(0, 0, objCornersData);
        objCornersData[0] = 0;
        objCornersData[1] = 0;
        objCornersData[2] = imgObject.cols();
        objCornersData[3] = 0;
        objCornersData[4] = imgObject.cols();
        objCornersData[5] = imgObject.rows();
        objCornersData[6] = 0;
        objCornersData[7] = imgObject.rows();
        objCorners.put(0, 0, objCornersData);

        Core.perspectiveTransform(objCorners, sceneCorners, H);
        float[] sceneCornersData = new float[(int) (sceneCorners.total() * sceneCorners.channels())];
        sceneCorners.get(0, 0, sceneCornersData);

        //-- Draw lines between the corners (the mapped object in the scene - image_2 )
        Imgproc.line(imgMatches, new Point(sceneCornersData[0] + imgObject.cols(), sceneCornersData[1]),
                new Point(sceneCornersData[2] + imgObject.cols(), sceneCornersData[3]), new Scalar(0, 255, 0), 9);
        Imgproc.line(imgMatches, new Point(sceneCornersData[2] + imgObject.cols(), sceneCornersData[3]),
                new Point(sceneCornersData[4] + imgObject.cols(), sceneCornersData[5]), new Scalar(0, 255, 0), 7);
        Imgproc.line(imgMatches, new Point(sceneCornersData[4] + imgObject.cols(), sceneCornersData[5]),
                new Point(sceneCornersData[6] + imgObject.cols(), sceneCornersData[7]), new Scalar(0, 255, 0), 5);
        Imgproc.line(imgMatches, new Point(sceneCornersData[6] + imgObject.cols(), sceneCornersData[7]),
                new Point(sceneCornersData[0] + imgObject.cols(), sceneCornersData[1]), new Scalar(0, 255, 0), 2);
        float x=sceneCornersData[0] +(sceneCornersData[2]-sceneCornersData[0])/2;
        float y= sceneCornersData[3]+(sceneCornersData[5]-sceneCornersData[3])/2;
        List<Point> imgPoints= new ArrayList<>();
        imgPoints.add(new Point(sceneCornersData[0] + imgObject.cols(), sceneCornersData[1]));
        imgPoints.add(new Point(sceneCornersData[2] + imgObject.cols(), sceneCornersData[3]));
        imgPoints.add(new Point(sceneCornersData[4] + imgObject.cols(), sceneCornersData[5]));
        imgPoints.add(new Point(sceneCornersData[6] + imgObject.cols(), sceneCornersData[7]));
        PointUtil  pp=new PointUtil();
        Point p=pp.getCenterOfIMGPoint(imgPoints);
        Imgproc.line(imgMatches, p,
                new Point(p.x+1, p.y+1), new Scalar(0, 255, 0), 16);
        Imgcodecs.imwrite(Environment.getExternalStorageDirectory() + "/newMatching.jpg",imgMatches);
        //-- Show detected matches
       // HighGui.imshow("Good Matches & Object detection", imgMatches);
        //HighGui.waitKey(0);
        System.out.println("x="+x+"     y="+y);

        return new Point(p.x- imgObject.cols(), p.y);
    }
}
