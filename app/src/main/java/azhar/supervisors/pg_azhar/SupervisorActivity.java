package azhar.supervisors.pg_azhar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.pg_azhar.R;

import azhar.supervisors.pg_azhar.ui.personalData.PersonalDataFragment;
import azhar.supervisors.pg_azhar.ui.reshershers.ResearchFragment;

import com.example.pg_azhar.databinding.ActivitySupervisorBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class SupervisorActivity extends AppCompatActivity {
    public static int supervisorId;
    ActivitySupervisorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_supervisor);
        MainActivity.setLocale(this, "ar");
        getSupportActionBar().setTitle("الدراسات العليا");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0529F4")));
        binding.navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        revieveData();

    }

    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_Personal_data:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment_activity_supervisor, new PersonalDataFragment())
                            .commit();
                    return true;
                case R.id.navigation_researchers:
                    setFragment(new ResearchFragment());
                    return true;
                case R.id.navigation_supervisorRequest:
                           setFragment(new ShowOrdersForSupervisor());
                           return true;
            }
            return false;
        }
    };

    //recieve id form MainActivity
    public void revieveData() {
        supervisorId = getIntent().getIntExtra("supervisorID", 0);
    }

    public void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_supervisor, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar_supervisor_activity, menu);
        return true;
    }

    @Override
    public  boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.toolbar_menu_logout:
                setAlerDialog();
                return true;

            case R.id.toolbar_menu_changeInformation:
                Intent intent=new Intent(SupervisorActivity.this,ChangeInformation.class);
                intent.putExtra("ResearcherId",supervisorId);
                startActivity(intent);

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
                        Intent intent=new Intent(SupervisorActivity.this,MainActivity.class);
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