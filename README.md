# TeemaSecond
Teema 2:n kehitys

Tällähetkellä pyritään asettamaan 15 joukkuetta 36 kierrokselle (210 peliä). Tämänhetkinen toteutus sallii yhden joukkueen poissaolon kierrokselta, mutta tästähuolimatta virheitä esiintyy edelleen. 

**Major update:**
Keskustelu Kynkään kanssa selvensi toteutusta huomattavasti ja aloin toteuttamaan "siirtoketju- funktiota" (_MM.BeginMoveChain_). Tämä funktio tekee 5-10 hyppyä, missä tutkitaan aiheutuneita kierrosvirheitä ja valitaan kierrokselta huonoiten sopiva ottelu seuraavaan hyppyyn. Kun hypyt loppuvat niin valitaan taas satunnainen ottelu mistä aloitetaan uusi siirtoketju

_Vieras ja kotiestoista on nyt ensimmäinen toteutus MM.ArrangeHomeAndVisitLocks(); ei kunnioita lukituksia, mutta toimii siitä huolimatta oudon hyvin, imaisee tosin vierasestojen kierrokset tyhjäksi/vähiin._

_Lukitut-, lisä- ja lukitutlisäpelit asettuvat nyt alustuksessa._
~~Yksi lisäpeli on lukittu kahdelle eri kierrokselle eli se nostaa kokonaiskierroslukumäärää yhdellä 226:n~~ Ei pidä paikkaansa, kyseessä oli ajatusvirhe.


Rajoitteet:
http://web.samk.fi/staff/jari.kyngas/teemat/Rajoitteet_2.txt


Jos haluat ajaa Teema 1:n niin:
- aseta TeemaFAS:ssa joukkueet 10:een ja kierrokset 18
- vaihda TeemaFAS:sta while loopin virheet > 1 
- kommentoi initializeList.javasta pelien lukitus silmukka ja lisäpelit silmukka (jos se on auki)


**Tehtävä - Jari Kyngäs**
Sisältö ja ohjeita

Kurssilla koodataan otteluohjelman tekevä algoritmi. Tärkeintä on oppia tämän tyyppisen algoritmin rakennusperiaatteet: ongelman kuvaus (miten asia esitetään ohjelmallisesti), perusratkaisu (ratkaisun etsimisen periaatteet), ratkaisun hyvyyden arviointi (kustannus-funktion määrittely), rajoitteet (mitä optimoidaan), rajoitteiden sisältöjen päivittäminen...

Kaikki Teemat suoritetaan tehden samaa ohjelmaa. Kielen voitte valita itse, mutta koodista on oltava saatavissa suorituskelpoinen ohjelma ilman mitään lisäasennuksia. Mikäli ohjelmanne vaatii jotain paketteja tai ympäristöjä toimiakseen, niin ne on pakattava mukaan palautettavaan pakettiin ja mukaan on laitettava dokumentaatio, jossa kerrotaan miten homma saadaan toimimaan. Mikäli ohjelmanne vaatii käynnistyksen yhteydessä jotain parametrejä niiden tarkoitus ja muoto on kerrottava. Minun koneelta löytyy vain ja ainoastaan Java joten jos sillä teette, niin palautukseen riittä pelkkä jar-tiedosto.

Ohjelman suoritusajalla ei ole isompaa merkitystä. Suoritusaikaa pitää voida säädellä joko kellonajalla tai kierrosmäärällä. Paras ratkaisu on varmastkin kierrosmäärä, koska sillä saadaan ohjelma aina pyörimään ns. loppuun asti. Muistakaa kuitenkin, että tarkoituksena ei missään nimessä ole saada aikaiseksi mahdollisimman nopeaa ohjelmaa. Tarkoituksena on löytää paras mahdollinen ratkaisu säällisessä ajassa.

Suorituskelpoisen ohjelman tulee vähintäänkin tulostaa kaikkien virheiden määrä sopivin väliajoin. Jokainen virhe on syytä on laskea omanaan – niitä ei siis missään tapauksessa pidä mennä yhdistelemään. Paras on tulostaa kaikkien virheiden summa sekä kaikki yksittäiset virheet erillisinä.
Opintojaksojen suorittaminen
Jokainen alla kerrottu vaihe vastaa yhden teeman suorittamista.

- Ohjelma osaa etsiä ratkaisua sen perusteella, että jokaisella joukkueella on tasan yksi peli per kierros. Kierroksia otteluohjelmassa on tässä vaiheessa tasan vaadittava minimimäärä. Joukkueita on oltava 10 kpl. Otteluohjelmassa jokainen joukkue kohtaa jokaisen joukkueen kaksi kertaa. Kyseessä on siis 2RR (Round Robin).
 - Opettajalle on palautettava suorituskelpoinen ohjelma sekä yksi muodostettu otteluohjelma (selkeä tekstitiedosto, jossa on lueteltu kaikki pelit pelipäivineen).
 - Otteluohjelmaan lisätään kierroksia todellisuutta (Liiga, 2016 syksy) vastaava määrä. Joukkuemäärä kasvatetaan 15:een. Otteluohjelmaan lisätään myös rajoitteita, kiinnitettyjä pelejä, vieraskiertueita ja 15 lohkopeliä. Ohjelma osaa etsiä ratkaisua em. seikat huomioon ottaen siten, että jokaisella joukkueella on 0–1 peliä per kierros. Tämä tuo ohjelmaan toisen rajoitteen eli joukkue ei voi pelata kotona, jos sen kotihalli on muussa käytössä. Joukkue ei voi tietenkään pelata vierassakaan, jos sellainen esto on laitettu. Se on kuitenkin käytännössä sama rajoite kuin kotiesto eli sitä ei tarvitse käsitellä mitenkään eri tavalla. Vierasestolla pyritään siihen, että joukkue pelaa kotona tai ei ollenkaan.

Kohdan kaksi rajoitteet. Huomauttakaa heti, jos havaitsette datassa minkäänlaista epäselvyyttä.
Opettajalle on palautettava suorituskelpoinen ohjelma sekä yksi muodostettu otteluohjelma.
Kohdassa 2 aikaansaatuun ohjelmaan lisätään breakkien minimointi. Opettajalle on palautettava suorituskelpoinen ohjelma sekä yksi muodostettu otteluohjelma. Tämän lisäksi on palautettava koodi, joka laskee ja ylläpitää breakit. Koodi on dokumentoitava hyvin.

Kohdissa 1 ja 2 on mahdollista aikaansaada täydellinen sarjaohjelma. Se tarkoittaa sitä, että otteluohjelmassa ei ole yhtään rajoitteiden mukaista virhettä. Jokainen joukkue pelaa siis 0–1 kertaa kierroksella sekä koti- ja vierasestot toteutuvat. Ja kaikki kiinnitetyt pelit ovat tietysti kiinnitetyt. Kohtien 1 ja 2 rajoittet ovat hard-rajoitteita eli niiden on aina mentävä nolliksi.

Kohdassa 3 breakkien minimääräksi sain omissa testeissäni 64-70 kpl. Ja tämä tietenkin niin, että kohtien 1 ja 2 rajoitteissa ei ollut yhtään virhettä. Breakkeja ei kuitenkaan voi saada koskaan nollille, joten tämä rajoite on soft-rajoite.

