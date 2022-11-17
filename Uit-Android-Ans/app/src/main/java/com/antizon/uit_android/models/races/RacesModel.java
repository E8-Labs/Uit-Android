package com.antizon.uit_android.models.races;

import android.os.Parcel;
import android.os.Parcelable;

import com.antizon.uit_android.generic.model.ModelApplicantRace;

import java.util.List;

public class RacesModel implements Parcelable {
    List<ModelApplicantRace> selectedRacesList;

    public RacesModel() {
    }

    public RacesModel(List<ModelApplicantRace> selectedRacesList) {
        this.selectedRacesList = selectedRacesList;
    }

    public List<ModelApplicantRace> getSelectedRacesList() {
        return selectedRacesList;
    }

    public void setSelectedRacesList(List<ModelApplicantRace> selectedRacesList) {
        this.selectedRacesList = selectedRacesList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.selectedRacesList);
    }

    public void readFromParcel(Parcel source) {
        this.selectedRacesList = source.createTypedArrayList(ModelApplicantRace.CREATOR);
    }

    protected RacesModel(Parcel in) {
        this.selectedRacesList = in.createTypedArrayList(ModelApplicantRace.CREATOR);
    }

    public static final Parcelable.Creator<RacesModel> CREATOR = new Parcelable.Creator<RacesModel>() {
        @Override
        public RacesModel createFromParcel(Parcel source) {
            return new RacesModel(source);
        }

        @Override
        public RacesModel[] newArray(int size) {
            return new RacesModel[size];
        }
    };
}
