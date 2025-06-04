# MPK Wrocław – Symulacja Ruchu Komunikacji Miejskiej

## Opis projektu

Projekt stanowi pełnoprawną symulację działania Miejskiego Przedsiębiorstwa Komunikacyjnego (MPK) we Wrocławiu, umożliwiając analizę przepływu pasażerów, tras pojazdów, sprzedaży biletów i efektywności kontroli. Aplikacja została stworzona w języku **Java** w oparciu o zasady **programowania obiektowego (OOP)**, z wykorzystaniem **JavaFX** do interfejsu graficznego oraz **CSV** jako formatu danych wejściowych i wyjściowych.

Symulacja umożliwia działanie zarówno w trybie tekstowym, jak i graficznym, w pełni konfigurowalnym przez użytkownika.

---

## Autorzy

- **Jakub Besz** — 284441
- **Miłosz Osadczuk** — 284604

**Prowadzący:** Mgr inż. Tobiasz Puślecki

---

## Kluczowe funkcjonalności

- Symulacja tras autobusów i tramwajów z przystankami
- Dynamiczne tworzenie pasażerów na podstawie popularności przystanków
- Losowe pojawianie się kontrolerów biletów w pojazdach
- Rejestracja biletów, mandatów i łącznych zarobków MPK
- Profesjonalne graficzne GUI z animowanym grafem trasy i raportami
- Eksport wyników do plików CSV

---

## Struktura projektu

Projekt podzielony jest na moduły i warstwy, zgodnie z dobrymi praktykami projektowania:

### `model` – Logika domenowa
- `Station` – reprezentuje przystanek z nazwą i popularnością
- `Passenger` – dane pasażera: cel podróży, status biletu
- `Vehicle` (bazowa) – wspólna dla `Bus` i `Tram`, przechowuje trasę i pasażerów
- `Controller` – sprawdza bilety i identyfikuje gapowiczów

### `engine` – Mechanika symulacji
- `Simulation` – centralna klasa uruchamiająca logikę symulacji

### `io` – Obsługa danych
- `CsvLoader` – ładowanie danych z CSV (trasy, pojazdy, pasażerowie)
- `CsvSaver` – zapis wyników symulacji do CSV

### `gui` – Interfejs graficzny (JavaFX)
- `GuiLauncher` – główne okno GUI z panelem kontrolnym i postępem symulacji
- `GraphPane` – animowany graf przystanków i tras
- `SummaryDialog` – szczegółowe podsumowanie: bilety, mandaty, kontrole

---

## Interfejs graficzny (JavaFX)

GUI zapewnia:
- Wybór liczby pojazdów i trybu symulacji
- Wizualizację tras pojazdów jako grafu przystanków
- Statystyki czasu rzeczywistego:
   - liczba pasażerów
   - postęp trasy
   - liczba biletów i mandatów
- Końcowe podsumowanie:
   - bilety per pojazd
   - liczba złapanych gapowiczów per przystanek
   - całkowite zarobki
- Przycisk zakończenia symulacji i zamknięcia programu

---

## Technologie

- Java 11+
- JavaFX (GUI)
- CSV I/O
- Paradygmat: Obiektowy

---

## Wymagania systemowe

- Java 11 lub nowsza
- JavaFX SDK (np. JavaFX 21)
- Windows / macOS / Linux
- IDE (np. IntelliJ IDEA) zalecane do uruchamiania GUI

---

## Uruchamianie projektu

### Tryb tekstowy

```bash
javac Main.java
java Main
```

### Tryb graficzny (GUI)

1. Pobierz JavaFX SDK:  
   https://gluonhq.com/products/javafx/

2. W IntelliJ:
   - File → Project Structure → Libraries → dodaj `lib` z JavaFX
   - Run → Edit Configurations → VM options:

     ```
     --module-path "C:\\javafx-sdk-21\\lib" --add-modules javafx.controls,javafx.fxml
     ```

3. Uruchom klasę `GuiLauncher`.

---

## Dane wejściowe (CSV)

- `routes.csv` – trasy z nazwami przystanków i popularnością
- `vehicles.csv` – pojemności pojazdów
- `names.csv`, `surnames.csv` – dane pasażerów

---

## 🧪 Dane wyjściowe

- `inspection_*.csv` – raporty: bilety, mandaty, kontrole
- Dane prezentowane w GUI: tabele, grafy, podsumowania

---

## Licencja

Projekt edukacyjny. Dozwolone użycie w celach naukowych, dydaktycznych i niekomercyjnych.

