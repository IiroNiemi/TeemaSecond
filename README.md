# TeemaSecond
Teema 2:n kehitys

Tällähetkellä pyritään asettamaan 15 joukkuetta 36 kierrokselle (210 peliä). Tämänhetkinen toteutus sallii yhden joukkueen poissaolon kierrokselta, mutta tästähuolimatta virheitä esiintyy edelleen. 

Vieras ja kotiestoista on nyt ensimmäinen toteutus MM.ArrangeHomeAndVisitLocks(); ei kunnioita lukituksia, mutta toimii siitä huolimatta oudon hyvin, imaisee tosin vierasestojen kierrokset tyhjäksi/vähiin.

Lukitut-, lisä- ja lukitutlisäpelit asettuvat nyt alustuksessa. **Yksi lisäpeli on lukittu kahdelle eri kierrokselle eli se nostaa kokonaiskierroslukumäärää yhdellä 226:n**


Pohdintoja:
- Parittomuus; Pitääkö pitää kirjaa siitä mille joukkueelle on sallittu poissaolo ja millä kierroksella? Onko jotain muita toimenpiteitä mitä en ole tajunnut ottaa huomioon?


Rajoitteet:
http://web.samk.fi/staff/jari.kyngas/teemat/Rajoitteet_2.txt


Jos haluat ajaa Teema 1:n niin:
- aseta TeemaFAS:ssa joukkueet 10:een ja kierrokset 18
- vaihda TeemaFAS:sta while loopin virheet > 1 
- kommentoi initializeList.javasta pelien lukitus silmukka ja lisäpelit silmukka (jos se on auki)
