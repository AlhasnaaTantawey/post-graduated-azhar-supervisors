package azhar.supervisors.pg_azhar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.FragmentNoteForSupervisorsInformationBinding;

import azhar.supervisors.pg_azhar.ui.personalData.PersonalDataFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class NoteForSupervisorsInformation extends Fragment {
    FragmentNoteForSupervisorsInformationBinding fragmentNoteForSupervisorsInformationBinding;
    Note note;
    Retrofit retrofit;
    EndPoint endPoint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentNoteForSupervisorsInformationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_note_for_supervisors_information, container, false);
        retrofit = Client.getRetrofit();
        endPoint = retrofit.create(EndPoint.class);
        clickSendNote();
        return fragmentNoteForSupervisorsInformationBinding.getRoot();
    }

    public void clickSendNote() {
        fragmentNoteForSupervisorsInformationBinding.buttonNotesFragmentFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentNoteForSupervisorsInformationBinding.edittextNotesFragmentNotes.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "يجب ان تكتب الملاحظه اولا", Toast.LENGTH_LONG).show();
                } else {
                    setAlerDialog();
                }
            }
        });
    }

    public void sendNote() {
        note = new Note();
        note.setSu_id(SupervisorActivity.supervisorId);
        note.setNote(fragmentNoteForSupervisorsInformationBinding.edittextNotesFragmentNotes.getText().toString());
        note.setDeptId(PersonalDataFragment.supervisor.getDeptNumber());
        note.setNoteType(2);
        Call<Note> call = endPoint.sendNotes(note);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "فشل فى ارسال الملاحظه ", Toast.LENGTH_LONG).show();
                    return;
                }
                Note note = response.body();

                if (note != null) {
                    Toast.makeText(getContext(), "تم ارسال الملاحظه ", Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                } else {
                    Toast.makeText(getContext(), "فشل فى ارسال الملاحظه ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                Toast.makeText(getContext(), "فشل فى ارسال الملاحظه ", Toast.LENGTH_LONG).show();
            }
        });

    }

    // make alerDialog
    public void setAlerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle("تسجيل الملاحظه")
                .setMessage("هل تريد تسجيل الملاحظه؟")
                .setCancelable(false)
                .setPositiveButton(" نعم ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendNote();

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