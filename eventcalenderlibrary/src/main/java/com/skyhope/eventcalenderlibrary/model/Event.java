package com.skyhope.eventcalenderlibrary.model;

/*
 *  ****************************************************************************
 *  * Created by : Md Tariqul Islam on 11/30/2018 at 11:40 AM.
 *  * Email : tariqul@w3engineers.com
 *  *
 *  * Purpose:
 *  *
 *  * Last edited by : Md Tariqul Islam on 11/30/2018.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

public class Event {
    private long time;
    private String eventText;
    private int eventColor;
    private String classId;

    public Event(long time, String eventText, int eventColor, String classId) {
        this.time = time;
        this.eventText = eventText;
        this.eventColor = eventColor;
        this.classId = classId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public Event(long time, String eventText) {
        this.time = time;
        this.eventText = eventText;
    }

    public Event(long time, String eventText, int eventColor) {
        this.time = time;
        this.eventText = eventText;
        this.eventColor = eventColor;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getEventText() {
        return eventText;
    }

    public void setEventText(String eventText) {
        this.eventText = eventText;
    }

    public int getEventColor() {
        return eventColor;
    }

    public void setEventColor(int eventColor) {
        this.eventColor = eventColor;
    }
}
