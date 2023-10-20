package com.example.chatsocketio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSocket.connect();
        Button boton = findViewById(R.id.button);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = findViewById(R.id.editTextTextPersonName2);
                TextView textView = findViewById(R.id.textView);
                mSocket.emit("new message", editText.getText().toString());
                editText.setText("");
            }
        });
        mSocket.on("new message", onNewMessage);
    }


    private Socket mSocket;



    private static final String ULR1 = "http://jchat.dam.inspedralbes.cat:3018/";
    private static final String ULR2 = "http://10.0.2.2:3018/";

    {
        try {
            mSocket = IO.socket(ULR2);
        } catch (URISyntaxException e) {
        }
    }

    private EditText mInputMessageView;

    private void attemptSend() {
        String message = mInputMessageView.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        }

        mInputMessageView.setText("");
        mSocket.emit("new message", message);
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView textView = findViewById(R.id.textView);
                    String message="";
                    if(args.length>0){
                        message = args[0].toString();
                        textView.setText(message);
                    }

                    // add the message to view
                    addMessage(message);
                }

                private void addMessage(String message) {
                    TextView textView = findViewById(R.id.textView);
                    textView.setText(message);
                }
            });
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        //mSocket.off("new message", onNewMessage);
    }


}