package com.antizon.uit_android.utilities;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/********** Developed Muhammad Ans **********
 * Created by : Muhammad Ans on 13/08/2021 at 11:37 AM
 ******************************************************/


public class ArfiDeveloperTime {

    private final int SEC = 1000;
    private final int MIN = 60000;
    private final long HOUR = 3600000;
    private final long DAY = 86400000;
    private final long WEEK = 604800000;
    private final long MONTH = 2629743000L;
    private final long YEAR = 31556926000L;
    TimeZone timeZone;
    int standardTime;
    int dayLightSaving;
    int netTime;

    public ArfiDeveloperTime() {
        timeZone = TimeZone.getDefault();
        standardTime = timeZone.getRawOffset();
        dayLightSaving = timeZone.getDSTSavings();
        netTime = standardTime + dayLightSaving;
    }

    public String getPrettyTime(String time, String pattern, String from) {
        if (TextUtils.isEmpty(time)) return time;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        try {
            Date date = simpleDateFormat.parse(time);
            if (from.equals("inbox")){
                return date == null ? time : getPrettyTime((long) (date.getTime()+1.8e+7));
            }else {
                return date == null ? time : getPrettyTime((long) (date.getTime()));
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return time;
        }
    }

    public String getTime(long milliseconds) {

        //setup the current time
        long currentTime = System.currentTimeMillis();
        //subtract the zone time from current time
        currentTime = currentTime + netTime;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-hh:mm a", Locale.getDefault());//UK time is UTC or 0 GMT
        String currentDate = dateFormat.format(new Date(currentTime));
        String[] separated;
        separated = currentDate.split("-");
        int currentDay = Integer.parseInt(separated[0]);
        int currentMonth = Integer.parseInt(separated[1]);
        int currentYear = Integer.parseInt(separated[2]);

        //changing to utc time according to relative timezone
        milliseconds = milliseconds + netTime;

        //setup the input time
        String inputDate = dateFormat.format(new Date(milliseconds));
        separated = inputDate.split("-");
        int inputDay = Integer.parseInt(separated[0]);
        int inputMonth = Integer.parseInt(separated[1]);
        int inputYear = Integer.parseInt(separated[2]);

        String output = "";
        long timeDiff = currentTime - milliseconds;
        //Same year
        if (timeDiff < YEAR) {

            //Same Month
            if (timeDiff < MONTH) {

                //Same Day
                if (timeDiff < DAY) {

                    //within Same Hour
                    if (timeDiff < HOUR) {

                        //within same minute
                        if (timeDiff < MIN) {

                            //seconds ago
                            int seconds = (int) (timeDiff / SEC);
                            if (seconds < 30) {
                                output = "Just now";
                            } else {
                                output = seconds + " s ago";
                            }
                        } else {
                            //Minutes ago
                            int mins = (int) (timeDiff / MIN);
                            output = mins + " min ago";
                        }
                    } else {

                        //Hours ago
                        int hours = (int) (timeDiff / HOUR);
                        output = hours + " hr ago";
                    }

                } else { //Difference from 1 day to 1 month

                    //Same Week
                    if (timeDiff < WEEK) {
                        //Days ago
                        output = (timeDiff / DAY) + " days ago";
                    } else {
                        //Weeks ago
                        output = (timeDiff / WEEK) + " week ago";
                    }

                }
            } else {
                //Months ago
                output = (timeDiff / MONTH) + " month ago";
            }

        } else {
            //years ago
            output = (timeDiff / YEAR) + " y ago";
        }

        return output;
    }

    // API Time : 2021-05-11T09:10:14.000000Z
    public long getMillisecondsFromApiTime(String input) {
        Date output = new Date();
        try {
            input = input.split("\\.")[0];
            Log.e("YAM", "Input : " + input);
            String[] arr = input.split("T");
            String date = arr[0];
            Log.e("YAM", "Date : " + date);
            String time = arr[1];
            Log.e("YAM", "Time : " + time);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            output = dateFormat.parse(date + " " + time);
            return output.getTime() + netTime;
        } catch (Exception e) {
            Log.e("YAM", "Exception : " + e.getMessage());
            e.printStackTrace();
        }
        return output.getTime() + netTime;
    }

    public String getPrettyTimeFromCreatedAt(String time) {
        long millis = getMillisecondsFromApiTime(time);
        return getPrettyTime(millis);
    }

    /**
     * This method will return time from the API (created at) Time : 2021-05-11T09:10:14.000000Z
     */
    public String getTime(String time) {
        //get milliseconds from API (created at) timestamp
        long milliseconds = getMillisecondsFromApiTime(time);
        return getTime(milliseconds);
    }

    public String getPrettyTime(long milliseconds) {
        long currentTime = System.currentTimeMillis() + this.netTime;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-hh:mm a", Locale.getDefault());
        String currentDate = dateFormat.format(new Date(currentTime));
        String[] separated = currentDate.split("-");
        Integer.parseInt(separated[0]);
        Integer.parseInt(separated[1]);
        Integer.parseInt(separated[2]);
        long milliseconds2 = milliseconds + this.netTime;
        String inputDate = dateFormat.format(new Date(milliseconds2));
        String[] separated2 = inputDate.split("-");
        Integer.parseInt(separated2[0]);
        Integer.parseInt(separated2[1]);
        Integer.parseInt(separated2[2]);
        long timeDiff = currentTime - milliseconds2;
        if (timeDiff >= 31556926000L) {
            String output = (timeDiff / 31556926000L) + " year ago";
            return output;
        } else if (timeDiff >= 2629743000L) {
            String output2 = (timeDiff / 2629743000L) + " mon ago";
            return output2;
        } else if (timeDiff >= 86400000) {
            if (timeDiff < 604800000) {
                String output3 = (timeDiff / 86400000) + " d ago";
                return output3;
            }
            String output4 = (timeDiff / 604800000) + " w ago";
            return output4;
        } else if (timeDiff >= 3600000) {
            int hours = (int) (timeDiff / 3600000);
            String output5 = hours + " hr ago";
            return output5;
        } else if (timeDiff >= 60000) {
            int mins = (int) (timeDiff / 60000);
            String output6 = mins + " min ago";
            return output6;
        } else {
            int seconds = (int) (timeDiff / 1000);
            if (seconds < 30) {
                return "Just now";
            }
            String output7 = seconds + " s ago";
            return output7;
        }
    }

}
