package com.antizon.uit_android.adapters.applicant;

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
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.ArrayList;
import java.util.List;

public class CompanyHiredApplicantAdapter extends RecyclerView.Adapter<CompanyHiredApplicantAdapter.ViewHolder> {

    Context context;
    List<GenericApplicantDataModel> hiredApplicantsList;
    HiredApplicantAdapterCallBack callBack;

    public CompanyHiredApplicantAdapter(Context context, List<GenericApplicantDataModel> hiredApplicantsList, HiredApplicantAdapterCallBack callBack) {
        this.context = context;
        this.hiredApplicantsList = hiredApplicantsList;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder( LayoutInflater.from(context).inflate(R.layout.single_item_hired_applicant, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GenericApplicantDataModel hiredApplicant = hiredApplicantsList.get(position);
        if (hiredApplicant !=null){
            Utilities.loadImage(context, hiredApplicant.getProfile_image(), holder.image_profile);
            holder.text_profileName.setText(hiredApplicant.getName());
            holder.text_location.setText(hiredApplicant.getJob_title());
            holder.btnMessage.setOnClickListener(v -> callBack.onMessageClicked(position));
            holder.itemView.setOnClickListener(v -> callBack.onItemClick(position));
        }
    }

    @Override
    public int getItemCount() {
        return hiredApplicantsList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<GenericApplicantDataModel> filteredList) {
        hiredApplicantsList = filteredList;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView image_profile;
        TextView text_profileName, text_location, btnMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_profile = itemView.findViewById(R.id.image_profile);
            text_profileName = itemView.findViewById(R.id.text_profileName);
            text_location = itemView.findViewById(R.id.text_location);
            btnMessage = itemView.findViewById(R.id.btnMessage);
        }
    }


    public interface HiredApplicantAdapterCallBack{
        void onItemClick(int position);
        void onMessageClicked(int position);
    }
}
