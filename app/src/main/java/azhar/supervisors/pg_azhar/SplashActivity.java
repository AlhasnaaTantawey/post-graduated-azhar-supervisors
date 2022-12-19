package azhar.supervisors.pg_azhar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.pg_azhar.R;

import java.io.File;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SplashActivity extends AppCompatActivity {
    Retrofit retrofit;
    EndPoint endPoint;
    public static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = Client.getRetrofit();
        endPoint = retrofit.create(EndPoint.class);
        setContentView(R.layout.activity_splash);
        MainActivity.setLocale(this, "ar");
        getSupportActionBar().hide();
        getSupportActionBar().setTitle("الدراسات العليا");
        makeSplash();
    }


    public void makeSplash() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(900);
                   checkLogin();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
    //get username and password from API and compare between current username and password also send id to supervisorActivity

    public void checkLoginSupervisor(String userName, String password) {
        Call<SupervisorModel> call = endPoint.getSupervisor(userName, password);
        call.enqueue(new Callback<SupervisorModel>() {
            @Override
            public void onResponse(Call<SupervisorModel> call, Response<SupervisorModel> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "ادخل البيانات بشكل صحيح", Toast.LENGTH_SHORT).show();
                    return;
                }
                SupervisorModel supervisorModel = response.body();
                if (supervisorModel.getIsAdmin() == null) {
                    int id = supervisorModel.getId();
                    Intent intent = new Intent(getApplicationContext(), SupervisorActivity.class);
                    intent.putExtra("supervisorID", supervisorModel.getId());
                    startActivity(intent);
                }else if(supervisorModel.getIsAdmin()==0){
                    Intent intent = new Intent(getApplicationContext(), Departments.class);
                    startActivity(intent);
                }else if(supervisorModel.getIsAdmin()==1){
                    Intent intent = new Intent(getApplicationContext(), AllSupervisorInOneDepartment.class);
                    intent.putExtra("adminNumber",supervisorModel.getDeptNumber());
                    sendDepartmentName(supervisorModel,intent);
                    startActivity(intent);
                }
                finish();
            }
            @Override
            public void onFailure(Call<SupervisorModel> call, Throwable t) {
            }
        });
    }

    //check login by get username and password from sharedPreferences and move to another activity
    public void checkLogin() {
         sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        try {
            File.createTempFile("usernameAndPassowrdData", null, getBaseContext().getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String usernameValue = sharedPreferences.getString("username", "");
        String passwordValue = sharedPreferences.getString("password", "");
        if (!usernameValue.isEmpty() && !passwordValue.isEmpty()) {
            checkLoginSupervisor(usernameValue, passwordValue);

        }else {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        }
    // send DepartmentName to AllSuperviosrInOneDepartment
    private void sendDepartmentName(SupervisorModel supervisorModel ,Intent intent) {
        if(supervisorModel.getDeptNumber()==2){
            intent.putExtra("adminType","مشرفين قسم هندسه تخطيط");
        }else if(supervisorModel.getDeptNumber()==3){
            intent.putExtra("adminType","مشرفين قسم الهندسه المدنيه");
        }else if(supervisorModel.getDeptNumber()==4){
            intent.putExtra("adminType","مشرفين قسم الهندسه الميكانيكيه");
        }else if(supervisorModel.getDeptNumber()==5){
            intent.putExtra("adminType","مشرفين قسم الهندسه الكهربيه");
        }else if(supervisorModel.getDeptNumber()==6){
            intent.putExtra("adminType","مشرفين قسم هندسه بترول وتعدين");
        }else if(supervisorModel.getDeptNumber()==7){
            intent.putExtra("adminType","مشرفين قسم الهندسه المعماريه");
        }else if(supervisorModel.getDeptNumber()==8){
            intent.putExtra("adminType","مشرفين قسم هندسه نظم وحاسبات");
        }
    }
    }