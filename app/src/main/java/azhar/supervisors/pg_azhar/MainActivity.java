package azhar.supervisors.pg_azhar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.ActivityMainBinding;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding buinding;
    EndPoint endPoint;
    Retrofit retrofit;


    @Override
    protected void onPause() {
        super.onPause();
        saveUsernameAndPassword();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = Client.getRetrofit();
        endPoint = retrofit.create(EndPoint.class);
        buinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setLocale(this, "ar");
        getSupportActionBar().setTitle("الدراسات العليا");
        getSupportActionBar().hide();
        touchListenerPassword();
        touchListenerUsername();
    }

    //show message to write username and password if not exist
    public void checkUsernameAndPassowrd() {
        String username = buinding.editTextUsername.getText().toString().trim();
        String password = buinding.editTextPassword.getText().toString().trim();

        if (username.isEmpty() && password.isEmpty()) {
            buinding.textviewUsernameIsEmpty.setVisibility(View.VISIBLE);
            buinding.textViewPasswordIsEmpty.setVisibility(View.VISIBLE);
        } else if (username.isEmpty() && !password.isEmpty()) {
            buinding.textviewUsernameIsEmpty.setVisibility(View.VISIBLE);
            buinding.textViewPasswordIsEmpty.setVisibility(View.INVISIBLE);
        } else if (!username.isEmpty() && password.isEmpty()) {
            buinding.textviewUsernameIsEmpty.setVisibility(View.INVISIBLE);
            buinding.textViewPasswordIsEmpty.setVisibility(View.VISIBLE);
        } else if (!username.isEmpty() && !password.isEmpty()) {
            checkLoginSupervisor(username, password);
        }
    }

    // make textviewUsernameISEmpty INVISABLE when user write text in editTextUsername
    public void touchListenerUsername() {
        buinding.editTextUsername.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                buinding.textviewUsernameIsEmpty.setVisibility(View.INVISIBLE);
                return false;
            }
        });
    }

    // make textviewPassowrdISEmpty INVISABLE when user write text in editTextpassword
    public void touchListenerPassword() {
        buinding.editTextPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                buinding.textViewPasswordIsEmpty.setVisibility(View.INVISIBLE);
                return false;
            }
        });
    }

    //OnClick for login Button
    public void loginAction(View view) {
        checkUsernameAndPassowrd();
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
                Toast.makeText(getApplicationContext(), "لا يوجد اتصال بالانترنت", Toast.LENGTH_LONG).show();

            }
        });
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

    // set specific language (arabic)
    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    //strore username and password in sharedPreferences
    public void saveUsernameAndPassword() {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("username", buinding.editTextUsername.getText().toString().trim());
        myEdit.putString("password", buinding.editTextPassword.getText().toString().trim());
        myEdit.apply();
    }

}