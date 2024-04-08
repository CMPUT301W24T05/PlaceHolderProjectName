package ca.cmput301t05.placeholder.milestones;

public enum MilestoneType {
    FIRSTATTENDEE("First Check In", 0, "FIRSTATTENDEE"),
    FIRSTSIGNUP("First Sign Up", 1, "FIRSTSIGNUP"),
    EVENTSTART("Event has started", 2, "EVENTSTART"),
    EVENTEND("Event has ended", 3, "EVENTEND"),
    HALFWAY("Event is at half capacity",4, "HALFWAY"),
    FULLCAPACITY("Event is at full capacity", 5, "FULLCAPACITY");

    private final String message;
    private final int id;

    private final String idString;

    MilestoneType(String message, int id, String idString){
        this.message = message;
        this.id = id;
        this.idString = idString;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public String getIdString() {
        return idString;
    }
}
