Beast

Zakladne informacie 

Tento adresar obsahuje zdrojove kody programu Beast - implementacie AUP. Je napisany v jazyku java a mal by teda byt nezavisly na platforme. Testovany bol ale iba na OS Linux (Ubuntu). Podadresar cortex obsahuje naucene korpusy v troch roznych jazykoch:

* cs_crotex - pre cesky jazyk
* sk_cortex - pre slovensky jazyk
* full_cortex - pre anglicky jazyk

Zakladne operacie:

1. Zkompilovanie programu
- Program sa skompiluje pomocou prikazu:
$ ./build.sh

2. Zkompilovanie a spustenie programu:
- Skript, ktory automaticky skompiluje a spusti graficke prostredie programu s datami naucenymi nad korpusom v slovenskom jazyku je:
$ ./run-gui.sh cortex/sk_cortex/cortex

3. Spustenie experimentu
- Experimenty su implementovane v podobe JUnit testov. Na spustenie testu sluzi prikaz run-experiment.sh, ktoremu treba predat nazov testu ako parameter. Napriklad experiment, ktory vypocita presnost a navratnost pri hladani zhluku mesiacov pre slovensky jazyk sa spusti prikazom:
$ ./run-experiment.sh FullClusterCortexTest

Vstupy, vratane naucenych fasciklov su parametrami testu a su jeho sucastou.
