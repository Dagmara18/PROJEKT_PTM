# GeoMark

## DOKUMENTACJA TECHNICZNA APLIKACJI MOBILNEJ GEOMARK, WYKONANEJ W RAMACH PROJEKTU Z PRZEDMIOTU PROJEKTOWANIE TECHNOLOGII MOBILNYCH
---

## 1. Opis ogólny

**GeoMark** to prosta i intuicyjna aplikacja mobilna służąca do zbierania punktowych danych przestrzennych w terenie - opisów topograficznych punktów geodezyjnych. Umożliwia zapisywanie współrzędnych GPS wraz z opisem i zdjęciem lokalizacji. W aplikacji można przeglądać zapisane punkty, które pozostają zapisane również po ponownym uruchomieniu aplikacji. System wykorzystuje mapę OpenStreetMap (biblioteka **osmdroid**), lokalizację GPS, kamerę urządzenia oraz lokalną bazę danych **SQLite**.

---

## 2. Moduły funkcjonalne

### A.  Moduł mapy

- Inicjalizacja mapy osmdroid w `MainActivity`.
- Ustawienie domyślnego źródła kafelków – **TileSourceFactory.MAPNIK**.
- Automatyczne przybliżenie do lokalizacji użytkownika po uruchomieniu aplikacji.
- Obsługa markerów zapisanych punktów z bazy danych.

---

### B. Moduł lokalizacji

- Wykorzystanie `LocationManager` i `LocationListener` do nasłuchiwania zmian lokalizacji.
- Po uzyskaniu lokalizacji aplikacja automatycznie przesuwa mapę do pozycji użytkownika.
- Marker wskazujący bieżącą pozycję użytkownika.

---

### C. Moduł dodawania punktów

- Formularz umożliwiający wprowadzenie:
  - nazwy punktu (dowolny tekst),
  - opisu punktu (np. „drzewo przy drodze”),
  - wykonanie zdjęcia przy użyciu aparatu.
- Automatyczne zapisanie punktu w lokalnej bazie danych z:
  - współrzędnymi GPS,
  - datą i godziną,
  - zakodowanym zdjęciem w formacie base64.
- Dodanie markera na mapie w miejscu zapisu punktu.

---

### D. Moduł przeglądania punktów

- Widok listy wszystkich zapisanych punktów (z nazwą, opisem i zdjęciem).
- Lista dostępna poprzez przycisk „Przeglądaj punkty”.
- Nagłówki z nazwą kolumn: ID, nazwa, opis, zdjęcie.
- Możliwość powrotu do mapy przyciskiem „Powrót”.

---

## 3. Struktura bazy danych (SQLite)

| Kolumna       | Typ danych | Opis                             |
|---------------|------------|----------------------------------|
| `id`          | INTEGER    | Unikalny identyfikator punktu   |
| `nazwa`       | TEXT       | Nazwa nadana punktowi przez użytkownika |
| `opis`        | TEXT       | Opis punktu terenowego          |
| `data`        | TEXT       | Data i godzina dodania punktu   |
| `x`           | REAL       | Współrzędna X (longitude)       |
| `y`           | REAL       | Współrzędna Y (latitude)        |
| `zdjecie`     | TEXT       | Zakodowane zdjęcie (base64)     |

---

## 4. Wymagane uprawnienia

Aby aplikacja działała poprawnie, wymaga następujących uprawnień:

- `ACCESS_FINE_LOCATION` – do pozyskiwania dokładnej lokalizacji.
- `ACCESS_COARSE_LOCATION` – alternatywne źródła lokalizacji.
- `INTERNET` i `ACCESS_NETWORK_STATE` – pobieranie kafelków mapy.
- `CAMERA` – do wykonywania zdjęć punktów.

---

## 5. Widoki i interfejs użytkownika

| Widok | Opis |
|-------|------|
| `StartActivity` | Strona powitalna aplikacji z nazwą GeoMark, autorami i przyciskiem „Start” |
| `MainActivity` | Główna mapa z możliwością dodawania punktów oraz przejścia do listy punktów |
| `Formularz` | Pola do wpisania nazwy, opisu oraz wykonania zdjęcia |
| `PointListActivity` | Lista zapisanych punktów wraz z miniaturami zdjęć |
| `Markery` | Wszystkie zapisane punkty są widoczne na mapie jako markery |

---

## 6. Etapy realizacji projektu

| Etap |

| 1. Stworzenie repozytorium 
| 2. Konfiguracja projektu + OSMDroid 
| 3. Moduł lokalizacji GPS
| 4. Formularz punktu 
| 5. Obsługa zdjęć i zapis do bazy 
| 6. Lista punktów + markery
| 7. Ekran startowy + personalizacja 
| 8. Dokumentacja projektu 

---

## 7. Instalacja i uruchomienie

1. Pobierz repozytorium:
   ```bash
   git clone https://github.com/nazwa-uzytkownika/GeoMark.git
