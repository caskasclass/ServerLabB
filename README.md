Servizi che possono essere richiesti:
  Ricerca brano (Arraylist tracks)
    Visualizzare emozioni brano
  Ricerca playlist(ArrayList playlist)
  Creare playlist(nick, nomePlaylist, AArraylist tracks)
  Mettere emozioni ad un brano(nick, track, emozione, note)
  Registrazione(...)

INSTALLAZIONE DEL SERVER:
Dovrebbe essere tutto corretto e a posto, l'unica cosa da fare è questa:
Aprire il terminale e copiare questo comando

mvn install:install-file -Dfile=*PATH*.jar -DgroupId=org.esclasslibrary -DartifactId=esclasslibrary -Dversion=1.3 -Dpackaging=jar

Dove *PATH* è da sostituire con il filepath del file ESClassLibrary-1.3.jar che trovate nella cartella esserver/lib.
