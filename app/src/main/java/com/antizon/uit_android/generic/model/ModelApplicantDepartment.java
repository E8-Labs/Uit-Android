package com.antizon.uit_android.generic.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ModelApplicantDepartment implements Parcelable {

    int id;
    String name;
    Boolean isSelected = false;

//     "id": 4,
//             "job_title": "Pilot",
//     ,
//            "city": "San Jose",
//            "state": "CA",
//            "location_status": 3,
//            "employment_type": 3,
//            "job_status": 1,
//            "min_salary": 260596,
//            "max_salary": 400000,
//            "user": {
//        "id": 75,
//                "name": "Uber",
//                "role": 2,
//                "profile_image": "http://www.zorroapp.tech/uit/storage/app/Images/KlVadCGTQeVD91v9WIEeNBum2QMDr5IBxDfXlIOW.jpg",
//                "job_title": "",
//                "user_id": 127,
//                "greenhouse_access_token": null
//    },
//            "company": {
//        "id": 75,
//                "name": "Uber",
//                "role": 2,
//                "profile_image": "http://www.zorroapp.tech/uit/storage/app/Images/KlVadCGTQeVD91v9WIEeNBum2QMDr5IBxDfXlIOW.jpg",
//                "job_title": "",
//                "user_id": 127,
//                "greenhouse_access_token": null
//    },
//            "created_at": "2022-04-04T06:33:29.000000Z",
//            "applications": [],
//            "total_applications": 0

    public ModelApplicantDepartment() {
    }

    protected ModelApplicantDepartment(Parcel in) {
        id = in.readInt();
        name = in.readString();
        byte tmpIsSelected = in.readByte();
        isSelected = tmpIsSelected == 0 ? null : tmpIsSelected == 1;
    }

    public static final Creator<ModelApplicantDepartment> CREATOR = new Creator<ModelApplicantDepartment>() {
        @Override
        public ModelApplicantDepartment createFromParcel(Parcel in) {
            return new ModelApplicantDepartment(in);
        }

        @Override
        public ModelApplicantDepartment[] newArray(int size) {
            return new ModelApplicantDepartment[size];
        }
    };

    public int getId() {
        return id;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeByte((byte) (isSelected == null ? 0 : isSelected ? 1 : 2));
    }
}
