# TeemaSecond
Teema 2:n kehitys

Tällähetkellä pyritään asettamaan 15 joukkuetta 36 kierrokselle (210 peliä). Tämänhetkinen toteutus sallii yhden joukkueen poissaolon kierrokselta, mutta tästähuolimatta virheitä esiintyy edelleen. 

Pohdintoja:
- Pitääkö pitää kirjaa siitä mille joukkueelle on sallittu poissaolo ja millä kierroksella?

~~Lisäpelit ja pelien lukituksen toteutukset löytyy initializeList.java:sta mutta en ole ottanut niitä vielä käyttöön ennenkuin saadaan noi joukkueet asettumaan kierroksille.~~

Lukitut-, lisä- ja lukitutlisäpelit asettuvat nyt alustuksessa. **Yksi lisäpeli on lukittu kahdelle eri kierrokselle eli se nostaa kokonaiskierroslukumäärää yhdellä 226:n**

Vieras- ja kotiestojen toteutus uupuu edelleen.

Rajoitteet:
http://web.samk.fi/staff/jari.kyngas/teemat/Rajoitteet_2.txt


Jos haluat ajaa Teema 1:n niin:
- aseta TeemaFAS:ssa joukkueet 10:een ja kierrokset 18
- vaihda TeemaFAS:sta while loopin virheet > 1 
- kommentoi initializeList.javasta pelien lukitus silmukka ja lisäpelit silmukka (jos se on auki)
