package azhar.supervisors.pg_azhar;

import android.os.Bundle;
import android.print.PrintAttributes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.RequestGroupViewBinding;
import com.uttampanchasara.pdfgenerator.CreatePdf;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecyclerViewForRequest extends RecyclerView.Adapter<RecyclerViewForRequest.ViewHolder> {
    ArrayList<RecievedRequestModel> data;
    ShowOrdersForSupervisor showOrdersForSupervisor;
    AllExtendsRequest allExtendsRequest;
    AllChangeSupervisorsRequest allChangeSupervisorsRequest;
    AllChangeTitleRequest allChangeTitleRequest;
    Retrofit retrofit;
    EndPoint endPoint;
    ModifySupervisorsModel changeSupervisorsRequestModel;
    String arname,fac_Council,forign,msgArAddress,uni_Council,
            specialization,qualUnivesity,deptName,degName ,reasons,yearExtension,
    esntial,arabicName,nationality,departName ,qualification , lastFac,
    oldAddress,newArabicAddress,department_approvement,faculity_approvement,degname;
    List<SupervisorModel2> oldSupervisors,newSupervisor;
    ExtensionRequestModel2 extensionRequestModel2;
    int extendPeriod;
    List<SupervisorModel2> list;
    InfoToChangeMessageTitleModel changeTitleRequestModel;


    public RecyclerViewForRequest(ArrayList<RecievedRequestModel> data, ShowOrdersForSupervisor showOrdersForSupervisor) {
        this.data = data;
        this.showOrdersForSupervisor = showOrdersForSupervisor;

    }

    public RecyclerViewForRequest(ArrayList<RecievedRequestModel> data, AllExtendsRequest allExtendsRequest) {
        this.data = data;
        this.allExtendsRequest = allExtendsRequest;

    }

    public RecyclerViewForRequest(ArrayList<RecievedRequestModel> data, AllChangeSupervisorsRequest allChangeSupervisorsRequest) {
        this.data = data;
        this.allChangeSupervisorsRequest = allChangeSupervisorsRequest;

    }

    public RecyclerViewForRequest(ArrayList<RecievedRequestModel> data, AllChangeTitleRequest allChangeTitleRequest) {
        this.data = data;
        this.allChangeTitleRequest = allChangeTitleRequest;

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
        if (showOrdersForSupervisor != null) {
            holder.binding.RequestGroupViewTxtvName.setText(data.get(position).getResearcher_name());
        }else {
            holder.binding.RequestGroupViewTxtvName.setText(data.get(position).getSupervisorName());
        }
        if (data.get(position).getType() == 1) {
            holder.binding.RequestGroupViewImage.setImageResource(R.drawable.expanddate);
            holder.binding.RequestGroupViewTxtvSub.setText("طلب مد");
        } else if (data.get(position).getType() == 2) {
            holder.binding.RequestGroupViewTxtvSub.setText("طلب تغير مشرفين");
            holder.binding.RequestGroupViewImage.setImageResource(R.drawable.exchange_supervisor);
        } else if (data.get(position).getType() == 3) {
            holder.binding.RequestGroupViewTxtvSub.setText("طلب تغير عنوان");
            holder.binding.RequestGroupViewImage.setImageResource(R.drawable.exchange_title);
        }


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
            retrofit=Client.getRetrofit();
            endPoint=retrofit.create(EndPoint.class);
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (showOrdersForSupervisor != null) {
                        setFragment(showOrdersForSupervisor, new RequestDetails(), R.id.nav_host_fragment_activity_supervisor);
                    } else if (allChangeSupervisorsRequest != null) {
                        getChangeSupervisorReqest(data.get(getAdapterPosition()).getOrder_id());
                    } else if (allChangeTitleRequest != null) {
                        getChangeTitleReqest(data.get(getAdapterPosition()).getOrder_id());
                    } else if (allExtendsRequest != null) {
                        getExtendReqest(data.get(getAdapterPosition()).getOrder_id());
                    }
                }
            });
        }

        // begin fragment
        public void setFragment(Fragment parent, Fragment fragment, int id) {
            sendId(fragment);
            parent.getParentFragmentManager()
                    .beginTransaction()
                    .replace(id, fragment)
                    .addToBackStack(null)
                    .commit();
        }

        //send research Id for personalInformationForOneResearcherFragment
        public void sendId(Fragment fragment) {
            Bundle bundle = new Bundle();
            bundle.putInt("RequestId", data.get(getAdapterPosition()).getOrder_id());
            fragment.setArguments(bundle);
        }
    }

    public void getChangeSupervisorReqest(int requestId){
        Call<ModifySupervisorsModel> call=endPoint.getOneChangeSupervisorRequest(requestId);
        call.enqueue(new Callback<ModifySupervisorsModel>() {
            @Override
            public void onResponse(Call<ModifySupervisorsModel> call, Response<ModifySupervisorsModel> response) {
                if(!response.isSuccessful()){
                    return;
                }

                changeSupervisorsRequestModel=response.body();
                arname=changeSupervisorsRequestModel.getArname();
                fac_Council=changeSupervisorsRequestModel.getFac_Council();
                forign=changeSupervisorsRequestModel.getForign();
                msgArAddress=changeSupervisorsRequestModel.getMsgArAddress();
                uni_Council=changeSupervisorsRequestModel.getUni_Council();
                specialization=changeSupervisorsRequestModel.getSpecialization();
                qualUnivesity=changeSupervisorsRequestModel.getQualUnivesity();
                deptName=changeSupervisorsRequestModel.getDeptName();
                degName=changeSupervisorsRequestModel.getDegname();
                oldSupervisors=changeSupervisorsRequestModel.getOldSupervisors();
                newSupervisor=changeSupervisorsRequestModel.getNewSupervisors();
                reasons=changeSupervisorsRequestModel.getReasons();


                if(oldSupervisors.size()==2){
                    changeSupervisorPrintPdf2();
                }else if(oldSupervisors.size()==3){
                    changeSupervisorPrintPdf3();
                }
                else if(oldSupervisors.size()==4){
                    changeSupervisorPrintPdf4();
                }


            }

            @Override
            public void onFailure(Call<ModifySupervisorsModel> call, Throwable t) {

            }
        });
    }
    //ChangeSupervisor
    private void changeSupervisorPrintPdf3(){
new Cre
        new CreatePdf(allChangeSupervisorsRequest.getContext())
                .setPdfName("FirstPdf")
                .openPrintDialog(true)
                .setPageSize(PrintAttributes.MediaSize.ISO_A4)
//                .setPageSize()
                .setContent("<table style=\"border: none;border-collapse: collapse;empty-cells: show;max-width: 100%;float: left;width: 680pt;margin-left: 0.1in;margin-right: 0.1in;\">\n" +
                        "    <tbody>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;\">AL-AZHAR</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;\">UNIVERSITY</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td rowspan=\"3\" style=\"border: 1pt solid windowtext;width: 163.7pt;padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span dir=\"LTR\" style=\"font-size: 13px;\">&nbsp;<img src=\"data:image/jpeg;base64,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\" style=\"width: 65px; height: 82.7273px;\"></span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: none;border-image: initial;user-select: text;width: 186.85pt;padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-family: \"Arabic Transparent\", \"sans-serif\";'>جامعـــة الأزهـــر</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: none;border-right: none;border-bottom: none;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;\">FACULTY OF ENGINEERING</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: none;border-left: none;border-image: initial;user-select: text;width: 186.85pt;padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-family: \"Arabic Transparent\", \"sans-serif\";'>كلية الهندســــــة</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;background: rgb(204, 204, 204);padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span dir=\"RTL\">&nbsp;</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: none;border-image: initial;user-select: text;width: 186.85pt;background: rgb(204, 204, 204);padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-family: \"Arabic Transparent\", \"sans-serif\";'>الدراسات الـعلــيــا</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "    </tbody>\n" +
                        "</table>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 15px;\"><span style=\"text-decoration: none;\">&nbsp;</span></span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<u>طلب الموافقة على تعـديل لجنة الأشراف لدرجةالتخصص{" +
                        degName +
                        "</u>} &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">أسـم الباحـث / " +
                        arname +
                        " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; الجـنسية / " +
                        forign +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">القـسـم / &nbsp; " +
                        deptName +
                        " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; الدرجة / التخصص(" +
                        degName +
                        ")" +
                        deptName +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">الـمـؤهـل الـدراسـي : " +
                        deptName +
                        "(" +
                        deptName +
                        ")" +
                        " –- جامعة " +
                        qualUnivesity +
                        "</span></strong></p>\n" +
                        "<div style=\"border: 1.5pt double windowtext;padding: 1pt 0in;\">\n" +
                        "    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";border: none;padding: 0in;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">موضوع الرسالة / \"" +
                        msgArAddress +
                        "(</span></strong><strong style=\"font-weight: 700;\"><span dir=\"LTR\" style=\"font-size: 15px;\">(A356</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">&nbsp;" +
                        "\"</span></strong></p>\n" +
                        "    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";border: none;padding: 0in;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">&nbsp;</span></strong></p>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">موافقة الكلية على التسجيل / : " +
                        uni_Council +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">&nbsp; &nbsp; &nbsp;\" &nbsp; &nbsp;جامعة &nbsp;\" &nbsp; &nbsp;\" &nbsp; &nbsp; &nbsp; &nbsp;/ : " +
                        fac_Council +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 15px;\">لجنة الأشراف المعـتمدة &nbsp;:ـ</span></u></strong></p>\n" +
                        "<div align=\"right\">\n" +
                        "    <table dir=\"rtl\" style=\"border: none;border-collapse: collapse;empty-cells: show;max-width: 100%;margin-left: -0.3pt;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border: 1pt solid windowtext;width: 28.35pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">م</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 140.25pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">لجنة الأشراف&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 63.8pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">الدرجـة</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">جهة العمل</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 26.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">1</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 140.25pt;padding: 0in 5.4pt;height: 26.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        oldSupervisors.get(0).getName() +
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 63.8pt;padding: 0in 5.4pt;height: 26.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        oldSupervisors.get(0).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 26.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\"" +
                        ">" +
                        oldSupervisors.get(0).getWorkplace() +
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">2</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 190.25pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\"" +
                        ">" +
                        oldSupervisors.get(1).getName() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 70.8pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        oldSupervisors.get(1).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 340.95pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        oldSupervisors.get(1).getWorkplace() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">3</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 140.25pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        oldSupervisors.get(2).getName()+
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 63.8pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        oldSupervisors.get(2).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        oldSupervisors.get(2).getWorkplace() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 15px;\">لجنة الأشراف المقترحة &nbsp;:ـ &nbsp; &nbsp; &nbsp;&nbsp;</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 15px;\">لجنة الأشراف المعـتمدة &nbsp;:ـ</span></u></strong></p>\n" +
                        "<div align=\"right\">\n" +
                        "    <table dir=\"rtl\" style=\"border: none;border-collapse: collapse;empty-cells: show;max-width: 100%;margin-left: -0.3pt;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border: 1pt solid windowtext;width: 28.35pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">م</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 140.25pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">لجنة الأشراف&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 63.8pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">الدرجـة</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">جهة العمل</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 26.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">1</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 140.25pt;padding: 0in 5.4pt;height: 26.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(0).getName() +
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 63.8pt;padding: 0in 5.4pt;height: 26.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(0).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 26.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\"" +
                        ">" +
                        newSupervisor.get(0).getWorkplace() +
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">2</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 190.25pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\"" +
                        ">" +
                        newSupervisor.get(1).getName() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 70.8pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(1).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 340.95pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(1).getWorkplace() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 13px;color: black;\">أسباب التعديل / " +
                        reasons +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">موافقة مجلس القسم على تعـديل لجنة الإشراف : &nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;م</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">موافقة مجلس الكلية على تعـديل لجنة الأشراف :&nbsp;&nbsp;&nbsp;&nbsp;/ &nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;&nbsp;م</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 21px;color: black;\">السيد الأستاذ الدكتور / نائب رئيس الجامعة</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">برجاء التكرم بالموافقة نحـو اعـتماد التعـديـل المشار إلـيه</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><em><span style=\"font-size: 15px;color: black;\">وتفضلوا بقبول فائق الاحترام ،،،،</span></em></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">مديرالدراسات العـليا &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; مدير عـام الكلية &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; يعتمد ،،،</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"color: black;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; أ.د./ وكيل الكلية للدراسات العليا والبحوث</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 15px;color: black;\"><span style=\"text-decoration: none;\">&nbsp;</span></span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 15px;color: black;\">الإدارة العامة للدراسات العـليا والبحوث بالجامعة</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">روجعت البيانات والإجراءات صحيحة واللوائح مطابقة&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">تحريرا في &nbsp; &nbsp; &nbsp; / &nbsp; &nbsp; &nbsp; &nbsp;/ &nbsp; 20م</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">الموظف المختص &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;مدير الإدارة &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;( المدير العام )</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">موافقة ،،،،يعتمد ،،،، &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"color: black;\">(أ.د./ نائب رئيس الجامعة المختص ) &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;color: black;\">( أ.د./ رئيس الجـامعـة&nbsp;</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">) &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;مصادقة مجلس الجامعة بجلستها رقم ( &nbsp; &nbsp; &nbsp; &nbsp; ) بتاريخ &nbsp; &nbsp; / &nbsp; &nbsp; / &nbsp; 20م</span></strong></p>\n" +
                        "<p style=\"margin-bottom: 10px !important;\"><span dir=\"RTL\" style='font-size: 16px;line-height: 18.4px;font-family: \"Times New Roman\", \"serif\";'><br>&nbsp;</span></p>\n" +
                        "<p style='margin: 0in 0in 10pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 18.4px;'><span dir=\"RTL\">&nbsp;</span></p>\n" +
                        "<p style=\"margin-bottom: 10px !important;\"><br style=\"color: rgb(0, 0, 0);font-family: &quot;Times New Roman&quot;;font-size: medium;font-style: normal;font-weight: 400;text-align: start;text-indent: 0px;\"></p>"+
                        ""+
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:justify;font-size:16px;font-family:\"Times New Roman\",\"serif\";'><br></p>"+
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:justify;font-size:16px;font-family:\"Times New Roman\",\"serif\";'><br></p>"+
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:justify;font-size:16px;font-family:\"Times New Roman\",\"serif\";'><br></p>"+
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:justify;font-size:16px;font-family:\"Times New Roman\",\"serif\";'><br></p>"+
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:justify;font-size:16px;font-family:\"Times New Roman\",\"serif\";'><br></p>"+
                        "<table style=\"border: none;border-collapse: collapse;empty-cells: show;max-width: 100%;float: left;width: 680pt;margin-left: 0.1in;margin-right: 0.1in;\">\n" +
                        "    <tbody>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">AL-AZHAR</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">UNIVERSITY</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td rowspan=\"3\" style=\"border: 1pt solid windowtext;width: 163.7pt;padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span dir=\"LTR\">&nbsp;<img src=\"data:image/jpeg;base64,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\" style=\"width: 73px; height: 92.3805px;\" width=\"170\" height=\"216\"></span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: none;border-image: initial;user-select: text;width: 186.85pt;padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-size: 21px;font-family: \"Arabic Transparent\", \"sans-serif\";'>جامعـــة الأزهـــر</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: none;border-right: none;border-bottom: none;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">FACULTY OF ENGINEERING</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: none;border-left: none;border-image: initial;user-select: text;width: 186.85pt;padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-size: 21px;font-family: \"Arabic Transparent\", \"sans-serif\";'>كلية الهندســــــة</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;background: rgb(204, 204, 204);padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span dir=\"RTL\" style=\"font-size: 21px;\">&nbsp;</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: none;border-image: initial;user-select: text;width: 186.85pt;background: rgb(204, 204, 204);padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-size: 21px;font-family: \"Arabic Transparent\", \"sans-serif\";'>الدراسات الـعلــيــا</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "    </tbody>\n" +
                        "</table>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'>&nbsp;</p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'>&nbsp;</p>\n" +
                        "<p style='margin: 0in 0in 10pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 18.4px;'><strong style=\"font-weight: 700;\"><span dir=\"RTL\" style=\"font-size: 19px;line-height: 21.85px;\">السيـد الأســتـاذ / مــديـر الإدارة العـامــة للـدراســات العـليـا والبحـوث بالجـامعــة</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">تحـيــة طيبـة وبعــد ,,</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">&nbsp;نتشــرف بأن نـحـيـط عـلم سيـادتـكم بأن مـجـلس الكـليــة المنعـقـد &nbsp;بجـلستـه رقــم {  } بتـاريـخ : &nbsp; &nbsp; / &nbsp; &nbsp; &nbsp;/ &nbsp; &nbsp;20م</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">قـد وافـق عـلى قــرار مجـلس قـســم&nbsp;" +
                        deptName +
                        "&nbsp;بالمـوافـقــة عـلى تـعـديـل لجــنـة الأشـراف الخـاصــة&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">بالبـاحـث /.&nbsp;" +
                        arname +
                        "&nbsp;المسجـلبالـدراسـات العـلــيـا بالكـلـيـة للحـصــول عـلــى درجـة &nbsp; التخـصص { " +
                        degName +
                        "}، العالميـة { " +
                        degName +
                        "}فـــي هـنـدســة &nbsp;" +
                        deptName +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">&nbsp;الأتي أسماؤهــم بعــد :-</span></strong></p>\n" +
                        "<div align=\"center\">\n" +
                        "    <table dir=\"rtl\" style=\"border: none;border-collapse: collapse;empty-cells: show;max-width: 100%;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border: 1pt solid black;width: 26.55pt;padding: 0in 5.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">م</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid black;border-right: none;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;user-select: text;width: 212.6pt;padding: 0in 5.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">الاســــــــــــــم</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid black;border-right: none;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;user-select: text;width: 70.9pt;padding: 0in 5.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">الـدرجـــــة</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid black;border-right: none;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;user-select: text;width: 211.8pt;padding: 0in 5.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">القـســــــــــــــم</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 26.3pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">1</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 147.35pt;padding: 0in 5.4pt;height: 26.3pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(0).getName() +
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 56.7pt;padding: 0in 5.4pt;height: 26.3pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(0).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 26.3pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(0).getWorkplace() +
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">2</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 147.35pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(1).getName() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 56.7pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(1).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(1).getWorkplace() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">ومــرفـق طيه نـمـاذج التـعـديـل وصـورة طـبق الأصـل بمـذكـرة مـجـلس القـسـم وكـذلك تـقـريـر لجـنـة الأشــراف.</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">مـرسـل بـرجـاء العـلم والإحاطة واتـخـاذ اللازم,,,</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 21px;\">وتـفـضـلوا بقـبول فـائق الاحـترام ،،</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><span style=\"font-size: 19px;\">&nbsp;</span></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;\">الـدراســات العـليـا &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;color: black;\">رئيس الأدارة</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;\">مــديـر عــام الكـلـيــة &nbsp; &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><span dir=\"LTR\">&nbsp;</span></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><span dir=\"LTR\">&nbsp;</span></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><span dir=\"LTR\">&nbsp;</span></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><span dir=\"LTR\">&nbsp;</span></p>\n" +
                        "<p style=\"margin-bottom: 10px !important;\"><br style=\"color: rgb(0, 0, 0);font-family: &quot;Times New Roman&quot;;font-size: medium;font-style: normal;font-weight: 400;text-align: start;text-indent: 0px;\"></p>").create();
    }

    private void changeSupervisorPrintPdf2(){
        new CreatePdf(allChangeSupervisorsRequest.getContext())
                .setPdfName("FirstPdf")
                .openPrintDialog(true)
                .setPageSize(PrintAttributes.MediaSize.ISO_A4)
                .setContent("<table style=\"border: none;border-collapse: collapse;empty-cells: show;max-width: 100%;float: left;width: 680pt;margin-left: 0.1in;margin-right: 0.1in;\">\n" +
                        "    <tbody>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;\">AL-AZHAR</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;\">UNIVERSITY</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td rowspan=\"3\" style=\"border: 1pt solid windowtext;width: 163.7pt;padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span dir=\"LTR\" style=\"font-size: 13px;\">&nbsp;<img src=\"data:image/jpeg;base64,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\" style=\"width: 65px; height: 82.7273px;\"></span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: none;border-image: initial;user-select: text;width: 186.85pt;padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-family: \"Arabic Transparent\", \"sans-serif\";'>جامعـــة الأزهـــر</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: none;border-right: none;border-bottom: none;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;\">FACULTY OF ENGINEERING</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: none;border-left: none;border-image: initial;user-select: text;width: 186.85pt;padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-family: \"Arabic Transparent\", \"sans-serif\";'>كلية الهندســــــة</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;background: rgb(204, 204, 204);padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span dir=\"RTL\">&nbsp;</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: none;border-image: initial;user-select: text;width: 186.85pt;background: rgb(204, 204, 204);padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-family: \"Arabic Transparent\", \"sans-serif\";'>الدراسات الـعلــيــا</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "    </tbody>\n" +
                        "</table>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 15px;\"><span style=\"text-decoration: none;\">&nbsp;</span></span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<u>طلب الموافقة على تعـديل لجنة الأشراف لدرجةالتخصص{" +
                        degName +
                        "</u>} &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">أسـم الباحـث / " +
                        arname +
                        " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; الجـنسية / " +
                        forign +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">القـسـم / &nbsp; " +
                        deptName +
                        " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; الدرجة / التخصص(" +
                        degName +
                        ")" +
                        deptName +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">الـمـؤهـل الـدراسـي : " +
                        deptName +
                        "(" +
                        deptName +
                        ")" +
                        " –- جامعة " +
                        qualUnivesity +
                        "</span></strong></p>\n" +
                        "<div style=\"border: 1.5pt double windowtext;padding: 1pt 0in;\">\n" +
                        "    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";border: none;padding: 0in;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">موضوع الرسالة / \"" +
                        msgArAddress +
                        "(</span></strong><strong style=\"font-weight: 700;\"><span dir=\"LTR\" style=\"font-size: 15px;\">(A356</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">&nbsp;" +
                        "\"</span></strong></p>\n" +
                        "    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";border: none;padding: 0in;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">&nbsp;</span></strong></p>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">موافقة الكلية على التسجيل / : " +
                        uni_Council +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">&nbsp; &nbsp; &nbsp;\" &nbsp; &nbsp;جامعة &nbsp;\" &nbsp; &nbsp;\" &nbsp; &nbsp; &nbsp; &nbsp;/ : " +
                        fac_Council +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 15px;\">لجنة الأشراف المعـتمدة &nbsp;:ـ</span></u></strong></p>\n" +
                        "<div align=\"right\">\n" +
                        "    <table dir=\"rtl\" style=\"border: none;border-collapse: collapse;empty-cells: show;max-width: 100%;margin-left: -0.3pt;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border: 1pt solid windowtext;width: 28.35pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">م</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 140.25pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">لجنة الأشراف&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 63.8pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">الدرجـة</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">جهة العمل</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 26.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">1</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 140.25pt;padding: 0in 5.4pt;height: 26.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        oldSupervisors.get(0).getName() +
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 63.8pt;padding: 0in 5.4pt;height: 26.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        oldSupervisors.get(0).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 26.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\"" +
                        ">" +
                        oldSupervisors.get(0).getWorkplace() +
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">2</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 190.25pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\"" +
                        ">" +
                        oldSupervisors.get(1).getName() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 70.8pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        oldSupervisors.get(1).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 340.95pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        oldSupervisors.get(1).getWorkplace() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +

                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 15px;\">لجنة الأشراف المقترحة &nbsp;:ـ &nbsp; &nbsp; &nbsp;&nbsp;</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 15px;\">لجنة الأشراف المعـتمدة &nbsp;:ـ</span></u></strong></p>\n" +
                        "<div align=\"right\">\n" +
                        "    <table dir=\"rtl\" style=\"border: none;border-collapse: collapse;empty-cells: show;max-width: 100%;margin-left: -0.3pt;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border: 1pt solid windowtext;width: 28.35pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">م</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 140.25pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">لجنة الأشراف&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 63.8pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">الدرجـة</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">جهة العمل</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 26.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">1</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 140.25pt;padding: 0in 5.4pt;height: 26.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(0).getName() +
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 63.8pt;padding: 0in 5.4pt;height: 26.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(0).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 26.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\"" +
                        ">" +
                        newSupervisor.get(0).getWorkplace() +
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">2</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 190.25pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\"" +
                        ">" +
                        newSupervisor.get(1).getName() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 70.8pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(1).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 340.95pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(1).getWorkplace() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:justify;font-size:16px;font-family:\"Times New Roman\",\"serif\";'><br></p>"+
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 13px;color: black;\">أسباب التعديل / " +
                        reasons +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">موافقة مجلس القسم على تعـديل لجنة الإشراف : &nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;م</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">موافقة مجلس الكلية على تعـديل لجنة الأشراف :&nbsp;&nbsp;&nbsp;&nbsp;/ &nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;&nbsp;م</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 21px;color: black;\">السيد الأستاذ الدكتور / نائب رئيس الجامعة</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">برجاء التكرم بالموافقة نحـو اعـتماد التعـديـل المشار إلـيه</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><em><span style=\"font-size: 15px;color: black;\">وتفضلوا بقبول فائق الاحترام ،،،،</span></em></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">مديرالدراسات العـليا &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; مدير عـام الكلية &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; يعتمد ،،،</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"color: black;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; أ.د./ وكيل الكلية للدراسات العليا والبحوث</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 15px;color: black;\"><span style=\"text-decoration: none;\">&nbsp;</span></span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 15px;color: black;\">الإدارة العامة للدراسات العـليا والبحوث بالجامعة</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">روجعت البيانات والإجراءات صحيحة واللوائح مطابقة&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">تحريرا في &nbsp; &nbsp; &nbsp; / &nbsp; &nbsp; &nbsp; &nbsp;/ &nbsp; 20م</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">الموظف المختص &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;مدير الإدارة &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;( المدير العام )</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">موافقة ،،،،يعتمد ،،،، &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"color: black;\">(أ.د./ نائب رئيس الجامعة المختص ) &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;color: black;\">( أ.د./ رئيس الجـامعـة&nbsp;</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">) &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;مصادقة مجلس الجامعة بجلستها رقم ( &nbsp; &nbsp; &nbsp; &nbsp; ) بتاريخ &nbsp; &nbsp; / &nbsp; &nbsp; / &nbsp; 20م</span></strong></p>\n" +
                        "<p style=\"margin-bottom: 10px !important;\"><span dir=\"RTL\" style='font-size: 16px;line-height: 18.4px;font-family: \"Times New Roman\", \"serif\";'><br>&nbsp;</span></p>\n" +
                        "<p style='margin: 0in 0in 10pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 18.4px;'><span dir=\"RTL\">&nbsp;</span></p>\n" +
                        "<p style=\"margin-bottom: 10px !important;\"><br style=\"color: rgb(0, 0, 0);font-family: &quot;Times New Roman&quot;;font-size: medium;font-style: normal;font-weight: 400;text-align: start;text-indent: 0px;\"></p>"+
                        "" +
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:justify;font-size:16px;font-family:\"Times New Roman\",\"serif\";'><br></p>"+
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:justify;font-size:16px;font-family:\"Times New Roman\",\"serif\";'><br></p>"+
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:justify;font-size:16px;font-family:\"Times New Roman\",\"serif\";'><br></p>"+
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:justify;font-size:16px;font-family:\"Times New Roman\",\"serif\";'><br></p>"+
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:justify;font-size:16px;font-family:\"Times New Roman\",\"serif\";'><br></p>"+
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:justify;font-size:16px;font-family:\"Times New Roman\",\"serif\";'><br></p>"+
                        "<table style=\"border: none;border-collapse: collapse;empty-cells: show;max-width: 100%;float: left;width: 680pt;margin-left: 0.1in;margin-right: 0.1in;\">\n" +
                        "    <tbody>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">AL-AZHAR</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">UNIVERSITY</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td rowspan=\"3\" style=\"border: 1pt solid windowtext;width: 163.7pt;padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span dir=\"LTR\">&nbsp;<img src=\"data:image/jpeg;base64,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\" style=\"width: 73px; height: 92.3805px;\" width=\"170\" height=\"216\"></span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: none;border-image: initial;user-select: text;width: 186.85pt;padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-size: 21px;font-family: \"Arabic Transparent\", \"sans-serif\";'>جامعـــة الأزهـــر</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: none;border-right: none;border-bottom: none;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">FACULTY OF ENGINEERING</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: none;border-left: none;border-image: initial;user-select: text;width: 186.85pt;padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-size: 21px;font-family: \"Arabic Transparent\", \"sans-serif\";'>كلية الهندســــــة</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;background: rgb(204, 204, 204);padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span dir=\"RTL\" style=\"font-size: 21px;\">&nbsp;</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: none;border-image: initial;user-select: text;width: 186.85pt;background: rgb(204, 204, 204);padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-size: 21px;font-family: \"Arabic Transparent\", \"sans-serif\";'>الدراسات الـعلــيــا</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "    </tbody>\n" +
                        "</table>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'>&nbsp;</p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'>&nbsp;</p>\n" +
                        "<p style='margin: 0in 0in 10pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 18.4px;'><strong style=\"font-weight: 700;\"><span dir=\"RTL\" style=\"font-size: 19px;line-height: 21.85px;\">السيـد الأســتـاذ / مــديـر الإدارة العـامــة للـدراســات العـليـا والبحـوث بالجـامعــة</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">تحـيــة طيبـة وبعــد ,,</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">&nbsp;نتشــرف بأن نـحـيـط عـلم سيـادتـكم بأن مـجـلس الكـليــة المنعـقـد &nbsp;بجـلستـه رقــم {  } بتـاريـخ : &nbsp; &nbsp; / &nbsp; &nbsp; &nbsp;/ &nbsp; &nbsp;20م</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">قـد وافـق عـلى قــرار مجـلس قـســم&nbsp;" +
                        deptName +
                        "&nbsp;بالمـوافـقــة عـلى تـعـديـل لجــنـة الأشـراف الخـاصــة&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">بالبـاحـث /.&nbsp;" +
                        arname +
                        "&nbsp;المسجـلبالـدراسـات العـلــيـا بالكـلـيـة للحـصــول عـلــى درجـة &nbsp; التخـصص { " +
                        degName +
                        "}، العالميـة { " +
                        degName +
                        "}فـــي هـنـدســة &nbsp;" +
                        deptName +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">&nbsp;الأتي أسماؤهــم بعــد :-</span></strong></p>\n" +
                        "<div align=\"center\">\n" +
                        "    <table dir=\"rtl\" style=\"border: none;border-collapse: collapse;empty-cells: show;max-width: 100%;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border: 1pt solid black;width: 26.55pt;padding: 0in 5.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">م</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid black;border-right: none;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;user-select: text;width: 212.6pt;padding: 0in 5.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">الاســــــــــــــم</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid black;border-right: none;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;user-select: text;width: 70.9pt;padding: 0in 5.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">الـدرجـــــة</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid black;border-right: none;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;user-select: text;width: 211.8pt;padding: 0in 5.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">القـســــــــــــــم</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 26.3pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">1</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 147.35pt;padding: 0in 5.4pt;height: 26.3pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(0).getName() +
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 56.7pt;padding: 0in 5.4pt;height: 26.3pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(0).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 26.3pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(0).getWorkplace() +
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">2</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 147.35pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(1).getName() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 56.7pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(1).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(1).getWorkplace() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">ومــرفـق طيه نـمـاذج التـعـديـل وصـورة طـبق الأصـل بمـذكـرة مـجـلس القـسـم وكـذلك تـقـريـر لجـنـة الأشــراف.</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">مـرسـل بـرجـاء العـلم والإحاطة واتـخـاذ اللازم,,,</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 21px;\">وتـفـضـلوا بقـبول فـائق الاحـترام ،،</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><span style=\"font-size: 19px;\">&nbsp;</span></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;\">الـدراســات العـليـا &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;color: black;\">رئيس الأدارة</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;\">مــديـر عــام الكـلـيــة &nbsp; &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><span dir=\"LTR\">&nbsp;</span></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><span dir=\"LTR\">&nbsp;</span></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><span dir=\"LTR\">&nbsp;</span></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><span dir=\"LTR\">&nbsp;</span></p>\n" +
                        "<p style=\"margin-bottom: 10px !important;\"><br style=\"color: rgb(0, 0, 0);font-family: &quot;Times New Roman&quot;;font-size: medium;font-style: normal;font-weight: 400;text-align: start;text-indent: 0px;\"></p>").create();
    }

    private void changeSupervisorPrintPdf4(){
        new CreatePdf(allChangeTitleRequest.getContext())
                .setPdfName("FirstPdf")
                .openPrintDialog(true)
                .setPageSize(PrintAttributes.MediaSize.ISO_A4)
                .setContent("<table style=\"border: none;border-collapse: collapse;empty-cells: show;max-width: 100%;float: left;width: 680pt;margin-left: 0.1in;margin-right: 0.1in;\">\n" +
                        "    <tbody>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;\">AL-AZHAR</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;\">UNIVERSITY</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td rowspan=\"3\" style=\"border: 1pt solid windowtext;width: 163.7pt;padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span dir=\"LTR\" style=\"font-size: 13px;\">&nbsp;<img src=\"data:image/jpeg;base64,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\" style=\"width: 65px; height: 82.7273px;\"></span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: none;border-image: initial;user-select: text;width: 186.85pt;padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-family: \"Arabic Transparent\", \"sans-serif\";'>جامعـــة الأزهـــر</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: none;border-right: none;border-bottom: none;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;\">FACULTY OF ENGINEERING</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: none;border-left: none;border-image: initial;user-select: text;width: 186.85pt;padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-family: \"Arabic Transparent\", \"sans-serif\";'>كلية الهندســــــة</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;background: rgb(204, 204, 204);padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span dir=\"RTL\">&nbsp;</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: none;border-image: initial;user-select: text;width: 186.85pt;background: rgb(204, 204, 204);padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-family: \"Arabic Transparent\", \"sans-serif\";'>الدراسات الـعلــيــا</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "    </tbody>\n" +
                        "</table>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 15px;\"><span style=\"text-decoration: none;\">&nbsp;</span></span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<u>طلب الموافقة على تعـديل لجنة الأشراف لدرجةالتخصص{" +
                        degName +
                        "</u>} &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">أسـم الباحـث / " +
                        arname +
                        " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; الجـنسية / " +
                        forign +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">القـسـم / &nbsp; " +
                        deptName +
                        " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; الدرجة / التخصص(" +
                        degName +
                        ")" +
                        deptName +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">الـمـؤهـل الـدراسـي : " +
                        "دبلومالدراسات العليا فى التاكل " +
                        "(" +
                        deptName +
                        ")" +
                        " –- جامعة " +
                        qualUnivesity +
                        "</span></strong></p>\n" +
                        "<div style=\"border: 1.5pt double windowtext;padding: 1pt 0in;\">\n" +
                        "    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";border: none;padding: 0in;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">موضوع الرسالة / \"" +
                        msgArAddress +
                        "(</span></strong><strong style=\"font-weight: 700;\"><span dir=\"LTR\" style=\"font-size: 15px;\">(A356</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">&nbsp;" +
                        "\"</span></strong></p>\n" +
                        "    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";border: none;padding: 0in;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">&nbsp;</span></strong></p>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">موافقة الكلية على التسجيل / : " +
                        uni_Council +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">&nbsp; &nbsp; &nbsp;\" &nbsp; &nbsp;جامعة &nbsp;\" &nbsp; &nbsp;\" &nbsp; &nbsp; &nbsp; &nbsp;/ : " +
                        fac_Council +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 15px;\">لجنة الأشراف المعـتمدة &nbsp;:ـ</span></u></strong></p>\n" +
                        "<div align=\"right\">\n" +
                        "    <table dir=\"rtl\" style=\"border: none;border-collapse: collapse;empty-cells: show;max-width: 100%;margin-left: -0.3pt;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border: 1pt solid windowtext;width: 28.35pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">م</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 190.25pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">لجنة الأشراف&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 70.8pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">الدرجـة</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 340.95pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">جهة العمل</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 26.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">1</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 140.25pt;padding: 0in 5.4pt;height: 26.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        oldSupervisors.get(0).getName() +
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 63.8pt;padding: 0in 5.4pt;height: 26.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        oldSupervisors.get(0).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 26.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\"" +
                        ">" +
                        oldSupervisors.get(0).getWorkplace() +
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">2</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 190.25pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\"" +
                        ">" +
                        oldSupervisors.get(1).getName() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 70.8pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        oldSupervisors.get(1).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 340.95pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        oldSupervisors.get(1).getWorkplace() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">3</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 140.25pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        oldSupervisors.get(2).getName()+
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 63.8pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        oldSupervisors.get(2).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        oldSupervisors.get(2).getWorkplace() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">4</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 140.25pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        oldSupervisors.get(3).getName()+
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 63.8pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        oldSupervisors.get(3).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        oldSupervisors.get(3).getWorkplace() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 15px;\">لجنة الأشراف المقترحة &nbsp;:ـ &nbsp; &nbsp; &nbsp;&nbsp;</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 15px;\">لجنة الأشراف المعـتمدة &nbsp;:ـ</span></u></strong></p>\n" +
                        "<div align=\"right\">\n" +
                        "    <table dir=\"rtl\" style=\"border: none;border-collapse: collapse;empty-cells: show;max-width: 100%;margin-left: -0.3pt;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border: 1pt solid windowtext;width: 28.35pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">م</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 140.25pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">لجنة الأشراف&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 63.8pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">الدرجـة</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">جهة العمل</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 26.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">1</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 140.25pt;padding: 0in 5.4pt;height: 26.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(0).getName() +
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 63.8pt;padding: 0in 5.4pt;height: 26.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(0).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 26.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\"" +
                        ">" +
                        newSupervisor.get(0).getWorkplace() +
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">2</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 190.25pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\"" +
                        ">" +
                        newSupervisor.get(1).getName() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 70.8pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(1).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 340.95pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(1).getWorkplace() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 13px;color: black;\">أسباب التعديل / " +
                        reasons +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">موافقة مجلس القسم على تعـديل لجنة الإشراف : &nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;م</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">موافقة مجلس الكلية على تعـديل لجنة الأشراف :&nbsp;&nbsp;&nbsp;&nbsp;/ &nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;&nbsp;م</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 21px;color: black;\">السيد الأستاذ الدكتور / نائب رئيس الجامعة</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">برجاء التكرم بالموافقة نحـو اعـتماد التعـديـل المشار إلـيه</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><em><span style=\"font-size: 15px;color: black;\">وتفضلوا بقبول فائق الاحترام ،،،،</span></em></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">مديرالدراسات العـليا &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; مدير عـام الكلية &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; يعتمد ،،،</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"color: black;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; أ.د./ وكيل الكلية للدراسات العليا والبحوث</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 15px;color: black;\"><span style=\"text-decoration: none;\">&nbsp;</span></span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 15px;color: black;\">الإدارة العامة للدراسات العـليا والبحوث بالجامعة</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">روجعت البيانات والإجراءات صحيحة واللوائح مطابقة&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">تحريرا في &nbsp; &nbsp; &nbsp; / &nbsp; &nbsp; &nbsp; &nbsp;/ &nbsp; 20م</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">الموظف المختص &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;مدير الإدارة &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;( المدير العام )</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">موافقة ،،،،يعتمد ،،،، &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"color: black;\">(أ.د./ نائب رئيس الجامعة المختص ) &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;color: black;\">( أ.د./ رئيس الجـامعـة&nbsp;</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">) &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;مصادقة مجلس الجامعة بجلستها رقم ( &nbsp; &nbsp; &nbsp; &nbsp; ) بتاريخ &nbsp; &nbsp; / &nbsp; &nbsp; / &nbsp; 20م</span></strong></p>\n" +
                        "<p style=\"margin-bottom: 10px !important;\"><span dir=\"RTL\" style='font-size: 16px;line-height: 18.4px;font-family: \"Times New Roman\", \"serif\";'><br>&nbsp;</span></p>\n" +
                        "<p style='margin: 0in 0in 10pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 18.4px;'><span dir=\"RTL\">&nbsp;</span></p>\n" +
                        "<p style=\"margin-bottom: 10px !important;\"><br style=\"color: rgb(0, 0, 0);font-family: &quot;Times New Roman&quot;;font-size: medium;font-style: normal;font-weight: 400;text-align: start;text-indent: 0px;\"></p>"+
                        ""+
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:justify;font-size:16px;font-family:\"Times New Roman\",\"serif\";'><br></p>"+
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:justify;font-size:16px;font-family:\"Times New Roman\",\"serif\";'><br></p>"+
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:justify;font-size:16px;font-family:\"Times New Roman\",\"serif\";'><br></p>"+
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:justify;font-size:16px;font-family:\"Times New Roman\",\"serif\";'><br></p>"+
                        "<table style=\"border: none;border-collapse: collapse;empty-cells: show;max-width: 100%;float: left;width: 680pt;margin-left: 0.1in;margin-right: 0.1in;\">\n" +
                        "    <tbody>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">AL-AZHAR</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">UNIVERSITY</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td rowspan=\"3\" style=\"border: 1pt solid windowtext;width: 163.7pt;padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span dir=\"LTR\">&nbsp;<img src=\"data:image/jpeg;base64,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\" style=\"width: 73px; height: 92.3805px;\" width=\"170\" height=\"216\"></span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: none;border-image: initial;user-select: text;width: 186.85pt;padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-size: 21px;font-family: \"Arabic Transparent\", \"sans-serif\";'>جامعـــة الأزهـــر</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: none;border-right: none;border-bottom: none;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">FACULTY OF ENGINEERING</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: none;border-left: none;border-image: initial;user-select: text;width: 186.85pt;padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-size: 21px;font-family: \"Arabic Transparent\", \"sans-serif\";'>كلية الهندســــــة</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;background: rgb(204, 204, 204);padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span dir=\"RTL\" style=\"font-size: 21px;\">&nbsp;</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: none;border-image: initial;user-select: text;width: 186.85pt;background: rgb(204, 204, 204);padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-size: 21px;font-family: \"Arabic Transparent\", \"sans-serif\";'>الدراسات الـعلــيــا</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "    </tbody>\n" +
                        "</table>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'>&nbsp;</p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'>&nbsp;</p>\n" +
                        "<p style='margin: 0in 0in 10pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 18.4px;'><strong style=\"font-weight: 700;\"><span dir=\"RTL\" style=\"font-size: 19px;line-height: 21.85px;\">السيـد الأســتـاذ / مــديـر الإدارة العـامــة للـدراســات العـليـا والبحـوث بالجـامعــة</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">تحـيــة طيبـة وبعــد ,,</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">&nbsp;نتشــرف بأن نـحـيـط عـلم سيـادتـكم بأن مـجـلس الكـليــة المنعـقـد &nbsp;بجـلستـه رقــم {  } بتـاريـخ : &nbsp; &nbsp; / &nbsp; &nbsp; &nbsp;/ &nbsp; &nbsp;20م</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">قـد وافـق عـلى قــرار مجـلس قـســم&nbsp;" +
                        deptName +
                        "&nbsp;بالمـوافـقــة عـلى تـعـديـل لجــنـة الأشـراف الخـاصــة&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">بالبـاحـث /.&nbsp;" +
                        arname +
                        "&nbsp;المسجـلبالـدراسـات العـلــيـا بالكـلـيـة للحـصــول عـلــى درجـة &nbsp; التخـصص { " +
                        degName +
                        "}، العالميـة { " +
                        degName +
                        "}فـــي هـنـدســة &nbsp;" +
                        deptName +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">&nbsp;الأتي أسماؤهــم بعــد :-</span></strong></p>\n" +
                        "<div align=\"center\">\n" +
                        "    <table dir=\"rtl\" style=\"border: none;border-collapse: collapse;empty-cells: show;max-width: 100%;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border: 1pt solid black;width: 26.55pt;padding: 0in 5.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">م</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid black;border-right: none;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;user-select: text;width: 212.6pt;padding: 0in 5.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">الاســــــــــــــم</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid black;border-right: none;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;user-select: text;width: 70.9pt;padding: 0in 5.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">الـدرجـــــة</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid black;border-right: none;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;user-select: text;width: 211.8pt;padding: 0in 5.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">القـســــــــــــــم</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 26.3pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">1</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 147.35pt;padding: 0in 5.4pt;height: 26.3pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(0).getName() +
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 56.7pt;padding: 0in 5.4pt;height: 26.3pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(0).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 26.3pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(0).getWorkplace() +
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">2</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 147.35pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(1).getName() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 56.7pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(1).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 12px;color: black;\">" +
                        newSupervisor.get(1).getWorkplace() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">ومــرفـق طيه نـمـاذج التـعـديـل وصـورة طـبق الأصـل بمـذكـرة مـجـلس القـسـم وكـذلك تـقـريـر لجـنـة الأشــراف.</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";line-height: 24px;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;line-height: 28.5px;\">مـرسـل بـرجـاء العـلم والإحاطة واتـخـاذ اللازم,,,</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 21px;\">وتـفـضـلوا بقـبول فـائق الاحـترام ،،</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><span style=\"font-size: 19px;\">&nbsp;</span></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;\">الـدراســات العـليـا &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;color: black;\">رئيس الأدارة</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;\">مــديـر عــام الكـلـيــة &nbsp; &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><span dir=\"LTR\">&nbsp;</span></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><span dir=\"LTR\">&nbsp;</span></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><span dir=\"LTR\">&nbsp;</span></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><span dir=\"LTR\">&nbsp;</span></p>\n" +
                        "<p style=\"margin-bottom: 10px !important;\"><br style=\"color: rgb(0, 0, 0);font-family: &quot;Times New Roman&quot;;font-size: medium;font-style: normal;font-weight: 400;text-align: start;text-indent: 0px;\"></p>").create();
    }

//ExtendRequist
public void getExtendReqest(int requestId) {
    Call<ExtensionRequestModel2> call = endPoint.getOneExtendRequest(requestId);
    call.enqueue(new Callback<ExtensionRequestModel2>() {
        @Override
        public void onResponse(Call<ExtensionRequestModel2> call, Response<ExtensionRequestModel2> response) {
                    if(!response.isSuccessful()){
                        return;
                    }
            extensionRequestModel2 = response.body();
            arname = extensionRequestModel2.getArname();
            extendPeriod = extensionRequestModel2.getExtend_period();
            fac_Council= extensionRequestModel2.getFac_Council();
            forign= extensionRequestModel2.getForign();
            list=    extensionRequestModel2.getSupervisors();
            msgArAddress= extensionRequestModel2.getMsgArAddress();
            uni_Council=   extensionRequestModel2.getUni_Council();
            specialization=   extensionRequestModel2.getSpecialization();
            qualUnivesity= extensionRequestModel2.getQualUnivesity();
            deptName    =  extensionRequestModel2.getDeptName();

            if (extendPeriod == 1) {
                yearExtension= "مد عام سادس";
            } else if (extendPeriod == 2) {
                yearExtension="مد عام سابع";
            } else if (extendPeriod == 3) {
                yearExtension="مد عام ثامن";
            }

            if(list.size()==2){
                extendRequistPrintPdf2();
            }else if(list.size()==3){
                extendRequistPrintPdf3();
            }
            else if(list.size()==4){
                extendRequistPrintPdf4();
            }

        }

        @Override
        public void onFailure(Call<ExtensionRequestModel2> call, Throwable t) {

        }
    });
}

    private void extendRequistPrintPdf2() {
        new CreatePdf(allExtendsRequest.getContext())
                .setPdfName("FirstPdf")
                .openPrintDialog(true)
                .setPageSize(PrintAttributes.MediaSize.ISO_A4)
                .setContent("<table style=\"border: none;border-collapse: collapse;empty-cells: show;max-width: 100%;float: left;width: 680pt;margin-left: 0.1in;margin-right: 0.1in;\">\n" +
                        "    <tbody>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">AL-AZHARUNIVERSITY</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td rowspan=\"3\" style=\"border: 1pt solid windowtext;width: 163.7pt;padding: 0in 5.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><span dir=\"LTR\" style=\"font-size:13px;\">&nbsp;<img src=\"file:///android_res/drawable/logo.png\"\n\"></span></p>\n" +
                        "                </td>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: none;border-image: initial;user-select: text;width: 186.85pt;padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-size: 21px;font-family: \"Arabic Transparent\", \"sans-serif\";'>جامعـــة الأزهـــر</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: none;border-right: none;border-bottom: none;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">FACULTY OF ENGINEERING</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: none;border-left: none;border-image: initial;user-select: text;width: 186.85pt;padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-size: 21px;font-family: \"Arabic Transparent\", \"sans-serif\";'>كلية الهندســــــة</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;background: rgb(204, 204, 204);padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span dir=\"RTL\" style=\"font-size: 21px;\">&nbsp;</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: none;border-image: initial;user-select: text;width: 186.85pt;background: rgb(204, 204, 204);padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-size: 21px;font-family: \"Arabic Transparent\", \"sans-serif\";'>الدراسات الـعلــيــا</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "    </tbody>\n" +
                        "</table>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 19px;\"><span style=\"text-decoration: none;\">&nbsp;</span></span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<u>طلب الموافقة على مد لجنة الأشراف لدرجة التخصص{الماجستير</u>} &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;\">أسـم الباحـث/ " +
                        arname +
                        " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;الجـنسية / " +
                        forign +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;\">" +
                        "القـسـم / " +
                        deptName+
                        " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;الدرجة /&nbsp;</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;\">الماجستير &nbsp; &nbsp;التخصص (القوى والالات الكهربية)</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;\">الـمـؤهـل الـدراسـي : " +
                        specialization +
                        "-" +
                        qualUnivesity +
                        "</span></strong></p>\n" +
                        "<div style=\"border: 1.5pt double windowtext;padding: 1pt 0in;\">\n" +
                        "    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";border: none;padding: 0in;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;\">موضوع الرسالة / \"</span></strong><strong style=\"font-weight: 700;\">" +
                        msgArAddress +
                        "&nbsp;</strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;\">\"</span></strong></p>\n" +
                        "    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";border: none;padding: 0in;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;\">&nbsp;</span></strong></p>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;\">موافقة الكلية على التسجيل &nbsp; &nbsp;" +
                        fac_Council +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;\">موافقة الجامعه على التسجيل &nbsp; &nbsp;" +
                        uni_Council +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 17px;\">لجنة الأشراف المعـتمدة &nbsp;:ـ</span></u></strong></p>\n" +
                        "<div align=\"right\">\n" +
                        "    <table dir=\"rtl\" style=\"border: none;border-collapse: collapse;empty-cells: show;max-width: 100%;margin-left: -0.3pt;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border: 1pt solid windowtext;width: 28.35pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">م</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 190.25pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">لجنة الأشراف&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 70.8pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">الدرجـة</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 340.95pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">جهة العمل</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">1-</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 140.25pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;" +
                        list.get(0).getName() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 63.8pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">" +
                        list.get(0).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">أستاذ" +
                        list.get(0).getWorkplace() +
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">2-</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 140.25pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">" +
                        list.get(1).getName() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 63.8pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">" +
                        list.get(1).getDegree()+
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">" +
                        list.get(1).getWorkplace()+
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:right;font-size:16px;font-family:\"Times New Roman\",\"serif\";line-height:115%;'><br></p>" +
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:right;font-size:16px;font-family:\"Times New Roman\",\"serif\";line-height:115%;'><br></p>"+
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"color: black;\">بداية المد الجديد: يبدأ من :&nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;م &nbsp;------ &nbsp; &nbsp;&nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp; " +
                        "("+yearExtension+")"+
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">موافقة مجلس القسم على المد: " +
                        "&nbsp;&nbsp;/&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">" +
                        "موافقة مجلس الكلية على المد: " +
                        "&nbsp;&nbsp;/&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 25px;color: black;\">السيد الأستاذ الدكتور / نائب رئيس الجامعة</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;color: black;\">برجاء التكرم بالموافقة نحـو اعـتماد المد المشار إلـيه</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><em><span style=\"font-size: 19px;color: black;\">وتفضلوا بقبول فائق الاحترام ،،،</span></em></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">مدير الإدارة</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; مدير عـام الكلية &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; يعتمد &nbsp; &nbsp;وكيل الكلية للدراسات العليا والبحوث</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;اد/</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 20px;color: black;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp; &nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 17px;color: black;\"><span style=\"text-decoration: none;\">&nbsp;</span></span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 17px;color: black;\">الإدارة العامة للدراسات العـليا والبحوث بالجامعة</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">روجعت البيانات والإجراءات صحيحة واللوائح مطابقة&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">تحريرا في &nbsp; &nbsp; &nbsp; / &nbsp; &nbsp; &nbsp; &nbsp;/ &nbsp; 20م</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">الموظف المختص &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; مدير الإدارة &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ( المدير العام )</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">موافقة ،،،، &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;يعتمد ،،،،&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 13px;color: black;\">اد/ نائب رئيس الجامعة للدراسات العليا &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 20px;color: black;\">&nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp;مصادقة مجلس الجامعة بجلستها رقم ( &nbsp; &nbsp; &nbsp; &nbsp; ) بتاريخ &nbsp; &nbsp; / &nbsp; &nbsp;/ &nbsp; 20م</span></strong></p>\n" +
                        "<p style=\"margin-bottom: 10px !important;\"><br style=\"color: rgb(0, 0, 0);font-family: &quot;Times New Roman&quot;;font-size: medium;font-style: normal;font-weight: 400;text-align: start;text-indent: 0px;\"></p>").create();
    }

    private void extendRequistPrintPdf3() {
        new CreatePdf(allExtendsRequest.getContext())
                .setPdfName("FirstPdf")
                .openPrintDialog(true)
                .setPageSize(PrintAttributes.MediaSize.ISO_A4)
                .setContent("<table style=\"border: none;border-collapse: collapse;empty-cells: show;max-width: 100%;float: left;width: 680pt;margin-left: 0.1in;margin-right: 0.1in;\">\n" +
                        "    <tbody>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">AL-AZHARUNIVERSITY</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td rowspan=\"3\" style=\"border: 1pt solid windowtext;width: 163.7pt;padding: 0in 5.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><span dir=\"LTR\" style=\"font-size:13px;\">&nbsp;<img src=\"file:///android_res/drawable/logo.png\"\n\"></span></p>\n" +
                        "                </td>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: none;border-image: initial;user-select: text;width: 186.85pt;padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-size: 21px;font-family: \"Arabic Transparent\", \"sans-serif\";'>جامعـــة الأزهـــر</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: none;border-right: none;border-bottom: none;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">FACULTY OF ENGINEERING</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: none;border-left: none;border-image: initial;user-select: text;width: 186.85pt;padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-size: 21px;font-family: \"Arabic Transparent\", \"sans-serif\";'>كلية الهندســــــة</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;background: rgb(204, 204, 204);padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span dir=\"RTL\" style=\"font-size: 21px;\">&nbsp;</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: none;border-image: initial;user-select: text;width: 186.85pt;background: rgb(204, 204, 204);padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-size: 21px;font-family: \"Arabic Transparent\", \"sans-serif\";'>الدراسات الـعلــيــا</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "    </tbody>\n" +
                        "</table>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 19px;\"><span style=\"text-decoration: none;\">&nbsp;</span></span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<u>طلب الموافقة على مد لجنة الأشراف لدرجة التخصص{الماجستير</u>} &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;\">أسـم الباحـث/ " +
                        arname +
                        " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;الجـنسية / " +
                        forign +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;\">" +
                        "القـسـم / " +
                        deptName+
                        " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;الدرجة /&nbsp;</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;\">الماجستير &nbsp; &nbsp;التخصص (القوى والالات الكهربية)</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;\">الـمـؤهـل الـدراسـي : " +
                        specialization +
                        "-" +
                        qualUnivesity +
                        "</span></strong></p>\n" +
                        "<div style=\"border: 1.5pt double windowtext;padding: 1pt 0in;\">\n" +
                        "    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";border: none;padding: 0in;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;\">موضوع الرسالة / \"</span></strong><strong style=\"font-weight: 700;\">" +
                        msgArAddress +
                        "&nbsp;</strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;\">\"</span></strong></p>\n" +
                        "    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";border: none;padding: 0in;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;\">&nbsp;</span></strong></p>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;\">موافقة الكلية على التسجيل &nbsp; &nbsp;" +
                        fac_Council +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;\">موافقة الجامعه على التسجيل &nbsp; &nbsp;" +
                        uni_Council +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 17px;\">لجنة الأشراف المعـتمدة &nbsp;:ـ</span></u></strong></p>\n" +
                        "<div align=\"right\">\n" +
                        "    <table dir=\"rtl\" style=\"border: none;border-collapse: collapse;empty-cells: show;max-width: 100%;margin-left: -0.3pt;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border: 1pt solid windowtext;width: 28.35pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">م</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 190.25pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">لجنة الأشراف&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 70.8pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">الدرجـة</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 340.95pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">جهة العمل</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">1-</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 140.25pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;" +
                        list.get(0).getName() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 63.8pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">" +
                        list.get(0).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">أستاذ" +
                        list.get(0).getWorkplace() +
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">2-</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 140.25pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">" +
                        list.get(1).getName() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 63.8pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">" +
                        list.get(1).getDegree()+
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">" +
                        list.get(1).getWorkplace()+
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">3-</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 140.25pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">" +
                        list.get(2).getName() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 63.8pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">" +
                        list.get(2).getDegree()+
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">" +
                        list.get(2).getWorkplace()+
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:right;font-size:16px;font-family:\"Times New Roman\",\"serif\";line-height:115%;'><br></p>" +
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:right;font-size:16px;font-family:\"Times New Roman\",\"serif\";line-height:115%;'><br></p>"+
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"color: black;\">بداية المد الجديد: يبدأ من :&nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;م &nbsp;------ &nbsp; &nbsp;وينتهي في :&nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " +
                        "("+yearExtension+")"+
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">موافقة مجلس القسم على المد: " +
                        "&nbsp;&nbsp;/&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">" +
                        "موافقة مجلس الكلية على المد: " +
                        "&nbsp;&nbsp;/&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 25px;color: black;\">السيد الأستاذ الدكتور / نائب رئيس الجامعة</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;color: black;\">برجاء التكرم بالموافقة نحـو اعـتماد المد المشار إلـيه</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><em><span style=\"font-size: 19px;color: black;\">وتفضلوا بقبول فائق الاحترام ،،،</span></em></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">مدير الإدارة</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; مدير عـام الكلية &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; يعتمد &nbsp; &nbsp;وكيل الكلية للدراسات العليا والبحوث</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;اد/</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 20px;color: black;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp; &nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 17px;color: black;\"><span style=\"text-decoration: none;\">&nbsp;</span></span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 17px;color: black;\">الإدارة العامة للدراسات العـليا والبحوث بالجامعة</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">روجعت البيانات والإجراءات صحيحة واللوائح مطابقة&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">تحريرا في &nbsp; &nbsp; &nbsp; / &nbsp; &nbsp; &nbsp; &nbsp;/ &nbsp; 20م</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">الموظف المختص &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; مدير الإدارة &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ( المدير العام )</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">موافقة ،،،، &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;يعتمد ،،،،&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 13px;color: black;\">اد/ نائب رئيس الجامعة للدراسات العليا &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 20px;color: black;\">&nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp;مصادقة مجلس الجامعة بجلستها رقم ( &nbsp; &nbsp; &nbsp; &nbsp; ) بتاريخ &nbsp; &nbsp; / &nbsp; &nbsp;/ &nbsp; 20م</span></strong></p>\n" +
                        "<p style=\"margin-bottom: 10px !important;\"><br style=\"color: rgb(0, 0, 0);font-family: &quot;Times New Roman&quot;;font-size: medium;font-style: normal;font-weight: 400;text-align: start;text-indent: 0px;\"></p>").create();
    }

    private void extendRequistPrintPdf4() {
        new CreatePdf(allExtendsRequest.getContext())
                .setPdfName("FirstPdf")
                .openPrintDialog(true)
                .setPageSize(PrintAttributes.MediaSize.ISO_A4)
                .setContent("<table style=\"border: none;border-collapse: collapse;empty-cells: show;max-width: 100%;float: left;width: 680pt;margin-left: 0.1in;margin-right: 0.1in;\">\n" +
                        "    <tbody>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">AL-AZHARUNIVERSITY</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td rowspan=\"3\" style=\"border: 1pt solid windowtext;width: 163.7pt;padding: 0in 5.4pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><span dir=\"LTR\" style=\"font-size:13px;\">&nbsp;<img src=\"file:///android_res/drawable/logo.png\"\n\"></span></p>\n" +
                        "                </td>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: none;border-image: initial;user-select: text;width: 186.85pt;padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-size: 21px;font-family: \"Arabic Transparent\", \"sans-serif\";'>جامعـــة الأزهـــر</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: none;border-right: none;border-bottom: none;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;\">FACULTY OF ENGINEERING</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: none;border-left: none;border-image: initial;user-select: text;width: 186.85pt;padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-size: 21px;font-family: \"Arabic Transparent\", \"sans-serif\";'>كلية الهندســــــة</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 193.5pt;background: rgb(204, 204, 204);padding: 0in 5.4pt;\">\n" +
                        "                <p style='margin: 0in 0in 0.0001pt;text-align: left;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span dir=\"RTL\" style=\"font-size: 21px;\">&nbsp;</span></strong></p>\n" +
                        "            </td>\n" +
                        "            <td style=\"border-top: 1pt solid windowtext;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: none;border-image: initial;user-select: text;width: 186.85pt;background: rgb(204, 204, 204);padding: 0in 5.4pt;\">\n" +
                        "                <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style='font-size: 21px;font-family: \"Arabic Transparent\", \"sans-serif\";'>الدراسات الـعلــيــا</span></strong></p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "    </tbody>\n" +
                        "</table>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 19px;\"><span style=\"text-decoration: none;\">&nbsp;</span></span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<u>طلب الموافقة على مد لجنة الأشراف لدرجة التخصص{الماجستير</u>} &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;\">أسـم الباحـث/ " +
                        arname +
                        " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;الجـنسية / " +
                        forign +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;\">" +
                        "القـسـم / " +
                        deptName+
                        " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;الدرجة /&nbsp;</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;\">الماجستير &nbsp; &nbsp;التخصص (القوى والالات الكهربية)</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;\">الـمـؤهـل الـدراسـي : " +
                        specialization +
                        "-" +
                        qualUnivesity +
                        "</span></strong></p>\n" +
                        "<div style=\"border: 1.5pt double windowtext;padding: 1pt 0in;\">\n" +
                        "    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";border: none;padding: 0in;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;\">موضوع الرسالة / \"</span></strong><strong style=\"font-weight: 700;\">" +
                        msgArAddress +
                        "&nbsp;</strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;\">\"</span></strong></p>\n" +
                        "    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";border: none;padding: 0in;'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;\">&nbsp;</span></strong></p>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;\">موافقة الكلية على التسجيل &nbsp; &nbsp;" +
                        fac_Council +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;\">موافقة الجامعه على التسجيل &nbsp; &nbsp;" +
                        uni_Council +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 17px;\">لجنة الأشراف المعـتمدة &nbsp;:ـ</span></u></strong></p>\n" +
                        "<div align=\"right\">\n" +
                        "    <table dir=\"rtl\" style=\"border: none;border-collapse: collapse;empty-cells: show;max-width: 100%;margin-left: -0.3pt;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "<td style=\"width: 28.45pt;border: 1pt solid windowtext;padding: 0in 5.4pt;height: 16.45pt;vertical-align: top;\">\n"+
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">م</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 190.25pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">لجنة الأشراف&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 70.8pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">الدرجـة</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: 1pt solid windowtext;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 340.95pt;padding: 0in 5.4pt;height: 20.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">جهة العمل</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">1-</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 140.25pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">&nbsp;" +
                        list.get(0).getName() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 63.8pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">" +
                        list.get(0).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">أستاذ" +
                        list.get(0).getWorkplace() +
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">2-</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 140.25pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">" +
                        list.get(1).getName() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 63.8pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">" +
                        list.get(1).getDegree()+
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">" +
                        list.get(1).getWorkplace()+
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">3-</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 140.25pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">" +
                        list.get(2).getName() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 63.8pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">" +
                        list.get(2).getDegree()+
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">" +
                        list.get(2).getWorkplace()+
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"border-top: none;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 28.35pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">4-</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 140.25pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">" +
                        list.get(3).getName() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 63.8pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">" +
                        list.get(3).getDegree()+
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"border-top: none;border-right: none;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;user-select: text;width: 318.95pt;padding: 0in 5.4pt;height: 24.15pt;\">\n" +
                        "                    <p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 15px;color: black;\">" +
                        list.get(3).getWorkplace()+
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +

                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:right;font-size:16px;font-family:\"Times New Roman\",\"serif\";line-height:115%;'><br></p>" +
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:right;font-size:16px;font-family:\"Times New Roman\",\"serif\";line-height:115%;'><br></p>"+
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"color: black;\">بداية المد الجديد: يبدأ من :&nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;م &nbsp;------ &nbsp; &nbsp;وينتهي في :&nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp; " +
                        "("+yearExtension+")"+
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">موافقة مجلس القسم على المد: " +
                        "&nbsp;&nbsp;/&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">" +
                        "موافقة مجلس الكلية على المد: " +
                        "&nbsp;&nbsp;/&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 25px;color: black;\">السيد الأستاذ الدكتور / نائب رئيس الجامعة</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 19px;color: black;\">برجاء التكرم بالموافقة نحـو اعـتماد المد المشار إلـيه</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><em><span style=\"font-size: 19px;color: black;\">وتفضلوا بقبول فائق الاحترام ،،،</span></em></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">مدير الإدارة</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; مدير عـام الكلية &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; يعتمد &nbsp; &nbsp;وكيل الكلية للدراسات العليا والبحوث</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;اد/</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: right;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 20px;color: black;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp; &nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 17px;color: black;\"><span style=\"text-decoration: none;\">&nbsp;</span></span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><u><span style=\"font-size: 17px;color: black;\">الإدارة العامة للدراسات العـليا والبحوث بالجامعة</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: center;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">روجعت البيانات والإجراءات صحيحة واللوائح مطابقة&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">تحريرا في &nbsp; &nbsp; &nbsp; / &nbsp; &nbsp; &nbsp; &nbsp;/ &nbsp; 20م</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">الموظف المختص &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; مدير الإدارة &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ( المدير العام )</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">موافقة ،،،، &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;يعتمد ،،،،&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span></strong><strong style=\"font-weight: 700;\"><span style=\"font-size: 13px;color: black;\">اد/ نائب رئيس الجامعة للدراسات العليا &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 20px;color: black;\">&nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin: 0in 0in 0.0001pt;text-align: justify;font-size:16px;font-family: \"Times New Roman\", \"serif\";'><strong style=\"font-weight: 700;\"><span style=\"font-size: 17px;color: black;\">&nbsp;مصادقة مجلس الجامعة بجلستها رقم ( &nbsp; &nbsp; &nbsp; &nbsp; ) بتاريخ &nbsp; &nbsp; / &nbsp; &nbsp;/ &nbsp; 20م</span></strong></p>\n" +
                        "<p style=\"margin-bottom: 10px !important;\"><br style=\"color: rgb(0, 0, 0);font-family: &quot;Times New Roman&quot;;font-size: medium;font-style: normal;font-weight: 400;text-align: start;text-indent: 0px;\"></p>"+
                        ""+
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:right;font-size:16px;font-family:\"Times New Roman\",\"serif\";'><br></p>"+
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:right;font-size:16px;font-family:\"Times New Roman\",\"serif\";'><br></p>"+
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:right;font-size:16px;font-family:\"Times New Roman\",\"serif\";'><br></p>"+
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:right;font-size:16px;font-family:\"Times New Roman\",\"serif\";'><br></p>"
                ).create();
    }

 //changeTitleRequest

    public void getChangeTitleReqest(int requestId){
        Call<InfoToChangeMessageTitleModel> call=endPoint.getOneChangeTitleRequest(requestId);
        call.enqueue(new Callback<InfoToChangeMessageTitleModel>() {
            @Override
            public void onResponse(Call<InfoToChangeMessageTitleModel> call, Response<InfoToChangeMessageTitleModel> response) {
                if(!response.isSuccessful()){
                    return;
                }

                changeTitleRequestModel=response.body();

                int  isEstential=changeTitleRequestModel.getIsSubstantial();
                if(isEstential==1){
                    esntial="جوهرى";
                }else if(isEstential==0){
                    esntial="غير جوهرى";
                }

                arabicName=changeTitleRequestModel.getArname();
                nationality=   changeTitleRequestModel.getForign();
                departName= changeTitleRequestModel.getDeptName();
                qualification = changeTitleRequestModel.getQualification();
                lastFac   = changeTitleRequestModel.getLastFac();
                oldAddress   =    changeTitleRequestModel.getOldAddress();
                newArabicAddress=  changeTitleRequestModel.getNewArabicAddress();
                fac_Council=changeTitleRequestModel.getFac_Council();
                uni_Council=changeTitleRequestModel.getUni_Council();
                department_approvement=  changeTitleRequestModel.getDepartment_approvement();
                faculity_approvement= changeTitleRequestModel.getFaculity_approvement();
                degname=changeTitleRequestModel.getDegname();
                list = changeTitleRequestModel.getSupervisors();


                if(list.size()==2){
                    printPdf2();
                }else if(list.size()==3){
                    printPdf3();
                }
                else if(list.size()==4){
                    printPdf4();
                }


            }

            @Override
            public void onFailure(Call<InfoToChangeMessageTitleModel> call, Throwable t) {

            }
        });
    }

    private  void printPdf3(){

        new CreatePdf(allChangeTitleRequest.getContext())
                .setPdfName("FirstPdf")
                .openPrintDialog(true)
                .setPageSize(PrintAttributes.MediaSize.ISO_A4)
                .setContent("<div align=\"right\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'>\n" +
                        "    <table dir=\"rtl\" style=\"width:549.95pt;margin-left:12.45pt;border-collapse:collapse;border:none;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 214.3pt;border: 1pt solid black;padding: 0in 5.4pt;height: 17.3pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";'>جـــامـعـــة الأزهـــر</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td rowspan=\"3\" style=\"width: 92.8pt;border-top: 1pt solid black;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;border-right: none;padding: 0in 5.4pt;height: 17.3pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><span dir=\"LTR\" style=\"font-size:13px;\">&nbsp;<img src=\"file:///android_res/drawable/logo.png\"\n\"></span></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 242.85pt;border-top: 1pt solid black;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;border-right: none;padding: 0in 5.4pt;padding-top: 0in 5.4pt;height: 10.7pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align: right;line-height: normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span dir=\"LTR\" style=\"font-size:13px;\">AL-AZHAR &nbsp;UNIVERSITY &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp;&nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 214.3pt;border-right: 1pt solid black;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;border-top: none;padding: 0in 5.4pt;height: 10.7pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";'>كـلــيـة الـهـنــدســـــة</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 242.85pt;border-top: none;border-left: 1pt solid black;border-bottom: 1pt solid black;border-right: none;padding: 0in 5.4pt;height: 10.7pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align: right;line-height: normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span dir=\"LTR\" style=\"font-size:13px;\">FACULTY &nbsp;OF &nbsp; &nbsp; ENGINEERING  &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 214.3pt;border-right: 1pt solid black;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;border-top: none;padding: 0in 5.4pt;height: 14.2pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";'>الــدراســات الـعــلــيــا</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 242.85pt;border-top: none;border-left: 1pt solid black;border-bottom: 1pt solid black;border-right: none;padding: 0in 5.4pt;height: 14.2pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align: right;line-height: normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";'>&nbsp;</span></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><u><span style='font-family:\"Arial\",\"sans-serif\";'>" +
                        "طلب الموافـقة عـلى تعـديـل الرســالة لدرجة العالمية{" +
                        degname +
                        "} <span style=\"color:black;\">، " +
                        "{ تعديل " +
                        esntial +
                        " }&nbsp;</span></span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-family:\"Arial\",\"sans-serif\";'>أسـم الباحث/ " +
                        arabicName +
                        " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp" +
                        ";" +
                        "الجنسيه" +
                        " /" +
                        nationality +
                        "&nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-family:\"Arial\",\"sans-serif\";'>القـسـم / " +
                        departName +
                        " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;الدرجة / " +
                        degname +
                        "&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-family:\"Arial\",\"sans-serif\";'>&nbsp;المؤهـل الدراسي :" +
                        qualification +" "+
                        departName +
                        " - &nbsp;" +
                        lastFac +
                        "&nbsp;</span></strong></p>\n" +
                        "<div align=\"right\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'>\n" +
                        "    <table dir=\"rtl\" style=\"margin-left:5.4pt;border-collapse:collapse;border:none;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 538.65pt;border: 1pt solid black;padding: 0in 5.4pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-family:\"Arial\",\"sans-serif\";'>مـوضـوع الرسـالة المعـتمـد:&quot;" +
                        oldAddress +
                        " &quot;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-family:\"Arial\",\"sans-serif\";'>موافقة الكلية عـلى التسجـيل :" +
                        fac_Council+
                        "&nbsp;----<span style=\"color:black;\">موافقـة الجامعة عـلى التسجيل: " +
                        uni_Council +
                        ".</span></span></strong></p>\n" +
                        "<div align=\"right\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'>\n" +
                        "    <table dir=\"rtl\" style=\"margin-left:5.4pt;border-collapse:collapse;border:none;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 536.75pt;border: 1pt solid black;padding: 0in 5.4pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-family:\"Arial\",\"sans-serif\";color:black;'>مـوضوع الرسـالة المقـترح : &quot;&nbsp;</span></strong><strong><span style='font-family:  \"Arial\",\"sans-serif\"" +
                        ";'" +
                        ">" +
                        newArabicAddress+
                        " <span style=\"color:black;\">&quot;</span></span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><u><span style='font-family:\"Arial\",\"sans-serif\";'>لجنة الأشراف المعـتمدة &nbsp; :</span></u></strong></p>\n" +
                        "<div align=\"right\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'>\n" +
                        "    <table dir=\"rtl\" style=\"width:536.85pt;margin-left:5.4pt;border-collapse:collapse;border:none;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 21.45pt;border: 1pt solid windowtext;padding: 0in 5.4pt;height: 16.45pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>م</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 155.05pt;border-top: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;border-right: none;padding: 0in 5.4pt;height: 16.45pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-bottom:2pt;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>لجنة الأشراف&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 56.5pt;border-top: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;border-right: none;padding: 0in 5.4pt;height: 16.45pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>الدرجـة</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 303.85pt;border-top: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;border-right: none;padding: 0in 5.4pt;height: 16.45pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>جهة العمل</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 21.45pt;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;border-top: none;padding: 0in 5.4pt;height: 12.65pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>1</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 155.05pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-right: none;padding: 0in 5.4pt;height: 12.65pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:  \"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(0).getName()+
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 56.5pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt " +
                        "solid windowtext;border-right: none;padding: 0in 5.4pt;height: 12.65pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(0).getDegree()+
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 303.85pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-right: none;padding: 0in 5.4pt;height: 12.65pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(0).getWorkplace() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 21.45pt;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;border-top: none;padding: 0in 5.4pt;height: 9.5pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>2</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 155.05pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-right: none;padding: 0in 5.4pt;height: 9.5pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:  \"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(1).getName() +
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 56.5pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-right: none;padding: 0in 5.4pt;height: 9.5pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(1).getDegree()+
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 303.85pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-right: none;padding: 0in 5.4pt;height: 9.5pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(1).getWorkplace() +
                        ".</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 21.45pt;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;border-top: none;padding: 0in 5.4pt;height: 9.5pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>3</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 155.05pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-right: none;padding: 0in 5.4pt;height: 9.5pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:  \"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(2).getName()+
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 56.5pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-right: none;padding: 0in 5.4pt;height: 9.5pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(2).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 303.85pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-right: none;padding: 0in 5.4pt;height: 9.5pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(2).getWorkplace() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:3pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-family:\"Arial\",\"sans-serif\";color:black;'>" +
                        "موافقة مجلس القسم على تعـديل الرسـالة : " +
                        " &nbsp;&nbsp;/ &nbsp;&nbsp;/&nbsp; &nbsp; &nbsp; &nbsp;&nbsp;م" +
                        "&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-family:\"Arial\",\"sans-serif\";color:black;'>&nbsp;" +
                        "موافقة مجلس الكلية على تعـديل الرسـالة :" +
                        " &nbsp;&nbsp;/ &nbsp;&nbsp;/&nbsp; &nbsp; &nbsp; &nbsp;&nbsp;م" +
                        "&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:center;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><u><span style='font-family:\"Arial\",\"sans-serif\";color:black;'>السيد الأستاذ الدكتور / نائب رئيس الجامعة</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:center;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><u><span style='font-family:\"Arial\",\"sans-serif\";color:black;'>برجاء التكرم بالموافقة نحو اعتماد التعديل المشار إليه،،</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>وتفضلوا بقبول فائق الاحترام ،،،،</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>الدراسات العـليا &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span></strong><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>رئيس الأدارة</span></strong><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>&nbsp;</span></strong><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; مديـر عـام الكلية &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;يعـتمد ،، &nbsp; أ.د./ وكيل الكلية للدراسات العليا</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ( &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; )</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:center;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><u><span style='font-family:\"Arial\",\"sans-serif\";color:black;'>الإدارة العامة للدراسات العـليا والبحوث بالجامعة</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:center;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><u><span style='font-family:\"Arial\",\"sans-serif\";color:black;'>روجعت البيانات والإجراءات صحيحة واللوائح مطابقة</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;line-height:115%;font-family:\"Arial\",\"sans-serif\";color:black;'>الموظف المختص &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;مدير الإدارة &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ( المدير العام )</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;line-height:115%;font-family:\"Arial\",\"sans-serif\";color:black;'>&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:justify;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;line-height:115%;font-family:\"Arial\",\"sans-serif\";color:black;'>موافقة ،،(أ.د./ نائب رئيس الجامعة المختص ) يعتمد ،،،،( أ.د./ رئيس الجـامعـة ) &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:justify;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;line-height:115%;font-family:\"Arial\",\"sans-serif\";color:black;'>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; مصـادقــة مجـلس الجــامعـة بجلسـتـه رقـــم { &nbsp; &nbsp; &nbsp; } بتـاريـخ &nbsp; &nbsp; &nbsp;/ &nbsp; &nbsp; &nbsp; / &nbsp; 20م</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:right;font-size:16px;font-family:\"Times New Roman\",\"serif\";line-height:115%;'><br></p>" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:17px;line-height:115%;font-family:\"Arial\",\"sans-serif\";color:black;'>&nbsp;</span></strong></p>" +
                        "<div align=\"right\" dir=\"rtl\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'>\n" +
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:right;font-size:16px;font-family:\"Times New Roman\",\"serif\";line-height:115%;'><br></p>"+
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:right;font-size:16px;font-family:\"Times New Roman\",\"serif\";line-height:115%;'><br></p>" +
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:right;font-size:16px;font-family:\"Times New Roman\",\"serif\";line-height:115%;'><br></p>"+
                        "    <table dir=\"rtl\" style=\"width:545.75pt;margin-left:12.45pt;border-collapse:collapse;border:none;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 212.65pt;border: 1pt solid black;padding: 0in 5.4pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";'>جـــامـعـــة الأزهـــر</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td rowspan=\"3\" style=\"width: 92.8pt;border-top: 1pt solid black;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;border-right: none;padding: 0in 5.4pt;height: 17.3pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><span dir=\"LTR\" style=\"font-size:13px;\">&nbsp;<img src=\"file:///android_res/drawable/logo.png\"\n\"></span></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 241pt;border-top: 1pt solid black;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;border-right: none;padding: 0in 5.4pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align: right;line-height: normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span dir=\"LTR\" style=\"font-size:16px;\">AL-AZHAR &nbsp;UNIVERSITY &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 212.65pt;border-right: 1pt solid black;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;border-top: none;padding: 0in 5.4pt;height: 14.45pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";'>كـلــيـة الـهـنــدســـــة</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 241pt;border-top: none;border-left: 1pt solid black;border-bottom: 1pt solid black;border-right: none;padding: 0in 5.4pt;height: 14.45pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align: right;line-height: normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span dir=\"LTR\" style=\"font-size:16px;\">FACULTY &nbsp;OF &nbsp; &nbsp; ENGINEERING &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 212.65pt;border-right: 1pt solid black;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;border-top: none;padding: 0in 5.4pt;height: 16.25pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";'>الــدراســات الـعــلــيــا</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 241pt;border-top: none;border-left: 1pt solid black;border-bottom: 1pt solid black;border-right: none;padding: 0in 5.4pt;height: 16.25pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align: right;line-height: normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";'>&nbsp;</span></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><span style='font-size:16px;line-height:115%;font-family:\"Arial\",\"sans-serif\";'>&nbsp;</span></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:150%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>السيـد الأســتـاذ / مــديـر الإدارة العـامــة للـدراســات العـليـا والبحـوث بالجـامعــة</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:150%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>تحـيــة طيبـة وبعــد ,,</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:150%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>&nbsp;نتشــرف بأن نـحـيـط عـلم سيـادتـكم بأن مـجـلس الكـليــة المنعـقـد بجـلستـه رقــم {603} بتـاريـخ : &nbsp;" +
                        "&nbsp&nbsp&nbsp/ &nbsp&nbsp &nbsp; / &nbsp&nbsp&nbsp&nbsp&nbspم " +
                        "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:150%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>قـد وافـق عـلى قــرار مـجـلـس قـســم " +
                        departName +
                        " بتاريخ: &nbsp; &nbsp&nbsp &nbsp;/ &nbsp; &nbsp&nbsp&nbsp&nbsp; / &nbsp; &nbsp;&nbsp&nbsp&nbsp&nbsp م بالمـوافـقــة عـلى تـعـديـل مـوضوع الرسـالـة الخـاص بالبـاحــث /&nbsp;</span></strong><strong><span style='font-size:21px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>" +
                        arabicName +
                        "</span></strong><strong><span style='font-size:17px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>&nbsp;</span></strong><strong><span style='font-size:19px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>" +
                        "المـسجـل بالـدراسـات العـلــيـا بالكـلـيـة للحـصـول عـلى درجــة التخـصص{" +
                        degname +
                        "} " +
                        "فــي " +
                        departName+
                        " { تعـديل " +
                        esntial+
                        "}.</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:150%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;ومــرفـق طيه نـموذج التـعـديـل وصـورة طـبق الأصـل بمـذكـرة مـجـلس القـسـم وكـذلك تـقـريـر لجنـة الأشراف .</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:150%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>مـرسـل بـرجـاء العـلم والإحاطة واتـخـاذ اللازم,,,</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:150%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>وتـفـضـلوا بقـبول فـائق الاحـترام ،،</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:115%;font-family:\"Arial\",\"sans-serif\";'>&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:115%;font-family:\"Arial\",\"sans-serif\";'>الـدراســات العـليـا &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span></strong><strong><span style='font-size:19px;line-height:115%;font-family:\"Arial\",\"sans-serif\";color:black;'>رئيس الأدارة</span></strong><strong><span style='font-size:19px;line-height:115%;font-family:\"Arial\",\"sans-serif\";'>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; مــديـر عــام الكـلـيــة</span></strong></p>\n" )
                .create();
    }

    private  void printPdf2(){

        new CreatePdf(allChangeTitleRequest.getContext())
                .setPdfName("FirstPdf")
                .openPrintDialog(true)
                .setPageSize(PrintAttributes.MediaSize.ISO_A4)
                .setContent("<div align=\"right\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'>\n" +
                        "    <table dir=\"rtl\" style=\"width:549.95pt;margin-left:12.45pt;border-collapse:collapse;border:none;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 214.3pt;border: 1pt solid black;padding: 0in 5.4pt;height: 17.3pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";'>جـــامـعـــة الأزهـــر</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td rowspan=\"3\" style=\"width: 92.8pt;border-top: 1pt solid black;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;border-right: none;padding: 0in 5.4pt;height: 17.3pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><span dir=\"LTR\" style=\"font-size:13px;\">&nbsp;<img src=\"file:///android_res/drawable/logo.png\"\n\"></span></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 242.85pt;border-top: 1pt solid black;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;border-right: none;padding: 0in 5.4pt;padding-top: 0in 5.4pt;height: 10.7pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align: right;line-height: normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span dir=\"LTR\" style=\"font-size:13px;\">AL-AZHAR &nbsp;UNIVERSITY &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp;&nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 214.3pt;border-right: 1pt solid black;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;border-top: none;padding: 0in 5.4pt;height: 10.7pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";'>كـلــيـة الـهـنــدســـــة</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 242.85pt;border-top: none;border-left: 1pt solid black;border-bottom: 1pt solid black;border-right: none;padding: 0in 5.4pt;height: 10.7pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align: right;line-height: normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span dir=\"LTR\" style=\"font-size:13px;\">FACULTY &nbsp;OF &nbsp; &nbsp; ENGINEERING  &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 214.3pt;border-right: 1pt solid black;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;border-top: none;padding: 0in 5.4pt;height: 14.2pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";'>الــدراســات الـعــلــيــا</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 242.85pt;border-top: none;border-left: 1pt solid black;border-bottom: 1pt solid black;border-right: none;padding: 0in 5.4pt;height: 14.2pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align: right;line-height: normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";'>&nbsp;</span></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><u><span style='font-family:\"Arial\",\"sans-serif\";'>طلب الموافـقة عـلى تعـديـل الرســالة لدرجة العالمية{" +
                        degname +
                        "} <span style=\"color:black;\">، " +
                        "{ تعديل " +
                        esntial +
                        " }&nbsp;</span></span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-family:\"Arial\",\"sans-serif\";'>أسـم الباحث/ " +
                        arabicName +
                        " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp" +
                        ";" +
                        "الجنسيه" +
                        " /" +
                        nationality +
                        "&nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-family:\"Arial\",\"sans-serif\";'>القـسـم / " +
                        departName +
                        " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;الدرجة / " +
                        degname +
                        "&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-family:\"Arial\",\"sans-serif\";'>&nbsp;المؤهـل الدراسي :" +
                        qualification +" "+
                        departName +
                        " - &nbsp;" +
                        lastFac +
                        "&nbsp;</span></strong></p>\n" +
                        "<div align=\"right\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'>\n" +
                        "    <table dir=\"rtl\" style=\"margin-left:5.4pt;border-collapse:collapse;border:none;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 538.65pt;border: 1pt solid black;padding: 0in 5.4pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-family:\"Arial\",\"sans-serif\";'>مـوضـوع الرسـالة المعـتمـد:&quot;" +
                        oldAddress +
                        " &quot;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-family:\"Arial\",\"sans-serif\";'>موافقة الكلية عـلى التسجـيل :" +
                        fac_Council+
                        "&nbsp;----<span style=\"color:black;\">موافقـة الجامعة عـلى التسجيل: " +
                        uni_Council +
                        ".</span></span></strong></p>\n" +
                        "<div align=\"right\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'>\n" +
                        "    <table dir=\"rtl\" style=\"margin-left:5.4pt;border-collapse:collapse;border:none;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 536.75pt;border: 1pt solid black;padding: 0in 5.4pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-family:\"Arial\",\"sans-serif\";color:black;'>مـوضوع الرسـالة المقـترح : &quot;&nbsp;</span></strong><strong><span style='font-family:  \"Arial\",\"sans-serif\"" +
                        ";'" +
                        ">" +
                        newArabicAddress+
                        " <span style=\"color:black;\">&quot;</span></span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><u><span style='font-family:\"Arial\",\"sans-serif\";'>لجنة الأشراف المعـتمدة &nbsp; :</span></u></strong></p>\n" +
                        "<div align=\"right\" style='margin-top:0in;margin-right:0in;margin-bottom:2pt;margin-left:0in;text-align:right;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'>\n" +
                        "    <table dir=\"rtl\" style=\"width:536.85pt;margin-left:5.4pt;border-collapse:collapse;border:none;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 21.45pt;border: 1pt solid windowtext;padding: 0in 5.4pt;height: 16.45pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>م</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 155.05pt;border-top: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;border-right: none;padding: 0in 5.4pt;height: 16.45pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>لجنة الأشراف&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 56.5pt;border-top: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;border-right: none;padding: 0in 5.4pt;height: 16.45pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>الدرجـة</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 303.85pt;border-top: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;border-right: none;padding: 0in 5.4pt;height: 16.45pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>جهة العمل</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 21.45pt;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;border-top: none;padding: 0in 5.4pt;height: 12.65pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>1</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 155.05pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-right: none;padding: 0in 5.4pt;height: 12.65pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:  \"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(0).getName()+
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 56.5pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt " +
                        "solid windowtext;border-right: none;padding: 0in 5.4pt;height: 12.65pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(0).getDegree()+
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 303.85pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-right: none;padding: 0in 5.4pt;height: 12.65pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(0).getWorkplace() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 21.45pt;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;border-top: none;padding: 0in 5.4pt;height: 9.5pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>2</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 155.05pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-right: none;padding: 0in 5.4pt;height: 9.5pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:  \"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(1).getName() +
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 56.5pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-right: none;padding: 0in 5.4pt;height: 9.5pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(1).getDegree()+
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 303.85pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-right: none;padding: 0in 5.4pt;height: 9.5pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(1).getWorkplace() +
                        ".</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-family:\"Arial\",\"sans-serif\";color:black;'>" +
                        "موافقة مجلس القسم على تعـديل الرسـالة : " +
                        " &nbsp;&nbsp;/ &nbsp;&nbsp;/&nbsp; &nbsp; &nbsp; &nbsp;&nbsp;م"+
                        "</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-family:\"Arial\",\"sans-serif\";color:black;'>&nbsp;موافقة مجلس الكلية على تعـديل الرسـالة : &nbsp;&nbsp;/&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;م&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:center;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><u><span style='font-family:\"Arial\",\"sans-serif\";color:black;'>السيد الأستاذ الدكتور / نائب رئيس الجامعة</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:center;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><u><span style='font-family:\"Arial\",\"sans-serif\";color:black;'>برجاء التكرم بالموافقة نحو اعتماد التعديل المشار إليه،،</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>وتفضلوا بقبول فائق الاحترام ،،،،</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>الدراسات العـليا &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span></strong><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>رئيس الأدارة</span></strong><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>&nbsp;</span></strong><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; مديـر عـام الكلية &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;يعـتمد ،، &nbsp; أ.د./ وكيل الكلية للدراسات العليا</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ( &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; )</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:center;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><u><span style='font-family:\"Arial\",\"sans-serif\";color:black;'>الإدارة العامة للدراسات العـليا والبحوث بالجامعة</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:center;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><u><span style='font-family:\"Arial\",\"sans-serif\";color:black;'>روجعت البيانات والإجراءات صحيحة واللوائح مطابقة</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;line-height:115%;font-family:\"Arial\",\"sans-serif\";color:black;'>الموظف المختص &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;مدير الإدارة &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ( المدير العام )</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;line-height:115%;font-family:\"Arial\",\"sans-serif\";color:black;'>&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:justify;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;line-height:115%;font-family:\"Arial\",\"sans-serif\";color:black;'>موافقة ،،(أ.د./ نائب رئيس الجامعة المختص ) يعتمد ،،،،( أ.د./ رئيس الجـامعـة ) &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:justify;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;line-height:115%;font-family:\"Arial\",\"sans-serif\";color:black;'>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; مصـادقــة مجـلس الجــامعـة بجلسـتـه رقـــم { &nbsp; &nbsp; &nbsp; } بتـاريـخ &nbsp; &nbsp; &nbsp;/ &nbsp; &nbsp; &nbsp; / &nbsp; 20م</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:right;font-size:16px;font-family:\"Times New Roman\",\"serif\";line-height:115%;'><br></p>"+
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:right;font-size:16px;font-family:\"Times New Roman\",\"serif\";line-height:115%;'><br></p>"+
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:right;font-size:16px;font-family:\"Times New Roman\",\"serif\";line-height:115%;'><br></p>"+
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:17px;line-height:115%;font-family:\"Arial\",\"sans-serif\";color:black;'>&nbsp;</span></strong></p>"
                        +"<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:right;font-size:16px;font-family:\"Times New Roman\",\"serif\";line-height:115%;'><br></p>" +
                        "<div align=\"right\" dir=\"rtl\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'>\n" +
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:right;font-size:16px;font-family:\"Times New Roman\",\"serif\";line-height:115%;'><br></p>" +
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:right;font-size:16px;font-family:\"Times New Roman\",\"serif\";line-height:115%;'><br></p>"+
                        "    <table dir=\"rtl\" style=\"width:545.75pt;margin-left:12.45pt;border-collapse:collapse;border:none;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 212.65pt;border: 1pt solid black;padding: 0in 5.4pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";'>جـــامـعـــة الأزهـــر</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td rowspan=\"3\" style=\"width: 92.8pt;border-top: 1pt solid black;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;border-right: none;padding: 0in 5.4pt;height: 17.3pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><span dir=\"LTR\" style=\"font-size:13px;\">&nbsp;<img src=\"file:///android_res/drawable/logo.png\"\n\"></span></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 241pt;border-top: 1pt solid black;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;border-right: none;padding: 0in 5.4pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align: right;line-height: normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span dir=\"LTR\" style=\"font-size:16px;\">AL-AZHAR &nbsp;UNIVERSITY &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 212.65pt;border-right: 1pt solid black;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;border-top: none;padding: 0in 5.4pt;height: 14.45pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";'>كـلــيـة الـهـنــدســـــة</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 241pt;border-top: none;border-left: 1pt solid black;border-bottom: 1pt solid black;border-right: none;padding: 0in 5.4pt;height: 14.45pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align: right;line-height: normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span dir=\"LTR\" style=\"font-size:16px;\">FACULTY &nbsp;OF &nbsp; &nbsp; ENGINEERING &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 212.65pt;border-right: 1pt solid black;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;border-top: none;padding: 0in 5.4pt;height: 16.25pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";'>الــدراســات الـعــلــيــا</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 241pt;border-top: none;border-left: 1pt solid black;border-bottom: 1pt solid black;border-right: none;padding: 0in 5.4pt;height: 16.25pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align: right;line-height: normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";'>&nbsp;</span></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><span style='font-size:16px;line-height:115%;font-family:\"Arial\",\"sans-serif\";'>&nbsp;</span></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:150%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>السيـد الأســتـاذ / مــديـر الإدارة العـامــة للـدراســات العـليـا والبحـوث بالجـامعــة</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:150%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>تحـيــة طيبـة وبعــد ,,</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:150%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>&nbsp;نتشــرف بأن نـحـيـط عـلم سيـادتـكم بأن مـجـلس الكـليــة المنعـقـد بجـلستـه رقــم {603} بتـاريـخ : &nbsp;" +
                        "&nbsp&nbsp&nbsp/ &nbsp&nbsp &nbsp; / &nbsp&nbsp&nbsp&nbsp&nbspم " +
                        "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:150%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>قـد وافـق عـلى قــرار مـجـلـس قـســم " +
                        departName +
                        " بتاريخ: &nbsp; &nbsp&nbsp &nbsp;/ &nbsp; &nbsp&nbsp&nbsp&nbsp; / &nbsp; &nbsp;&nbsp&nbsp&nbsp&nbsp م بالمـوافـقــة عـلى تـعـديـل مـوضوع الرسـالـة الخـاص بالبـاحــث /&nbsp;</span></strong><strong><span style='font-size:21px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>" +
                        arabicName +
                        "</span></strong><strong><span style='font-size:17px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>&nbsp;</span></strong><strong><span style='font-size:19px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>" +
                        "المـسجـل بالـدراسـات العـلــيـا بالكـلـيـة للحـصـول عـلى درجــة التخـصص{" +
                        degname+
                        "} " +
                        "فــي " +
                        departName+
                        " { تعـديل " +
                        esntial+
                        "}.</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:150%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;ومــرفـق طيه نـموذج التـعـديـل وصـورة طـبق الأصـل بمـذكـرة مـجـلس القـسـم وكـذلك تـقـريـر لجنـة الأشراف .</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:150%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>مـرسـل بـرجـاء العـلم والإحاطة واتـخـاذ اللازم,,,</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:150%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>وتـفـضـلوا بقـبول فـائق الاحـترام ،،</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:115%;font-family:\"Arial\",\"sans-serif\";'>&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:115%;font-family:\"Arial\",\"sans-serif\";'>الـدراســات العـليـا &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span></strong><strong><span style='font-size:19px;line-height:115%;font-family:\"Arial\",\"sans-serif\";color:black;'>رئيس الأدارة</span></strong><strong><span style='font-size:19px;line-height:115%;font-family:\"Arial\",\"sans-serif\";'>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; مــديـر عــام الكـلـيــة</span></strong></p>\n" )
                .create();

    }

    private  void printPdf4(){

        new CreatePdf(allChangeTitleRequest.getContext())
                .setPdfName("FirstPdf")
                .openPrintDialog(true)
                .setPageSize(PrintAttributes.MediaSize.ISO_A4)
                .setContent("<div align=\"right\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'>\n" +
                        "    <table dir=\"rtl\" style=\"width:549.95pt;margin-left:12.45pt;border-collapse:collapse;border:none;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 214.3pt;border: 1pt solid black;padding: 0in 5.4pt;height: 17.3pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";'>جـــامـعـــة الأزهـــر</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td rowspan=\"3\" style=\"width: 92.8pt;border-top: 1pt solid black;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;border-right: none;padding: 0in 5.4pt;height: 17.3pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><span dir=\"LTR\" style=\"font-size:13px;\">&nbsp;<img src=\"file:///android_res/drawable/logo.png\"\n\"></span></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 242.85pt;border-top: 1pt solid black;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;border-right: none;padding: 0in 5.4pt;padding-top: 0in 5.4pt;height: 10.7pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align: right;line-height: normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span dir=\"LTR\" style=\"font-size:13px;\">AL-AZHAR &nbsp;UNIVERSITY &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp;&nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 214.3pt;border-right: 1pt solid black;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;border-top: none;padding: 0in 5.4pt;height: 10.7pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";'>كـلــيـة الـهـنــدســـــة</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 242.85pt;border-top: none;border-left: 1pt solid black;border-bottom: 1pt solid black;border-right: none;padding: 0in 5.4pt;height: 10.7pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align: right;line-height: normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span dir=\"LTR\" style=\"font-size:13px;\">FACULTY &nbsp;OF &nbsp; &nbsp; ENGINEERING  &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 214.3pt;border-right: 1pt solid black;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;border-top: none;padding: 0in 5.4pt;height: 14.2pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";'>الــدراســات الـعــلــيــا</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 242.85pt;border-top: none;border-left: 1pt solid black;border-bottom: 1pt solid black;border-right: none;padding: 0in 5.4pt;height: 14.2pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align: right;line-height: normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";'>&nbsp;</span></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><u><span style='font-family:\"Arial\",\"sans-serif\";'>طلب الموافـقة عـلى تعـديـل الرســالة لدرجة العالمية{" +
                        degname +
                        "} <span style=\"color:black;\">، " +
                        "{ تعديل " +
                        esntial +
                        " }&nbsp;</span></span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-family:\"Arial\",\"sans-serif\";'>أسـم الباحث/ " +
                        arabicName +
                        " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp" +
                        ";" +
                        "الجنسيه" +
                        " /" +
                        nationality +
                        "&nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-family:\"Arial\",\"sans-serif\";'>القـسـم / " +
                        departName +
                        " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;الدرجة / " +
                        degname +
                        "&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-family:\"Arial\",\"sans-serif\";'>&nbsp;المؤهـل الدراسي :" +
                        qualification +" "+
                        departName +
                        " - &nbsp;" +
                        lastFac +
                        "&nbsp;</span></strong></p>\n" +
                        "<div align=\"right\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'>\n" +
                        "    <table dir=\"rtl\" style=\"margin-left:5.4pt;border-collapse:collapse;border:none;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 538.65pt;border: 1pt solid black;padding: 0in 5.4pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-family:\"Arial\",\"sans-serif\";'>مـوضـوع الرسـالة المعـتمـد:&quot;" +
                        oldAddress +
                        " &quot;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-family:\"Arial\",\"sans-serif\";'>موافقة الكلية عـلى التسجـيل :" +
                        fac_Council+
                        "&nbsp;----<span style=\"color:black;\">موافقـة الجامعة عـلى التسجيل: " +
                        uni_Council +
                        ".</span></span></strong></p>\n" +
                        "<div align=\"right\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'>\n" +
                        "    <table dir=\"rtl\" style=\"margin-left:5.4pt;border-collapse:collapse;border:none;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 536.75pt;border: 1pt solid black;padding: 0in 5.4pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-family:\"Arial\",\"sans-serif\";color:black;'>مـوضوع الرسـالة المقـترح : &quot;&nbsp;</span></strong><strong><span style='font-family:  \"Arial\",\"sans-serif\"" +
                        ";'" +
                        ">" +
                        newArabicAddress+
                        " <span style=\"color:black;\">&quot;</span></span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><u><span style='font-family:\"Arial\",\"sans-serif\";'>لجنة الأشراف المعـتمدة &nbsp; :</span></u></strong></p>\n" +
                        "<div align=\"right\" style='margin-top:0in;margin-right:0in;margin-bottom:2pt;margin-left:0in;text-align:right;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'>\n" +
                        "    <table dir=\"rtl\" style=\"width:536.85pt;margin-left:5.4pt;border-collapse:collapse;border:none;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 21.45pt;border: 1pt solid windowtext;padding: 0in 5.4pt;height: 16.45pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>م</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 155.05pt;border-top: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;border-right: none;padding: 0in 5.4pt;height: 16.45pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>لجنة الأشراف&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 56.5pt;border-top: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;border-right: none;padding: 0in 5.4pt;height: 16.45pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>الدرجـة</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 303.85pt;border-top: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;border-right: none;padding: 0in 5.4pt;height: 16.45pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>جهة العمل</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 21.45pt;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;border-top: none;padding: 0in 5.4pt;height: 12.65pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>1</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 155.05pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-right: none;padding: 0in 5.4pt;height: 12.65pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:  \"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(0).getName()+
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 56.5pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt " +
                        "solid windowtext;border-right: none;padding: 0in 5.4pt;height: 12.65pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(0).getDegree()+
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 303.85pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-right: none;padding: 0in 5.4pt;height: 12.65pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(0).getWorkplace() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 21.45pt;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;border-top: none;padding: 0in 5.4pt;height: 9.5pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>2</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 155.05pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-right: none;padding: 0in 5.4pt;height: 9.5pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:  \"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(1).getName() +
                        "&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 56.5pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-right: none;padding: 0in 5.4pt;height: 9.5pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(1).getDegree()+
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 303.85pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-right: none;padding: 0in 5.4pt;height: 9.5pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(1).getWorkplace() +
                        ".</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 21.45pt;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;border-top: none;padding: 0in 5.4pt;height: 9.5pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>3</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 155.05pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-right: none;padding: 0in 5.4pt;height: 9.5pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:  \"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(2).getName()+
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 56.5pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-right: none;padding: 0in 5.4pt;height: 9.5pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(2).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 303.85pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-right: none;padding: 0in 5.4pt;height: 9.5pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(2).getWorkplace() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 21.45pt;border-right: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-left: 1pt solid windowtext;border-image: initial;border-top: none;padding: 0in 5.4pt;height: 9.5pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>4</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 155.05pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-right: none;padding: 0in 5.4pt;height: 9.5pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:  \"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(3).getName()+
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 56.5pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-right: none;padding: 0in 5.4pt;height: 9.5pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(3).getDegree() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 303.85pt;border-top: none;border-left: 1pt solid windowtext;border-bottom: 1pt solid windowtext;border-right: none;padding: 0in 5.4pt;height: 9.5pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:13px;font-family:\"Arial\",\"sans-serif\";color:black;'>" +
                        list.get(3).getWorkplace() +
                        "</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-family:\"Arial\",\"sans-serif\";color:black;'>موافقة مجلس القسم على تعـديل الرسـالة : &nbsp;&nbsp; &nbsp;/&nbsp;&nbsp; &nbsp;/&nbsp;&nbsp;&nbsp;&nbsp;م&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-family:\"Arial\",\"sans-serif\";color:black;'>&nbsp;موافقة مجلس الكلية على تعـديل الرسـالة : &nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;م&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:center;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><u><span style='font-family:\"Arial\",\"sans-serif\";color:black;'>السيد الأستاذ الدكتور / نائب رئيس الجامعة</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:center;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><u><span style='font-family:\"Arial\",\"sans-serif\";color:black;'>برجاء التكرم بالموافقة نحو اعتماد التعديل المشار إليه،،</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>وتفضلوا بقبول فائق الاحترام ،،،،</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>الدراسات العـليا &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span></strong><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>رئيس الأدارة</span></strong><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>&nbsp;</span></strong><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; مديـر عـام الكلية &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;يعـتمد ،، &nbsp; أ.د./ وكيل الكلية للدراسات العليا</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";color:black;'>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ( &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; )</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:center;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><u><span style='font-family:\"Arial\",\"sans-serif\";color:black;'>الإدارة العامة للدراسات العـليا والبحوث بالجامعة</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:center;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><u><span style='font-family:\"Arial\",\"sans-serif\";color:black;'>روجعت البيانات والإجراءات صحيحة واللوائح مطابقة</span></u></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;line-height:115%;font-family:\"Arial\",\"sans-serif\";color:black;'>الموظف المختص &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;مدير الإدارة &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ( المدير العام )</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;line-height:115%;font-family:\"Arial\",\"sans-serif\";color:black;'>&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:justify;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;line-height:115%;font-family:\"Arial\",\"sans-serif\";color:black;'>موافقة ،،(أ.د./ نائب رئيس الجامعة المختص ) يعتمد ،،،،( أ.د./ رئيس الجـامعـة ) &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:.0001pt;margin-left:0in;text-align:justify;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;line-height:115%;font-family:\"Arial\",\"sans-serif\";color:black;'>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; مصـادقــة مجـلس الجــامعـة بجلسـتـه رقـــم { &nbsp; &nbsp; &nbsp; } بتـاريـخ &nbsp; &nbsp; &nbsp;/ &nbsp; &nbsp; &nbsp; / &nbsp; 20م</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:justify;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:17px;line-height:115%;font-family:\"Arial\",\"sans-serif\";color:black;'>&nbsp;</span></strong></p>"+
                        "<p dir=\"RTL\" style='margin:0in;margin-bottom:.0001pt;text-align:right;font-size:16px;font-family:\"Times New Roman\",\"serif\";line-height:115%;'><br></p>"
                        +"<div align=\"right\" dir=\"rtl\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'>\n" +
                        "    <table dir=\"rtl\" style=\"width:545.75pt;margin-left:12.45pt;border-collapse:collapse;border:none;\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 212.65pt;border: 1pt solid black;padding: 0in 5.4pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";'>جـــامـعـــة الأزهـــر</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td rowspan=\"3\" style=\"width: 92.8pt;border-top: 1pt solid black;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;border-right: none;padding: 0in 5.4pt;height: 17.3pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:  normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><span dir=\"LTR\" style=\"font-size:13px;\">&nbsp;<img src=\"file:///android_res/drawable/logo.png\"\n\"></span></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 241pt;border-top: 1pt solid black;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;border-right: none;padding: 0in 5.4pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align: right;line-height: normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span dir=\"LTR\" style=\"font-size:16px;\">AL-AZHAR &nbsp;UNIVERSITY &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 212.65pt;border-right: 1pt solid black;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;border-top: none;padding: 0in 5.4pt;height: 14.45pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";'>كـلــيـة الـهـنــدســـــة</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 241pt;border-top: none;border-left: 1pt solid black;border-bottom: 1pt solid black;border-right: none;padding: 0in 5.4pt;height: 14.45pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align: right;line-height: normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span dir=\"LTR\" style=\"font-size:16px;\">FACULTY &nbsp;OF &nbsp; &nbsp; ENGINEERING &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td style=\"width: 212.65pt;border-right: 1pt solid black;border-bottom: 1pt solid black;border-left: 1pt solid black;border-image: initial;border-top: none;padding: 0in 5.4pt;height: 16.25pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";'>الــدراســات الـعــلــيــا</span></strong></p>\n" +
                        "                </td>\n" +
                        "                <td style=\"width: 241pt;border-top: none;border-left: 1pt solid black;border-bottom: 1pt solid black;border-right: none;padding: 0in 5.4pt;height: 16.25pt;vertical-align: top;\">\n" +
                        "                    <p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align: right;line-height: normal;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><span style='font-size:16px;font-family:\"Arial\",\"sans-serif\";'>&nbsp;</span></p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "</div>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><span style='font-size:16px;line-height:115%;font-family:\"Arial\",\"sans-serif\";'>&nbsp;</span></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:150%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>السيـد الأســتـاذ / مــديـر الإدارة العـامــة للـدراســات العـليـا والبحـوث بالجـامعــة</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:150%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>تحـيــة طيبـة وبعــد ,,</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:150%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>&nbsp;نتشــرف بأن نـحـيـط عـلم سيـادتـكم بأن مـجـلس الكـليــة المنعـقـد بجـلستـه رقــم {603} بتـاريـخ : &nbsp;" +
                        "&nbsp&nbsp&nbsp/ &nbsp&nbsp &nbsp; / &nbsp&nbsp&nbsp&nbsp&nbspم " +
                        "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:150%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>قـد وافـق عـلى قــرار مـجـلـس قـســم " +
                        departName +
                        " بتاريخ: &nbsp; &nbsp&nbsp &nbsp;/ &nbsp; &nbsp&nbsp&nbsp&nbsp; / &nbsp; &nbsp;&nbsp&nbsp&nbsp&nbsp م بالمـوافـقــة عـلى تـعـديـل مـوضوع الرسـالـة الخـاص بالبـاحــث /&nbsp;</span></strong><strong><span style='font-size:21px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>" +
                        arabicName +
                        "</span></strong><strong><span style='font-size:17px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>&nbsp;</span></strong><strong><span style='font-size:19px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>" +
                        "المـسجـل بالـدراسـات العـلــيـا بالكـلـيـة للحـصـول عـلى درجــة التخـصص{" +
                        degname+
                        "} " +
                        "فــي " +
                        departName+
                        " { تعـديل " +
                        esntial+
                        "}.</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:150%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;ومــرفـق طيه نـموذج التـعـديـل وصـورة طـبق الأصـل بمـذكـرة مـجـلس القـسـم وكـذلك تـقـريـر لجنـة الأشراف .</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:150%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>مـرسـل بـرجـاء العـلم والإحاطة واتـخـاذ اللازم,,,</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:150%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:150%;font-family:\"Arial\",\"sans-serif\";'>وتـفـضـلوا بقـبول فـائق الاحـترام ،،</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:right;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:115%;font-family:\"Arial\",\"sans-serif\";'>&nbsp;</span></strong></p>\n" +
                        "<p dir=\"RTL\" style='margin-top:0in;margin-right:0in;margin-bottom:10.0pt;margin-left:0in;text-align:center;line-height:115%;font-size:15px;font-family:\"Calibri\",\"sans-serif\";'><strong><span style='font-size:19px;line-height:115%;font-family:\"Arial\",\"sans-serif\";'>الـدراســات العـليـا &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span></strong><strong><span style='font-size:19px;line-height:115%;font-family:\"Arial\",\"sans-serif\";color:black;'>رئيس الأدارة</span></strong><strong><span style='font-size:19px;line-height:115%;font-family:\"Arial\",\"sans-serif\";'>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; مــديـر عــام الكـلـيــة</span></strong></p>\n" )
                .create();
    }


}
