# Symulacja MPK Wrocław

## Opis projektu

Projekt został stworzony w ramach zajęć **Programowania Obiektowego** i stanowi symulację działania Miejskiego Przedsiębiorstwa Komunikacyjnego (MPK) we Wrocławiu. Symulacja uwzględnia autobusy, tramwaje, pasażerów, kontrolerów oraz przystanki. Projekt napisany został w języku Java z użyciem paradygmatu programowania obiektowego oraz wykorzystuje pliki CSV do importu i eksportu danych.

## Autorzy

- Jakub Besz — 284441  
- Miłosz Osadczuk — 284604  

**Prowadzący:** Mgr inż. Tobiasz Puślecki

## Funkcjonalności

- Symulacja tras pojazdów (autobusów i tramwajów)
- Pasażerowie wsiadający i wysiadający na przystankach według prawdopodobieństwa
- Obecność kontrolerów sprawdzających bilety
- Obsługa plików CSV do ładowania tras
- Raportowanie liczby pasażerów i gapowiczów

## Struktura klas

- `Stop` – przystanek z nazwą
- `Vehicle` – pojazd transportowy (bazowa klasa dla `Bus` i `Tram`)
- `Passenger` – pasażer z celem podróży i biletem
- `Controller` – kontroler sprawdzający bilety
- `CsvLoader` – ładowanie danych z plików CSV
- `Simulation` – główna klasa symulująca przebieg
- `Person` – generator pasażerów na podstawie prawdopodobieństwa
- `Main` – uruchomienie programu

## Technologie

- Java (programowanie obiektowe)
- Obsługa plików CSV

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

## Wymagania

- Java 11 lub nowsza
- System operacyjny: dowolny (Windows/Linux/macOS)

## Licencja

Projekt edukacyjny — brak oficjalnej licencji.
