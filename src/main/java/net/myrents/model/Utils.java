package net.myrents.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {
    private static final int MAX_IMAGE_SIZE = 600;

    // Crop image to square
    public static byte[] cropImage(byte[] src) {
        if(src != null) {
            // convert byte array to BufferedImage
            BufferedImage bImageFromConvert = byteToBufferedImage(src);
            BufferedImage dest;

            int height = bImageFromConvert.getHeight();
            int width = bImageFromConvert.getWidth();
            // if not square
            if (height != width) {
                if (height > width) {
                    dest = bImageFromConvert.getSubimage(0, (height - width) / 2, width, width);
                } else {
                    dest = bImageFromConvert.getSubimage((width - height) / 2, 0, height, height);
                }
                src = bufferedImageToByte(dest);
            }
        }

        return src;
    }

    // Check image size
    public static byte[] sizeImage(byte[] src) {
        if(src != null) {
            // convert byte array to BufferedImage
            BufferedImage bImageFromConvert = byteToBufferedImage(src);

            // check image size
            boolean changed = false;
            int height = bImageFromConvert.getHeight();
            int width = bImageFromConvert.getWidth();
            while (width > MAX_IMAGE_SIZE || height > MAX_IMAGE_SIZE){
                width = width - (width / 10);
                height = height - (height / 10);
                if(!changed){
                    changed = true;
                }
            }
            if(changed){
                bImageFromConvert = resizeImage(bImageFromConvert,bImageFromConvert.getType(),width,height);
                src = bufferedImageToByte(bImageFromConvert);
            }
        }

        // return resized image
        return src;
    }

    // resize image
    private static BufferedImage resizeImage(BufferedImage originalImage, int type, int width, int height){
        BufferedImage newImage = new BufferedImage(width,height,type);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return newImage;
    }

    // convert BufferedImage to byte array
    private static byte[] bufferedImageToByte(BufferedImage dest){
        byte[] src = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(dest, "jpg", baos);
            baos.flush();
            src = baos.toByteArray();
            baos.close();

        } catch (IOException e) {

        }
        return src;
    }

    // convert byte array to BufferedImage
    private static BufferedImage byteToBufferedImage(byte[] src){
        InputStream in = new ByteArrayInputStream(src);
        BufferedImage bImageFromConvert = null;
        try {
            bImageFromConvert = ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bImageFromConvert;
    }

    public static boolean isEmptyOrNull(String string) {
        return string == null || string.isEmpty();
    }
}
