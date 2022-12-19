package azhar.supervisors.pg_azhar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.ActivityDepartmentsBinding;

public class Departments extends AppCompatActivity {
    ActivityDepartmentsBinding activityDepartmentsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDepartmentsBinding = DataBindingUtil.setContentView(this, R.layout.activity_departments);
        MainActivity.setLocale(this, "ar");
        getSupportActionBar().setTitle("الدراسات العليا");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0529F4")));
        moveToShowAllSupervisor(5, activityDepartmentsBinding.departmentsCardViewElectricity,"مشرفين الهندسه الكهربيه");
        moveToShowAllSupervisor(7, activityDepartmentsBinding.departmentsCardViewArchitecture,"مشرفين الهندسه المعماريه");
        moveToShowAllSupervisor(4, activityDepartmentsBinding.departmentsCardViewMechanics,"مشرفين الهندسه الميكانيكيه");
        moveToShowAllSupervisor(6, activityDepartmentsBinding.departmentsCardViewPetrol,"مشرفين هندسه بترول وتعدين");
        moveToShowAllSupervisor(8, activityDepartmentsBinding.departmentsCardViewProgramming,"مشرفين قسم نظم وحاسبات");
        moveToShowAllSupervisor(2, activityDepartmentsBinding.departmentsCardViewPlanning,"مشرفين قسم هندسه تخطيط");
        moveToShowAllSupervisor(3, activityDepartmentsBinding.departmentsCardViewCivil,"مشرفين قسم الهندسه المدنيه");

    }

    public void moveToShowAllSupervisor(Integer adminNumber, CardView cardView,String department) {
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Departments.this, AllSupervisorInOneDepartment.class);

                intent.putExtra("adminNumber", adminNumber);
                intent.putExtra("adminType",department);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
    @Override
    public  boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.toolbar_menu_logout:
                setAlerDialog();
                return true;
            default:
                return true;
        }
    }
    // make alerDialog and logout
    public void setAlerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("تسجيل الخروج")
                .setMessage("هل تريد تسجيل الخروج؟")
                .setCancelable(false)
                .setPositiveButton(" نعم ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SplashActivity.sharedPreferences.edit().clear().commit();
                        Intent intent=new Intent(Departments.this,MainActivity.class);
                        startActivity(intent);
                        finishAffinity();

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
}