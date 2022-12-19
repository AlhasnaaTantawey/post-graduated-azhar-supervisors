package azhar.supervisors.pg_azhar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.ActivityForAdminBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActivityForAdmin extends AppCompatActivity {
    ActivityForAdminBinding activityForAdminBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityForAdminBinding = DataBindingUtil.setContentView(this, R.layout.activity_for_admin);
        MainActivity.setLocale(this,"ar");
        getSupportActionBar().setTitle("الدراسات العليا");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0529F4")));
        activityForAdminBinding.adminActivityBottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

    }

    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.adminMenu_notes:
                  setFragment(new ShowAllNotesOnOneDepartment());
                    return true;
                case R.id.adminMenu_requests:
                    setFragment(new ShowAllRequestInOneDepartment());
                    return true;
                case R.id.adminMenu_reports:
                    setFragment(new ShowAllReportsInOneDepartment());
                    return true;
            }
            return false;
        }
    };
    public void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.adminActivity_fragmetnArea, fragment)
                .addToBackStack(null)
                .commit();
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
                        Intent intent=new Intent(ActivityForAdmin.this,MainActivity.class);
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