package edu.university.locker.context;

import edu.university.locker.model.LockerStatusSnapshot;
import edu.university.locker.model.OperationResult;
import edu.university.locker.model.PackageInfo;
import edu.university.locker.model.StateTransitionRecord;
import edu.university.locker.service.LockerAuditLogger;
import edu.university.locker.service.SecurityCodeGenerator;
import edu.university.locker.state.AvailableState;
import edu.university.locker.state.LockerState;
import edu.university.locker.state.MaintenanceState;
import edu.university.locker.state.OverdueState;
import edu.university.locker.state.PackageStoredState;
import edu.university.locker.state.ReadyForPickupState;
import edu.university.locker.state.ReservedState;

import java.util.List;

public class SmartLocker {

    private final LockerAuditLogger auditLogger;
    private final SecurityCodeGenerator securityCodeGenerator;
    private final LockerState availableState;
    private final LockerState reservedState;
    private final LockerState packageStoredState;
    private final LockerState readyForPickupState;
    private final LockerState overdueState;
    private final LockerState maintenanceState;

    private LockerState currentState;
    private String studentName;
    private String reservationSecurityCode;
    private PackageInfo packageInfo;
    private String lastMessage;

    public SmartLocker(LockerAuditLogger auditLogger, SecurityCodeGenerator securityCodeGenerator) {
        this.auditLogger = auditLogger;
        this.securityCodeGenerator = securityCodeGenerator;
        this.availableState = new AvailableState();
        this.reservedState = new ReservedState();
        this.packageStoredState = new PackageStoredState();
        this.readyForPickupState = new ReadyForPickupState();
        this.overdueState = new OverdueState();
        this.maintenanceState = new MaintenanceState();
        this.currentState = availableState;
        this.lastMessage = "El casillero esta disponible y listo para usarse.";
        this.auditLogger.log("Sistema iniciado. Estado actual: Disponible.");
        this.auditLogger.logTransition("Sin estado", availableState.getDisplayName(), "systemStart", this.lastMessage);
    }

    public synchronized OperationResult reserve(String studentName) {
        if (isBlank(studentName)) {
            return rejectValidation("El nombre del estudiante es obligatorio.");
        }
        return completeAction("Reservar casillero", currentState.reserve(this, studentName.trim()));
    }

    public synchronized OperationResult storePackage(String packageCode) {
        if (isBlank(packageCode)) {
            return rejectValidation("El codigo del paquete es obligatorio.");
        }
        return completeAction("Guardar paquete", currentState.storePackage(this, packageCode.trim()));
    }

    public synchronized OperationResult markReadyForPickup() {
        return completeAction("Marcar listo para recoger", currentState.markReadyForPickup(this));
    }

    public synchronized OperationResult pickupPackage(String securityCode) {
        if (isBlank(securityCode)) {
            return rejectValidation("El codigo de seguridad es obligatorio.");
        }
        return completeAction("Recoger paquete", currentState.pickupPackage(this, securityCode.trim()));
    }

    public synchronized OperationResult markAsOverdue() {
        return completeAction("Marcar como vencido", currentState.markAsOverdue(this));
    }

    public synchronized OperationResult startMaintenance() {
        return completeAction("Iniciar mantenimiento", currentState.startMaintenance(this));
    }

    public synchronized OperationResult finishMaintenance() {
        return completeAction("Finalizar mantenimiento", currentState.finishMaintenance(this));
    }

    public synchronized OperationResult getStatus() {
        return currentState.getStatus(this);
    }

    public synchronized LockerStatusSnapshot getStatusSnapshot() {
        return new LockerStatusSnapshot(
                getStateDisplayName(),
                getStudentName(),
                getPackageCode(),
                getSecurityCode(),
                getLastMessage(),
                getAllowedActions(),
                getLastTransition()
        );
    }

    public synchronized LockerState getAvailableState() {
        return availableState;
    }

    public synchronized LockerState getReservedState() {
        return reservedState;
    }

    public synchronized LockerState getPackageStoredState() {
        return packageStoredState;
    }

    public synchronized LockerState getReadyForPickupState() {
        return readyForPickupState;
    }

    public synchronized LockerState getOverdueState() {
        return overdueState;
    }

    public synchronized LockerState getMaintenanceState() {
        return maintenanceState;
    }

    public synchronized void transitionTo(LockerState nextState, String actionName, String message) {
        String fromState = currentState.getDisplayName();
        this.currentState = nextState;
        auditLogger.logTransition(fromState, nextState.getDisplayName(), actionName, message);
    }

    public synchronized void assignReservation(String studentName, String securityCode) {
        this.studentName = studentName;
        this.reservationSecurityCode = securityCode;
        this.packageInfo = null;
    }

    public synchronized void storeReservedPackage(String packageCode) {
        this.packageInfo = new PackageInfo(packageCode, reservationSecurityCode);
    }

    public synchronized boolean matchesSecurityCode(String securityCode) {
        String currentSecurityCode = getSecurityCode();
        return currentSecurityCode != null && currentSecurityCode.equals(securityCode);
    }

    public synchronized void clearLockerData() {
        this.studentName = null;
        this.reservationSecurityCode = null;
        this.packageInfo = null;
    }

    public synchronized String generateSecurityCode() {
        return securityCodeGenerator.generateCode();
    }

    public synchronized String getStudentName() {
        return studentName;
    }

    public synchronized String getPackageCode() {
        return packageInfo == null ? null : packageInfo.getPackageCode();
    }

    public synchronized String getSecurityCode() {
        if (packageInfo != null) {
            return packageInfo.getSecurityCode();
        }
        return reservationSecurityCode;
    }

    public synchronized String getStateDisplayName() {
        return currentState.getDisplayName();
    }

    public synchronized String getLastMessage() {
        return lastMessage;
    }

    public synchronized List<String> getAuditEntries() {
        return auditLogger.getEntries();
    }

    public synchronized List<String> getAllowedActions() {
        return currentState.getAllowedActions();
    }

    public synchronized StateTransitionRecord getLastTransition() {
        return auditLogger.getLastTransition();
    }

    private OperationResult rejectValidation(String message) {
        this.lastMessage = message;
        auditLogger.log("Validacion rechazada. " + message + " Estado actual: " + currentState.getDisplayName() + ".");
        return OperationResult.failure(message);
    }

    private OperationResult completeAction(String actionName, OperationResult result) {
        this.lastMessage = result.message();
        String outcome = result.success() ? "OK" : "ERROR";
        auditLogger.log(actionName + " -> " + outcome + ". " + result.message() + " Estado actual: " + currentState.getDisplayName() + ".");
        return result;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
