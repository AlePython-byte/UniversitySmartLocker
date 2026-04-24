package edu.university.locker.model;

public final class LockerAction {

    public static final String RESERVE = "reserve";
    public static final String STORE_PACKAGE = "storePackage";
    public static final String MARK_READY_FOR_PICKUP = "markReadyForPickup";
    public static final String PICKUP_PACKAGE = "pickupPackage";
    public static final String MARK_AS_OVERDUE = "markAsOverdue";
    public static final String START_MAINTENANCE = "startMaintenance";
    public static final String FINISH_MAINTENANCE = "finishMaintenance";
    public static final String GET_STATUS = "getStatus";

    private LockerAction() {
    }
}
