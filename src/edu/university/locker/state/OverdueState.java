package edu.university.locker.state;

import edu.university.locker.context.SmartLocker;
import edu.university.locker.model.LockerAction;
import edu.university.locker.model.OperationResult;

import java.util.List;

public class OverdueState implements LockerState {

    @Override
    public String getDisplayName() {
        return "Vencido";
    }

    @Override
    public List<String> getAllowedActions() {
        return List.of(LockerAction.START_MAINTENANCE, LockerAction.GET_STATUS);
    }

    @Override
    public OperationResult pickupPackage(SmartLocker locker, String securityCode) {
        return OperationResult.failure("El paquete vencido no se puede recoger desde la interfaz normal. Se requiere revision manual.");
    }

    @Override
    public OperationResult startMaintenance(SmartLocker locker) {
        String message = "Se inicio mantenimiento para revisar manualmente el paquete vencido.";
        locker.transitionTo(locker.getMaintenanceState(), LockerAction.START_MAINTENANCE, message);
        return OperationResult.success(message);
    }
}
