import java.awt.*;
import java.awt.image.BufferedImage;

public class binariz {
    public static byte[][] binarizeImage(BufferedImage bfImage){
        final int THRESHOLD = 160;
        int height = bfImage.getHeight();
        int width = bfImage.getWidth();
        byte[][] image = new byte[width][height];

        for(int i=0; i<width; i++)
            for (int j = 0; j < height; j++) {
                Color c = new Color(bfImage.getRGB(i, j));
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();
                if (red < THRESHOLD && green < THRESHOLD && blue < THRESHOLD) {
                    image[i][j] = 1;
                } else {
                    image[i][j] = 0;
                }
            }
        return image;
    }
}
