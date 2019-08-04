package ru.agro.teststm;

import android.app.Activity;
import android.icu.util.TimeUnit;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import static java.lang.Thread.sleep;
import static ru.agro.teststm.ESPServer.LOG_TAG;


public class TwoActivity extends Activity {



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two);
        final ESPServer[] mServer = new ESPServer[1];

        Button connBtn = (Button) findViewById(R.id.connBtn);//находим кнопку по идентификатору
        Button sendBtn = (Button) findViewById(R.id.sendBtn);//находим кнопку по идентификатору
        Button closeBtn = (Button) findViewById(R.id.closeBtn);//находим кнопку по идентификатору
        final EditText editText = (EditText) findViewById(R.id.editText);//находим  по идентификатору
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);//находим  по идентификатору (зелёная галочка)
        imageView.setVisibility(View.INVISIBLE);//делаем галочку невидимой по умолчанию
        final TextView textViewSended =  (TextView) findViewById(R.id.textViewSended);
       // textViewSended.setVisibility(View.INVISIBLE);

        View.OnClickListener OnClickListener = new View.OnClickListener() { /*Создаём обработчик для  кнопок*/
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    /* действия по нажатию кнопки Соединение с сеялкой */
                    case R.id.connBtn:
                        /* создаем объект для работы с сервером TCP */
                        mServer[0] = new ESPServer();
                        /* Открываем соединение. Открытие должно происходить в отдельном потоке от ui */
                        new Thread(new Runnable() {
                            @Override public void run() {
                                try {
                                    mServer[0].openConnection();

                                    /* показываем галочку, что соединение установлено
                                     Все данные по обновлению интерфеса должны обрабатывается в Ui потоке,
                                     а так как мы сейчас находимся в отдельном потоке, нам необходимо вызвать метод
                                     runOnUiThread() */
                                    runOnUiThread(new Runnable() {
                                        @Override public void run() {
                                            imageView.setVisibility(View.VISIBLE);
                                        }
                                    });

                                } catch (Exception e) {
                                    Log.e(LOG_TAG, e.getMessage());
                                 mServer[0] = null;
                                }
                            }
                        }).start();
                        break;
                    /* действия по нажатию кнопки Send */
                    case R.id.sendBtn:
                        new Thread(new Runnable() {
                            @Override public void run() {
                                try {
                                    String st = editText.getText().toString();
                                    byte [] byteArray = st.getBytes();
                                    mServer[0].sendData(byteArray);

                                } catch (Exception e) {
                                    Log.e(LOG_TAG, "Не получается отправить " +  e.getMessage());
                                    mServer[0] = null;
                                }
                            }
                        }).start();
                    break;
                    /* действия по нажатию кнопки Close connection */
                    case R.id.closeBtn:
                        mServer[0].closeConnection();
                        runOnUiThread(new Runnable() {
                            @Override public void run() {
                                imageView.setVisibility(View.INVISIBLE);
                            }
                        });
                    break;
                }
            }
        };
        /*устанавливаем этого обработчика для всех кнопок*/
        connBtn.setOnClickListener(OnClickListener);
        sendBtn.setOnClickListener(OnClickListener);
        closeBtn.setOnClickListener(OnClickListener);

    }
}
