package com.example.umesh.omrscanner;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.opencv.android.JavaCameraView;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;

public class Tutorial3View extends JavaCameraView implements PictureCallback {

    private static final String TAG = "Sample::Tutorial3View";
    private String mPictureFileName;
    byte[] image;

    public Tutorial3View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void takePicture(final String fileName) {
        Log.i(TAG, "Taking picture");
        this.mPictureFileName = fileName;
        // Postview and jpeg are sent in the same buffers if the queue is not empty when performing a capture.
        // Clear up buffers to avoid mCamera.takePicture to be stuck because of a memory issue
        mCamera.setPreviewCallback(null);

        // PictureCallback is implemented by the current class
        mCamera.takePicture(null, null, this);
        Log.v("Image", String.valueOf(image));
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.i(TAG, "Saving a bitmap to file");
        // The camera preview was automatically stopped. Start it again.

//        Bitmap bitmap= BitmapFactory.decodeByteArray(data,0,data.length);
//
//        Mat rgba = new Mat();
//        Utils.bitmapToMat(bitmap, rgba);


        image=data;

////        Mat edges = new Mat(rgba.size(), CvType.CV_8UC1);
////        Mat edges_copy = edges;
////        Imgproc.cvtColor(rgba, edges, Imgproc.COLOR_RGB2GRAY,4);
////        org.opencv.core.Size s = new org.opencv.core.Size(5,5);
////        Imgproc.GaussianBlur(edges, edges, s, 0);
////        Imgproc.Canny(edges, edges, 75, 200);
////        ArrayList contours=new ArrayList();
////        Imgproc.findContours(edges_copy,contours,edges,Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
//
////        contours=contours[0];
//
//
//
//        FindTest.Quadrilateral fq=FindTest.findDocument(rgba);
//        MatOfPoint contr=fq.contour;
//        Point[] point=fq.points;
//
//
//        List<MatOfPoint> contour=new ArrayList<MatOfPoint>();
//
////        for(int i = 0; i < point.length; i++)
////            contour.add(i,new MatOfPoint(point[i]));
//
//        contour.add(contr);
//
//        Scalar Contour_Color=new Scalar(255,0,0);
//        Imgproc.drawContours(rgba,contour,-1,Contour_Color);
//
//
//        Utils.matToBitmap(rgba,bitmap);
//
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] byteArray = stream.toByteArray();

//        mCamera.startPreview();
//        mCamera.setPreviewCallback(this);

//        // Write the image in a file (in jpeg format)
//        try {
//            FileOutputStream fos = new FileOutputStream(mPictureFileName);
//
//            //fos.write();
//            fos.close();
//
//        } catch (java.io.IOException e) {
//            Log.e("PictureDemo", "Exception in photoCallback", e);
//        }

    }

    public byte[] getImage() {
        return image;
    }

}
