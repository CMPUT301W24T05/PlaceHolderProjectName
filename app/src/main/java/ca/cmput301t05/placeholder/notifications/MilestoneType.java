package ca.cmput301t05.placeholder.notifications;

public enum MilestoneType {
    FIRSTATTENDEE("First Check In", 0),
    FIRSTSIGNUP("First Sign Up", 1),
    EVENTSTART("Event has started", 2),
    EVENTEND("Event has ended", 3),
    HALFWAY("Event is at half capacity",4),
    FULLCAPACITY("Event is at full capacity", 5);

    private final String message;
    private final int id;

    MilestoneType(String message, int id){
        this.message = message;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }
}
