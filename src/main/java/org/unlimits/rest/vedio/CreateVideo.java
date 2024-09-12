package org.unlimits.rest.vedio;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

public class CreateVideo {
	public static final File dir = new File("C:\\app_runs\\unlimits-resources\\resource\\sub_cat_images\\");

    public static void main(String[] args) throws FileNotFoundException, IOException, JCodecException {
    	double startSec = 51.632;
    	int frameCount = 10;
    	File vediofile = new File("video.mp4");
    	vediofile.createNewFile();
    	FrameGrab grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(vediofile));
    	grab.seekToSecondPrecise(startSec);

    	for (File file:dir.listFiles()) {
    	    Picture picture = grab.getNativeFrame();
    	    System.out.println(picture.getWidth() + "x" + picture.getHeight() + " " + picture.getColor());
    	    //for JDK (jcodec-javase)
    	   // BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
    	    //ImageIO.write(bufferedImage, file.getName().split(".")[1], file); 
    	}
    }
}