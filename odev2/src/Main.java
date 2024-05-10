import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.time.ZoneId;

public class Main {
    private static final String url = "jdbc:postgresql://localhost:5432/odv2";
    private static final String user = "postgres";
    private static final String password = "1234";

    public static void main(String[] args) {


        System.out.println("--Gezgin Gemi Şirketi--\n");
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            // Ana menü seçeneklerini ekrana yazdırma
            System.out.println("------ Ana Menü ------");
            System.out.println("1. Gemiler");
            System.out.println("2. Seferler");
            System.out.println("3. Kaptanlar");
            System.out.println("4. Mürettebat");
            System.out.println("5. Limanlar");
            System.out.println("0. Çıkış");
            System.out.print("Seçiminizi yapın: ");

            // kullanıcının seçimini al
            choice = scanner.nextInt();

            // kullanıcının seçimine göre ilgili menüyü açma işlemleri;
            switch (choice) {
                case 1:
                    gemiMenu(scanner);
                    break;
                case 2:
                    seferMenu(scanner);
                    break;
                case 3:
                    kaptanMenu(scanner);
                    break;
                case 4:
                    murettebatMenu(scanner);
                    break;
                case 5:
                    limanMenu(scanner);
                    break;
                case 0:
                    System.out.println("Programdan çıkılıyor...");
                    break;
                default:
                    System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
                    break;
            }
        } while (choice != 0);

        scanner.close();
    }

    // Gemiler Menüsü
    public static void gemiMenu(Scanner scanner) {
        int choice;

        do {
            // Gemilerin menü seçeneklerini ekrana yazdırma;
            System.out.println("------ Gemiler Menüsü ------");
            System.out.println("1. Gemileri Listele");
            System.out.println("2. Gemi Ekle");
            System.out.println("3. Gemi Sil");
            System.out.println("4. Gemi Düzenle");
            System.out.println("0. Ana Menüye Dön");
            System.out.print("Seçiminizi yapın: ");

            // kullanıcının seçimini alın
            choice = scanner.nextInt();

            // Kullanıcının seçimine göre ilgili işlemi gerçekleştir ^
            switch (choice) {
                case 1:
                    try {
                        Connection connection = DriverManager.getConnection(url, user, password);
                        System.out.println("Veritabanına başarıyla bağlandı!");

                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM \"ship\"");


                        while (resultSet.next()) {
                            System.out.println("Gemi İD: " + resultSet.getInt("ship_id") +
                                    ", Gemi Adı: " + resultSet.getString("name") +
                                    ", Gemi Ağırlığı: " + resultSet.getBigDecimal("weight") +
                                    ", Yapım Yılı: " + resultSet.getInt("construction_year") +
                                    ", Gemi Tipi: " + resultSet.getString("ship_type") +
                                    ", Y.Gemi Kapasite: " + resultSet.getInt("passenger_capacity") +
                                    ", Petrol Kapasite:" + resultSet.getBigDecimal("fuel_capacity_liters") +
                                    ", Konteyner kapasite: " + resultSet.getInt("container_capacity") +
                                    ", Max konteyner Ağırlığı: " + resultSet.getBigDecimal("max_weight_capacity"));
                        }

                        resultSet.close();
                        statement.close();
                        connection.close(); // vys kapa
                    } catch (SQLException e) {
                        e.printStackTrace(); // vt bağlanırken oluşan hataları çıkarır
                    }

                    System.out.println("Gemiler listelendi.");
                    break;
                case 2:
                    System.out.println("------ Gemi Ekle Menüsü ------");
                    System.out.println("1. Yolcu Gemisi Ekle");
                    System.out.println("2. Konteyner Gemisi Ekle");
                    System.out.println("3. Petrol Tankeri Ekle");
                    System.out.println("0. Ana Menüye Dön");
                    System.out.print("Seçiminizi yapın: ");
                    int gemiSecim = scanner.nextInt();
                    switch (gemiSecim) {
                        case 1:
                            PassengerShip passengerShip = new PassengerShip();

                            System.out.println("Gemi ID'sini girin:");
                            int shipId = scanner.nextInt();
                            passengerShip.setShipId(shipId);
                            scanner.nextLine();
                            System.out.println("Gemi adını girin:");
                            String shipName = scanner.nextLine();
                            passengerShip.setName(shipName);

                            System.out.println("Gemi ağırlığını girin:");
                            BigDecimal shipWeight = scanner.nextBigDecimal();
                            passengerShip.setWeight(shipWeight);

                            System.out.println("Gemi yapım yılını girin:");
                            int shipYear = scanner.nextInt();
                            passengerShip.setConstructionYear(shipYear);

                            System.out.println("Gemi yolcu kapasitesi girin:");
                            int shipPassenger = scanner.nextInt();
                            passengerShip.setPassengerCapacity(shipPassenger);
                            try {
                                Connection connection = DriverManager.getConnection(url, user, password);
                                System.out.println("Veritabanına başarıyla bağlandı!");

                                // PreparedStatement kullanarak gemi bilgisi ekleme;
                                String sql = "INSERT INTO Ship (ship_id, name, weight, construction_year, ship_type, passenger_capacity) " +
                                        "VALUES (?, ?, ?, ?, ?, ?)";
                                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                                preparedStatement.setInt(1, passengerShip.getShipId());
                                preparedStatement.setString(2, passengerShip.getName());
                                preparedStatement.setBigDecimal(3, passengerShip.getWeight());
                                preparedStatement.setInt(4, passengerShip.getConstructionYear());
                                preparedStatement.setString(5, "Yolcu"); // Ship_type değeri
                                preparedStatement.setInt(6, passengerShip.getPassengerCapacity());

                                // Gemi eklenirken olası hata kontrolü;
                                int rowsAffected = preparedStatement.executeUpdate();
                                if (rowsAffected > 0) {
                                    System.out.println("Gemi başarıyla eklendi.");
                                } else {
                                    System.out.println("Gemi eklenirken bir hata oluştu.");
                                }
                                // VTS Bağlantıyı kapat
                                preparedStatement.close();
                                connection.close();
                            } catch (SQLException e) {
                                e.printStackTrace();// Veri tabanı bağlantı hatası
                            }

                            break;

                        case 2:
                            ContainerShip containerShip = new ContainerShip();

                            System.out.println("Gemi ID'sini girin:");
                            int shipConId = scanner.nextInt();
                            containerShip.setShipId(shipConId);
                            scanner.nextLine();
                            System.out.println("Gemi adını girin:");
                            String shipConName = scanner.nextLine();
                            containerShip.setName(shipConName);

                            System.out.println("Gemi ağırlığını girin:");
                            BigDecimal shipConWeight = scanner.nextBigDecimal();
                            containerShip.setWeight(shipConWeight);

                            System.out.println("Gemi yapım yılını girin:");
                            int shipConYear = scanner.nextInt();
                            containerShip.setConstructionYear(shipConYear);

                            System.out.println("Gemi konteyner kapasitesi girin:");
                            int shipContainer = scanner.nextInt();
                            containerShip.setContainerCapacity(shipContainer);
                            System.out.println("Gemi max konteyner kapasitesi girin:");
                            BigDecimal shipConMax = scanner.nextBigDecimal();
                            containerShip.setMaxWeightCapacity(shipConMax);
                            try {
                            Connection connection = DriverManager.getConnection(url, user, password);
                            System.out.println("Veritabanına başarıyla bağlandı!");

                            // PreparedStatement kullanarak gemi bilgisi ekleme;
                            String sql = "INSERT INTO Ship (ship_id, name, weight, construction_year, ship_type, " +
                                    "container_capacity, max_weight_capacity) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";
                            PreparedStatement preparedStatement = connection.prepareStatement(sql);
                            preparedStatement.setInt(1, containerShip.getShipId());
                            preparedStatement.setString(2, containerShip.getName());
                            preparedStatement.setBigDecimal(3, containerShip.getWeight());
                            preparedStatement.setInt(4, containerShip.getConstructionYear());
                            preparedStatement.setString(5, "Konteyner"); // Ship_type değeri
                            preparedStatement.setInt(6, containerShip.getContainerCapacity());
                            preparedStatement.setBigDecimal(7, containerShip.getMaxWeightCapacity());
                            // Gemi eklenirken olası hata kontrolü;
                            int rowsAffected = preparedStatement.executeUpdate();
                            if (rowsAffected > 0) {
                                System.out.println("Gemi başarıyla eklendi.");
                            } else {
                                System.out.println("Gemi eklenirken bir hata oluştu.");
                            }
                            // VTS Bağlantıyı kapat
                            preparedStatement.close();
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();// Veri tabanı bağlantı hatası
                        }

                        break;
                        case 3:
                            PetrolTankerShip petrolTankerShip = new PetrolTankerShip();

                            System.out.println("Gemi ID'sini girin:");
                            int shipTanId = scanner.nextInt();
                            petrolTankerShip.setShipId(shipTanId);
                            scanner.nextLine();
                            System.out.println("Gemi adını girin:");
                            String shipTanName = scanner.nextLine();
                            petrolTankerShip.setName(shipTanName);

                            System.out.println("Gemi ağırlığını girin:");
                            BigDecimal shipTanWeight = scanner.nextBigDecimal();
                            petrolTankerShip.setWeight(shipTanWeight);

                            System.out.println("Gemi yapım yılını girin:");
                            int shipTanYear = scanner.nextInt();
                            petrolTankerShip.setConstructionYear(shipTanYear);

                            System.out.println("Gemi petrol kapasitesi girin:");
                            BigDecimal shipTanMax = scanner.nextBigDecimal();
                            petrolTankerShip.setFuelCapacityLiters(shipTanMax);
                            try {
                                Connection connection = DriverManager.getConnection(url, user, password);
                                System.out.println("Veritabanına başarıyla bağlandı!");

                                // PreparedStatement kullanarak gemi bilgisi ekleme;
                                String sql = "INSERT INTO Ship (ship_id, name, weight, construction_year, ship_type, " +
                                        "fuel_capacity_liters) " + "VALUES (?, ?, ?, ?, ?, ?)";
                                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                                preparedStatement.setInt(1, petrolTankerShip.getShipId());
                                preparedStatement.setString(2, petrolTankerShip.getName());
                                preparedStatement.setBigDecimal(3, petrolTankerShip.getWeight());
                                preparedStatement.setInt(4, petrolTankerShip.getConstructionYear());
                                preparedStatement.setString(5, "Konteyner"); // Ship_type değeri
                                preparedStatement.setBigDecimal(6, petrolTankerShip.getFuelCapacityLiters());

                                // Gemi eklenirken olası hata kontrolü;
                                int rowsAffected = preparedStatement.executeUpdate();
                                if (rowsAffected > 0) {
                                    System.out.println("Gemi başarıyla eklendi.");
                                } else {
                                    System.out.println("Gemi eklenirken bir hata oluştu.");
                                }
                                // VTS Bağlantıyı kapat
                                preparedStatement.close();
                                connection.close();
                            } catch (SQLException e) {
                                e.printStackTrace();// Veri tabanı bağlantı hatası
                            }
                            break;
                        case 0:
                            // Ana menüye dön
                            break;
                        default:
                            System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
                            break;
                    }

                    break;
                case 3:

                    try {
                        System.out.println("Silinecek gemi ID'sini girin:");
                        int silinecekGemiId = scanner.nextInt();

                        Connection connection = DriverManager.getConnection(url, user, password);
                        System.out.println("Veritabanına başarıyla bağlandı!");

                        String sql = "DELETE FROM ship WHERE ship_id = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setInt(1, silinecekGemiId);

                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Gemi başarıyla silindi.");
                        } else {
                            System.out.println("Belirtilen ID'ye sahip bir gemi bulunamadı.");
                        }

                        preparedStatement.close();
                        connection.close(); //vt bağlantısı kapat
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                System.out.println("Gemi silindi.");
                    break;

                case 4:
                    Connection connection = null;
                    PreparedStatement preparedStatement = null;

                    try {
                        // Gemi ID'sini al
                        System.out.println("Düzenlenecek gemi ID'sini girin:");
                        int duzenlenecekGemiId = scanner.nextInt();
                        scanner.nextLine(); // Boş satırı oku


                        System.out.println("Hangi bilgiyi düzenlemek istiyorsunuz?");
                        System.out.println("1. Gemi Adı");
                        System.out.println("2. Gemi Ağırlığı");
                        System.out.println("3. Yapım Yılı");
                        System.out.println("4. Yolcu Gemisi-> Yolcu Kapasitesi");
                        System.out.println("5. Gemi TÜRÜ (!)");
                        System.out.println("6. Petrol Gemisi-> Petrol Kapasitesi");
                        System.out.println("7. Konteyner Gemisi-> Konteyner Kapasitesi" );
                        System.out.println("8. Konteyner Gemisi-> Konteyner Ağırlık Kapasitesi");
                        System.out.println("9. Gemi İD değişrime");
                        System.out.print("Seçiminizi yapın: ");
                        int secim = scanner.nextInt();
                        scanner.nextLine(); // boş satırı okuma
                        Ship shipUpdate = new Ship();
                        shipUpdate.setShipId(duzenlenecekGemiId);
                        PassengerShip passengerShip = new PassengerShip();

                        switch (secim) {
                            case 1:
                                connection = DriverManager.getConnection(url, user, password);
                                System.out.println("Veritabanına başarıyla bağlandı!");

                                System.out.println("Yeni gemi adını girin:");
                                String newName = scanner.nextLine();
                                shipUpdate.setName(newName);

                                String updateNameSql = "UPDATE ship SET name = ? WHERE ship_id = ?";
                                preparedStatement = connection.prepareStatement(updateNameSql);
                                preparedStatement.setString(1, shipUpdate.getName());
                                preparedStatement.setInt(2, shipUpdate.getShipId());
                                break;
                            case 2:
                                connection = DriverManager.getConnection(url, user, password);
                                System.out.println("Veritabanına başarıyla bağlandı!");

                                System.out.println("Yeni gemi ağırlığını girin:");
                                BigDecimal newWeight = scanner.nextBigDecimal();
                                shipUpdate.setWeight(newWeight);

                                String updateWeightSql = "UPDATE ship SET weight = ? WHERE ship_id = ?";
                                preparedStatement = connection.prepareStatement(updateWeightSql);
                                preparedStatement.setBigDecimal(1, shipUpdate.getWeight());
                                preparedStatement.setInt(2, shipUpdate.getShipId());
                                break;
                            case 3:
                                connection = DriverManager.getConnection(url, user, password);
                                System.out.println("Veritabanına başarıyla bağlandı!");

                                System.out.println("Yeni yapım yılını girin:");
                                int newYear = scanner.nextInt();
                                shipUpdate.setConstructionYear(newYear);

                                String updateYearSql = "UPDATE ship SET construction_year = ? WHERE ship_id = ?";
                                preparedStatement = connection.prepareStatement(updateYearSql);
                                preparedStatement.setInt(1, shipUpdate.getConstructionYear());
                                preparedStatement.setInt(2, shipUpdate.getShipId());
                                break;
                            case 4:
                                connection = DriverManager.getConnection(url, user, password);
                                System.out.println("Veritabanına başarıyla bağlandı!");
                                System.out.println("Yolcu Gemisi için değşim yapıyorsunuz!!\n" +
                                        "Yolcu kapasitesini girin: ");
                                int newPassengerCap = scanner.nextInt();
                                passengerShip.setPassengerCapacity(newPassengerCap);

                                String updatePasCapSql = "UPDATE ship SET passenger_capacity = ? WHERE ship_id = ?";
                                preparedStatement = connection.prepareStatement(updatePasCapSql);
                                preparedStatement.setInt(1, passengerShip.getPassengerCapacity());
                                preparedStatement.setInt(2, shipUpdate.getShipId());
                                break;
                            case 5:
                                connection = DriverManager.getConnection(url, user, password);
                                System.out.println("Veritabanına başarıyla bağlandı!");
                                System.out.println("----UYARI----\n" +
                                        "DEĞİŞECEK GEMİ TÜRÜNE GÖRE BİLGİLERİ DEĞİŞTİRMEYİ UNUTMAYIN!!");
                                System.out.println("Yeni Gemi Türü giriniz: 'Yolcu', 'Petrol Tankeri', 'Konteyner' ");
                                String newShipTyp = scanner.nextLine();

                                String updateShipTypSql = "UPDATE ship SET ship_type = ? WHERE ship_id = ?";
                                preparedStatement = connection.prepareStatement(updateShipTypSql);
                                preparedStatement.setString(1, newShipTyp);
                                preparedStatement.setInt(2, shipUpdate.getShipId());
                                break;
                            case 6:
                                connection = DriverManager.getConnection(url, user, password);
                                System.out.println("Veri tabanınabağlandı");
                                System.out.println("Petrol Gemisi için değşim yapıyorsunuz!!\n" +
                                        "Petrol kapasitesini girin (litre): ");
                                BigDecimal newPetrol = scanner.nextBigDecimal();

                                String updateSh = "UPDATE ship SET fuel_capacity_liters = ? WHERE ship_id = ?";
                                preparedStatement = connection.prepareStatement(updateSh);
                                preparedStatement.setBigDecimal(1, newPetrol);
                                preparedStatement.setInt(2, shipUpdate.getShipId());
                                break;
                            case 7:
                                connection = DriverManager.getConnection(url, user, password);
                                System.out.println("veritabanına bağlandı");
                                System.out.println("Konteyner Gemisi için değişim yapıyorsunuz !!\n"
                                        +"Konteyner Kapasitesini girin (adet): ");
                                int newConteynr = scanner.nextInt();

                                String updateConteynr = "UPDATE ship SET container_capacity = ? WHERE ship_id = ?";
                                preparedStatement = connection.prepareStatement(updateConteynr);
                                preparedStatement.setInt(1, newConteynr);
                                preparedStatement.setInt(2, shipUpdate.getShipId());
                                break;

                            case 8:
                                connection = DriverManager.getConnection(url, user, password);
                                System.out.println("veritabanına bağlandı");
                                System.out.println("Konteyner Gemisi için değişim yapıyorsunuz !!\n"
                                        +"Konteyner Kapasitesini girin (adet): ");
                                BigDecimal newConteynrW = scanner.nextBigDecimal();

                                String updateConteynrW = "UPDATE ship SET max_weight_capacity = ? WHERE ship_id = ?";
                                preparedStatement = connection.prepareStatement(updateConteynrW);
                                preparedStatement.setBigDecimal(1, newConteynrW);
                                preparedStatement.setInt(2, shipUpdate.getShipId());
                                break;
                            case 9:
                                connection = DriverManager.getConnection(url, user, password);
                                System.out.println("veri tabanına bağlandı");
                                System.out.println("GEMİ İD DEĞİŞTİRİYORSUNUZ\n"
                                + "Yeni Gemi İD: ");
                                int newShipId = scanner.nextInt();
                                String updateShipIdSql = "UPDATE ship SET ship_id = ? WHERE ship_id = ?";
                                preparedStatement = connection.prepareStatement(updateShipIdSql);
                                preparedStatement.setInt(1, newShipId);
                                preparedStatement.setInt(2, shipUpdate.getShipId());
                                break;
                            default:
                                System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
                                return; // Ana menüye geri dön
                        }

                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Gemi başarıyla güncellendi.");
                        } else {
                            System.out.println("Belirtilen ID'ye sahip bir gemi bulunamadı.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Veritabanına bağlanırken veya sorgu çalıştırılırken bir hata oluştu.");
                        e.printStackTrace();
                    } finally {
                        // Her durumda bağlantıyı ve preparedStatement nesnesini kapat!-
                        if (preparedStatement != null) {
                            try {
                                preparedStatement.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (connection != null) {
                            try {
                                connection.close();
                                System.out.println("Veritabanı bağlantısı kapatıldı.");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    System.out.println("Gemi düzenlendi.");
                    break;
                case 0:
                    // Ana menüye dön
                    break;
                default:
                    System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
                    break;
            }
        } while (choice != 0);
    }

    private static void updatePortFirstTimeStatus(List<VoyagePorts> portVisits) {
        String sql = "UPDATE PORT SET is_first_time = false WHERE id = ? AND is_first_time = true";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (VoyagePorts portVisit : portVisits) {
                pstmt.setInt(1, portVisit.getPortId());
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Liman ID " + portVisit.getPortId() + ": is_first_time güncellendi.");
                } else {
                    System.out.println("Liman ID " + portVisit.getPortId() + " için is_first_time güncellenmedi veya zaten false.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Veritabanı bağlantısında bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private static void updateVoyageStatus(int voyageId, java.sql.Date returnDate) {
        // Mevcut tarih currentdate
        java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());

        if (returnDate.before(currentDate)) {
            // Dönüş tarihi geçmişte ise durumu 'Geçmiş' olarak güncelle;!!!
            String sql = "UPDATE voyages SET voyage_status = 'Geçmiş' WHERE voyageid = ?";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, voyageId);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Sefer durumu başarıyla 'Geçmiş' olarak güncellendi.");
                } else {
                    System.out.println("Sefer durumu güncellenemedi.");
                }
            } catch (SQLException e) {
                System.out.println("Veritabanı bağlantısında bir hata oluştu: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Dönüş tarihi henüz geçmemiş, güncelleme yapılmadı.");
        }
    }
    private static boolean addVoyageToDatabase(Voyage voyage) {
        String insertVoyageSQL = "INSERT INTO voyages (voyageid, ship_id, departure_date, return_date, departure_port) VALUES (?, ?, ?, ?, ?)";
        String insertCaptainSQL = "INSERT INTO voyage_captains (voyage_id, captain_id) VALUES (?, ?)";
        String insertCrewSQL = "INSERT INTO voyage_crew (voyage_id, crew_id) VALUES (?, ?)";
        String insertPortsSQL = "INSERT INTO voyage_ports (voyage_id, port_id, arrival_date, departure_date) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);  // Start transaction

            // Insert voyage--ilişkilendirme
            pstmt = conn.prepareStatement(insertVoyageSQL);
            pstmt.setInt(1, voyage.getVoyageId());
            pstmt.setInt(2, voyage.getShipId());
            pstmt.setDate(3, new java.sql.Date(voyage.getDepartureDate().getTime()));  // Convert java.util.Date to java.sql.Date
            pstmt.setDate(4, new java.sql.Date(voyage.getReturnDate().getTime()));  // Convert java.util.Date to java.sql.Date
            pstmt.setInt(5, voyage.getDeparturePort());
            pstmt.executeUpdate();

            // Insert captains--ilişkilendirme
            pstmt = conn.prepareStatement(insertCaptainSQL);
            for (VoyageCaptains captain : voyage.getCaptainIds()) {
                pstmt.setInt(1, voyage.getVoyageId());
                pstmt.setInt(2, captain.getCaptainId());
                pstmt.executeUpdate();
            }

            // Insert crew ilişkilendirme
            pstmt = conn.prepareStatement(insertCrewSQL);
            for (VoyageCrew crew : voyage.getCrewIds()) {
                pstmt.setInt(1, voyage.getVoyageId());
                pstmt.setInt(2, crew.getCrewId());
                pstmt.executeUpdate();
            }

            // Insert ports--ilişkilendirme
            pstmt = conn.prepareStatement(insertPortsSQL);
            for (VoyagePorts port : voyage.getVoyagePorts()) {
                pstmt.setInt(1, voyage.getVoyageId());
                pstmt.setInt(2, port.getPortId());
                pstmt.setDate(3, java.sql.Date.valueOf(port.getArrivalDate())); // Convert LocalDate to java.sql.Date
                pstmt.setDate(4, java.sql.Date.valueOf(port.getDepartureDate())); // Convert LocalDate to java.sql.Date
                pstmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static boolean updateVoyagePortsInDatabase(int voyageId, List<VoyagePorts> portVisits) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);  // işlemi başlatma kısmı

            // Mevcut liman ziyaretlerini sil--
            String deleteOldPorts = "DELETE FROM voyage_ports WHERE voyage_id = ?";
            pstmt = conn.prepareStatement(deleteOldPorts);
            pstmt.setInt(1, voyageId);
            pstmt.executeUpdate();

            // Yeni liman ziyaretlerini ekle++
            String insertNewPorts = "INSERT INTO voyage_ports (voyage_id, port_id, arrival_date, departure_date) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(insertNewPorts);
            for (VoyagePorts portVisit : portVisits) {
                pstmt.setInt(1, voyageId);
                pstmt.setInt(2, portVisit.getPortId());
                pstmt.setDate(3, java.sql.Date.valueOf(portVisit.getArrivalDate()));

                pstmt.setDate(4, java.sql.Date.valueOf(portVisit.getDepartureDate()));
                pstmt.executeUpdate();
            }

            conn.commit();  // <--Değişiklikleri onayla
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();  // Hata durumunda yapılandeğişiklikleri geri al
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void seferMenu(Scanner scanner) {
        int choice;

        do {
            // Sefer menü seçeneklerini ekrana yazdır
            System.out.println("------ SEFERLER Menüsü ------");
            System.out.println("1. Seferleri Listele");
            System.out.println("2. Sefer Ekle");
            System.out.println("3. Sefer Sil");
            System.out.println("4. Sefer Düzenle");
            System.out.println("0. Ana Menüye Dön");
            System.out.print("Seçiminizi yapın: ");

            // Kullanıcının seçimini al
            choice = scanner.nextInt();

            // kullanıcının seçimine göre ilgili işlemi gerçekleştir
            switch (choice) {
                case 1:
                    System.out.println("------ Seferleri listeleme Menüsü ------");
                    System.out.println("1. Seferleri Listele");
                    System.out.println("2. Sefer Kaptanlarını listele");
                    System.out.println("3. Sefer Mürettebatlarını listele");
                    System.out.println("3. Seferde geminin demirlendiği limanları listele");
                    System.out.println("0. Ana Menüye Dön");
                    System.out.print("Seçiminizi yapın: ");

                    // Kullanıcı seçimini al
                    choice = scanner.nextInt();
                    switch (choice) {
                        case 1:
                            try {
                                Connection connection = DriverManager.getConnection(url, user, password);
                                System.out.println("Veritabanına başarıyla bağlandı!");

                                Statement statement = connection.createStatement();
                                ResultSet resultSet = statement.executeQuery("SELECT * FROM \"voyages\"");


                                while (resultSet.next()) {
                                    System.out.println("Sefer İD: " + resultSet.getInt("voyageid") +
                                            ", Gemi İD: " + resultSet.getString("ship_id") +
                                            ", Çıkış Limanı id : " + resultSet.getString("departureport_id"));
                                }

                                resultSet.close();
                                statement.close();
                                connection.close(); // vys kapa
                            } catch (SQLException e) {
                                e.printStackTrace(); // vt bağlanırken oluşan hataları çıkarır
                            }

                            System.out.println("Sefer listelendi.");
                            break;
                        case 2:
                            System.out.println("------ Sefer Kaptanlarını Listeleme Menüsü ------");
                            System.out.print("Listelemek istediğiniz sefer ID'sini girin: ");
                            int voyageId = scanner.nextInt(); // Kullanıcıdan sefer ID'si alınır

                            try {
                                Connection connection = DriverManager.getConnection(url, user, password);
                                System.out.println("Veritabanına başarıyla bağlandı!");

                                // Sefer ID'sine göre kaptanları listeleme sorgusu:
                                String query = "SELECT c.captain_name, c.captain_lastname " +
                                        "FROM voyage_captains vc " +
                                        "JOIN captains c ON vc.captain_id = c.captain_id " +
                                        "WHERE vc.voyage_id = ?";
                                PreparedStatement preparedStatement = connection.prepareStatement(query);
                                preparedStatement.setInt(1, voyageId);

                                ResultSet resultSet = preparedStatement.executeQuery();

                                while (resultSet.next()) {
                                    System.out.println("Kaptan Adı: " + resultSet.getString("captain_name") +
                                            ", Kaptan Soyadı: " + resultSet.getString("captain_lastname"));
                                }

                                resultSet.close();
                                preparedStatement.close();
                                connection.close(); // Veritabanı bağlantısını kapat
                            } catch (SQLException e) {
                                System.out.println("Veritabanı bağlantısında bir hata oluştu.");
                                e.printStackTrace();
                            }

                            break;

                        case 3:
                            System.out.println("------ Sefer Mürettebatlarını Listeleme Menüsü ------");
                            System.out.print("Listelemek istediğiniz sefer ID'sini girin: ");
                            int voyageIdForCrew = scanner.nextInt(); // Kullanıcıdan sefer ID'si alınır

                            try {
                                Connection connection = DriverManager.getConnection(url, user, password);
                                System.out.println("Veritabanına başarıyla bağlandı!");

                                // Sefer ID'sine göre mürettebatları listeleme sorgusu
                                String query = "SELECT c.crew_name, c.crew_lastname " +
                                        "FROM voyage_crew vc " +
                                        "JOIN crew c ON vc.crew_id = c.crew_id " +
                                        "WHERE vc.voyage_id = ?";
                                PreparedStatement preparedStatement = connection.prepareStatement(query);
                                preparedStatement.setInt(1, voyageIdForCrew);

                                ResultSet resultSet = preparedStatement.executeQuery();

                                while (resultSet.next()) {
                                    System.out.println("Mürettebat Adı: " + resultSet.getString("crew_name") +
                                            ", Mürettebat Soyadı: " + resultSet.getString("crew_lastname"));
                                }

                                resultSet.close();
                                preparedStatement.close();
                                connection.close(); // Veritabanı bağlantısını kapat
                            } catch (SQLException e) {
                                System.out.println("Veritabanı bağlantısında bir hata oluştu.");
                                e.printStackTrace();
                            }
                            break;
                        case 4:
                            System.out.println("------ Seferde Geminin Uğradığı Limanları Listeleme Menüsü ------");
                            System.out.print("Listelemek istediğiniz sefer ID'sini girin: ");
                            int voyageIdForPorts = scanner.nextInt(); // kullanıcıdan sefer ID'si al

                            try {
                                Connection connection = DriverManager.getConnection(url, user, password);
                                System.out.println("Veritabanına başarıyla bağlandı!");

                                //!! Sefer ID'sine göre limanları listele;
                                String query = "SELECT p.name AS port_name, vp.arrival_date, vp.departure_date " +
                                        "FROM voyage_ports vp " +
                                        "JOIN \"PORT\" p ON vp.port_id = p.id " +
                                        "WHERE vp.voyage_id = ? " +
                                        "ORDER BY vp.arrival_date ASC"; // Limanlara varış tarihine göre sırala
                                PreparedStatement preparedStatement = connection.prepareStatement(query);
                                preparedStatement.setInt(1, voyageIdForPorts);

                                ResultSet resultSet = preparedStatement.executeQuery();

                                while (resultSet.next()) {
                                    System.out.println("Liman Adı: " + resultSet.getString("port_name") +
                                            ", Varış Tarihi: " + resultSet.getDate("arrival_date") +
                                            (resultSet.getDate("departure_date") != null ?
                                                    ", Ayrılış Tarihi: " + resultSet.getDate("departure_date") : ""));
                                }

                                resultSet.close();
                                preparedStatement.close();
                                connection.close(); // Veritabanı bağlantısını kapat
                            } catch (SQLException e) {
                                System.out.println("Veritabanı bağlantısında bir hata oluştu.");
                                e.printStackTrace();
                            }
                            break;

                    }
                    while (choice != 0) ;


                case 2:
                    System.out.println("----Yeni Sefer Ekleme----\n");
                    Voyage voyage1 = new Voyage();
                    System.out.println("Yeni Sefer ID'sini girin: ");
                    int newId = scanner.nextInt();
                    voyage1.setVoyageId(newId);

                    System.out.println("Yeni Sefer için Gemi İD girin: ");
                    int newId2 = scanner.nextInt();
                    voyage1.setShipId(newId2);

                    System.out.println("Yeni Sefere Çıkış Tarihi girin: dd-MM-yyyy");
                    String dateInput = scanner.nextLine();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate localDate = LocalDate.parse(dateInput, formatter);
                    Date departureDateVoyage = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    voyage1.setDepartureDate((java.sql.Date) departureDateVoyage);

                    System.out.println("Yeni Sefer Dönüş Tarihi girin: dd-MM-yyyy");
                    String dateInput1 = scanner.nextLine();
                    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate localDate1 = LocalDate.parse(dateInput1, formatter1);
                    Date returnDate = Date.from(localDate1.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    voyage1.setDepartureDate((java.sql.Date) returnDate);
                    updateVoyageStatus(newId, (java.sql.Date) returnDate); // voyage_status geçmiş zaman kontrolü yap

                    System.out.println("Sefer Çıkış Limanı ID girin: ");
                    int newId3 = scanner.nextInt();
                    voyage1.setDeparturePort(newId3);

                    System.out.println("Seferde uğranılan limanların sayısını girin: ");
                    int numberOfPorts = scanner.nextInt();  // ? Kaç tane liman ziyareti olacağını al
                    scanner.nextLine();  // Scanner'ın geçmişini temizle

                    List<VoyagePorts> portVisits = new ArrayList<>();  // Liman ziyaretleri için liste oluştur

                    for (int i = 0; i < numberOfPorts; i++) {
                        System.out.print((i + 1) + ". Liman ID: ");
                        int portId = scanner.nextInt();  // Liman ID'sini al
                        scanner.nextLine();  // Scanner'ın geçmişini temizle

                        System.out.print("Varış tarihi (YYYY-MM-DD): ");
                        String arrival = scanner.nextLine();
                        LocalDate arrivalDate = LocalDate.parse(arrival, formatter);


                        System.out.print("Ayrılış tarihi (YYYY-MM-DD): ");
                        String departure = scanner.nextLine();
                        LocalDate departureDate = LocalDate.parse(departure, formatter);


                        // yeni VoyagePorts nesnesi oluştur , bilgileri set et
                        VoyagePorts voyagePort = new VoyagePorts(portId, arrivalDate, departureDate);
                        portVisits.add(voyagePort);  // Listeye ekle ++

                    }

                    // voyage1 nesnesine liman ziyaretlerini set et
                    voyage1.setVoyagePorts(portVisits);

                    // Kaydedilen liman ziyaretlerini yazdır
                    System.out.println("Seferde uğranılan limanlar ve tarihleri:");
                    for (VoyagePorts visit : voyage1.getVoyagePorts()) {
                        System.out.println("Liman ID: " + visit.getPortId() +
                                ", Varış Tarihi: " + visit.getArrivalDate() +
                                ", Ayrılış Tarihi: " + visit.getDepartureDate());
                    }
                    updatePortFirstTimeStatus(portVisits);


                    List<VoyageCaptains> captainIds = new ArrayList<>();
                    System.out.println("Sefer için kaptan ID'lerini girin (en az 2):");
                    for (int i = 0; i < 2; i++) {  // En az iki kaptan gerekli
                        System.out.print("Kaptan ID " + (i + 1) + ": ");
                        int captainId = scanner.nextInt();
                        scanner.nextLine();  // Scanner'ın geçmişini temizle
                        VoyageCaptains captain = new VoyageCaptains();
                        captain.setCaptainId(captainId);
                        captainIds.add(captain);
                    }

                    // Mürettebatı almak için liste;
                    List<VoyageCrew> crewIds = new ArrayList<>();
                    System.out.println("Sefer için mürettebat ID'lerini girin (en az 1):");
                    int crewId = scanner.nextInt();
                    scanner.nextLine();  //   scanner'ın geçmişini temizle
                    VoyageCrew crew = new VoyageCrew();
                    crew.setCrewId(crewId);
                    crewIds.add(crew);

                    // Sefer nesnesi oluşturma , bilgileri set etme

                    voyage1.setVoyagePorts(portVisits);
                    voyage1.setCaptainIds(captainIds);
                    voyage1.setCrewIds(crewIds);

                    // ! Database'e sefer ekle meetodu;
                    if (addVoyageToDatabase(voyage1)) {
                        System.out.println("Sefer başarıyla eklendi.");
                    } else {
                        System.out.println("Sefer eklenirken bir sorun oluştu.");
                    }

                    System.out.println("Yeni Sefer eklendi.");
                    break;

                case 3:
                    System.out.println("----Sefer Silme----\n");
                    try {
                        System.out.println("Silinecek Sefer ID'sini girin:");
                        int silinecekSeferId = scanner.nextInt();

                        Connection connection = DriverManager.getConnection(url, user, password);
                        System.out.println("Veritabanına başarıyla bağlandı!");

                        String sql = "DELETE FROM voyages WHERE voyageid = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setInt(1, silinecekSeferId);

                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Sefer başarıyla silindi.");
                        } else {
                            System.out.println("Belirtilen ID'ye sahip bir Sefer bulunamadı.");
                        }

                        preparedStatement.close();
                        connection.close(); //vt bağlantısı kapat
                    } catch (SQLException e) {
                        e.printStackTrace(); // veri tabanı bağlantı hataları
                    }
                    System.out.println("Sefer silindi.");
                    break;
                case 4:
                    System.out.println("----Sefer Düzenleme----\n");
                    Connection connection = null;
                    PreparedStatement preparedStatement = null;
                    Voyage voyage2 = new Voyage();

                    try {
                        // Sefer ID'si
                        System.out.println("Düzenlenecek Sefer ID'sini girin:");
                        int duzenlenecekSeferId = scanner.nextInt();
                        scanner.nextLine(); // Boş satırı oku

                        System.out.println("Hangi bilgiyi düzenlemek istiyorsunuz?");
                        System.out.println("1. Sefer İD düzenle");
                        System.out.println("2. Sefere Çıkan Gemi İD düzenle");
                        System.out.println("3. Sefere Çıkış Tarihi düzenle");
                        System.out.println("4. Seferden Dönüş Tarihini düzenle");
                        System.out.println("5. Sefer Çıkış Limanı İD düzenle");
                        System.out.println("6. Seferde Uğranılan Liman Bilgilerini düzenle ");
                        System.out.println("7. Seferdeki Kaptanların İD Bilgilerini düzenle");
                        System.out.println("8. Seferdeki Mürettebat İD bilgilerini düzenle");
                        connection = DriverManager.getConnection(url, user, password);
                        System.out.println("Veritabanına başarıyla bağlandı!");
                        System.out.print("Seçiminizi yapın: ");
                        int secim = scanner.nextInt();
                        scanner.nextLine(); // Boş satırı okuma

                        switch (secim) {
                            case 1:
                                System.out.println("Yeni Sefer İd güncelle:");
                                int newVoyageId = scanner.nextInt();
                                voyage2.setVoyageId(newVoyageId);

                                String updateVoyageIdSql = "UPDATE voyages SET voyageid = ? WHERE voyageid = ?";
                                preparedStatement = connection.prepareStatement(updateVoyageIdSql);
                                preparedStatement.setInt(1, voyage2.getVoyageId());
                                preparedStatement.setInt(2, duzenlenecekSeferId);
                                break;
                            case 2:
                                System.out.println("Yeni Sefer Gemi İD güncelle:");
                                int newVoyageShipId = scanner.nextInt();
                                voyage2.setShipId(newVoyageShipId);

                                String updateVoyageShipIdSql = "UPDATE voyages SET ship_id = ? WHERE voyageid = ?";
                                preparedStatement = connection.prepareStatement(updateVoyageShipIdSql);
                                preparedStatement.setInt(1, voyage2.getShipId());
                                preparedStatement.setInt(2, duzenlenecekSeferId);
                                break;
                            case 3:
                                System.out.println("Yeni Sefere Çıkış Tarihi güncelle: dd-MM-yyyy");
                                String newVoyageDDate = scanner.nextLine();
                                DateTimeFormatter formatterDepartureVoyageDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                                LocalDate localDDateUpdate = LocalDate.parse(newVoyageDDate, formatterDepartureVoyageDate);
                                java.sql.Date newDDateVoyage = java.sql.Date.valueOf(localDDateUpdate);  // -LocalDate'den java.sql.Date'e

                                String updateDepartureDateSql = "UPDATE voyages SET departuredate = ? WHERE voyageid = ?";
                                preparedStatement = connection.prepareStatement(updateDepartureDateSql);
                                preparedStatement.setDate(1, voyage2.getDepartureDate());
                                preparedStatement.setInt(2, duzenlenecekSeferId);
                                break;
                            case 4:
                                System.out.println("Yeni Sefereden Dönüş Tarihi güncelle: dd-MM-yyyy");
                                String newVoyageRDate = scanner.nextLine();
                                DateTimeFormatter formatterReturnVoyageDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                                LocalDate localRDateUpdate = LocalDate.parse(newVoyageRDate, formatterReturnVoyageDate);
                                java.sql.Date newReturnDateVoyage = java.sql.Date.valueOf(localRDateUpdate);  // -LocalDate'den java.sql.Date'e
                                voyage2.setDepartureDate(newReturnDateVoyage);
                                updateVoyageStatus(duzenlenecekSeferId, newReturnDateVoyage);

                                String updateReturnDateSql = "UPDATE voyages SET returndate = ? WHERE voyageid = ?";
                                preparedStatement = connection.prepareStatement(updateReturnDateSql);
                                preparedStatement.setDate(1, voyage2.getDepartureDate());
                                preparedStatement.setInt(2, duzenlenecekSeferId);
                                break;
                            case 5:
                                System.out.println("Sefere Çıkış Liman İD güncelle: ");
                                int newUpDepartuePort = scanner.nextInt();
                                voyage2.setDeparturePort(newUpDepartuePort);

                                String updatePortIdVoyage = "UPDATE voyages SET departureport_id = ? WHERE voyageid = ?";
                                preparedStatement = connection.prepareStatement(updatePortIdVoyage);
                                preparedStatement.setInt(1,voyage2.getDeparturePort());
                                preparedStatement.setInt(2, duzenlenecekSeferId);
                                break;
                            case 6:
                                System.out.println("Seferde uğranılan limanların yeni sayısını girin: ");
                                int updatedNumberOfPorts = scanner.nextInt();  // Kaç tane yeni liman ziyareti olacağını al?
                                scanner.nextLine();  // scanner'ın geçmişii temizle

                                List<VoyagePorts> updatedPortVisits = new ArrayList<>();  // güncel liman ziyaretleri için liste oluştur

                                for (int i = 0; i < updatedNumberOfPorts; i++) {
                                    System.out.print((i + 1) + ". Liman ID: ");
                                    int portId = scanner.nextInt();  // liman ID'si al
                                    scanner.nextLine();  // scanner'ı temizle

                                    System.out.print("Varış tarihi (YYYY-MM-DD): ");
                                    String newArrival = scanner.nextLine();
                                    LocalDate newArrivalDate = LocalDate.parse(newArrival, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                                    System.out.print("Ayrılış tarihi (YYYY-MM-DD): ");
                                    String newDeparture = scanner.nextLine();
                                    LocalDate newDepartureDate = LocalDate.parse(newDeparture, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                                    // Yeni bir VoyagePorts nesnesi oluştur ve bilgileri set et
                                    VoyagePorts updatedVoyagePort = new VoyagePorts(portId, newArrivalDate, newDepartureDate);
                                    updatedPortVisits.add(updatedVoyagePort);  // Listeye ekle
                                }

                                // Veri tabanında ilgili seferin liman ziyaretlerini güncelle
                                if (updateVoyagePortsInDatabase(duzenlenecekSeferId, updatedPortVisits)) {
                                    System.out.println("Liman ziyaretleri başarıyla güncellendi.");
                                } else {
                                    System.out.println("Liman ziyaretlerini güncellerken bir sorun oluştu.");
                                }
                                break;
                            case 7:
                                try {
                                    connection = DriverManager.getConnection(url, user, password);
                                    // Mevcut kaptanları listele;
                                    System.out.println("Düzenlenecek seferin mevcut kaptanları:");
                                    String selectCaptains = "SELECT captain_id FROM voyage_captains WHERE voyage_id = ?";
                                    preparedStatement = connection.prepareStatement(selectCaptains);
                                    preparedStatement.setInt(1, duzenlenecekSeferId);
                                    ResultSet rs = preparedStatement.executeQuery();

                                    while (rs.next()) {
                                        System.out.println("Kaptan ID: " + rs.getInt("captain_id"));
                                    }

                                    System.out.println("Değiştirmek istediğiniz mevcut Kaptan ID'sini girin:");
                                    int oldCaptainId = scanner.nextInt();
                                    System.out.println("Yeni Kaptan ID'sini girin: ");
                                    int newCaptainId = scanner.nextInt();

                                    // eski kaptanı güncelle
                                    String updateCaptain = "UPDATE voyage_captains SET captain_id = ? WHERE captain_id = ? AND voyage_id = ?";
                                    preparedStatement = connection.prepareStatement(updateCaptain);
                                    preparedStatement.setInt(1, newCaptainId);
                                    preparedStatement.setInt(2, oldCaptainId);
                                    preparedStatement.setInt(3, duzenlenecekSeferId);
                                    int affectedRows = preparedStatement.executeUpdate();

                                    if (affectedRows > 0) {
                                        System.out.println("Kaptan başarıyla güncellendi.");
                                    } else {
                                        System.out.println("Kaptan güncellenirken bir hata oluştu.");
                                    }
                                } catch (SQLException e) {
                                    System.out.println("Veritabanı işlemi sırasında hata oluştu.");
                                    e.printStackTrace();
                                } finally {
                                    if (preparedStatement != null) {
                                        try {
                                            preparedStatement.close();
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (connection != null) {
                                        try {
                                            connection.close();
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                break;
                            case 8:
                                try {
                                    connection = DriverManager.getConnection(url, user, password);
                                    // Mevcut mürettebatları listele
                                    System.out.println("Düzenlenecek seferin mevcut mürettebatları: ");
                                    String selectCrew = "SELECT crew_id FROM voyage_crew WHERE voyage_id = ?";
                                    preparedStatement = connection.prepareStatement(selectCrew);
                                    preparedStatement.setInt(1, duzenlenecekSeferId);
                                    ResultSet rs = preparedStatement.executeQuery();

                                    while (rs.next()) {
                                        System.out.println("Mürettebat ID: " + rs.getInt("crew_id"));
                                    }

                                    System.out.println("Değiştirmek istediğiniz mevcut Mürettebat ID'sini girin: ");
                                    int oldCrewId = scanner.nextInt();
                                    System.out.println("Yeni Mürettebat ID'sini girin:");
                                    int newCrewId = scanner.nextInt();

                                    // Eski mürettebatı güncelle
                                    String updateCrew = "UPDATE voyage_crew SET crew_id = ? WHERE crew_id = ? AND voyage_id = ?";
                                    preparedStatement = connection.prepareStatement(updateCrew);
                                    preparedStatement.setInt(1, newCrewId);
                                    preparedStatement.setInt(2, oldCrewId);
                                    preparedStatement.setInt(3, duzenlenecekSeferId);
                                    int affectedRows = preparedStatement.executeUpdate();

                                    if (affectedRows > 0) {
                                        System.out.println("Mürettebat başarıyla güncellendi.");
                                    } else {
                                        System.out.println("Mürettebat güncellenirken bir hata oluştu.");
                                    }
                                } catch (SQLException e) {
                                    System.out.println("Veritabanı işlemi sırasında hata oluştu.");
                                    e.printStackTrace();
                                } finally {
                                    if (preparedStatement != null) {
                                        try {
                                            preparedStatement.close();
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (connection != null) {
                                        try {
                                            connection.close();
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                break;
                            default:
                                System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
                                return; // Ana menüye geri dön
                        }

                        // Sefer id'sini aldı veritabanı kapat ve güncelle

                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Sefer başarıyla güncellendi.");
                        } else {
                            System.out.println("Belirtilen ID'ye sahip bir Sefer bulunamadı.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Veritabanına bağlanırken veya sorgu çalıştırılırken bir hata oluştu.");
                        e.printStackTrace();
                    } finally {
                        // Her durumda bağlantıyı ve preparedStatement nesnesini kapat
                        if (preparedStatement != null) {
                            try {
                                preparedStatement.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (connection != null) {
                            try {
                                connection.close();
                                System.out.println("Veritabanı bağlantısı kapatıldı.");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    System.out.println("Sefer düzenlendi.");
                    break;
                case 0:
                    // Ana menüye dön
                    break;
                default:
                    System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
                    break;
            }
        } while (choice != 0);
    }

    public static void kaptanMenu(Scanner scanner) {
        int choice;

        do {
            // Sefer menü seçeneklerini ekrana yazdırma;
            System.out.println("------ KAPTAN Menüsü ------");
            System.out.println("1. Kaptanları Listele");
            System.out.println("2. Kaptan Ekle");
            System.out.println("3. Kaptan Sil");
            System.out.println("4. Kaptan Düzenle");
            System.out.println("0. Ana Menüye Dön");
            System.out.print("Seçiminizi yapın: ");


            choice = scanner.nextInt();

            // kullanıcının seçimine göre ilgili işlemi gerçekleştirme
            switch (choice) {
                case 1:
                    System.out.println("----Kaptan Listeleme----");
                    try (Connection connection = DriverManager.getConnection(url, user, password);
                            Statement statement = connection.createStatement();
                            ResultSet resultSet = statement.executeQuery("SELECT * FROM captain")) {

                    System.out.println("Kaptanlar Listesi:");
                    while (resultSet.next()) {
                        System.out.println("ID: " + resultSet.getInt("captain_id") +
                                ", İsim: " + resultSet.getString("captain_name") +
                                ", Soyisim: " + resultSet.getString("captain_lastname") +
                                ", Adres: " + resultSet.getString("captain_address"));
                    }
                } catch (SQLException e) {
                    System.out.println("Veritabanı hatası: " + e.getMessage());
                    e.printStackTrace();
                }System.out.println("Kaptanlar listelendi.");
                    break;
                case 2:
                    System.out.println("----Yeni Kaptan Ekleme----");
                    Captain captain = new Captain();
                    System.out.println("Kaptan ID'sini girin: ");
                    int id = scanner.nextInt();
                    captain.setCaptainId(id);

                    System.out.println("Kaptan ismi girin: ");
                    String name = scanner.nextLine();
                    captain.setCaptainName(name);

                    System.out.println("Kaptan Soyisim girin: ");
                    String lastname = scanner.nextLine();
                    captain.setCaptainLastName(lastname);

                    System.out.println(" Adres bilgisi giriniz: ");
                    String address = scanner.nextLine();
                    captain.setCaptainAddress(address);

                    System.out.println("Kaptan kimlik no girin: ");
                    int tckn = scanner.nextInt();
                    captain.setCaptainTckn(tckn);

                    System.out.println("Kaptan doğum tarihi girin: (gün/ay/yıl)");
                    String dateInput = scanner.next();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate localDate = LocalDate.parse(dateInput, formatter);
                    Date birthDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    captain.setCaptainBirthDate(birthDate);

                    System.out.println("Kaptan başlangıç tarihi girin: (gün/ay/yıl)");
                    String dateInput1 = scanner.next();
                    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate localDate1 = LocalDate.parse(dateInput1, formatter1);
                    Date strtDate = Date.from(localDate1.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    captain.setCaptainStartDate(strtDate);

                    System.out.println("Kaptan lisans no girin: ");
                    int licanseNo = scanner.nextInt();
                    captain.setCaptainLicense(licanseNo);

                    String sql = "INSERT INTO captain (captain_id, captain_name, captain_lastname, captain_address, captain_tckn, captain_birth_date, captain_start_date, captain_license_no) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    try (Connection connection = DriverManager.getConnection(url, user, password);
                         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setInt(1, captain.getCaptainId());
                        preparedStatement.setString(2, captain.getCaptainName());
                        preparedStatement.setString(3, captain.getCaptainLastName());
                        preparedStatement.setString(4, captain.getCaptainAddress());
                        preparedStatement.setInt(5, captain.getCaptainTckn());
                        preparedStatement.setDate(6, new java.sql.Date(captain.getCaptainBirthDate().getTime())); // java.util.Date'den java.sql.Date'e
                        preparedStatement.setDate(7, new java.sql.Date(captain.getCaptainStartDate().getTime()));
                        preparedStatement.setInt(8, captain.getCaptainLicense());

                        int affectedRows = preparedStatement.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Kaptan başarıyla eklendi.");
                        } else {
                            System.out.println("Kaptan eklenemedi.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Veritabanı hatası: " + e.getMessage());
                        e.printStackTrace();
                    }

                    System.out.println("Yeni Kaptan eklendi.");
                    break;
                case 3:
                    System.out.println("----Kaptan Silme----");
                    System.out.println("Silinecek kaptanın ID'sini girin:");
                    int kaptanId = scanner.nextInt(); // Kullanıcıdan kaptanın ID'sini alma

                    String sqlDelete = "DELETE FROM captain WHERE captain_id = ?"; // SQL sorgusu
                    try (Connection connection = DriverManager.getConnection(url, user, password);
                         PreparedStatement preparedStatement = connection.prepareStatement(sqlDelete)) {
                        preparedStatement.setInt(1, kaptanId); // sorguya kaptanın ID'sini ekleme yeri

                        int rowsAffected = preparedStatement.executeUpdate(); // sorguyu çalıştırve etkilenen satır sayısını al
                        if (rowsAffected > 0) {
                            System.out.println("Kaptan başarıyla silindi."); // eğer kaptan silinirse kullanıcıya bildir
                        } else {
                            System.out.println("Belirtilen ID'ye sahip kaptan bulunamadı."); // eğer silinecek kaptan bulunamazsa hata mesajı ver
                        }
                    } catch (SQLException e) {
                        System.out.println("Veritabanı işlemi sırasında hata oluştu: " + e.getMessage());
                        e.printStackTrace();
                    }
                    System.out.println("Kaptan silindi.");
                    break;
                case 4:
                    System.out.println("----Kaptan Düzenle----");
                    Connection connection = null;
                    PreparedStatement preparedStatement = null;

                    try {
                        // Kaptan ID'sini al
                        System.out.println("Düzenlenecek Kaptan ID'sini girin:");
                        int duzenlenecekKaptanId = scanner.nextInt();
                        scanner.nextLine(); // Boş satırı oku

                        System.out.println("Hangi bilgiyi düzenlemek istiyorsunuz?");
                        System.out.println("1. Kaptan Adı");
                        System.out.println("2. Kaptan soyadı");
                        System.out.println("3. Kaptan adresi");
                        System.out.println("4. Kaptan tkno");
                        System.out.println("5. Kaptan doğum tarihi");
                        System.out.println("6. Kaptan başlangıç tarihi ");
                        System.out.println("7. Kaptan lisans no");
                        System.out.println("8. Kaptan id");
                        connection = DriverManager.getConnection(url, user, password);
                        System.out.println("Veritabanına başarıyla bağlandı!");
                        System.out.print("Seçiminizi yapın: ");
                        int secim = scanner.nextInt();
                        scanner.nextLine(); // Boş satırı okuma
                        Captain captain2 = new Captain();
                        switch (secim) {
                            case 1:
                                System.out.println("Yeni Kaptan adını girin:");
                                String newName = scanner.nextLine();
                                captain2.setCaptainName(newName);

                                String updateNameSql = "UPDATE captain SET captain_name = ? WHERE captain_id = ?";
                                preparedStatement = connection.prepareStatement(updateNameSql);
                                preparedStatement.setString(1, captain2.getCaptainName());
                                preparedStatement.setInt(2, duzenlenecekKaptanId);
                                break;
                            case 2:
                                System.out.println("Yeni Kaptan Soyadı girin:");
                                String newLastName = scanner.nextLine();
                                captain2.setCaptainLastName(newLastName);

                                String updateWeightSql = "UPDATE captain SET captain_lastname = ? WHERE captain_id = ?";
                                preparedStatement = connection.prepareStatement(updateWeightSql);
                                preparedStatement.setString(1, captain2.getCaptainLastName());
                                preparedStatement.setInt(2, duzenlenecekKaptanId);
                                break;
                            case 3:
                                System.out.println("Yeni Kaptan Adresi girin:");
                                String newAddress = scanner.nextLine();
                                captain2.setCaptainAddress(newAddress);

                                String updateYearSql = "UPDATE captain SET captain_address = ? WHERE captain_id = ?";
                                preparedStatement = connection.prepareStatement(updateYearSql);
                                preparedStatement.setString(1, captain2.getCaptainAddress());
                                preparedStatement.setInt(2, duzenlenecekKaptanId);
                                break;
                            case 4:
                                System.out.println("Yeni Kaptan Tckno girin: ");
                                int newTckn = scanner.nextInt();
                                captain2.setCaptainTckn(newTckn);

                                String updateCapTckn = "UPDATE captain SET captain_tckn = ? WHERE captain_id = ?";
                                preparedStatement = connection.prepareStatement(updateCapTckn);
                                preparedStatement.setInt(1, captain2.getCaptainTckn());
                                preparedStatement.setInt(2, duzenlenecekKaptanId);
                                break;
                            case 5:
                                System.out.println("Yeni Kaptan Doğum Tarihi girin: (gün/ay/yıl)");
                                String dateInputUpdate = scanner.next();
                                DateTimeFormatter formatterBirthDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                                LocalDate localDateUpdate = LocalDate.parse(dateInputUpdate, formatterBirthDate);
                                Date birthDateUpdate = Date.from(localDateUpdate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                                captain2.setCaptainBirthDate(birthDateUpdate);

                                String updateCaptianBirth = "UPDATE captain SET captain_birth_date = ? WHERE captain_id = ?";
                                preparedStatement = connection.prepareStatement(updateCaptianBirth);
                                preparedStatement.setDate(1, new java.sql.Date(captain2.getCaptainBirthDate().getTime()));
                                preparedStatement.setInt(2, duzenlenecekKaptanId);
                                break;
                            case 6:
                                System.out.println("Yeni Kaptan Başlangıç Tarihi girin: (gün/ay/yıl)");
                                String dateStartUpdate = scanner.next();
                                DateTimeFormatter formatterStartDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                                LocalDate localDateStartUpdate = LocalDate.parse(dateStartUpdate, formatterStartDate);
                                Date startDate = Date.from(localDateStartUpdate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                                captain2.setCaptainBirthDate(startDate);
                                String updateCapStart = "UPDATE captain SET captain_start_date = ? WHERE captain_id = ?";
                                preparedStatement = connection.prepareStatement(updateCapStart);
                                preparedStatement.setDate(1, new java.sql.Date(captain2.getCaptainStartDate().getTime()));
                                preparedStatement.setInt(2, duzenlenecekKaptanId);
                                break;

                            case 7:
                                System.out.println("Yeni Lisans no girin:");
                                int licensenNo = scanner.nextInt();
                                captain2.setCaptainLicense(licensenNo);
                                String updateCap = "UPDATE captain SET captain_license = ? WHERE captain_id = ?";
                                preparedStatement = connection.prepareStatement(updateCap);
                                preparedStatement.setInt(1, captain2.getCaptainLicense());
                                preparedStatement.setInt(2, duzenlenecekKaptanId);
                                break;
                            case 8:
                                System.out.println("Kaptan İD giriniz: ");
                                int captainid = scanner.nextInt();
                                captain2.setCaptainId(captainid);
                                String updateCapId = "UPDATE captain SET captain_id = ? WHERE captain_id = ?";
                                preparedStatement = connection.prepareStatement(updateCapId);
                                preparedStatement.setInt(1, captain2.getCaptainId());
                                preparedStatement.setInt(2, duzenlenecekKaptanId);
                                break;
                            default:
                                System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
                                return; // Ana menüye geri dön
                        }

                        // Kaptan ID'sini aldıktan sonra diğer işlemler devam eder veritabanı kapatma ve güncelleme

                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Kaptan başarıyla güncellendi.");
                        } else {
                            System.out.println("Belirtilen ID'ye sahip bir Kaptan bulunamadı.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Veritabanına bağlanırken veya sorgu çalıştırılırken bir hata oluştu.");
                        e.printStackTrace();
                    } finally {
                        // Her durumda bağlantıyı ve preparedStatement nesnesini kapat
                        if (preparedStatement != null) {
                            try {
                                preparedStatement.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (connection != null) {
                            try {
                                connection.close();
                                System.out.println("Veritabanı bağlantısı kapatıldı.");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    System.out.println("Kaptan düzenlendi.");
                    break;
                case 0:
                    // Ana menüye dön
                    break;
                default:
                    System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
                    break;
            }
        } while (choice != 0);
    }

    public static void murettebatMenu(Scanner scanner) {
        int choice;

        do {
            // Sefer menü seçeneklerini ekrana yazdırma
            System.out.println("------ MURETTEBAT Menüsü ------");
            System.out.println("1. Mürettebat Listele");
            System.out.println("2. Mürettebat Ekle");
            System.out.println("3. Mürettebat Sil");
            System.out.println("4. Mürettebat Düzenle");
            System.out.println("0. Ana Menüye Dön");
            System.out.print("Seçiminizi yapın: ");

            // Kullanıcının seçimini alın;
            choice = scanner.nextInt();

            // Kullanıcının seçimine göre ilgili işlemi gerçekleştirme;
            switch (choice) {
                case 1:
                    System.out.println("----Mürettebat Listeleme----");
                    try (Connection connection = DriverManager.getConnection(url, user, password);
                         Statement statement = connection.createStatement();
                         ResultSet resultSet = statement.executeQuery("SELECT * FROM crew")) {

                        System.out.println("Mürettebat Listesi:");
                        while (resultSet.next()) {
                            System.out.println("ID: " + resultSet.getInt("crew_id") +
                                    ", İsim: " + resultSet.getString("crew_name") +
                                    ", Soyisim: " + resultSet.getString("crew_lastname") +
                                    ", Adres: " + resultSet.getString("crew_address"));
                        }
                    } catch (SQLException e) {
                        System.out.println("Veritabanı hatası: " + e.getMessage());
                        e.printStackTrace();
                    }System.out.println("Mürettebat listelendi.");
                    break;
                case 2:
                    System.out.println("----Mürettebat Ekleme----");
                    Crew crew = new Crew();
                    System.out.println("Mürettebat ID'sini girin: ");
                    int id = scanner.nextInt();
                    crew.setCrewId(id);

                    System.out.println("Mürettebat ismi girin: ");
                    String name = scanner.nextLine();
                    crew.setCrewName(name);

                    System.out.println("Mürettebat Soyisim girin: ");
                    String lastname = scanner.nextLine();
                    crew.setCrewLastName(lastname);

                    System.out.println(" Mürettebat bilgisi giriniz: ");
                    String address = scanner.nextLine();
                    crew.setCrewAddress(address);

                    System.out.println("Mürettebat kimlik no girin: ");
                    int tckn = scanner.nextInt();
                    crew.setCrewTckn(tckn);

                    System.out.println("Mürettebat doğum tarihi girin: (gün/ay/yıl)");
                    String dateInput = scanner.next();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate localDate = LocalDate.parse(dateInput, formatter);
                    Date birthDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    crew.setCrewBirthDate(birthDate);

                    System.out.println("Mürettebat başlangıç tarihi girin: (gün/ay/yıl)");
                    String dateInput1 = scanner.next();
                    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate localDate1 = LocalDate.parse(dateInput1, formatter1);
                    Date strtDate = Date.from(localDate1.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    crew.setCrewStartDate(strtDate);

                    System.out.println("Mürettebat lisans no girin: ");
                    String task = scanner.nextLine();
                    crew.setCrewTask(task);

                    String sql = "INSERT INTO crew (crew_id, crew_name, crew_lastname, crew_address, crew_tckn, crew_birth_date, crew_start_date, crew_task) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    try (Connection connection = DriverManager.getConnection(url, user, password);
                         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setInt(1, crew.getCrewId());
                        preparedStatement.setString(2, crew.getCrewName());
                        preparedStatement.setString(3, crew.getCrewLastName());
                        preparedStatement.setString(4, crew.getCrewAddress());
                        preparedStatement.setInt(5, crew.getCrewTckn());
                        preparedStatement.setDate(6, new java.sql.Date(crew.getCrewBirthDate().getTime())); // java.util.Date'den java.sql.Date'e
                        preparedStatement.setDate(7, new java.sql.Date(crew.getCrewStartDate().getTime()));
                        preparedStatement.setString(8, crew.getCrewTask());

                        int affectedRows = preparedStatement.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Mürettebat başarıyla eklendi.");
                        } else {
                            System.out.println("Mürettebat eklenemedi.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Veritabanı hatası: " + e.getMessage());
                        e.printStackTrace();
                    }

                    System.out.println("Yeni Mürettebat eklendi.");
                    break;
                case 3:
                    System.out.println("----Mürettebat silme----\n");
                    System.out.println("Silinecek Mürettebat ID'sini girin:");
                    int crewId = scanner.nextInt(); // Kullanıcıdan kaptanın ID'sini alma

                    String sqlDelete = "DELETE FROM crew WHERE crew_id = ?"; // SQL sorgusu
                    try (Connection connection = DriverManager.getConnection(url, user, password);
                         PreparedStatement preparedStatement = connection.prepareStatement(sqlDelete)) {
                        preparedStatement.setInt(1, crewId); // sorguya Mürettebat ID'sini ekleme yeri

                        int rowsAffected = preparedStatement.executeUpdate(); // sorguyu çalıştırve etkilenen satır sayısını al
                        if (rowsAffected > 0) {
                            System.out.println("Mürettebat başarıyla silindi."); // eğer Mürettebat silinirse kullanıcıya bildir
                        } else {
                            System.out.println("Belirtilen ID'ye sahip Mürettebat bulunamadı."); // eğer silinecek Mürettebat bulunamazsa hata mesajı ver
                        }
                    } catch (SQLException e) {
                        System.out.println("Veritabanı işlemi sırasında hata oluştu: " + e.getMessage());
                        e.printStackTrace();
                    }
                    System.out.println("Mürettebat silindi.");
                    break;
                case 4:
                    System.out.println("----Mürettebat Düzenleme----");
                    Connection connection = null;
                    PreparedStatement preparedStatement = null;

                    try {
                        // Kaptan ID'sini al
                        System.out.println("Düzenlenecek Mürettebat ID'sini girin:");
                        int duzenlenecekMurettebatId = scanner.nextInt();
                        scanner.nextLine(); // Boş satırı oku

                        System.out.println("Hangi bilgiyi düzenlemek istiyorsunuz?");
                        System.out.println("1. Mürettebat Adı");
                        System.out.println("2. Mürettebat soyadı");
                        System.out.println("3. Mürettebat adresi");
                        System.out.println("4. Mürettebat tkno");
                        System.out.println("5. Mürettebat doğum tarihi");
                        System.out.println("6. Mürettebat başlangıç tarihi ");
                        System.out.println("7. Mürettebat lisans no");
                        System.out.println("8. Mürettebat id");
                        connection = DriverManager.getConnection(url, user, password);
                        System.out.println("Veritabanına başarıyla bağlandı!");
                        System.out.print("Seçiminizi yapın: ");
                        int secim = scanner.nextInt();
                        scanner.nextLine(); // Boş satırı okuma
                        Crew crew1 = new Crew();
                        switch (secim) {
                            case 1:
                                System.out.println("Yeni Mürettebat adını girin:");
                                String newName = scanner.nextLine();
                                crew1.setCrewName(newName);

                                String updateNameSql = "UPDATE crew SET crew_name = ? WHERE crew_id = ?";
                                preparedStatement = connection.prepareStatement(updateNameSql);
                                preparedStatement.setString(1, crew1.getCrewName());
                                preparedStatement.setInt(2, duzenlenecekMurettebatId);
                                break;
                            case 2:
                                System.out.println("Yeni Mürettebat Soyadı girin:");
                                String newLastName = scanner.nextLine();
                                crew1.setCrewLastName(newLastName);

                                String updateLastNameSql = "UPDATE crew SET crew_lastname = ? WHERE crew_id = ?";
                                preparedStatement = connection.prepareStatement(updateLastNameSql);
                                preparedStatement.setString(1, crew1.getCrewLastName());
                                preparedStatement.setInt(2, duzenlenecekMurettebatId);
                                break;
                            case 3:
                                System.out.println("Yeni Mürettebat Adresi girin:");
                                String newAddress = scanner.nextLine();
                                crew1.setCrewAddress(newAddress);

                                String updateAddressSql = "UPDATE crew SET crew_address = ? WHERE crew_id = ?";
                                preparedStatement = connection.prepareStatement(updateAddressSql);
                                preparedStatement.setString(1, crew1.getCrewAddress());
                                preparedStatement.setInt(2, duzenlenecekMurettebatId);
                                break;
                            case 4:
                                System.out.println("Yeni Mürettebat Tckno girin: ");
                                int newTckn = scanner.nextInt();
                                crew1.setCrewTckn(newTckn);

                                String updateCrewTckn = "UPDATE crew SET crew_tckn = ? WHERE crew_id = ?";
                                preparedStatement = connection.prepareStatement(updateCrewTckn);
                                preparedStatement.setInt(1, crew1.getCrewTckn());
                                preparedStatement.setInt(2, duzenlenecekMurettebatId);
                                break;
                            case 5:
                                System.out.println("Yeni Mürettebat Doğum Tarihi girin: (gün/ay/yıl)");
                                String dateInputUpdate = scanner.next();
                                DateTimeFormatter formatterBirthDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                                LocalDate localDateUpdate = LocalDate.parse(dateInputUpdate, formatterBirthDate);
                                Date birthDateUpdate = Date.from(localDateUpdate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                                crew1.setCrewBirthDate(birthDateUpdate);

                                String updateCrewBirth = "UPDATE crew SET crew_birth_date = ? WHERE crew_id = ?";
                                preparedStatement = connection.prepareStatement(updateCrewBirth);
                                preparedStatement.setDate(1, new java.sql.Date(crew1.getCrewBirthDate().getTime()));
                                preparedStatement.setInt(2, duzenlenecekMurettebatId);
                                break;
                            case 6:
                                System.out.println("Yeni Mürettebat Başlangıç Tarihi girin: (gün/ay/yıl)");
                                String dateStartUpdate = scanner.next();
                                DateTimeFormatter formatterStartDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                                LocalDate localDateStartUpdate = LocalDate.parse(dateStartUpdate, formatterStartDate);
                                Date startDate = Date.from(localDateStartUpdate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                                crew1.setCrewStartDate(startDate);
                                String updateCrewStart = "UPDATE crew SET crew_start_date = ? WHERE crew_id = ?";
                                preparedStatement = connection.prepareStatement(updateCrewStart);
                                preparedStatement.setDate(1, new java.sql.Date(crew1.getCrewStartDate().getTime()));
                                preparedStatement.setInt(2, duzenlenecekMurettebatId);
                                break;

                            case 7:
                                System.out.println("Yeni Mürettebat görevi girin:");
                                String tasknew = scanner.nextLine();
                                crew1.setCrewTask(tasknew);

                                String updateTask = "UPDATE crew SET crew_task = ? WHERE crew_id = ?";
                                preparedStatement = connection.prepareStatement(updateTask);
                                preparedStatement.setString(1, crew1.getCrewTask());
                                preparedStatement.setInt(2, duzenlenecekMurettebatId);
                                break;
                            case 8:
                                System.out.println("Mürettebat İD giriniz: ");
                                int captainid = scanner.nextInt();
                                crew1.setCrewId(captainid);

                                String updateCrewId = "UPDATE crew SET crew_id = ? WHERE crew_id = ?";
                                preparedStatement = connection.prepareStatement(updateCrewId);
                                preparedStatement.setInt(1, crew1.getCrewId());
                                preparedStatement.setInt(2, duzenlenecekMurettebatId);
                                break;
                            default:
                                System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
                                return; // Ana menüye geri dön
                        }

                        // Mürettebat ID'sini aldı veritabanı kapatma ve güncelleme

                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Mürettebat başarıyla güncellendi.");
                        } else {
                            System.out.println("Belirtilen ID'ye sahip bir Mürettebat bulunamadı.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Veritabanına bağlanırken veya sorgu çalıştırılırken bir hata oluştu.");
                        e.printStackTrace();
                    } finally {
                        // Her durumda bağlantıyı ve preparedStatement nesnesini kapat
                        if (preparedStatement != null) {
                            try {
                                preparedStatement.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (connection != null) {
                            try {
                                connection.close();
                                System.out.println("Veritabanı bağlantısı kapatıldı.");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    System.out.println("Mürettebat düzenlendi.");
                    break;
                case 0:
                    // Ana menüye dön
                    break;
                default:
                    System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
                    break;
            }
        } while (choice != 0);

    }
    public static void limanMenu(Scanner scanner) {
        int choice;

        do {
            // Gemilerin menü seçeneklerini ekrana yazdırma
            System.out.println("------ Liman Menüsü ------");
            System.out.println("1. Limanları Listele");
            System.out.println("2. Liman Ekle");
            System.out.println("3. Liman Sil");
            System.out.println("4. Liman Düzenle");
            System.out.println("0. Ana Menüye Dön");
            System.out.print("Seçiminizi yapın: ");

            // Kullanıcının seçimini alın
            choice = scanner.nextInt();

            // Kullanıcının seçimine göre ilgili işlemi gerçekleştir ^
            switch (choice) {
                case 1:
                    try {
                        Connection connection = DriverManager.getConnection(url, user, password);
                        System.out.println("Veritabanına başarıyla bağlandı!");

                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM \"PORT\"");


                        while (resultSet.next()) {
                            System.out.println("Liman İD: " + resultSet.getInt("id") +
                                    ", Liman Adı: " + resultSet.getString("name") +
                                    ", Liman Ülkesi: " + resultSet.getString("country"));
                        }

                        resultSet.close();
                        statement.close();
                        connection.close(); // vys kapa
                    } catch (SQLException e) {
                        e.printStackTrace(); // vt bağlanırken oluşan hataları çıkarır
                    }

                    System.out.println("Limanlar listelendi.");
                    break;
                case 2:
                    System.out.println("------ Liman Ekle Menüsü ------");
                    PORT port = new PORT();

                    System.out.println("Liman ismi girin:");
                    String portNameSql = scanner.nextLine();
                    port.setName(portNameSql);

                    System.out.println("Liman ülkesi girin:");
                    String portCountry = scanner.nextLine();
                    port.setCountry(portCountry);

                    System.out.println("Liman nüfus bilgisi girin:");
                    int portPopulation = scanner.nextInt();
                    port.setPopulation(portPopulation);

                    System.out.println("Liman demirleme ücreti girin:");
                    double portFee = scanner.nextDouble();
                    port.setAnchorageFee(portFee);

                    System.out.println("Pasaport istiyor= 'true'\n"+"Pasaport istemiyor= 'false' \n"
                            +"Liman Pasaport istiyor mu girin:");
                    boolean portPass = scanner.nextBoolean();
                    port.setPassport(portPass);

                    /*System.out.println("Limana sefer yapıldı= 'true'\n"+"Limana sefer yapılmadı= 'false'\n"
                            +"Limana daha önce sefer yapıldı mı girin");
                    boolean portFirtTime = scanner.nextBoolean();
                    port.setFirstTime(portFirtTime);*/
                    try {
                        Connection connection = DriverManager.getConnection(url, user, password);
                        System.out.println("Veritabanına başarıyla bağlandı!");

                        // PreparedStatement kullanarak Liman bilgisi ekleme;
                        String sql = "INSERT INTO \"PORT\" (name, country, population, anchorage_fee, passport) " +  //, is_first_time) "
                                "VALUES (?, ?, ?, ?, ?)"; //, ?)"
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, port.getName());
                        preparedStatement.setString(2, port.getCountry());
                        preparedStatement.setInt(3, port.getPopulation());
                        preparedStatement.setDouble(4, port.getAnchorageFee());
                        preparedStatement.setBoolean(5,port.isPassport()); // Ship_type değeri
                        //preparedStatement.setBoolean(6, port.isFirstTime());  set false olarak ayarlandı

                        // Liman eklenirken olası hata kontrolü;
                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Liman başarıyla eklendi.");
                        } else {
                            System.out.println("Liman eklenirken bir hata oluştu.");
                        }
                        // VTS Bağlantıyı kapat
                        preparedStatement.close();
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();// Veri tabanı bağlantı hatası
                    }
                    break;
                case 3:
                    try {
                        Connection connection = DriverManager.getConnection(url, user, password);
                        System.out.println("Veritabanına başarıyla bağlandı!\n"+
                                "! ! İlk Önce Listeyi Kontrol Edip İD Bilgisini Alın ! !");

                        PORT port1 = new PORT();
                        scanner.nextLine();
                        System.out.println("Silinnecek Liman İD girin: ");
                        int deleteIdPort = scanner.nextInt();
                        port1.setId(deleteIdPort);

                        String sql = "DELETE FROM \"PORT\" WHERE \"id\" = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setInt(1, deleteIdPort);
                        /*System.out.println("Silinecek Liman İsmini girin:");
                        String deletePort = scanner.nextLine();
                        port1.setName(deletePort);

                        String sql = "DELETE FROM \"PORT\" WHERE name = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, port1.getName());*/

                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Liman başarıyla silindi.");
                        } else {
                            System.out.println("Belirtilen isme sahip Liman bulunamadı.");
                        }

                        preparedStatement.close();
                        connection.close(); //vt bağlantısı kapat
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    Connection connection = null;
                    PreparedStatement preparedStatement = null;

                    try {
                        // Liman ID'sini al
                        System.out.println("Düzenlenecek Liman ID'sini girin:");
                        int duzenlenecekLimanId = scanner.nextInt();
                        scanner.nextLine(); // Boş satırı oku

                        System.out.println("Hangi bilgiyi düzenlemek istiyorsunuz?");
                        System.out.println("1. Liman Adı");
                        System.out.println("2. Liman Ülkesi");
                        System.out.println("3. Nüfus");
                        System.out.println("4. Demirleme ücreti");
                        System.out.println("5. Pasaport bilgisi");
                        System.out.println("6. Limana daha önce uğrandı ");

                        System.out.print("Seçiminizi yapın: ");
                        int secim = scanner.nextInt();
                        scanner.nextLine(); // Boş satırı okuma
                        PORT port2 = new PORT();
                        switch (secim) {
                            case 1:
                                connection = DriverManager.getConnection(url, user, password);
                                System.out.println("Veritabanına başarıyla bağlandı!");

                                System.out.println("Yeni Liman adını girin:");
                                String newName = scanner.nextLine();
                                port2.setName(newName);

                                String updateNameSql = "UPDATE \"PORT\" SET name = ? WHERE id = ?";
                                preparedStatement = connection.prepareStatement(updateNameSql);
                                preparedStatement.setString(1, port2.getName());
                                preparedStatement.setInt(2, duzenlenecekLimanId);
                                break;
                            case 2:
                                connection = DriverManager.getConnection(url, user, password);
                                System.out.println("Veritabanına başarıyla bağlandı!");

                                System.out.println("Yeni Liman Ülkesi girin:");
                                String newCountry = scanner.nextLine();
                                port2.setCountry(newCountry);

                                String updateWeightSql = "UPDATE \"PORT\" SET country = ? WHERE id = ?";
                                preparedStatement = connection.prepareStatement(updateWeightSql);
                                preparedStatement.setString(1, port2.getCountry());
                                preparedStatement.setInt(2, duzenlenecekLimanId);
                                break;
                            case 3:
                                connection = DriverManager.getConnection(url, user, password);
                                System.out.println("Veritabanına başarıyla bağlandı!");

                                System.out.println("Yeni Liman Nüfus girin:");
                                int newPop = scanner.nextInt();
                                port2.setPopulation(newPop);

                                String updateYearSql = "UPDATE \"PORT\" SET population = ? WHERE id = ?";
                                preparedStatement = connection.prepareStatement(updateYearSql);
                                preparedStatement.setInt(1, port2.getPopulation());
                                preparedStatement.setInt(2, duzenlenecekLimanId);
                                break;
                            case 4:
                                connection = DriverManager.getConnection(url, user, password);
                                System.out.println("Veritabanına başarıyla bağlandı!");
                                System.out.println("Yeni Liman Demirleme Ücreti girin: ");
                                Double newAnchorageFee = scanner.nextDouble();
                                port2.setAnchorageFee(newAnchorageFee);

                                String updatePasCapSql = "UPDATE \"PORT\" SET anchorage_fee = ? WHERE id = ?";
                                preparedStatement = connection.prepareStatement(updatePasCapSql);
                                preparedStatement.setDouble(1, port2.getAnchorageFee());
                                preparedStatement.setInt(2, duzenlenecekLimanId);
                                break;
                            case 5:
                                connection = DriverManager.getConnection(url, user, password);
                                System.out.println("Veritabanına başarıyla bağlandı!");

                                System.out.println("Pasaport istiyor = true\n" +
                                        "Pasaport istemiyor = false\n"+"Yeni Pasaport bilgisini güncelleyin: ");
                                Boolean newPass = scanner.nextBoolean();
                                port2.setPassport(newPass);
                                String updateShipTypSql = "UPDATE \"PORT\" SET passport = ? WHERE id = ?";
                                preparedStatement = connection.prepareStatement(updateShipTypSql);
                                preparedStatement.setBoolean(1, port2.isPassport());
                                preparedStatement.setInt(2, duzenlenecekLimanId);
                                break;
                            case 6:
                                connection = DriverManager.getConnection(url, user, password);
                                System.out.println("Veri tabanınabağlandı");
                                System.out.println("Limana uğranma bilgisini değiştiriyorsunuz\n bu bilgi zaten oto dolduruluyor!!\n" +
                                        "Uğrandı = true, Uğranmadı = false \n"+ "Limana Uğrandı mı girin: ");
                                Boolean newTime = scanner.nextBoolean();
                                port2.setFirstTime(newTime);
                                String updateSh = "UPDATE \"PORT\" SET is_first_time = ? WHERE id = ?";
                                preparedStatement = connection.prepareStatement(updateSh);
                                preparedStatement.setBoolean(1, port2.isFirstTime());
                                preparedStatement.setInt(2, duzenlenecekLimanId);
                                break;


                            default:
                                System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
                                return; // Ana menüye geri dön
                        }

                        // Liman ID'sini aldıktan sonra diğer işlemler devam eder veritabanı kapatma ve güncelleme

                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("LİMAN başarıyla güncellendi.");
                        } else {
                            System.out.println("Belirtilen ID'ye sahip bir LİMAN bulunamadı.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Veritabanına bağlanırken veya sorgu çalıştırılırken bir hata oluştu.");
                        e.printStackTrace();
                    } finally {
                        // Her durumda bağlantıyı ve preparedStatement nesnesini kapat
                        if (preparedStatement != null) {
                            try {
                                preparedStatement.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (connection != null) {
                            try {
                                connection.close();
                                System.out.println("Veritabanı bağlantısı kapatıldı.");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    System.out.println("Gemi düzenlendi.");

                    break;
            }
        } while (choice != 0);
    }
}