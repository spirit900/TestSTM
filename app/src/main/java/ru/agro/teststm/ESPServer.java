package ru.agro.teststm;

import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.net.Socket;

public class ESPServer {
    public static final String LOG_TAG = "myLog";
    private String mServerName = "192.168.43.191"; // ip адрес сервера на ESP8266, который принимает соединения
    private int mServerPort = 88; // номер порта на ESP, на котором сервер принимает соединения
    private Socket mSocket = null;// сокет, через который приложение общается с сервером TCP

    public ESPServer() {
    }

    /* Открытие нового соединения. Если сокет уже открыт, то он закрывается, чтобы не было  мусора.
      @throws Exception  если не удалось открыть сокет */
    public void openConnection() throws Exception {
        /* Освобождаем ресурсы */
        closeConnection();
        try {
            /* Создаем новый сокет. Указываем на каком компьютере и порту запущен наш процесс,
             который будет принимать наше соединение. */
            mSocket = new Socket(mServerName, mServerPort);

        } catch (IOException e) {
            throw new Exception("Невозможно создать сокет: "+e.getMessage());
        }
    }

    /* Метод для закрытия сокета, по которому мы общались. */
    public void closeConnection() {
        /* Проверяем сокет. Если он не закрыт, то закрываем его и освобдождаем соединение.*/
        if (mSocket != null && !mSocket.isClosed()) {
            try {
                mSocket.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Невозможно закрыть сокет: " + e.getMessage());
            } finally { mSocket = null; }
        } mSocket = null;
    }

    /* Метод для отправки данных по сокету.
      * @param data * Данные, которые будут отправлены
      * @throws Exception * Если невозможно отправить данные */
      public void sendData(byte[] data) throws Exception {
          /* Проверяем сокет. Если он не создан или закрыт, то выдаем исключение */
          if (mSocket == null || mSocket.isClosed()) {
              Log.e(LOG_TAG, "Сокет не создан или закрыт.");

          }
          /* Отправка данных */
          try {
              mSocket.getOutputStream().write(data);
              mSocket.getOutputStream().flush();
          } catch (IOException e) {
              throw new Exception("Невозможно получит исходящий поток: " + e.getMessage());
          }
      }

   }



