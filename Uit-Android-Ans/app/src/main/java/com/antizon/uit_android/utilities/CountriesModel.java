package com.antizon.uit_android.utilities;

public class CountriesModel {
    String name, code, format, flag;
    String dial_code, digit1, digit2;

    public CountriesModel(String name, String code, String format, String flag, String dial_code, String digit1, String digit2) {
        this.name = name;
        this.code = code;
        this.format = format;
        this.flag = flag;
        this.dial_code = dial_code;
        this.digit1 = digit1;
        this.digit2 = digit2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDial_code() {
        return dial_code;
    }

    public void setDial_code(String dial_code) {
        this.dial_code = dial_code;
    }

    public String getDigit1() {
        return digit1;
    }

    public void setDigit1(String digit1) {
        this.digit1 = digit1;
    }

    public String getDigit2() {
        return digit2;
    }

    public void setDigit2(String digit2) {
        this.digit2 = digit2;
    }
}
