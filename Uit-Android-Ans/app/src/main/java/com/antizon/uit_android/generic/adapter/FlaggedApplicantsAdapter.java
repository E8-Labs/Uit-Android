package com.antizon.uit_android.generic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.models.report.flaguser.ApplicantFlagDataModel;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.ArrayList;
import java.util.List;


public class FlaggedApplicantsAdapter extends RecyclerView.Adapter<FlaggedApplicantsAdapter.ViewHolder> {

    Context context;
    List<ApplicantFlagDataModel> flaggedApplicantsList;
    FlaggedApplicantsAdapterCallback callBack;

    public FlaggedApplicantsAdapter(Context context, List<ApplicantFlagDataModel> flaggedApplicantsList, FlaggedApplicantsAdapterCallback callBack) {
        this.context = context;
        this.flaggedApplicantsList = flaggedApplicantsList;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_item_flagged_applicant, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ApplicantFlagDataModel flaggedModel = flaggedApplicantsList.get(position);
        if (flaggedModel !=null){
            GenericApplicantDataModel flaggedByApplicantModel = flaggedModel.getFlaggedBy();
            GenericApplicantDataModel flaggedApplicantModel = flaggedModel.getFlaggedPerson();
            if (flaggedByApplicantModel !=null){
                Utilities.loadImage(context, flaggedByApplicantModel.getProfile_image(), holder.flaggedByApplicantProfile);
                holder.text_flaggedByApplicantName.setText(flaggedByApplicantModel.getName());
            }

            if (flaggedApplicantModel !=null){
                String flaggedApplicantName = "Flagged " + flaggedApplicantModel.getName();
                Utilities.loadImage(context, flaggedApplicantModel.getProfile_image(), holder.flaggedApplicantProfile);
                holder.text_flaggedApplicantName.setText(flaggedApplicantName);
            }

            holder.itemView.setOnClickListener(v -> callBack.onItemClick(position));

        }

    }

    @Override
    public int getItemCount() {
        return flaggedApplicantsList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<ApplicantFlagDataModel> filteredList) {
        flaggedApplicantsList = filteredList;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView flaggedByApplicantProfile, flaggedApplicantProfile;
        TextView text_flaggedByApplicantName, text_flaggedApplicantName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flaggedByApplicantProfile = itemView.findViewById(R.id.flaggedByApplicantProfile);
            flaggedApplicantProfile = itemView.findViewById(R.id.flaggedApplicantProfile);
            text_flaggedByApplicantName = itemView.findViewById(R.id.text_flaggedByApplicantName);
            text_flaggedApplicantName = itemView.findViewById(R.id.text_flaggedApplicantName);
        }
    }


    public interface FlaggedApplicantsAdapterCallback{
        void onItemClick(int position);
    }
}
