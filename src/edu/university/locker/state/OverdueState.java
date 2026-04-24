package edu.university.locker.state;

import edu.university.locker.context.SmartLocker;
import edu.university.locker.model.OperationResult;

public class OverdueState implements LockerState {

    @Override
    public String getDisplayName() {
        return "Vencido";
    }

    @Override
    public OperationResult pickupPackage(SmartLocker locker, String securityCode) {
        return OperationResult.failure("El paquete vencido no se puede recoger desde la interfaz normal. Se requiere revisión manual.");
    }

    @Override
    public OperationResult startMaintenance(SmartLocker locker) {
        locker.transitionTo(locker.getMaintenanceState());
        return OperationResult.success("Se inició mantenimiento para revisar manualmente el paquete vencido.");
    }
}
