package edu.university.locker;

import edu.university.locker.context.SmartLocker;
import edu.university.locker.server.WebServer;
import edu.university.locker.service.LockerAuditLogger;
import edu.university.locker.service.SecurityCodeGenerator;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        LockerAuditLogger auditLogger = new LockerAuditLogger();
        SecurityCodeGenerator securityCodeGenerator = new SecurityCodeGenerator();
        SmartLocker smartLocker = new SmartLocker(auditLogger, securityCodeGenerator);
        WebServer webServer = new WebServer(smartLocker, 8080);

        webServer.start();
        System.out.println("Smart Locker server running at http://localhost:8080");
    }
}
