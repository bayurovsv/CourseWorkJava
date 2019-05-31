package sample;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import javafx.scene.image.*;

public class CvUtilsFX {
    /** Преобразование из матрицы в изображение */
    public static WritableImage MatToImageFX(Mat m) {
        if (m == null || m.empty())
            return null;
        if (m.depth() == CvType.CV_8U) {}
        else if (m.depth() == CvType.CV_16U) {
            Mat m_16 = new Mat();
            m.convertTo(m_16, CvType.CV_8U, 255.0 / 65535);
            m = m_16;
        }
        else if (m.depth() == CvType.CV_32F) {
            Mat m_32 = new Mat();
            m.convertTo(m_32, CvType.CV_8U, 255);
            m = m_32;
        }    else
            return null;

            if (m.channels() == 1) {
                Mat m_bgra = new Mat();
                Imgproc.cvtColor(m, m_bgra, Imgproc.COLOR_GRAY2BGRA);
                m = m_bgra;
            }
   else if (m.channels() == 3) {
       Mat m_bgra = new Mat();
       Imgproc.cvtColor(m, m_bgra, Imgproc.COLOR_BGR2BGRA);
       m = m_bgra;
   }
   else if (m.channels() == 4) { }
   else
       return null;

            byte[] buf = new byte[m.channels() * m.cols() * m.rows()];
            m.get(0, 0, buf);

            WritableImage wim = new WritableImage(m.cols(), m.rows());
            PixelWriter pw = wim.getPixelWriter();
            pw.setPixels(0, 0, m.cols(), m.rows(),WritablePixelFormat.getByteBgraInstance(),buf, 0, m.cols() * 4);
            return wim;
    }
}
