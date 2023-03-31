package com.example.chatgptapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageButton imageButton;
    EditText editText;
    TextView textView;

    List<Message> messageList;
    MessageAdapter messageAdapter;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageList= new ArrayList<>();

        recyclerView= findViewById(R.id.recycler_view);
        textView= findViewById(R.id.txt_Welcome);
        editText= findViewById(R.id.edt_message);
        imageButton= findViewById(R.id.btn_send);
        imageButton.setOnClickListener((v)->{
            String question= editText.getText().toString().trim();
            AddToChat(question,Message.SENT_BY_ME);
            editText.setText("");
            CallAPI(question);
            textView.setVisibility(View.GONE);
        });

        //setup recycler view
        messageAdapter= new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm= new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

    }
    void AddToChat(String mess, String sendBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(mess,sendBy));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }
    void AddResponse(String response){
        messageList.remove(messageList.size()-1);
        AddToChat(response,Message.SENT_BY_BOT);
    }
    void CallAPI(String ques){

        messageList.add(new Message("...",Message.SENT_BY_BOT));

        JSONObject jsonBody= new JSONObject();
        try {
            jsonBody.put("model","text-davinci-003");
            jsonBody.put("prompt",ques);
            jsonBody.put("max_tokens", 1000);
            jsonBody.put("temperature", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body= RequestBody.create(jsonBody.toString(),JSON);
        Request request= new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization","Bearer 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")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                AddResponse("Không thể tải phản hồi do"+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    JSONObject jsonObject=null;
                    try {
                        jsonObject= new JSONObject(response.body().string());
                        JSONArray jsonArray= jsonObject.getJSONArray("choices");
                        String result= jsonArray.getJSONObject(0).getString("text");
                        AddResponse(result.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    AddResponse("Không phản hồi do "+response.body().toString());
                }
            }
        });
    }
}