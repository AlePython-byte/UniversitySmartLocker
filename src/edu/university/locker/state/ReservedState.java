package edu.university.locker.state;

import edu.university.locker.context.SmartLocker;
import edu.university.locker.model.LockerAction;
import edu.university.locker.model.OperationResult;

import java.util.List;

public class ReservedState implements LockerState {

    @Override
    public String getDisplayName() {
        return "Reservado";
    }

    @Override
    public List<String> getAllowedActions() {
        return List.of(LockerAction.STORE_PACKAGE, LockerAction.START_MAINTENANCE, LockerAction.GET_STATUS);
    }

    @Override
    public OperationResult reserve(SmartLocker locker, String studentName) {
        return OperationResult.failure("El casillero ya esta reservado para " + locker.getStudentName() + ".");
    }

    @Override
    public OperationResult storePackage(SmartLocker locker, String packageCode) {
        String message = "Paquete guardado correctamente en el casillero.";
        locker.storeReservedPackage(packageCode);
        locker.transitionTo(locker.getPackageStoredState(), LockerAction.STORE_PACKAGE, message);
        return OperationResult.success(message);
    }

    @Override
    public OperationResult pickupPackage(SmartLocker locker, String securityCode) {
        return OperationResult.failure("El paquete todavia no esta listo para ser recogido.");
    }

    @Override
    public OperationResult startMaintenance(SmartLocker locker) {
        String message = "El casillero reservado paso a mantenimiento para revision.";
        locker.transitionTo(locker.getMaintenanceState(), LockerAction.START_MAINTENANCE, message);
        return OperationResult.success(message);
    }
}
