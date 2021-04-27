package com.luzhi.tmall.util;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.PixelGrabber;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21
 * 对图片的处理,捕获图片保存到资源路径下.
 * 总的来说这玩意就是一工具类....................
 */
public class ImageUtil {

    public static BufferedImage change2jpg(File f) {
        try {
            Image i = Toolkit.getDefaultToolkit().createImage(f.getAbsolutePath());
            PixelGrabber pg = new PixelGrabber(i, 0, 0, -1, -1, true);
            pg.grabPixels();
            int width = pg.getWidth(), height = pg.getHeight();
            final int[] rgbMasks = {0xFF0000, 0xFF00, 0xFF};
            final ColorModel rgbOpaque = new DirectColorModel(32, rgbMasks[0], rgbMasks[1], rgbMasks[2]);
            DataBuffer buffer = new DataBufferInt((int[]) pg.getPixels(), pg.getWidth() * pg.getHeight());
            WritableRaster raster = Raster.createPackedRaster(buffer, width, height, width, rgbMasks, null);
            return new BufferedImage(rgbOpaque, raster, false, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings({"unused"})
    public static void resizeImage(File srcFile, int width, int height, File destFile) {

        try {
            if (!destFile.getParentFile().exists()) {

                //noinspection ResultOfMethodCallIgnored
                destFile.getParentFile().mkdirs();
            }
            Image image = ImageIO.read(srcFile);
            image = resizeImage(image, width, height);
            // 图片不为空
            assert image != null;
            ImageIO.write((RenderedImage) image, "jpg", destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Image resizeImage(Image srcImage, int width, int height) {
        try {

            BufferedImage buffImg;
            buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            buffImg.getGraphics().drawImage(srcImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);

            return buffImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
