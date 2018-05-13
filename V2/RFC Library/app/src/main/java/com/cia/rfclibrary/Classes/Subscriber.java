package com.cia.rfclibrary.Classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Subscriber implements Parcelable{

    private String name, id, enrolledFor, reb, leb, center, enrollmentType, enrolledOn, dob, gender, phone, jointAccount, toyIssued, bookIssued, isToy, isGen, bookCount, toyCount;

    public Subscriber() {
    }

    protected Subscriber(Parcel in) {
        name = in.readString();
        id = in.readString();
        enrolledFor = in.readString();
        reb = in.readString();
        leb = in.readString();
        center = in.readString();
        enrollmentType = in.readString();
        enrolledOn = in.readString();
        dob = in.readString();
        gender = in.readString();
        phone = in.readString();
        jointAccount = in.readString();
        toyIssued = in.readString();
        bookIssued = in.readString();
        isToy = in.readString();
        isGen = in.readString();
        bookCount = in.readString();
        toyCount = in.readString();
    }

    public static final Creator<Subscriber> CREATOR = new Creator<Subscriber>() {
        @Override
        public Subscriber createFromParcel(Parcel in) {
            return new Subscriber(in);
        }

        @Override
        public Subscriber[] newArray(int size) {
            return new Subscriber[size];
        }
    };

    public String getBookCount() {
        return bookCount;
    }

    public void setBookCount(String bookCount) {
        this.bookCount = bookCount;
    }

    public String getToyCount() {
        return toyCount;
    }

    public void setToyCount(String toyCount) {
        this.toyCount = toyCount;
    }

    public String getReb() {
        return reb;
    }

    public void setReb(String reb) {
        this.reb = reb;
    }

    public String getLeb() {
        return leb;
    }

    public void setLeb(String leb) {
        this.leb = leb;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getEnrollmentType() {
        return enrollmentType;
    }

    public void setEnrollmentType(String enrollmentType) {
        this.enrollmentType = enrollmentType;
    }

    public String getEnrolledOn() {
        return enrolledOn;
    }

    public void setEnrolledOn(String enrolledOn) {
        this.enrolledOn = enrolledOn;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getJointAccount() {
        return jointAccount;
    }

    public void setJointAccount(String jointAccount) {
        this.jointAccount = jointAccount;
    }

    public String getToyIssued() {
        return toyIssued;
    }

    public void setToyIssued(String toyIssued) {
        this.toyIssued = toyIssued;
    }

    public String getBookIssued() {
        return bookIssued;
    }

    public void setBookIssued(String bookIssued) {
        this.bookIssued = bookIssued;
    }

    public String getIsToy() {
        return isToy;
    }

    public void setIsToy(String isToy) {
        this.isToy = isToy;
    }

    public String getIsGen() {
        return isGen;
    }

    public void setIsGen(String isGen) {
        this.isGen = isGen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnrolledFor() {
        return enrolledFor;
    }

    public void setEnrolledFor(String enrolledFor) {
        this.enrolledFor = enrolledFor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(enrolledFor);
        dest.writeString(reb);
        dest.writeString(leb);
        dest.writeString(center);
        dest.writeString(enrollmentType);
        dest.writeString(enrolledOn);
        dest.writeString(dob);
        dest.writeString(gender);
        dest.writeString(phone);
        dest.writeString(jointAccount);
        dest.writeString(toyIssued);
        dest.writeString(bookIssued);
        dest.writeString(isToy);
        dest.writeString(isGen);
        dest.writeString(bookCount);
        dest.writeString(toyCount);
    }
}
