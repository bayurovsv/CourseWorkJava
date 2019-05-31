package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.IOException;
/**
 *
 * Студент группы ПИн-116 Баюров Сергей Вячеславович
 * @version 1.0
 */
public class Controller extends Component {
    public TextField x_cm;
    public TextField y_vm;
    public int x,y;
    @FXML
    ImageView image = new ImageView();
    /** Открытие изображения */
    Transform tr = new Transform();
    public void Open_img(ActionEvent actionEvent)throws IOException {
        tr.M_Open_img(image);
    }
    /** Сохранение множества изображений */
    public void Seve_image(ActionEvent actionEvent) throws IOException {
        tr.M_Seve_image();
    }
    /** Бинаризация изображения*/
    public void Binarization(ActionEvent actionEvent) throws IOException {
    tr.M_Binarization(image);
    }
    /** Медианная фильтрация */
    public void Median(ActionEvent actionEvent) throws IOException{
        tr.M_Median(image);
    }
    /** Удаление фона на изображение */
    public void Del_back(ActionEvent actionEvent)throws IOException {
        tr.M_Del_back(image);
    }
    /** Вращение изображения */
    public void Vrashenie(ActionEvent actionEvent)throws IOException {
        tr.M_Vrashenie(image);
    }
    /** Перемещение изображения */
    public void Smeshenie(ActionEvent actionEvent)throws IOException {
        this.x = Integer.parseInt(x_cm.getText());
        this.y = Integer.parseInt(y_vm.getText());
        tr.x=x;
        tr.y=y;
        tr.M_Smeshenie(image);
    }
    /** Сброс всех изменений (возвращение изображение в первоначальное состояние) */
    public void Remove(ActionEvent actionEvent)throws IOException {
        tr.M_Remove(image);
    }
    /** Поиск контура объекта на изображении */
    public void Search_contr(ActionEvent actionEvent)throws IOException {
           tr.M_Search_contr(image);
    }
    /** Преобразование изображения в полутоновое */
        public void Polyton(ActionEvent actionEvent) throws IOException {
       tr.M_Polyton(image);
    }
}


