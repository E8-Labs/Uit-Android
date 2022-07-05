package com.antizon.uit_android.generic.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.model.ModelCompanyInvite;


import java.util.List;

public class CompanyInviteAdapter extends RecyclerView.Adapter<CompanyInviteAdapter.MyViewHolder> {

    private static final String TAG = CompanyInviteAdapter.class.getSimpleName();
    List<ModelCompanyInvite> list;
    Context context;
    SelectionListener selectionListener;

    public CompanyInviteAdapter(List<ModelCompanyInvite> list, Context context, SelectionListener selectionListener) {

        this.list = list;
        this.context = context;
        this.selectionListener = selectionListener;
    }

    @NonNull
    @Override
    public CompanyInviteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_company_invite, parent, false);
        return new CompanyInviteAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CompanyInviteAdapter.MyViewHolder holder, final int position) {
        final ModelCompanyInvite dataModel = list.get(getItemViewType(position));

        holder.name.setText(dataModel.getName());
        holder.email.setText(dataModel.getEmail());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionListener.edit(dataModel);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionListener.delete(position);
            }
        });

        holder.companyInviteItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionListener.edit(dataModel);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, email, edit;
        ImageView profileImage, delete;
        ConstraintLayout companyInviteItemLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            email = itemView.findViewById(R.id.email);
            name = itemView.findViewById(R.id.name);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            profileImage = itemView.findViewById(R.id.profile_image);

            companyInviteItemLayout = itemView.findViewById(R.id.company_invite_item_layout);
        }
    }

    public interface SelectionListener {
        void edit(ModelCompanyInvite dataModel);

        void delete(int position);
    }

}