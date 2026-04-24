package edu.university.locker.state;

import edu.university.locker.context.SmartLocker;
import edu.university.locker.model.OperationResult;

public class ReservedState implements LockerState {

    @Override
    public String getDisplayName() {
        return "Reservado";
    }

    @Override
    public OperationResult reserve(SmartLocker locker, String studentName) {
        return OperationResult.failure("El casillero ya está reservado para " + locker.getStudentName() + ".");
    }

    @Override
    public OperationResult storePackage(SmartLocker locker, String packageCode) {
        locker.storeReservedPackage(packageCode);
        locker.transitionTo(locker.getPackageStoredState());
        return OperationResult.success("Paquete guardado correctamente en el casillero.");
    }

    @Override
    public OperationResult pickupPackage(SmartLocker locker, String securityCode) {
        return OperationResult.failure("El paquete todavía no está listo para ser recogido.");
    }

    @Override
    public OperationResult startMaintenance(SmartLocker locker) {
        locker.transitionTo(locker.getMaintenanceState());
        return OperationResult.success("El casillero reservado pasó a mantenimiento para revisión.");
    }
}
