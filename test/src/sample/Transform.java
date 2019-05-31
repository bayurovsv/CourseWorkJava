package sample;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import javafx.scene.control.TextField;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import static sample.CvUtilsFX.MatToImageFX;

/** Класс для работы с изображениями */
public class Transform {
    /** Путь до выбранного изображения */
    String fileName;
    /** Матрица для сохранения промежуточного результата*/
    Mat img_test= new Mat();
    /** Матрица для сохранения обработанного изображения*/
    Mat img_pov= new Mat();
    @FXML
    ImageView image = new ImageView();
    public TextField x_cm;
    public TextField y_vm;
    public int x;
    public int y;
    /** Угол вращения изображения*/
    public int angle = 360;
    /** Флаг проверки поворота изображения перед сохранением*/
    boolean check = false;
    /** Флаг проверки преобразования изображения в полутонное бля выполнения бинаризации*/
    boolean bn = false;
    /** Открытие изображения
     * @param image Исходное изображение над которым будут происходить изменения*/
    public void M_Open_img(ImageView image) {
        JFileChooser fileopen = new JFileChooser();
        int ret = fileopen.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
            fileName = file.getPath();
        }
        Mat img2 = Imgcodecs.imread(fileName);
        Image im2 = MatToImageFX(img2);
        img_test = img2;
        image.setImage(im2);
    }
    /** Сохранение множества изображений*/
    public void M_Seve_image(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("*.png", "png"));
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                if (check == true)
                {
                    for(int i = angle;i>=0; i--) {
                        Mat M = Imgproc.getRotationMatrix2D(new Point(img_pov.width() / 2, img_pov.height() / 2), i, 1);
                        Rect rect = new RotatedRect(new Point(img_pov.width() / 2, img_pov.height() / 2), new Size(img_pov.width(), img_pov.height()), i).boundingRect();
                        double[] arrX = M.get(0, 2);
                        double[] arrY = M.get(1, 2);
                        arrX[0] -= rect.x;
                        arrY[0] -= rect.y;
                        M.put(0, 2, arrX);
                        M.put(1, 2, arrY);
                        // Трансформация
                        Mat img2 = new Mat();
                        Imgproc.warpAffine(img_pov, img2, M, rect.size(), Imgproc.INTER_LINEAR, Core.BORDER_CONSTANT, new Scalar(255, 255, 255, 255));
                        img_test = img2;
                        Mat img4 = img_test;
                        Image im1 = MatToImageFX(img4);
                        image.setImage(im1);
                        ImageIO.write(MatToBufferedImage(img4), "png", new File(file.getAbsolutePath()+ i + ".png"));
                    }
                }
                else {
                    ImageIO.write(MatToBufferedImage(img_test), "png", new File(file.getAbsolutePath() + ".png"));
                }
            }
            catch (IOException ex) {
            }
        }
        else {}
    }
    public static BufferedImage MatToBufferedImage(Mat img) {
        if (img == null || img.empty()) return null;
        if (img.depth() == CvType.CV_8U) {}
        else if (img.depth() == CvType.CV_16U)
        { Mat m_16 = new Mat();
            img.convertTo(m_16, CvType.CV_8U, 255.0 / 65535);
            img = m_16;
        }
        else if (img.depth() == CvType.CV_32F) {
            Mat m_32 = new Mat();
            img.convertTo(m_32, CvType.CV_8U, 255);
            img = m_32;
        }
        else
            return null;
        int type = 0;
        if (img.channels() == 1)
            type = BufferedImage.TYPE_BYTE_GRAY;
        else if (img.channels() == 3)
            type = BufferedImage.TYPE_3BYTE_BGR;
        else if (img.channels() == 4)
            type = BufferedImage.TYPE_4BYTE_ABGR;
        else
            return null;
        byte[] buf = new byte[img.channels() * img.cols() * img.rows()];
        img.get(0, 0, buf);
        byte tmp = 0;
        if (img.channels() == 4) {
            for (int i = 0; i < buf.length; i += 4) {
                tmp = buf[i + 3];
                buf[i + 3] = buf[i + 2];
                buf[i + 2] = buf[i + 1];
                buf[i + 1] = buf[i];
                buf[i] = tmp;
            }
        }
        BufferedImage image_b = new BufferedImage(img.cols(), img.rows(), type);
        byte[] data = ((DataBufferByte) image_b.getRaster().getDataBuffer()).getData();
        System.arraycopy(buf, 0, data, 0, buf.length);
        return image_b;
    }

    /** Бинаризация изображения
     * @param image
     * Бинаризированное изображение
     */
    public void M_Binarization(ImageView image) {
        if (bn==false)
        {
            Mat img2 = new Mat();
            Imgproc.cvtColor(img_test,img2,Imgproc.COLOR_BGR2GRAY);
            Mat img3 = new Mat();
            Imgproc.threshold(img2,img3,100,255,Imgproc.THRESH_BINARY);
            Image im = MatToImageFX(img3);
            image.setImage(im);
            img_test = img3;
            bn=true;
            }
        else{
            Mat img2 = img_test;
            Mat img3 = new Mat();
            Imgproc.threshold(img2,img3,100,255,Imgproc.THRESH_BINARY);
            Image im = MatToImageFX(img3);
            image.setImage(im);
            img_test = img3;
            bn=false;
        }
    }
    /** Медианная фильтрация
     * @param image
     * Изображение после медианной фильтрации
     * */
    public void M_Median(ImageView image) {
        Mat img4 = new Mat();
        Imgproc.medianBlur(img_test,img4,3);
        Image im = MatToImageFX(img4);
        image.setImage(im);
        img_test = img4;
    }
    /** Удаление фона на изображение
     * @param image
     */
    public void M_Del_back(ImageView image) {
        Mat bgdModel = new Mat();
        Mat fgdModel = new Mat();
        Mat mask = new Mat();
        Rect rect = new Rect(1, 1, img_test.width() - 2, img_test.height() - 2);
        Imgproc.grabCut(img_test, mask, rect, bgdModel, fgdModel, 1,Imgproc.GC_INIT_WITH_RECT);
        Imgproc.grabCut(img_test, mask, new Rect(), bgdModel, fgdModel, 1,Imgproc.GC_EVAL);
        Mat maskPR_FGD = new Mat(); Core.compare(mask, new Scalar(Imgproc.GC_PR_FGD), maskPR_FGD,Core.CMP_EQ);
        Mat resultPR_FGD = new Mat(img_test.rows(), img_test.cols(), CvType.CV_8UC3,CvUtils.COLOR_WHITE);
        img_test.copyTo(resultPR_FGD, maskPR_FGD);
        Image im = MatToImageFX(resultPR_FGD);
        image.setImage(im);
        img_test = resultPR_FGD;
    }

    /** Вращение изображения
     * @param image Повернутое изображение
     */
    public void M_Vrashenie(ImageView image) {
        img_pov=img_test;
        check=true;
        Mat M = Imgproc.getRotationMatrix2D(new Point(img_test.width() / 2, img_test.height() / 2), angle, 1);
        Rect rect = new RotatedRect(new Point(img_test.width() / 2, img_test.height() / 2),new Size(img_test.width(), img_test.height()), angle).boundingRect();
        double[] arrX = M.get(0, 2);
        double[] arrY = M.get(1, 2);
        arrX[0] -= rect.x; arrY[0] -= rect.y; M.put(0, 2, arrX); M.put(1, 2, arrY);
        Mat img2 = new Mat();
        Imgproc.warpAffine(img_test, img2, M, rect.size(),Imgproc.INTER_LINEAR, Core.BORDER_CONSTANT,new Scalar(255, 255, 255, 255));
        Image im = MatToImageFX(img2);
        image.setImage(im);
        img_test = img2;
    }

    /** Перемещение изображения
     *
     * @param image Смещенное изображение
     */
    public void M_Smeshenie(ImageView image) {
        Mat M = new Mat (2,3,CvType.CV_32FC1);
        M.put(0,0,1,0,x,0,1,y);
        Mat img = new Mat();
        Imgproc.warpAffine(img_test,img,M,new Size(img_test.width(),img_test.height()),Imgproc.INTER_LINEAR, Core.BORDER_CONSTANT,new Scalar(255, 255, 255, 255));
        Image im = MatToImageFX(img);
        image.setImage(im);
        img_test = img;
    }

    /** Сброс всех изменений (возвращение изображение в первоначальное состояние)
     *
     * @param image Переоткрытие исходного изображения
     */
    public void M_Remove(ImageView image) {
        Mat img2 = Imgcodecs.imread(fileName);
        Image im2 = MatToImageFX(img2);
        img_test = img2;
        image.setImage(im2);
        bn = false;
    }

    /** Поиск контура объекта на изображении
     *
     * @param image Контур объекта на изображении
     */
    public void M_Search_contr(ImageView image) {
        Mat img = img_test;
        Mat bgdModel = new Mat(); Mat fgdModel = new Mat();
        Mat mask = new Mat();
        Rect rect = new Rect(1, 1, img.width() - 2, img.height() - 2);
        Imgproc.grabCut(img, mask, rect, bgdModel, fgdModel, 1,Imgproc.GC_INIT_WITH_RECT);
        Imgproc.grabCut(img, mask, new Rect(), bgdModel, fgdModel, 1,Imgproc.GC_EVAL);
        Mat maskPR_BGD = new Mat();
        Core.compare(mask, new Scalar(Imgproc.GC_PR_BGD), maskPR_BGD,Core.CMP_EQ);
        Mat resultPR_BGD = new Mat(img.rows(), img.cols(), CvType.CV_8UC3,new Scalar(Imgproc.GC_PR_FGD));
        img.copyTo(resultPR_BGD, maskPR_BGD);
        Image im = MatToImageFX(resultPR_BGD);
        image.setImage(im);
        img_test=resultPR_BGD;
    }

    /** Преобразование изображения в полутоновое
     *
     * @param image Полутоновое изображение
     */
    public void M_Polyton(ImageView image) {
        bn=true;
        Mat img2 = new Mat();
        Imgproc.cvtColor(img_test,img2,Imgproc.COLOR_BGR2GRAY);
        Image im = MatToImageFX(img2);
        image.setImage(im);
        img_test = img2;
    }
}
