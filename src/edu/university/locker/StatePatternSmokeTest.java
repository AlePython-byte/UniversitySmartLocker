package edu.university.locker;

import edu.university.locker.context.SmartLocker;
import edu.university.locker.model.LockerAction;
import edu.university.locker.service.LockerAuditLogger;
import edu.university.locker.service.SecurityCodeGenerator;

public class StatePatternSmokeTest {

    public static void main(String[] args) {
        SmartLocker locker = new SmartLocker(new LockerAuditLogger(), new SecurityCodeGenerator());

        assertState(locker, "Disponible");
        assertAllowed(locker, LockerAction.RESERVE, LockerAction.START_MAINTENANCE, LockerAction.GET_STATUS);

        locker.reserve("Juan Perez");
        assertState(locker, "Reservado");
        assertAllowed(locker, LockerAction.STORE_PACKAGE, LockerAction.START_MAINTENANCE, LockerAction.GET_STATUS);

        locker.storePackage("PKG-100");
        assertState(locker, "Paquete almacenado");
        assertAllowed(locker, LockerAction.MARK_READY_FOR_PICKUP, LockerAction.GET_STATUS);

        locker.markReadyForPickup();
        assertState(locker, "Listo para recoger");
        assertAllowed(locker, LockerAction.PICKUP_PACKAGE, LockerAction.MARK_AS_OVERDUE, LockerAction.GET_STATUS);

        String securityCode = locker.getSecurityCode();
        locker.pickupPackage(securityCode);
        assertState(locker, "Disponible");

        locker.reserve("Laura Gomez");
        locker.storePackage("PKG-200");
        locker.markReadyForPickup();
        locker.markAsOverdue();
        assertState(locker, "Vencido");
        assertAllowed(locker, LockerAction.START_MAINTENANCE, LockerAction.GET_STATUS);

        locker.startMaintenance();
        assertState(locker, "Mantenimiento");
        assertAllowed(locker, LockerAction.FINISH_MAINTENANCE, LockerAction.GET_STATUS);

        locker.finishMaintenance();
        assertState(locker, "Disponible");

        System.out.println("StatePatternSmokeTest completed successfully.");
    }

    private static void assertState(SmartLocker locker, String expectedState) {
        if (!expectedState.equals(locker.getStateDisplayName())) {
            throw new IllegalStateException("Expected state " + expectedState + " but was " + locker.getStateDisplayName());
        }
    }

    private static void assertAllowed(SmartLocker locker, String... expectedActions) {
        for (String action : expectedActions) {
            if (!locker.getAllowedActions().contains(action)) {
                throw new IllegalStateException("Expected allowed action " + action + " in state " + locker.getStateDisplayName());
            }
        }
    }
}
