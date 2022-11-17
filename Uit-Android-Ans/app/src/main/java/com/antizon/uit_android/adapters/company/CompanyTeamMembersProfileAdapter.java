package com.antizon.uit_android.adapters.company;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.utilities.Utilities;
import java.util.List;

public class CompanyTeamMembersProfileAdapter extends RecyclerView.Adapter<CompanyTeamMembersProfileAdapter.ViewHolder> {

    Context context;
    List<GenericApplicantDataModel> list;
    CompanyTeamMembersProfileAdapterCallBack callBack;

    public CompanyTeamMembersProfileAdapter(Context context, List<GenericApplicantDataModel> list, CompanyTeamMembersProfileAdapterCallBack callBack) {
        this.context = context;
        this.list = list;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.design_job_people, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GenericApplicantDataModel current = list.get(position);
        if (current !=null){
            holder.ivProfile.setVisibility(View.VISIBLE);
            holder.tvNumber.setVisibility(View.GONE);
            Utilities.loadImage(context, current.getProfile_image(), holder.ivProfile);

            holder.itemView.setOnClickListener(v -> callBack.onCompanyTeamMemberClick(position));
        }
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }else {
            return 0;
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfile;
        TextView tvNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvNumber = itemView.findViewById(R.id.tv_number);
        }
    }

    public interface CompanyTeamMembersProfileAdapterCallBack{
        void onCompanyTeamMemberClick(int position);
    }
}
