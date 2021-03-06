package com.xinleju.cloud.oa.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

public class ImageCompressingUtils {

    public static void main(String[] args) {
        String path = "C:\\Users\\admin\\Desktop\\qq.jpg";
        long c = System.currentTimeMillis();
        //ImageCompressingUtils.compress(path, "C:\\Users\\admin\\Desktop\\1.jpg", "jpg", 0.5);
        System.out.println("elapse time:"
                + (System.currentTimeMillis() - c) / 1000.0f + "s");

        BufferedImage image = new BufferedImage(400,300,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        image = g2d.getDeviceConfiguration().createCompatibleImage(400,300, Transparency.TRANSLUCENT);
        g2d.dispose();
        g2d = image.createGraphics();
        g2d.setColor(new Color(255,0,0));
        g2d.setStroke(new BasicStroke(1));
    }

    public static BufferedImage getTransparentScaledImage(BufferedImage originalImage, int finalWidth, int finalHeight) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        if (originalWidth == 0 || originalHeight == 0
                || (originalWidth == finalWidth && originalHeight == finalHeight)) {
            return originalImage;
        }

        BufferedImage intermediateImage = new BufferedImage(finalWidth, finalHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gi = intermediateImage.createGraphics();
        gi.setComposite(AlphaComposite.SrcOver);
        gi.setColor(new Color(0, 0, 0, 255));
        gi.setBackground(new Color(255,255,255,0));
        gi.fillRect(0, 0, finalWidth, finalHeight);
        gi.drawImage(originalImage.getScaledInstance(finalWidth, finalHeight, Image.SCALE_SMOOTH), 0, 0, null);
        gi.dispose();

        return intermediateImage;
    }

    public static Image makeColorTransparent(Image im, final Color color) {
        ImageFilter filter = new RGBImageFilter() {
            public int markerRGB = color.getRGB() | 0xFF000000;

            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    return 0x00FFFFFF & rgb;
                } else {
                    return rgb;
                }
            }
        };

        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    /**
     * 压缩
     * @param datas 数据
     * @param rate 压缩比率
     * @return
     */
    public static byte[] compress(byte[] datas, String format, double rate) {
        try {
            BufferedImage srcImage = ImageIO.read(new ByteArrayInputStream(datas));
            int width = srcImage.getWidth();
            int height = srcImage.getHeight();
            BufferedImage descImage = getTransparentScaledImage(srcImage, ((Double)(width * rate)).intValue(), ((Double) (height * rate)).intValue());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(descImage, format == null ? "jpg" : format, out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void compress(String srcPath, String descPath, String format, double rate) {
        try {
            byte[] datas = FileUtils.readFileToByteArray(new File(srcPath));
            byte[] finalDatas = compress(datas, format, rate);
            FileUtils.writeByteArrayToFile(new File(descPath), finalDatas);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
