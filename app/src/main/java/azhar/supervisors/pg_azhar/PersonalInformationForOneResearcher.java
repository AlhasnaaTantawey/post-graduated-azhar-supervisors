package azhar.supervisors.pg_azhar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.FragmentPersonalInformationForOneResearcherBinding;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PersonalInformationForOneResearcher extends Fragment {

    FragmentPersonalInformationForOneResearcherBinding binding;
    Retrofit retrofit;
    EndPoint endPoint;
    public static final String RESEARCHERID = "currentId";
    public static final String RESTERATION_DATE = "resterationDate";
    public static final String TITLE = "title";
    public static final String DEPT_ID = "dept_id";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal_information_for_one_researcher, container, false);
        retrofit = Client.getRetrofit();
        endPoint = retrofit.create(EndPoint.class);
        getResearcher(recieveId());
        floatingButton(binding.fragmentOneResearcherFloatingButtonNotes, new NotesFragment());
        floatingButton(binding.fragmentOneResearcherFloatingButtonReport, new ReportFragment());
        return binding.getRoot();

    }


    public void getResearcher(int id) {
        Call<ResearcherModel> call = endPoint.getOneResearcher(id);
        call.enqueue(new Callback<ResearcherModel>() {
            @Override
            public void onResponse(Call<ResearcherModel> call, Response<ResearcherModel> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                ResearcherModel researcher = response.body();
                handleData(researcher.getArabicName(), binding.personalInformationForResearcherTextViewNameValue);
                handleData(researcher.getCurrentPlace(), binding.personalInformationforResearcherTextViewAddressValue);
                handleData(researcher.getNationalID(), binding.personalInformationForResearchertextViewSsnValue);
                handleData(researcher.getRegsitretion_Period(), binding.personalInformationforResearcherTextViewRegisterdateValue);
                handleData(researcher.getSpecialization(), binding.personalInformationforResearcherTextViewSpecialityValue);
                handleData(researcher.getMsgArAddress(), binding.personalInformationforResearcherTextViewMsAddressValue);
                handleData(researcher.getDepartment(), binding.personalInformationforResearcherTextViewDepartmentValue);
                handleData(researcher.getMobaile(), binding.personalInformationforResearcherTextViewPhoneValue);
                handleData(researcher.getDegree(), binding.personalInformationforResearcherTextViewDegreeValue);
                handleData(researcher.getQualUnivesity(), binding.personalInformationForResearcherTextViewIsExternalValue);

                if (researcher.getIsGranted() == null) {
                    binding.personalInformationforResearcherTextViewGrantedValue.setText("غير مسجل");

                } else if (researcher.getIsGranted() == 0) {
                    binding.personalInformationforResearcherTextViewGrantedValue.setText("مستمر");

                } else if (researcher.getIsGranted() == -1) {
                    binding.personalInformationforResearcherTextViewGrantedValue.setText("منح");
                }
                buttonRequired(researcher);

            }

            @Override
            public void onFailure(Call<ResearcherModel> call, Throwable t) {
            }
        });
    }

    public int recieveId() {
        int receivedID = 0;
        if (getArguments() != null) {
            receivedID = getArguments().getInt("ResearcherID");
        }
        return receivedID;
    }

    public void floatingButton(ExtendedFloatingActionButton extendedFloatingActionButton, Fragment fragment) {
        extendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData(fragment, null);
                setFragment(fragment);
            }
        });
    }

    public void buttonRequired(ResearcherModel researcher) {
        binding.fragmentOneResearcherFloatingButtonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (researcher.getIsGranted()!=null&&researcher.getIsGranted() == 0 && researcher.getIsCancelled() == 0) {
                    // Initializing the popup menu and giving the reference as current context
                    PopupMenu popupMenu = new PopupMenu(getContext(), binding.fragmentOneResearcherFloatingButtonRequest);
                    // Inflating popup menu from popup_menu.xml file
                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId() == R.id.extend) {
                                if (isMorethanFive(researcher)) {
                                    RequestAnExtensionFragment requestAnExtensionFragment = new RequestAnExtensionFragment();
                                    sendData(requestAnExtensionFragment, researcher);
                                    setFragment(requestAnExtensionFragment);
                                } else {
                                    Toast.makeText(getContext(), "لم يتجاوز مده الخمس سنوات", Toast.LENGTH_SHORT).show();
                                }
                            } else if (menuItem.getItemId() == R.id.change_title) {
                                ChangeTitleFragment changeTitleFragment = new ChangeTitleFragment();
                                sendData(changeTitleFragment, researcher);
                                setFragment(changeTitleFragment);
                            } else if (menuItem.getItemId() == R.id.change_supervisor) {
                                ChangeSupervisorsFragment changeSupervisorFragment = new ChangeSupervisorsFragment();
                                sendData(changeSupervisorFragment, researcher);
                                setFragment(changeSupervisorFragment);
                            }

                            return true;
                        }
                    });
                    // Showing the popup menu
                    popupMenu.show();
                } else {
                    if (researcher.getIsGranted() == null) {
                        Toast.makeText(getContext(), "الباحث غير مسجل", Toast.LENGTH_SHORT).show();}
                        else if (researcher.getIsGranted() == -1) {
                            Toast.makeText(getContext(), "الباحث منح بالفعل", Toast.LENGTH_SHORT).show();

                        } else if (researcher.getIsCancelled() == -1) {
                            Toast.makeText(getContext(), "الباحث تم فصله", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            });
        }

        private void setFragment (Fragment fragment){
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment_activity_supervisor, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        public void sendData (Fragment fragment, ResearcherModel researcherModel){
            Bundle args = new Bundle();
            args.putInt(RESEARCHERID, recieveId());
            if (researcherModel != null) {
                args.putString(RESTERATION_DATE, researcherModel.getRegsitretion_Period());
                args.putString(TITLE, researcherModel.getMsgArAddress());
                args.putInt(DEPT_ID, researcherModel.getDeptId());
            }
            fragment.setArguments(args);
        }

        public void handleData (String text, TextView textView){
            if (text == null) {
                textView.setText("_______");
            } else {
                textView.setText(text.toString());
            }
        }

        public boolean isMorethanFive (ResearcherModel researcherModel){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/YYYY");
            LocalDate currentDate = LocalDate.now();
            Date date1 = new Date(currentDate.format(dateTimeFormatter));
            LocalDate registerationDate = LocalDate.parse(researcherModel.getRegsitretion_Period());
            Date date2 = new Date(registerationDate.format(dateTimeFormatter));
            long numberOfDayInFiveYear = 5 * 365;
            long numberOfDayInCurrentDate = date1.getTime() / 1000 / 60 / 60 / 24;
            long numberOfDayInRegisterationDate = date2.getTime() / 1000 / 60 / 60 / 24;
            long diffBetweenDatesInDays = numberOfDayInCurrentDate - numberOfDayInRegisterationDate;
            if (diffBetweenDatesInDays > numberOfDayInFiveYear) {
                return true;
            } else {
                return false;
            }

        }
    }