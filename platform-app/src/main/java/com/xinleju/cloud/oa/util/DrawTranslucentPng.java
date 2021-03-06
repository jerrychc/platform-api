package com.xinleju.cloud.oa.util;

/**
 * Created by admin on 2017/8/24.
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 绘制透明背景的文字图片
 *
 * @author cxy @ www.cxyapi.com
 */
public class DrawTranslucentPng {
    public static BufferedImage drawTranslucentStringPic(int width, int height, Integer fontHeight, String drawStr) {
        try {
            BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D gd = buffImg.createGraphics();
            //设置透明  start
            buffImg = gd.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
            gd = buffImg.createGraphics();
            //设置透明  end
            gd.setFont(new Font("微软雅黑", Font.PLAIN, fontHeight)); //设置字体
            gd.setColor(Color.ORANGE); //设置颜色
            gd.drawRect(0, 0, width - 1, height - 1); //画边框
            gd.drawString(drawStr, width / 2 - fontHeight * drawStr.length() / 2, fontHeight); //输出文字（中文横向居中）
            return buffImg;
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        /*BufferedImage imgMap = drawTranslucentStringPic(400, 80, 36, "欢迎访问我的博客");
        File imgFile = new File("C:\\Users\\admin\\Desktop\\aa.png");
        try {
            ImageIO.write(imgMap, "PNG", imgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("生成完成");*/

        BufferedImage img = rotateImg("C:\\Users\\admin\\Desktop\\qq.jpg",50,"png");

         FileOutputStream fos = null;
        fos = new FileOutputStream("C:\\Users\\admin\\Desktop\\aaa.png");
        ImageIO.write(img, "png", fos);
    }

    public static BufferedImage rotateImg(String srcImagePath, int degree, String imageFormat) throws IOException {
        BufferedImage image = ImageIO.read(new File(srcImagePath));
        int iw = image.getWidth();// 原始图象的宽度
        int ih = image.getHeight();// 原始图象的高度
        int w = 0;
        int h = 0;
        int x = 0;
        int y = 0;
        degree = degree % 360;
        if (degree < 0)
            degree = 360 + degree;// 将角度转换到0-360度之间
        double ang = Math.toRadians(degree);// 将角度转为弧度
        /**
         * 确定旋转后的图象的高度和宽度
         */
        if (degree == 180 || degree == 0 || degree == 360) {
            w = iw;
            h = ih;
        } else if (degree == 90 || degree == 270) {
            w = ih;
            h = iw;
        } else {
            // int d = iw + ih;
            double cosVal = Math.abs(Math.cos(ang));
            double sinVal = Math.abs(Math.sin(ang));
            w = (int) (sinVal * ih) + (int) (cosVal * iw);
            h = (int) (sinVal * iw) + (int) (cosVal * ih);
        }
        x = (w / 2) - (iw / 2);// 确定原点坐标
        y = (h / 2) - (ih / 2);
        BufferedImage rotatedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D gs = rotatedImage.createGraphics();
        rotatedImage = gs.getDeviceConfiguration().createCompatibleImage(w, h,
                Transparency.TRANSLUCENT);
        gs.dispose();
        gs = rotatedImage.createGraphics();
        gs.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        AffineTransform at = new AffineTransform();
        at.rotate(ang, w / 2, h / 2);// 旋转图象
        at.translate(x, y);
        AffineTransformOp op = new AffineTransformOp(at,
                AffineTransformOp.TYPE_BICUBIC);
        op.filter(image, rotatedImage);
        image = rotatedImage;
// FileOutputStream fos = null;
// fos = new FileOutputStream("c:\\aaa.png");
// ImageIO.write(image, imageFormat, fos);
        return image;
    }

}
