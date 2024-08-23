package com.example.upjsfd

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.upjsfd.dao.FilmDao
import com.example.upjsfd.entities.Film
import kotlin.concurrent.thread

class SearchActivity : AppCompatActivity() {
    private var isLoggedIn: Boolean = false
    private var idUser: Int =-1
    private lateinit var filmDao: FilmDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var filmAdapter: FilmAdapter


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        idUser = intent.getIntExtra("idUser",-1)
        isLoggedIn = intent.getBooleanExtra("isLoggedIn", false)
        filmDao = UPJSFdDatabase.getDatabase(this).filmDao()





        val profileButton = findViewById<ImageButton>(R.id.button2)
        val searchView = findViewById<SearchView>(R.id.searchView)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        filmAdapter = FilmAdapter(emptyList())
        recyclerView.adapter = filmAdapter
//        thread {
//            val movies = listOf(
//                Film(idFilm = 0,nazov = "Klub bitkárov", popis = "Jack, ktorý pracuje vo veľkej automobilovej spoločnosti ako koordinátor zvolávacích akcií, sa už šesť mesiacov poriadne nevyspal. Je v akomsi polospánku alebo polobdelosti a nevie, čo má robiť. Svojmu lekárovi, ktorý sa mu chystá predpísať cvičenie a valeriánske kvapky, sa zverí, že je to skutočné utrpenie. Lekár mu však odpovie, že by sa mal ísť pozrieť na mužov trpiacich rakovinou semenníkov - tí naozaj trpia. Jack teda ide na ich terapeutickú skupinu a rovnako ako oni sa vyplače na ramene jedného z chorých - Boba, z ktorého hormonálna liečba po odstránení semenníkov urobila polovičnú ženu. Postupne začne navštevovať ďalšie terapeutické skupiny, až jedného dňa stretne dievča, Marlu Singerovú, ktorá je rovnakým podvodníkom ako on. Aj ona chodí na terapiu, hoci je v poriadku. Jack si myslí, že by sa mali rozdeliť skupiny a chodiť každý len na svoju... Potom sa na služobnej ceste v lietadle zoznámi s Tylerom Durdenom, mladým mužom, ktorý vykonáva príležitostné práce - napríklad obsluhuje na banketoch - a hlavne predáva mydlo. Keď po návrate domov Jack zistí, že jeho byt vyhorel - v dôsledku výbuchu - a on nemá kam ísť, spomenie si, že si vzal Tylerovu adresu. A požiada ho, aby u neho mohol bývať. Tyler žiada len jedno: aby mu dal poriadnu ranu. Keď to Jack urobí, okamžite mu úder opätuje. A je to dobrá bitka, ktorá sa im vlastne páči: obaja sa cítia lepšie. Tyler žije v pochmúrnom starom opustenom dome, okolie je pusté a prázdne. Mladí muži sa začnú pravidelne biť a prichádza čoraz viac problémov "
//                    , rok = 1999, fotoPath = "https://m.media-amazon.com/images/M/MV5BMmEzNTkxYjQtZTc0MC00YTVjLTg5ZTEtZWMwOWVlYzY0NWIwXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_FMjpg_UX1000_.jpg"),
//                Film(idFilm = 0, nazov = "Avengers", popis = "Z nebies na Zem prichádza zlo a tajný agent Nick Fury musí zostaviť bojový tím Avengers, aby ochránil ľudstvo. Na pomoc mu prichádzajú najlepší z najlepších - superhrdinovia Iron Man, Hulk, Hawkeye, Čierna vdova, Kapitán Amerika a boh hromu Thor.", rok = 2012, fotoPath = "https://m.media-amazon.com/images/M/MV5BNDYxNjQyMjAtNTdiOS00NGYwLWFmNTAtNThmYjU5ZGI2YTI1XkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg"),
//                Film(idFilm = 0, nazov = "Temný Rytier", popis = "Mestečko Gotham City, kedysi raj zločincov, sa pomaly spamätáva z najhoršieho a začína rozkvitať, predovšetkým vďaka pomoci maskovaného netopierieho hrdinu Batmana a jeho občianskeho alter ega multimilionára Brucea Waynea (Christian Bale). Povestnou novou metlou, ktorá dobre metie, sa ukáže byť aj nový štátny žalobca Harvey Dent (Aaron Eckhart), ktorému sa zas a znova darí výrazne oslabovať svet organizovaného zločinu a vďaka ktorému sa čoraz viac mafiánov ocitá na nedobrovoľnom odpočinku - za mrežami. Harvey bojuje proti kriminalite, korupcii - a zároveň veľmi intenzívne i o priazeň Bruceovej priateľky Rachel Dawesovej (Maggie Gyllenhaal). Hoci sa možno na prvý i druhý pohľad zdá, že rezervovaný a samotársky Bruce je nad vecou a nejako výraznejšie mu to žily netrhá, v skutočnosti je všetko trochu inak... Odstup si totiž drží cielene! Tak či onak, všetko je razom nepodstatné, keď sa na scéne ako blesk z jasného neba objaví nová desivá hrozba, s ktorou nik nepočítal – šialený nekorunovaný kráľ zločincov Joker (Heath Ledger), ktorý mesto uvrhne do nového, dosiaľ nepoznaného chaosu a ktorý prinúti temného rytiera Batmana siahnuť až na dno svojich možností a temer prekročiť už skoro neexistujúcu hranicu medzi Dobrom a Zlom", rok = 2008, fotoPath = "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_.jpg"),
//                Film(idFilm = 0, nazov = "Vykúpenie z väznice Shawshank", popis = "Mladého a úspešného viceprezidenta banky Andyho Dufresnea (Tim Robbins) v roku 1946 odsúdia na doživotie za vraždu svojej manželky a jej milenca. On sám síce tvrdí, že je nevinný, ale nedokáže to na súde preukázať. Dostane sa do väznice Shawshank, kde sú všetci na doživotie. Dočká sa šikanovania, depresií, tvrdej práce, ale postupne sa adaptuje a spriatelí sa s Redom (Morgan Freeman), černošským väzňom, ktorý ako jediný priznáva, že je vinný. Po nejakom čase pomôže Andy jednému dozorcovi vyriešiť problém s daňovým priznaním. A netrvá dlho a stane sa vyhľadávaným daňovým poradcom nielen všetkých dozorcov, ale predovšetkým riaditeľa väznice, pokryteckého a zákerného Nortona (Bob Gunton). S Andyho pomocou rozbehne Norton neuveriteľné finančné machinácie a podnikateľské aktivity, na ktorých fantasticky zarába. Andy sa tak stane prominentným väzňom. Nečakaný obrat nastane vo chvíli, keď jeden z nových väzňov, mladý Tommy, rozpráva Andymu o človeku, s ktorým sedel v inej väznici, a ten sa priznal, že zavraždil Andyho ženu a jej milenca. Andy chce okamžite obnoviť svoj proces, ale narazí na tvrdý odpor riaditeľa Nortona. Ten si nemôže dovoliť o Andyho prísť, ale nielen pre svoje kšefty, ale ja zo strachu, že Andy by mohol na slobode prehovoriť o riaditeľových podvodoch. Andy sa tak znovu ocitne na dne, ale nie je taký hlúpy, ani bezmocný, ako si Norton myslí. Dokáže totiž využiť svoje schopnosti finančného poradcu najmä pre seba, a tak má teraz v rukách kľúč k veľkému majetku. Po celý čas si tiež tajne kope dieru, aby z väznice ušiel", rok = 1994, fotoPath = "https://m.media-amazon.com/images/M/MV5BNDE3ODcxYzMtY2YzZC00NmNlLWJiNDMtZDViZWM2MzIxZDYwXkEyXkFqcGdeQXVyNjAwNDUxODI@._V1_.jpg"),
//                Film(idFilm = 0, nazov = "Zelená míľa", popis = "V roku 1935 pracoval Paul Edgecomb ako vrchný dozorca v celách smrti jednej lousianskej väznici, kam priviedli nového odsúdenca, obrovského černocha Johna Coffeyho. Aj keď pôsobil mierumilovným dojmom, bol odsúdený za vraždu dvoch malých dievčat. Omnoho viac starostí ako väzni odsúdení na smrť, spôsobuje v tom čase Paulovi ťažký zápal močového mechúra. Jediná priaznivá správa je, že sadistický, všetkými nenávidený dozorca Percy plánuje podať žiadosť o preloženie, ale iba vtedy, keď bude môcť viesť nasledujúcu popravu. V tom čase je privezený ďalší väzeň, nebezpečný Bill Wharton, ktorý hneď po svojom príchode napadne strážnika. Jedného dňa chce s bolesťou zmietaným Paulom hovoriť Coffey. Keď sa dozorca priblíži, chytí ho rukou v rozkroku. Po tom, ako šokovaného Paula pustí, černoch vydýchne z úst čierny oblak. Súčasne si uvedomí, že jeho bolestivý zápal zmizol. Ďalší zázrak predvedie neskôr, keď Percy s potešením zabije ochočenú myš, ktorú choval jeden z trestancov. Svojou nadprirodzenou mocou ju pred prekvapenými strážami oživí. Pri poprave Percy zámerne nenamočí hubku, ktorá sa prikladá na odsúdencovu hlavu. Popravovaný muž sa namiesto rýchlej smrti uškvarí v strašných bolestiach. Paul ho donúti napísať žiadosť o preloženie. Keď je potom svedkom bolesti svojho priateľa, riaditeľa väznice, starajúceho sa o umierajúcu manželku s neliečiteľným mozgovým nádorom, presvedčí kolegov, aby mu pomohli dostať k trpiacej žene Johna Coffeyho, ktorý by jej možno dokázal pomôcť. Černoch ženu skutočne uzdraví. Ale vo väznici nečakane napadne Percyho a vdýchne do neho čierny oblak. Omámený dozorca vzápätí zastrelí Billa Whartona, ktorý sa ho pokúsil napadnúť. John rozpovie Paulovi pravdu. Vrahom dvoch malých dievčatiek nebol on. Zúfalý Paul to už dávno tušil. Hľadá spôsob, ako zabrániť poprave nevinného", rok = 1999, fotoPath = "https://m.media-amazon.com/images/M/MV5BMTUxMzQyNjA5MF5BMl5BanBnXkFtZTYwOTU2NTY3._V1_FMjpg_UX1000_.jpg"),
//                Film(idFilm = 0, nazov = "Forrest Gump", popis = "S Forrestom Gumpom (Tom Hanks) prežijeme tri vzrušujúce desiatky rokov - od prostého detstva, kedy malého Forresta (Michael Conner Humphreys) nechcú kvôli nízkemu IQ prijať do školy, až po absolvovanie univerzity, od telesného postihnutia až k hviezdnej kariére hráča amerického futbalu, od hrdinu z Vietnamu až po krevetového magnáta. Svoj neustálymi premenami nabitý život spojí s niekoľkými ľuďmi: s kamarátkou zo školy Jenny Curranovou (Hanna R. Hall), ktorá preňho zostala celoživotnou láskou (Robin Wright Penn). Potom s priateľom z vojny Benjaminom Bufordom „Bubbom\" Blueom (Mykelti Williamson), ktorý dáva smer jeho úvahám o love kreviet, a tiež s poručíkom Danom Taylorom (Gary Sinise), ktorému zachránil život vo Vietname. Dlhá Forrestova púť je rozprávaná nefalšovaným pohľadom hlavného hrdinu, ktorý sa stretáva so slávnymi ľuďmi - J.F. Kennedym i Johnom Lennonom - ale sám zostáva nedotknutý slávou. Forrest Gump je stelesnením éry a nevinnosti v krajine, ktorá svoju nevinnosť stráca. Svojim srdcom vycíti to, na čo jeho obmedzené IQ nestačí", rok = 1994, fotoPath = "https://m.media-amazon.com/images/M/MV5BNWIwODRlZTUtY2U3ZS00Yzg1LWJhNzYtMmZiYmEyNmU1NjMzXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_.jpg"),
//                Film(idFilm = 0, nazov = "Sedem", popis = "Detektív poručík William Somerset (Morgan Freeman) pomaly končí svoju kariéru a jeho poslednou úlohou je zaučiť mladého a ambiciózneho detektíva Davida Millsa (Brad Pitt). Obaja sú nasadení na vyšetrovanie prípadu brutálnej vraždy, ktorý ich neodvolateľne vtiahne do pomýleného a zvráteného sveta psychicky narušeného, ale inteligentného a rafinovaného zločinca, s krycím menom Johna Doe. Vražedný maniak v besnom zabíjaní pokračuje - už na druhý deň sa nájde zohavené telo najlepšieho advokáta v meste, vedľa ktorého je krvou napísané slovo „chamtivosť“. Zdá sa, že to nie je posledná obeť a kým polícia vraha vypátra, bude každý zo siedmich smrteľných hriechov - obžerstvo, chamtivosť, lenivosť, smilstvo, pýcha, závisť a hnev - kruto potrestaný", rok = 1995, fotoPath = "https://m.media-amazon.com/images/M/MV5BZDgyZmY5MmItY2I3Ny00NjA4LWFhNmItMGQ4ZGJhZDk5YjU3XkEyXkFqcGdeQXVyMTAzMDM4MjM0._V1_.jpg"),
//                Film(idFilm = 0, nazov = "MIKI", popis = "MIKI je príbeh muža, ktorý sa vráti zo zahraničia naspäť do svojej rodnej dediny, do krajiny, ktorá sa rýchlo mení a v ktorej sa otvárajú nové veľké možnosti aj v oblasti organizovaného zločinu. Miki, pôvodným povolaním šofér autobusu, v sebe postupne objaví a aktivuje schopnosti na to, aby vybudoval jednu z najväčších a najobávanejších mafiánskych skupín v krajine", rok = 2024, fotoPath = "https://www.cine-max.sk/fileadmin/user_upload/Miki_792x1080.jpg"),
//                Film(idFilm = 0, nazov = "Vlny", popis = "Praha 1967 – Tomáš vychováva svojho mladšieho brata Pavla, sám sa o politiku nezaujíma a snaží sa aj brata od nej držať čo najďalej. Za podivných okolností sa Tomáš nechá naverbovať ako technik do prodemokratickej zahraničnej redakcie Československého rozhlasu, tá získava stále väčší vplyv na spoločnosť a Tomáš sa tým dostáva do nebezpečnej situácie. Film mapuje malú komunitu vo vnútri vysielacích štúdií, ktorá prostredníctvom mikrofónu menila spoločnosť", rok = 2024, fotoPath = "https://www.cine-max.sk/fileadmin/_processed_/2/4/csm_Vlny_Webposter_Cinemax_792x1080_SK_97e54392c7.jpg")
//
//
//
//
//
//            )
//
//
//
//
//            movies.forEach { filmDao.insertFilm(it) }
//        }

        loadAllFilms()





        filmAdapter.setOnItemClickListener { selectedFilm ->
            val intent = Intent(this, FilmDetailActivity::class.java)
            intent.putExtra("film_id", selectedFilm.idFilm)
            intent.putExtra("isLoggedIn", isLoggedIn)
            intent.putExtra("idUser",idUser)

            startActivity(intent)
        }

        profileButton.setOnClickListener {
            if (isLoggedIn) {
                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra("isLoggedIn", isLoggedIn)
                intent.putExtra("idUser",idUser)
                startActivity(intent)
            } else {
                showLoginDialog()
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchFilmsByTitle(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchFilmsByTitle(it) }
                return false
            }
        })
    }
    private fun loadAllFilms() {
        thread {
            val films = filmDao.getAllFilms()
            runOnUiThread {
                filmAdapter.updateFilms(films)
            }
        }
    }

    private fun searchFilmsByTitle(query: String) {
        thread {
            val films = filmDao.searchFilmsByTitle(query)
            runOnUiThread {
                filmAdapter.updateFilms(films)
            }
        }
    }



    private fun showLoginDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Nie ste prihlásený")
        builder.setMessage("Chcete sa vrátiť na login stránku?")

        builder.setPositiveButton("Áno") { dialog, _ ->
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            dialog.dismiss()
        }

        builder.setNegativeButton("Nie") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }




}
