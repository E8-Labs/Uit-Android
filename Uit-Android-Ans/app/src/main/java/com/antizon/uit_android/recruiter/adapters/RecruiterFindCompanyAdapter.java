package com.antizon.uit_android.recruiter.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.model.ModelRecruiterFindCompany;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.List;

public class RecruiterFindCompanyAdapter extends RecyclerView.Adapter<RecruiterFindCompanyAdapter.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    boolean isLoadingAdded = false;

    Context context;
    List<ModelRecruiterFindCompany> companyList;
    RecruiterFindCompanyAdapterCallBack callBack;

    public RecruiterFindCompanyAdapter(Context context, List<ModelRecruiterFindCompany> companyList, RecruiterFindCompanyAdapterCallBack callBack) {
        this.companyList = companyList;
        this.context = context;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_HEADER) {
            view = LayoutInflater.from(context).inflate(R.layout.recruiter_find_company_header, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.recruiter_find_company_single_item, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (position == 0){
            if (companyList.size() == 0) {
                holder.layout_noCompanyFound.setVisibility(View.VISIBLE);
                holder.btnCreateCompany.setOnClickListener(v -> callBack.onCreateCompanyProfileClicked());
            }else {
                holder.layout_noCompanyFound.setVisibility(View.GONE);
                holder.searchOffer.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        callBack.onSearchFilter(s.toString());
                    }
                });
            }
        }else {
            ModelRecruiterFindCompany companyModel = companyList.get(position-1);
            if (companyModel != null){
                Utilities.loadImage(context, companyModel.getProfile_image(), holder.company_profileImage);
                String location;
                if (!companyModel.getCity().isEmpty()){
                    location = companyModel.getCity() + ", " + companyModel.getState();
                }else {
                    location = companyModel.getState();
                }

                holder.text_companyLocation.setText(location);
                holder.text_companyName.setText(companyModel.getName());

                holder.itemView.setOnClickListener(v -> callBack.onItemClicked(position-1));
            }
        }


    }

    @Override
    public int getItemCount() {
        return companyList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        }else {
            return VIEW_TYPE_NORMAL;
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<ModelRecruiterFindCompany> filteredList) {
        companyList = filteredList;
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        if (companyList.size() >= 12) {
            int position = companyList.size() - 1;
            ModelRecruiterFindCompany result = companyList.get(position);
            if (result != null && result.getName() == null) {
                //remove the LOADING item
                companyList.remove(position);
                //notify the removed item
                notifyItemRemoved(position);
            }
        }
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_companyName, text_companyLocation;
        RoundedImageView company_profileImage;

        // Layout Header
        RelativeLayout layout_search, layout_noCompanyFound;
        TextView btnCreateCompany;
        EditText searchOffer;

        public ViewHolder(View itemView) {
            super(itemView);
            text_companyName = itemView.findViewById(R.id.text_companyName);
            text_companyLocation = itemView.findViewById(R.id.text_companyLocation);
            company_profileImage = itemView.findViewById(R.id.company_profileImage);

            // Layout Header
            layout_noCompanyFound = itemView.findViewById(R.id.layout_noCompanyFound);
            layout_search = itemView.findViewById(R.id.layout_search);
            searchOffer = itemView.findViewById(R.id.searchOffer);
            btnCreateCompany = itemView.findViewById(R.id.btnCreateCompany);

        }
    }



    public interface RecruiterFindCompanyAdapterCallBack{
        void onItemClicked(int position);
        void onSearchFilter(String filter);
        void onCreateCompanyProfileClicked();
    }

}