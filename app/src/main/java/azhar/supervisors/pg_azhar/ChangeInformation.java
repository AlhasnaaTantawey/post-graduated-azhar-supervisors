package azhar.supervisors.pg_azhar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.ActivityChangeInformationBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangeInformation extends AppCompatActivity {

    ActivityChangeInformationBinding activityChangeInformationBinding;
    SupervisorModel supervisorModel;
    String password1, password2, username;
    Retrofit retrofit;
    EndPoint endPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChangeInformationBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_information);
        MainActivity.setLocale(this, "ar");
        retrofit = Client.getRetrofit();
        endPoint = retrofit.create(EndPoint.class);
        getSupportActionBar().hide();

        activityChangeInformationBinding.changeInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getText();
                if(username.isEmpty()|password1.isEmpty()|password2.isEmpty()){
                    Toast.makeText(getApplicationContext(),"اكمل ادخال البيانات",Toast.LENGTH_LONG).show();
                }else {
                if(password1.equals(password2)) {
                    setAlerDialog();
                }else {
                    Toast.makeText(getApplicationContext(), "تاكد من ادخال كلمه المرور بشكل صحيح", Toast.LENGTH_LONG).show();
                }
            }}
        });

    }

    public void changeUsernameAndPassword(int id, String username, String password) {
        Call<SupervisorModel> call = endPoint.changeUsernameAndPassword(id, username, password);
        call.enqueue(new Callback<SupervisorModel>() {
            @Override
            public void onResponse(Call<SupervisorModel> call, Response<SupervisorModel> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "فشل فى تغير بيانات المستخدم", Toast.LENGTH_LONG).show();

                    return;
                }
                supervisorModel = response.body();
                if(supervisorModel!=null){
                    Toast.makeText(getApplicationContext(), "تم تغير اسم المستخدم وكلمه المرور", Toast.LENGTH_LONG).show();
                    saveUsernameAndPassword(username,password);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<SupervisorModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "كلمه المرور مكرره رجاء تغيرها", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getText() {
        username = activityChangeInformationBinding.username.getText().toString().trim();
        password1 = activityChangeInformationBinding.textViewPassword1.getText().toString().trim();
        password2 = activityChangeInformationBinding.textViewPassword2.getText().toString().trim();
    }

    public int getResearcherId() {
        return getIntent().getIntExtra("ResearcherId", 0);
    }

    // make alerDialog
    public void setAlerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("تغير البيانات")
                .setMessage("هل متاكد من تغير اسم المستخدم وكلمه المرور؟")
                .setCancelable(false)
                .setPositiveButton(" نعم ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        changeUsernameAndPassword(getResearcherId(), username, password1);
                    }
                }).setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //strore username and password in sharedPreferences
    public void saveUsernameAndPassword(String user,String pass) {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("username", user);
        myEdit.putString("password", pass);
        myEdit.apply();
    }
}