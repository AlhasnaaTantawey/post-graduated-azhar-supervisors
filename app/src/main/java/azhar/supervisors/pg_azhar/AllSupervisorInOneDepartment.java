package azhar.supervisors.pg_azhar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.ActivityAllSupervisorInOneDepartmentBinding;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AllSupervisorInOneDepartment extends AppCompatActivity {
    ActivityAllSupervisorInOneDepartmentBinding activityAllSupervisorInOneDepartmentBinding;
    RecyclerViewForSupervisor recyclerViewForSupervisor;
    ArrayList<SupervisorModel> allSuperviosrOnOneDepartment;

    Retrofit retrofit;
    EndPoint endPoint;
    public static Integer adminNumber;
    String adminType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAllSupervisorInOneDepartmentBinding = DataBindingUtil.setContentView(this, R.layout.activity_all_supervisor_in_one_department);
        retrofit = Client.getRetrofit();
        endPoint = retrofit.create(EndPoint.class);
        getSupportActionBar().setTitle("الدراسات العليا");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0529F4")));
        recevieData();
        getAllSupervisorOnOneDepartment(adminNumber);
        activityAllSupervisorInOneDepartmentBinding.activityForAllAupervisorOnOneDepartmentTxtDepartmentName.setText(adminType.toString());
        floatingButton(activityAllSupervisorInOneDepartmentBinding.allSuperviosrForOneDepartmentFloatingButtonAdmin, new ShowAllNotesOnOneDepartment());
        MainActivity.setLocale(this,"ar");
    }

 //for make search on supervisors lsit  on one department
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_bar_all_supervisor_in_one_department_activity, menu);
        MenuItem item = menu.findItem(R.id.toolbar_menu_search);
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerViewForSupervisor.getFilter().filter(newText);
                return false;
            }
        });
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

    private void recevieData() {
        adminNumber = getIntent().getIntExtra("adminNumber", 0);
        adminType = getIntent().getStringExtra("adminType");
    }

    //pass the data to adapter and attach recyclerView and defind layout
    public void setAdapter(ArrayList<SupervisorModel> data) {
        recyclerViewForSupervisor = new RecyclerViewForSupervisor(data);
        activityAllSupervisorInOneDepartmentBinding.AllSupervisorInOneDepartmentRecyclerview.setAdapter(recyclerViewForSupervisor);
        activityAllSupervisorInOneDepartmentBinding.AllSupervisorInOneDepartmentRecyclerview.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false));
        recyclerViewForSupervisor.notifyDataSetChanged();
    }

    public void getAllSupervisorOnOneDepartment(int adminNumber) {
        Call<ArrayList<SupervisorModel>> call = endPoint.getAllSuperviosrOnOneDepartment(adminNumber);
        call.enqueue(new Callback<ArrayList<SupervisorModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SupervisorModel>> call, Response<ArrayList<SupervisorModel>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                allSuperviosrOnOneDepartment = response.body();
                setAdapter(allSuperviosrOnOneDepartment);

            }

            @Override
            public void onFailure(Call<ArrayList<SupervisorModel>> call, Throwable t) {
            }
        });
    }

    public void floatingButton(ExtendedFloatingActionButton extendedFloatingActionButton, Fragment fragment) {
        extendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityForAdmin.class);
                startActivity(intent);
            }
        });
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
                        Intent intent=new Intent(AllSupervisorInOneDepartment.this,MainActivity.class);
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