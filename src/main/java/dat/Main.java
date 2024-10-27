package dat;

import dat.config.AppConfig;
import dat.config.HibernateConfig;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static void main(String[] args) {

        AppConfig.startServer();
    }
}