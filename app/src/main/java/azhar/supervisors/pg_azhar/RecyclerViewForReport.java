package azhar.supervisors.pg_azhar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.RequestGroupViewBinding;

import java.util.ArrayList;

public class RecyclerViewForReport extends RecyclerView.Adapter<RecyclerViewForReport.ViewHolder>{
    ArrayList<ReportModel> data;
    ShowAllReportsInOneDepartment showAllReportsInOneDepartment;

    public RecyclerViewForReport(ArrayList<ReportModel> data,ShowAllReportsInOneDepartment showAllReportsInOneDepartment) {
        this.data = data;
        this.showAllReportsInOneDepartment=showAllReportsInOneDepartment;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RequestGroupViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.request_group_view, parent, false);
        ViewHolder holder = new ViewHolder(binding);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.binding.RequestGroupViewImage.setImageResource(R.drawable.checklist);
        holder.binding.RequestGroupViewTxtvName.setText(data.get(position).getSuperviosrName());
        holder.binding.RequestGroupViewTxtvSub.setText(data.get(position).getResearcherName());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RequestGroupViewBinding binding;

        public ViewHolder(@NonNull RequestGroupViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  setFragment(new ReportInformationFragment());
                }
            });
        }

        // begin fragment
        public void setFragment(Fragment fragment) {
            Bundle bundle=new Bundle();
            bundle.putInt("ReportId",data.get(getAdapterPosition()).getRep_id());
            fragment.setArguments(bundle);
            showAllReportsInOneDepartment.getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.adminActivity_fragmetnArea, fragment)
                    .addToBackStack(null)
                    .commit();
        }

    }
}


