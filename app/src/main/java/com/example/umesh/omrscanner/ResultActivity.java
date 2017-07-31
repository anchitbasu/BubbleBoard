package com.example.umesh.omrscanner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.opencv.core.CvType.CV_32F;
import static org.opencv.core.CvType.CV_8UC1;
import static org.opencv.core.CvType.CV_8UC4;
import static org.opencv.imgproc.Imgproc.COLOR_GRAY2RGB;
import static org.opencv.imgproc.Imgproc.boundingRect;
import static org.opencv.imgproc.Imgproc.contourArea;

public class ResultActivity extends AppCompatActivity {

    private ImageView url,url1;
    int largest_contour_index;
    Double largest_area=0.0;
    Rect bounding_rect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        url=(ImageView) findViewById(R.id.imageView);

        url1=(ImageView) findViewById(R.id.transform);

        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.omr_test_01);

        Bitmap bmp= BitmapFactory.decodeResource(getResources(), R.drawable.omr_test_01);

        Mat rgba = new Mat(bitmap.getWidth(), bitmap.getHeight(), CV_8UC4);
        Utils.bitmapToMat(bitmap, rgba);

        Mat grey=new Mat(bitmap.getWidth(), bitmap.getHeight(), CV_8UC4);

        Mat edge=new Mat(bitmap.getWidth(), bitmap.getHeight(), CV_8UC4);

        Mat blur=new Mat(bitmap.getWidth(), bitmap.getHeight(), CV_8UC4);

        Imgproc.cvtColor(rgba,grey,Imgproc.COLOR_RGB2GRAY);

        Imgproc.GaussianBlur(grey,blur,new Size(5,5),0);

        Imgproc.Canny(blur,edge,75,200);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();

        Imgproc.findContours(edge,contours,hierarchy,Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint2f approxCurve = new MatOfPoint2f();
        MatOfPoint temp_contour = contours.get(0);

        for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++){

            temp_contour = contours.get(contourIdx);
            double area = contourArea(contours.get(contourIdx));  //  Find the area of contour

            if( area > largest_area ) {
                MatOfPoint2f new_mat = new MatOfPoint2f(temp_contour.toArray());
                double approxDistance = Imgproc.arcLength(new_mat, true) * 0.02;
                int contourSize = (int)temp_contour.total();
                MatOfPoint2f approxCurve_temp = new MatOfPoint2f();
                largest_area = area;
                largest_contour_index = contourIdx;               //Store the index of largest contour
                bounding_rect = boundingRect(contours.get(contourIdx)); // Find the bounding rectangle for biggest contour

                Imgproc.approxPolyDP(new_mat, approxCurve_temp, approxDistance,true);

                if (approxCurve_temp.total() == 4) {
                    approxCurve = approxCurve_temp;
                }
            }
        }

        //Draw rectangle for the detected countour on the original image
//        Imgproc.rectangle(rgba, new Point(bounding_rect.x, bounding_rect.y), new Point(bounding_rect.x + bounding_rect.width, bounding_rect.y + bounding_rect.height),
//                new Scalar(255, 0, 255, 255), 2);
//        Imgproc.drawContours(rgba, contours, largest_contour_index, new Scalar(0,255,0),4);


        double[] temp_double;

//        FindTest.Quadrilateral fq=FindTest.findDocument(rgba);
//        MatOfPoint contr=fq.contour;
//        Point[] point=fq.points;
//
//        Log.v("Test",bounding_rect.x+" "+ bounding_rect.y+" "+bounding_rect.br()+" "+bounding_rect.tl()+" "+bounding_rect.width+" "+bounding_rect.height);
//
//
//        temp_double = approxCurve.get(0,0);
//        Point p1 = new Point(temp_double[0], temp_double[1]);
//        Log.v("P1",temp_double[0]+" "+temp_double[1]);
//
//        temp_double = approxCurve.get(1,0);
//        Point p2 = new Point(bounding_rect.x, bounding_rect.width+bounding_rect.x);
//        Log.v("P2",temp_double[0]+" "+temp_double[1]);
//
//        temp_double = approxCurve.get(2,0);
//        Point p3 = bounding_rect.br();
//        Log.v("P3",temp_double[0]+" "+temp_double[1]);
//
//        temp_double = approxCurve.get(3,0);
//        Point p4 = new Point(bounding_rect.width+bounding_rect.y, bounding_rect.y);
//        Log.v("P4",temp_double[0]+" "+temp_double[1]);

//        double[] tl=approxCurve.get(0,0);
//        double[] bl=approxCurve.get(1,0);
//        double[] br=approxCurve.get(2,0);
//        double[] tr=approxCurve.get(3,0);

        temp_double = approxCurve.get(0,0);
        Point p1 = new Point(temp_double[0], temp_double[1]);
        Log.v("P1",temp_double[0]+" "+temp_double[1]);
        Imgproc.circle(rgba,p1,8,new Scalar(0,255,0),-1);

        temp_double = approxCurve.get(1,0);
        Point p2 = new Point(temp_double[0], temp_double[1]);
        Log.v("P2",temp_double[0]+" "+temp_double[1]);
        Imgproc.circle(rgba,p2,8,new Scalar(255,168,0),-1);


        temp_double = approxCurve.get(2,0);
        Point p3 = new Point(temp_double[0], temp_double[1]);
        Log.v("P3",temp_double[0]+" "+temp_double[1]);
        Imgproc.circle(rgba,p3,8,new Scalar(255,0,0),-1);


        temp_double = approxCurve.get(3,0);
        Point p4 = new Point(temp_double[0], temp_double[1]);
        Log.v("P4",temp_double[0]+" "+temp_double[1]);
        Imgproc.circle(rgba,p4,8,new Scalar(0,255,255),-1);


        List<Point> source = new ArrayList<Point>();
        source.add(p1);
        source.add(p2);
        source.add(p3);
        source.add(p4);
        Mat startM = Converters.vector_Point2f_to_Mat(source);

//        double widthA=(br[0] - bl[0]);
//        double widthB=(tr[0] - tl[0]);
//        double maxWidth=Math.max(widthA,widthB);
//
//        double heightA = Math.abs(tr[1] - br[1]);
//        double heightB = Math.abs(tl[1] - bl[1]);
//        double maxHeight = Math.max(heightA, heightB);
//        Log.v("Size1",widthA+" "+widthB+" "+heightA+" "+heightB );
//
//        Log.v("Size",maxWidth+" "+maxHeight);

        Point ocvPOut1 = new Point(0, 0);
        Point ocvPOut2 = new Point(0,grey.height()-1);
        Point ocvPOut3 = new Point(grey.width()-1, grey.height()-1);
        Point ocvPOut4 = new Point(grey.width()-1,0);
        List<Point> dest = new ArrayList<Point>();
        dest.add(ocvPOut1);
        dest.add(ocvPOut2);
        dest.add(ocvPOut3);
        dest.add(ocvPOut4);
        Mat endM = Converters.vector_Point2f_to_Mat(dest);

        Mat transform = Imgproc.getPerspectiveTransform(startM,endM);

        //Mat temp=new Mat(bitmap.getWidth(),bitmap.getHeight(),CV_8UC1);

//        Mat dst=new Mat((int) maxWidth,(int) maxHeight,CV_8UC1);

        Mat trans=new Mat(grey.width(),grey.height(),CV_8UC4);

        Imgproc.warpPerspective(grey,trans, transform, new Size(grey.width(),grey.height()));

//
//        Imgproc.adaptiveThreshold(grey,grey,255,Imgproc.ADAPTIVE_THRESH_MEAN_C,Imgproc.THRESH_BINARY_INV+Imgproc.THRESH_OTSU,5,4);
//
//        Imgproc.findContours(rgba,contours,hierarchy,Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);



        Mat thres=new Mat(grey.width(),grey.height(),CV_8UC4);

        Imgproc.threshold(trans,thres, -1, 255,
                Imgproc.THRESH_BINARY_INV+Imgproc.THRESH_OTSU);

        Imgproc.findContours(thres,contours,hierarchy,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);

        List<MatOfPoint> questionContr = new ArrayList<>();

        Imgproc.cvtColor(trans,trans,COLOR_GRAY2RGB);

        float ar;
        int i=0;

        for(MatOfPoint c:contours){
            bounding_rect=Imgproc.boundingRect(c);
            ar=bounding_rect.width/(float) bounding_rect.height;
            if(bounding_rect.width>=100 && bounding_rect.height>=100 && ar>=0.9 && ar<=1.1){
                questionContr.add(i,c);

                Imgproc.drawContours(trans,questionContr,i,new Scalar(255,0,0),4);

                Log.v("Question Contours", String.valueOf(questionContr.get(i)));

                i++;

            }
        }







        Utils.matToBitmap(rgba,bitmap);

        url1.setImageBitmap(bitmap);


        Utils.matToBitmap(trans,bmp);

        url.setImageBitmap(bmp);


    }

    public Mat findCircle(Mat rgba){

        Mat thres=new Mat(rgba.width(),rgba.height(),CV_8UC4);

        Imgproc.adaptiveThreshold(rgba,thres,255,Imgproc.ADAPTIVE_THRESH_MEAN_C,Imgproc.THRESH_OTSU,5,4);

        return thres;

    }



}
