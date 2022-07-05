package com.antizon.uit_android.generic.model;

import java.io.Serializable;
import java.util.List;

public class ModelRecruiterFindCompany implements Serializable {

    int id;
    String name;
    String email;
    List<ModelApplicantDepartment> industryModel;

//    {
//        "name": "Antzion",
//            "email": "salman@gmail.com",
//            "city": "Chichawatni",
//            "state": "Punjab",
//            "profile_image": "http://www.zorroapp.tech/uit/storage/app/Images/RH38TDxs7XHn55h4YeFhddBEVeZTp3S672V1adNk.jpg",
//            "id": 4,
//            "user_id": 6,
//            "industries": [
//        {
//            "id": 1,
//                "name": "Accounting"
//        },
//        {
//            "id": 3,
//                "name": "Alternative Dispute Resolution"
//        }
//            ]
//    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    String profileImage;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public ModelRecruiterFindCompany() {
    }

    public int getId() {
        return id;
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


}
