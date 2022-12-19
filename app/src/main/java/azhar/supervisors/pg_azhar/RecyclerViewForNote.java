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

public class RecyclerViewForNote extends RecyclerView.Adapter<RecyclerViewForNote.ViewHolder> {
    ArrayList<Note> data;

    ResearcherInformationNote researcherInformationNote = null;
    SupervisorInformationNote supervisorInformationNote = null;
    ResearcherNoteForSupervisors researcherNoteForSupervisors = null;
    SupervisorNoteForResearchers supervisorNoteForResearchers = null;

    public RecyclerViewForNote(ArrayList<Note> data, ResearcherInformationNote researcherInformationNote) {
        this.data = data;
        this.researcherInformationNote = researcherInformationNote;
    }

    public RecyclerViewForNote(ArrayList<Note> data, SupervisorInformationNote supervisorInformationNote) {
        this.data = data;
        this.supervisorInformationNote = supervisorInformationNote;
    }

    public RecyclerViewForNote(ArrayList<Note> data, ResearcherNoteForSupervisors researcherNoteForSupervisors) {
        this.data = data;
        this.researcherNoteForSupervisors = researcherNoteForSupervisors;
    }

    public RecyclerViewForNote(ArrayList<Note> data, SupervisorNoteForResearchers supervisorNoteForResearchers) {
        this.data = data;
        this.supervisorNoteForResearchers = supervisorNoteForResearchers;
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
        Integer  noteType = data.get(position).getNoteType();
        if (noteType==1|noteType==2) {
            holder.binding.RequestGroupViewTxtvName.setText(data.get(position).getSupervisorName());
        }else if (noteType==3||noteType==4){
            holder.binding.RequestGroupViewTxtvName.setText(data.get(position).getResearcherName());
        }
        holder.binding.RequestGroupViewTxtvSub.setText(data.get(position).getNote());
        holder.binding.RequestGroupViewImage.setImageResource(R.drawable.notes);

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
                    if (researcherInformationNote != null) {
                        setFragment(researcherInformationNote, new ResearcherInformationOneNoteDetails());
                    } else if (supervisorInformationNote != null) {
                        setFragment(supervisorInformationNote, new SuperviosrInformationOneNoteDetails());
                    } else if (researcherNoteForSupervisors != null) {
                        setFragment(researcherNoteForSupervisors, new SupervisorForResearchersOneNoteDetails());
                    } else if (supervisorNoteForResearchers != null) {
                        setFragment(supervisorNoteForResearchers, new SupervisorForResearchersOneNoteDetails());
                    }
                }
            });
        }


        public void sendIdNote(Fragment fragment) {
            Bundle bundle = new Bundle();
            bundle.putInt("NoteId", data.get(getAdapterPosition()).getNoteId());
            fragment.setArguments(bundle);
        }

        public void setFragment(Fragment parent, Fragment fragment) {
            sendIdNote(fragment);
            parent.getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.adminActivity_fragmetnArea, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
