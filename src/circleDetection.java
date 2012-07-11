import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.cpp.opencv_core.*;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_highgui;
import com.googlecode.javacv.cpp.opencv_imgproc;
import com.googlecode.javacv.cpp.opencv_imgproc.IplConvKernel;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

public class circleDetection{
	
	public static void main(String[] args){
		
		IplImage src = cvLoadImage("test6.bmp");
		//IplImage gray = cvCreateImage(cvGetSize(src), 8, 1);
		IplImage bin = cvCreateImage(cvGetSize(src), 8, 1);
		getBinary(src, bin);
		bin = process(bin);		
		
		CvMemStorage mem = CvMemStorage.create();
		
		CvSeq circles = cvHoughCircles( bin, mem, CV_HOUGH_GRADIENT, 
		    		2, 100, 100, 100, 15, 500);
		   
	    
	    for(int i = 0; i < circles.total(); i++){
	    	CvPoint3D32f point = new CvPoint3D32f(cvGetSeqElem(circles, i));
	    	CvPoint center = cvPointFrom32f(new CvPoint2D32f(point.x(), point.y()));
	    	int radius = Math.round(point.z());
	    	cvCircle(src, center, radius, CvScalar.GREEN, 6, CV_AA, 0);
            
	    }
		
	    //cvSaveImage("binary.bmp", bin);
		cvShowImage("binary", bin);
		cvShowImage("src", src);		
		cvWaitKey(0);
	}
	
	public static IplImage process (IplImage src){
		
		IplConvKernel kernel = cvCreateStructuringElementEx(
		        7, 7, 4, 4, CV_SHAPE_ELLIPSE, null);
		
		cvDilate(src, src, null,0);
		cvErode(src, src, null, 4);
		cvDilate(src, src, null,0);
		cvSmooth(src, src, CV_GAUSSIAN, 3);
		//GaussianBlur( bin, bin, cvSize( 3, 3 ), 0, 0, 0 );
		//cvCanny(bin, bin, 100, 100, 3 );
		
		return src;
	}
	
	public static void getBinary(IplImage src, IplImage bin){
		
		CvMat mtx = new CvMat(src); 
		CvMat mtx_bin = new CvMat(bin);
		
		for (int i = 0; i < src.height(); i++) {
		    for (int j = 0; j < src.width(); j++) {
		        CvScalar pix = cvGet2D(mtx, i, j); 
		        double rb_ratio = pix.red()/pix.blue();
		      
		        if(pix.red() > pix.blue()/* rb_ratio > 2*/){
		        	if(pix.blue() < 150){
		        		cvSet2D(mtx_bin, i, j, CvScalar.WHITE);
		        	}
		        }
		        else{
		        	cvSet2D(mtx_bin, i, j, CvScalar.BLACK);
		        }
		        
		    	//cvSet2D(mtx, i, j, CvScalar.WHITE);
		    }
		}
		bin = new IplImage (mtx_bin);
		//bin = gray;
		//cvCvtColor(bin, gray, CV_BINARY2GRAY);	
		
	}
	
}