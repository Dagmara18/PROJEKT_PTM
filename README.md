# PROJEKT_PTM
DOKUMENTACJA TECHNICZNA PROJEKTU APLIKACJI MOBILNEJ – MAPOWANIE TERENU 

1. Opis ogólny 

Aplikacja mobilna służąca do zbierania danych przestrzennych w terenie, z funkcją zapisu punktów topograficznych, opisu lokalizacji, wykonania zdjęcia, tworzenia wielokątów i eksportu danych do plików CSV. System wykorzystuje mapę (OSMDroid + WMS), lokalizację GPS, kamerę i pamięć urządzenia. 

2. Moduły funkcjonalne 

A. Moduł Mapy (OSMDroid + WMS) 

•	Inicjalizacja komponentu mapy w MainActivity. 

•	Dodanie warstwy WMS jako warstwy zdalnej do OSMDroid. 

•	Obsługa przybliżenia do bieżącej lokalizacji użytkownika za pomocą GPS. 

B. Moduł Lokalizacji 

•	Użycie LocationManager + LocationListener. 

•	Nasłuchiwanie zmiany lokalizacji  

•	Po kliknięciu „Zapisz punkt”: 

	Odczyt współrzędnych (X, Y, Z – jeśli dostępna). 

	Otwarcie formularza opisu punktu. 

C. Moduł Punktów i Opisu Topograficznego 

•	Formularz dodawania punktu zawiera: 

	Numer punktu (wprowadzany przez użytkownika). 

	Opis lokalizacji (np. „środek studzienki”). 

	Możliwość zrobienia zdjęcia z kamery. 

•	Dane zapisywane lokalnie: 

	Zdjęcie jako numer_punktu.jpg. 

	CSV jako numer_punktu.csv zawierający: numer, opis, data, X, Y, Z. 

D. Moduł Tworzenia Wielokąta 

•	Wyświetlenie listy zapisanych punktów. 

•	Graficzne rysowanie wielokąta (punkty + linie łączące). 

•	Aktywacja przycisku „Oblicz pole” po dodaniu min. 3 punktów. 

•	Obliczanie pola powierzchni. 

•	Możliwość wyboru jednostki powierzchni: m², a, ha. 

E. Moduł Eksportu 

•	Eksport wielokąta do pliku CSV: numer_punktu,X,Y. 

•	Eksport każdego punktu jako osobny CSV z opisem. 

•	Pliki zapisywane lokalnie w folderze Documents/Punkty/. 

3. Struktura bazy danych (SQLite) 

Kolumna	Typ danych	Opis 

id	INTEGER	Identyfikator punktu 

nazwa	TEXT	Nazwa/numer punktu 

opis	TEXT	Opis punktu 

data	TEXT	Data dodania 

x	REAL	Współrzędna X (longitude) 

y	REAL	Współrzędna Y (latitude) 

z	REAL / NULL	Wysokość (jeśli dostępna) 

nazwa_zdjecia	TEXT	Nazwa pliku zdjęcia 

 

4. Wymagane uprawnienia (permissions) 

•	ACCESS_FINE_LOCATION – lokalizacja GPS. 

•	CAMERA – wykonywanie zdjęć. 

•	WRITE_EXTERNAL_STORAGE – zapis CSV i zdjęć w pamięci urządzenia. 

 

5. Widoki i UI (kluczowe elementy interfejsu) 

•	MainActivity: mapa + przyciski akcji (dodaj punkt, lokalizuj, rysuj wielokąt, eksport). 

•	Formularz punktu: numer, opis, zdjęcie. 

•	Lista punktów (RecyclerView): wyświetlanie zapisanych punktów. 

•	Podgląd wielokąta: linie i markery na mapie. 

•	Dialog wyboru jednostek pola powierzchni. 

 

6. Etapy realizacji projektu  

1	Stworzenie repozytorium (np. GitHub) – Dagmara Wancel 

2	Konfiguracja projektu + dodanie OSMDroid i warstwy WMS – Julia Mioduszewska, Dagmara Wancel 

3	Implementacja modułu lokalizacji GPS – Julia Mioduszewska 

4	Dodanie formularza opisu punktu – Małgorzata Chmolowska 

5	Obsługa zdjęcia + zapis CSV i zdjęcia – Małgorzata Kubarska 

6	Widok listy punktów + rysowanie wielokąta – Dagmara Wancel  

7	Obliczanie pola powierzchni  - Małgorzata Chmolowska 

8	Eksport danych do CSV – Julia Mioduszewska 

9	Dokumentacja końcowa  - Małgorzata Kubarska 

 

 
