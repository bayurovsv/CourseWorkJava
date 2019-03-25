import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
public class ImageRead {
    public static void main(String[] args) {
        try {
            File f = new File("C:\\zemlja.jpg");
            BufferedImage input = ImageIO.read(f);
            int width = input.getWidth();
            int height = input.getHeight();
            BufferedImage image = new BufferedImage(width, height,
                    BufferedImage.TYPE_BYTE_GRAY);
            Graphics g = image.getGraphics();
            g.drawImage(input, 0, 0, null);
            g.dispose();
            ImageIO.write(image, "jpg", new File("./a.JPG"));
            getGrayscaleImageData(image);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static int[][] getGrayscaleImageData(BufferedImage image) throws Exception {

        int k=0;
        int width  = image.getWidth();
        int heigth = image.getHeight();
        int[] lineData = new int[width * heigth];
        image.getRGB(0, 0, width, heigth, lineData,0,width);
        image.setRGB(0, 0, width, heigth, lineData,0,width);
        File n=new File("C:\\zemlja.jpg");
        ImageIO.write(image, "JPG", n);


        int[][] result = new int[heigth][width];
        for (int x=0; x < heigth; x++){
            for (int y=0; y < width; y++){
                result[x][y]=lineData[k];
                k++;   }
        }


        int o;
        int p;
        for(p=0; p < heigth; p++){
            for (o=0; o < width; o++){
                image.setRGB(o,p,result[p][o]);
            }
        }
        File z=new File("./assa.JPG");
        ImageIO.write(image, "JPG", z);
        return null;
    }

}
