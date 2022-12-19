package azhar.supervisors.pg_azhar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.ResearcherGroupViewBinding;

import azhar.supervisors.pg_azhar.ui.reshershers.ResearchFragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerViewForResearcher extends RecyclerView.Adapter<RecyclerViewForResearcher.ViewHolder> {
    ArrayList<ResearcherModel> data;
    ResearchFragment researchFragment;
    PersonalInformationForOneResearcher personalInformationForOneResearcher;

    public RecyclerViewForResearcher(ArrayList<ResearcherModel> data, ResearchFragment researchFragment) {
        this.data = data;
        this.researchFragment = researchFragment;
        personalInformationForOneResearcher = new PersonalInformationForOneResearcher();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ResearcherGroupViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.researcher_group_view, parent, false);
        ViewHolder holder = new ViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.researcherGroupViewTxtvName.setText(data.get(position).getArabicName());
        holder.binding.researcherGroupViewTxtvSub.setText(data.get(position).getMsgArAddress());
        holder.binding.researcherGroupviewTxtvNumberofExpandsValue.setText(data.get(position).getCount_payments()+"");
        holder.binding.researcherGroupviewTxtvNumberofReportsValue.setText(data.get(position).getCount_reports()+"");
        isGranted(position, holder.binding.supervisorGroupViewTxtvIsgranted);
        qualificationGrade(position, holder.binding.supervisorGroupViewTxtvQualificationGrad);
        if (data.get(position).getIsGranted()!=null&&data.get(position).getIsCancelled()!=null&&data.get(position).getIsGranted() == 0 && data.get(position).getIsCancelled() == 0) {
            showTextView(holder,View.VISIBLE);
            holder.binding.researcherGroupviewTxtvNumberofyearsValue.setText(numberOfYears(data.get(position)) + "");
        } else {
           showTextView(holder,View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ResearcherGroupViewBinding binding;

        public ViewHolder(@NonNull ResearcherGroupViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendId(data.get(getAdapterPosition()).getId());
                    setFragment(personalInformationForOneResearcher);
                }
            });
        }

        // begin fragment
        public void setFragment(Fragment fragment) {
            researchFragment.getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_supervisor, fragment)
                    .addToBackStack(null)
                    .commit();
        }

        //send research Id for personalInformationForOneResearcherFragment
        public void sendId(int id) {
            Bundle bundle = new Bundle();
            bundle.putInt("ResearcherID", id);
            personalInformationForOneResearcher.setArguments(bundle);
            android.util.Log.i("islam", id + "");
        }
    }

    // researcher is granted or not
    public void isGranted(int position, TextView txt) {
        if(data.get(position).getIsGranted()!=null){
        if (data.get(position).getIsGranted() == -1) {
            txt.setText(R.string.granted);
        } else if (data.get(position).getIsGranted() == 0) {
            if (data.get(position).getIsCancelled() == 0) {
                txt.setText(R.string.continous);
            } else {
                txt.setText(R.string.cancelled);
            }

        }
        }else {
            txt.setText("غير مسجل");
        }
    }

    // method to determine PhD or Master's
    public void qualificationGrade(int position, TextView txt) {
        if (data.get(position).getDegId() == 1) {
            txt.setText(R.string.masters);
        } else if (data.get(position).getDegId() == 2) {
            txt.setText(R.string.doctorate);
        } else {
            txt.setText("---------");

        }
    }

    public long numberOfYears(ResearcherModel researcherModel) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/YYYY");
        LocalDate currentDate = LocalDate.now();
        Date date1 = new Date(currentDate.format(dateTimeFormatter));
        LocalDate registerationDate = LocalDate.parse(researcherModel.getRegsitretion_Period());
        Date date2 = new Date(registerationDate.format(dateTimeFormatter));

        long numberOfDayInCurrentDate = date1.getTime() / 1000 / 60 / 60 / 24;
        long numberOfDayInRegisterationDate = date2.getTime() / 1000 / 60 / 60 / 24;
        long diffBetweenDatesInDays = numberOfDayInCurrentDate - numberOfDayInRegisterationDate;
        return diffBetweenDatesInDays / 365;
    }

    //make textView VISABLE or GONE
    public void showTextView(ViewHolder holder,int i) {
        holder.binding.researcherGroupviewTxtvNumberofyearsValue.setVisibility(i);
        holder.binding.researcherGroupviewTxtvNumberofyears.setVisibility(i);
        holder.binding.researcherGroupviewTxtvNumberofexpands.setVisibility(i);
        holder.binding.researcherGroupviewTxtvNumberofExpandsValue.setVisibility(i);
        holder.binding.researcherGroupviewTxtvNumberofReports.setVisibility(i);
        holder.binding.researcherGroupviewTxtvNumberofReportsValue.setVisibility(i);
        holder.binding.researcherGroupviewTxtvNumberofExtendYears.setVisibility(i);
        holder.binding.researcherGroupviewTxtvNumberofExtendsYearsValue.setVisibility(i);
    }

}
