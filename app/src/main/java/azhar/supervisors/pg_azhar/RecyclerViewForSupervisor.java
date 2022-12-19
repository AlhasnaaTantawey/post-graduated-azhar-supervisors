package azhar.supervisors.pg_azhar;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.SupervisorGroupViewBinding;

import java.util.ArrayList;
import java.util.Collection;

public class RecyclerViewForSupervisor extends RecyclerView.Adapter<RecyclerViewForSupervisor.ViewHolder> implements Filterable {


    ArrayList<SupervisorModel> data;
    ArrayList<SupervisorModel> fullList;

    public RecyclerViewForSupervisor(ArrayList<SupervisorModel> data) {
        this.data = data;
        fullList = new ArrayList();
        fullList.addAll(data);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SupervisorGroupViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.supervisor_group_view, parent, false);
        ViewHolder holder = new ViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewForSupervisor.ViewHolder holder, int position) {
        holder.binding.supervisorGroupViewTxtvName.setText(data.get(position).getName());
        holder.binding.supervisorGroupViewTxtvSub.setText(data.get(position).getDegname());
        holder.binding.supervisorGroupViewTxtvNumberOfContinueSupervior.setText(data.get(position).getContinued_researchers() + "");

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<SupervisorModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(fullList);
            } else {
                for (SupervisorModel list : fullList) {
                    if (list.getName().contains(constraint.toString())) {
                        filteredList.add(list);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data.clear();
            data.addAll((Collection<? extends SupervisorModel>) results.values);
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        SupervisorGroupViewBinding binding;

        public ViewHolder(@NonNull SupervisorGroupViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int supervisorId = data.get(getAdapterPosition()).getId();
                    Intent intent = new Intent(v.getContext(), SupervisorActivity.class);
                    intent.putExtra("supervisorID", supervisorId);
                    v.getContext().startActivity(intent);
                }
            });

        }
    }
}
