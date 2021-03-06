package com.xinleju.cloud.oa.util;

import com.sun.imageio.plugins.bmp.BMPImageReader;
import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.jpeg.JPEGImageReader;
import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import com.sun.imageio.plugins.png.PNGImageReader;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;

/**
 * Created by admin on 2017/8/23.
 */
public class CompressImgUtil {
    public static byte[] compressImg(byte[] icon,int width,int height) throws Exception {
        ByteArrayInputStream inputStream1 = new ByteArrayInputStream(icon);
        String type = "jpeg";
        ImageInputStream imageInputStream = ImageIO.createImageInputStream(inputStream1);
        //mcis = new MemoryCacheImageInputStream(imageInputStream);
        Iterator itr = ImageIO.getImageReaders(imageInputStream);
        while (itr.hasNext()) {
            ImageReader reader = (ImageReader)itr.next();
            if (reader instanceof GIFImageReader)
                type = "gif";
            else if (reader instanceof JPEGImageReader)
                type = "jpeg";
            else if (reader instanceof PNGImageReader)
                type = "png";
            else if (reader instanceof BMPImageReader)
                type = "x-bmp";
        }
        inputStream1.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(icon);
        Image img = ImageIO.read(inputStream);
        inputStream.close();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ByteArrayInputStream inputStream2 = new ByteArrayInputStream(icon);
        BufferedImage image1 = ImageIO.read(inputStream2);
        Color backGround = null;
        AlphaComposite composite = null;
        if(image1!=null){
            Graphics2D graphics2D = (Graphics2D) image1.getGraphics();
            backGround = graphics2D.getBackground();
            composite = (AlphaComposite) graphics2D.getComposite();
        }else{
            return null;
        }

        inputStream2.close();

        BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB );
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.drawImage(img, 0, 0, width, height, null); // 绘制缩小后的图

        //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outputStream);
        //encoder.encode(image); // JPEG编码


        ImageIO.write(image,type,outputStream);
        byte[] icon2 = outputStream.toByteArray();

        outputStream.flush();
        outputStream.close();

        return icon2;
    }

    public static byte[] compressImg2(byte[] icon,int width,int height) throws Exception{
        ByteArrayInputStream inputStream = new ByteArrayInputStream(icon);
        BufferedImage image = ImageIO.read(inputStream);
        if (image == null) {
            return null;
        }

        BufferedImage rotatedImage = new BufferedImage(width, height, image.getType());
        Graphics2D gs = rotatedImage.createGraphics();
        //rotatedImage = gs.getDeviceConfiguration().createCompatibleImage(width, height,Transparency.TRANSLUCENT);

        gs.dispose();
        gs = rotatedImage.createGraphics();
        //gs.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        //AffineTransform at = new AffineTransform();
        //at.rotate(0, 0, 0);// 旋转图象
        //at.translate(0, 0);
        //AffineTransformOp op = new AffineTransformOp(at,AffineTransformOp.TYPE_BICUBIC);
        //op.filter(image, rotatedImage);
        //image = rotatedImage;
        gs.drawImage(image, 0, 0, width, height, null);

        ByteArrayInputStream inputStream1 = new ByteArrayInputStream(icon);
        ImageInputStream imageInputStream = ImageIO.createImageInputStream(inputStream1);
        Iterator itr = ImageIO.getImageReaders(imageInputStream);
        String type = "jpg";
        if (itr.hasNext()) {
            ImageReader reader = (ImageReader)itr.next();
            if (reader instanceof GIFImageReader)
                type = "gif";
            else if (reader instanceof JPEGImageReader)
                type = "jpg";
            else if (reader instanceof PNGImageReader)
                type = "png";
            else if (reader instanceof BMPImageReader)
                type = "x-bmp";
        }
        imageInputStream.close();
        inputStream1.close();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ImageIO.write(rotatedImage,type,outputStream);
        byte[] icon2 = outputStream.toByteArray();
        return icon2;
    }

    public static byte[] compressImg3(byte[] icon,int width,int height) throws Exception{
        ByteArrayInputStream inputStream = new ByteArrayInputStream(icon);
        ByteArrayInputStream inputStream2 = new ByteArrayInputStream(icon);
        ImageInputStream imageInputStream = ImageIO.createImageInputStream(inputStream2);
        ImageIO.getImageReaders(imageInputStream);
        BufferedImage bufferedImage =  ImageIO.read(inputStream);
        if (bufferedImage == null) {
            return null;
        }

        int new_w = width;
        int new_h = height;

        BufferedImage image_to_save = new BufferedImage(new_w, new_h,bufferedImage.getType());

       /* Graphics2D gs = image_to_save.createGraphics();
        image_to_save = gs.getDeviceConfiguration().createCompatibleImage(width, height,Transparency.TRANSLUCENT);
        gs.dispose();*/

        image_to_save.getGraphics().drawImage(bufferedImage.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0,0, null);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        JPEGImageWriter imageWriter = (JPEGImageWriter) ImageIO.getImageWritersByFormatName("jpg").next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(byteArrayOutputStream);
        imageWriter.setOutput(ios);
        // and metadata
        IIOMetadata imageMetaData = imageWriter.getDefaultImageMetadata(
                new ImageTypeSpecifier(image_to_save), null);

        float JPEGcompression = 0.7f;
        if (JPEGcompression >= 0 && JPEGcompression <= 1f) {
            JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter
                    .getDefaultWriteParam();
            jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
            jpegParams.setCompressionQuality(JPEGcompression);

        }

        imageWriter.write(imageMetaData, new IIOImage(image_to_save, null, null), null);
        ios.close();
        imageWriter.dispose();

        return byteArrayOutputStream.toByteArray();
    }

    /*public static void main(String[] args) throws Exception {
        File file = new File("C:\\Users\\admin\\Desktop\\ceshi.jpg");
        FileInputStream fileInputStream = new FileInputStream(file);
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int temp = 0;
        int len = 0;
        while ((temp=fileInputStream.read(buff))!=-1){
            swapStream.write(buff,0,temp);
        }
        fileInputStream.close();

        //ByteArrayInputStream inputStream = new ByteArrayInputStream(swapStream.toByteArray());

        byte[] out = compressImg3(swapStream.toByteArray(),50,50);

        FileOutputStream outputStream = new FileOutputStream(new File("C:\\Users\\admin\\Desktop\\111.jpg"));
        outputStream.write(out);
        outputStream.close();
    }*/
}
