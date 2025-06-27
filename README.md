# BankApp

Projekt zaliczeniowy na przedmiot **Metodologia tworzenia projektów informatycznych**.

System bankowy w technologii Java EE umożliwiający:
- Rejestrację i logowanie użytkowników
- Obsługę salda konta: wpłaty, wypłaty, przelewy
- Rejestrowanie historii transakcji z możliwością filtrowania
- Zapis danych do pliku JSON (użytkownicy i transakcje)
- Interfejs oparty na JSP

## Technologie
- Java 8+
- Java EE (Servlety, JSP)
- Apache Tomcat
- Maven
- JSON (javax.json)

## Struktura projektu
- `model` – klasy danych (User, Transaction)
- `storage` – operacje logiki biznesowej i zapisu
- `servlet` – logika żądań HTTP
- `webapp` – pliki JSP i konfiguracja `web.xml`

## Uruchomienie
1. `mvn clean package`
2. Wgraj plik `.war` do katalogu `webapps` w Tomcat
3. Uruchom Tomcat
4. Wejdź na `http://localhost:8080/BankApp`

## Autor
Projekt stworzony przez Mikita Kryvenia w ramach kursu MTPI.

---

# BankApp

Final project for the course **Methodology of Developing IT Projects**.

A Java EE-based banking system that allows:
- User registration and login
- Account balance operations: deposits, withdrawals, transfers
- Transaction history logging and filtering
- Data persistence in JSON format (users and transactions)
- JSP-based user interface

## Technologies
- Java 8+
- Java EE (Servlets, JSP)
- Apache Tomcat
- Maven
- JSON (javax.json)

## Project Structure
- `model` – data classes (User, Transaction)
- `storage` – business logic and data persistence
- `servlet` – HTTP request logic
- `webapp` – JSP pages and `web.xml` configuration

## How to Run
1. `mvn clean package`
2. Deploy the `.war` file into Tomcat's `webapps` directory
3. Start Tomcat
4. Go to `http://localhost:8080/BankApp`

## Author
Project developed by Mikita Kryvenia as part of the MTPI course.