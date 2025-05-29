# Symulacja MPK Wrocław

## Opis projektu

Projekt został stworzony w ramach zajęć **Programowania Obiektowego** i stanowi symulację działania Miejskiego Przedsiębiorstwa Komunikacyjnego (MPK) we Wrocławiu. Symulacja uwzględnia autobusy, tramwaje, pasażerów, kontrolerów oraz przystanki. Projekt napisany został w języku Java z użyciem paradygmatu programowania obiektowego oraz wykorzystuje pliki CSV do importu i eksportu danych.

## Autorzy

* Jakub Besz — 284441
* Miłosz Osadczuk — 284604

**Prowadzący:** Mgr inż. Tobiasz Puślecki

## Funkcjonalności

* Symulacja tras pojazdów (autobusów i tramwajów)
* Pasażerowie wsiadający i wysiadający na przystankach według prawdopodobieństwa
* Obecność kontrolerów sprawdzających bilety
* Obsługa plików CSV do ładowania tras
* Raportowanie liczby pasażerów i gapowiczów

## Struktura klas

* `Stop` – przystanek z nazwą
* `Vehicle` – pojazd transportowy (bazowa klasa dla `Bus` i `Tram`)
* `Passenger` – pasażer z celem podróży i biletem
* `Controller` – kontroler sprawdzający bilety
* `CsvLoader` – ładowanie danych z plików CSV
* `Simulation` – główna klasa symulująca przebieg
* `Person` – generator pasażerów na podstawie prawdopodobieństwa
* `Main` – uruchomienie programu

## Technologie

* Java (programowanie obiektowe)
* Obsługa plików CSV

## Wymagania

* Java 11 lub nowsza
* System operacyjny: dowolny (Windows/Linux/macOS)

## Uruchomienie projektu

1. Skompiluj projekt za pomocą kompilatora Javy:

   ```bash
   javac Main.java
   ```
2. Uruchom program:

   ```bash
   java Main
   ```
3. Upewnij się, że pliki CSV z trasami znajdują się w odpowiednim katalogu.

## Uruchomienie środowiska graficznego (JavaFX)

Aby uruchomić wersję graficzną symulacji, wykonaj poniższe kroki:

1. **Pobierz JavaFX SDK**

   * Przejdź na stronę:
     [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/)
   * Kliknij **Download JavaFX** i pobierz wersję zgodną z Twoim JDK (np. JavaFX 21 dla JDK 21).
   * Wybierz system operacyjny (Windows/macOS/Linux).
   * Rozpakuj pobrany plik ZIP, np. do `C:\javafx-sdk-21`.

2. **Dodaj JavaFX do projektu w IntelliJ IDEA**

   * Otwórz projekt w IntelliJ.
   * Przejdź do **File > Project Structure > Libraries**.
   * Kliknij **+** → **Java**.
   * Wskaż folder `lib` w katalogu SDK, np. `C:\javafx-sdk-21\lib`.
   * Zatwierdź i upewnij się, że nowa biblioteka jest przypisana do modułu projektu.

3. **Skonfiguruj opcje VM dla uruchomienia aplikacji**

   * Przejdź do **Run > Edit Configurations**.
   * Wybierz konfigurację uruchomieniową Twojej klasy głównej (np. `GuiMain`).
   * W polu **VM options** wklej:

     ```text
     --module-path "C:\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml
     ```
   * Upewnij się, że ścieżki wskazują na właściwy katalog, np. zmień `C:\javafx-sdk-21` na własną lokalizację JavaFX SDK.

4. **Uruchom wersję graficzną symulacji**

   * Uruchom konfigurację `GuiMain` w IntelliJ.
   * Powinno otworzyć się okno aplikacji z interfejsem graficznym.

---

## Licencja

Projekt edukacyjny — brak oficjalnej licencji.
