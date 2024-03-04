package com.example.vexaplicao2

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.example.vexaplicao2.databinding.ActivityPrincipalBinding
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.serialization.Serializable
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Principal : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    private lateinit var binding: ActivityPrincipalBinding
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var map: GoogleMap
    private lateinit var lastLocation: Location
    private val permissionCode =1
    companion object{
        private const val LOCATION_REQUEST_CODE = 1
    }



    private val places = arrayListOf(
        Place("Pingo Doce Carlos Mardel", LatLng(38.7340809,-9.133378),"R. Carlos Mardel 12","1900-122","CCS2 & CHAdeMO", "3"),
        Place("Hotel Travel Park Lisboa", LatLng(38.7271382,-9.1373133),"Av. Alm. Reis 64","1150-020", "CHAdeMO & CCS2","3"),
        Place("Hotel Travel Park Lisboa", LatLng(38.7271382,-9.1373133),"Av. Alm. Reis 64","1150-020", "TYPE2","2"),
        Place("Alameda Dom Afonso Henriques", LatLng(38.7374043,-9.1351184),"Alameda Dom Afonso Henriques","1000-125", "TYPE2","2"),
        Place("R. Alves Redol", LatLng(38.7365007,-9.1414391),"R. Alves Redol","1000-029", "TYPE 2 ","2"),
        Place("KLC Charging Station", LatLng(38.7379657,-9.1405744),"R. Alves Redol 7C","1049-001", "TYPE 2","2"),
        Place("Av. de António José de Almeida", LatLng(38.7382815,-9.1413488),"Av. de António José de Almeida 28A","1000-021", "TYPE 2","2"),
        Place("Praça de Londres", LatLng(38.7396857,-9.1376345),"Parque Praça de Londres Telpark by Empark","1000-000", "TYPE 2 ","2"),
        Place("Praça de Londres", LatLng(38.7401948,-9.1368325),"Praça de Londres 10 B","1000-192", "TYPE 2 ","2"),
        Place("Instituto Superior Tecnico", LatLng(38.737216, -9.139005),"Av. Rovisco Pais 1","1049-001", "TYPE 2 ","2"),
        Place("Edificio Atrium Saldanha", LatLng(38.7329841,-9.1454947),"Praça Duque de Saldanha 1","1050-094", "TYPE 2 ","2"),
        Place("Edificio Atrium Saldanha ", LatLng(38.7329841,-9.1454947),"Praça Duque de Saldanha 1","1050-094", "CCS2 & CHAdeMO & TYPE2","4"),
        Place("Empark Saldanha Residence", LatLng(38.7317411,-9.1472002),"Av. Fontes Pereira de Melo","1050-000", "TYPE 2 ","2"),
        Place("Parque Estacionamento Saldanha", LatLng(38.7319167,-9.1472255),"R. Latino Coelho,Lisboa","1050-135", "TYPE 2 ","2"),
        Place("Parque Arco do Cego - Telpark", LatLng(38.7363883,-9.1436078),"Av. João Crisóstomo","1000-178", "TYPE 2 ","2"),
        Place("MOBI.E Charging Station", LatLng(38.7371572,-9.1280937),"R. Cristóvão Falcão 26","1900-167", "TYPE 2 ","1"),
        Place("Estacionamento C. M. da Pátria", LatLng(38.7213921,-9.1443789),"Campo dos Mártires da Pátria","1169-017", "TYPE 2 ","2"),
        Place("Lgo do Mastro", LatLng(38.7218661,-9.1412937),"Lgo do Mastro 29B","1150-000", "TYPE 2 & CCS2 & CHAdeMO","3"),
        Place("Travessa do cidadao joao gonçalves", LatLng(38.7213263,-9.137648),"Tv. Cidadão João Gonçalves 17","1150-017", "TYPE 2 ","2"),
        Place("Continente Bom Dia Rua da Palma", LatLng(38.7371572,-9.1280937),"R. da Palma 265 287","1100-089", "TYPE 2 ","2"),
        Place("Praça Martim Moniz (Hotel Mundial)", LatLng(38.7150513,-9.1395574),"Praça Martim Moniz 2","1100-341", "TYPE 2 ","1"),
        Place("Largo da Boa Hora", LatLng(38.709879,-9.1410122),"Largo Boa Hora 1","1200-300", "TYPE 2 ","2"),
        Place("Rua do Comércio", LatLng(38.708445,-9.1399172),"R. do Comércio","1100-038", "TYPE 2 ","2"),
        Place("Praça do Município", LatLng(38.7080397,-9.1408732),"Praça do Município","1200-005", "TYPE 2 ","2"),
        Place("Rua Antonio Maria Cardoso", LatLng(38.7090356,-9.1449146),"R. António Maria Cardoso 42","1200-027", "TYPE 2 ","2"),
        Place("Praça Luis de Camoes", LatLng(38.7108195,-9.144485),"Praça Luís de Camões 43","1200-243", "TYPE 2 ","2"),
        Place("Praça Dom Luis I", LatLng(38.7070344,-9.1471865),"Praça Dom Luís I 44","1200-148", "TYPE 2 ","2"),
        Place("Avenida 24 de Julho", LatLng(38.706585,-9.1459447),"Av. 24 de Julho","1200-479", "TYPE 2 ","2"),
        Place("Hotel Lumiares Lisboa", LatLng(38.7138184,-9.1451198),"R. do Diário de Notícias 138-142","1200-211", "CCS2 ","4"),
        Place("Avenida da Liberdade", LatLng(38.7164062,-9.1435245),"Av. da Liberdade 45-1","1250-001", "TYPE 2 ","2"),
        Place("Praça dos Restauradores", LatLng(38.7170775,-9.1446091),"Av. da Liberdade 45","1250-001", "TYPE 2 ","2"),
        Place("Bessa hotel Liberdade - Tesla", LatLng(38.7170007,-9.1435321),"Av. da Liberdade 37","1250-001", "TYPE 2 & TYPE 2 - Tesla ","2"),
        Place("Rua viriato", LatLng(38.730563,-9.1481314),"R. Viriato 13","1700-008", "TYPE 2 ","2"),
        Place("Pingo Doce Tomas Ribeiro", LatLng(38.7315114,-9.1519835),"R. Tomás Ribeiro 97","1050-227", "TYPE 2 & CCS2 & CHAdeMO","3"),
        Place("Palacio Sottomayor", LatLng(38.7285649,-9.1514092),"Av. Fontes Pereira de Melo 16","1050-208", "TYPE 2 ","2"),
        Place("Palacio Sottomayor", LatLng(38.7288831,-9.1526219),"Av. Fontes Pereira de Melo 16","1050-208", "TYPE 2 & CCS2 & CHAdeMO ","3"),
        Place("Avenida Fontes Pereira Melo", LatLng(38.7275021,-9.1487771),"Av. Fontes Pereira de Melo 8G","1050-121", "TYPE 2 ","2"),
        Place("H10 Duque de Loule-Tesla", LatLng(38.726421,-9.1477427),"Av. Duque de Loulé 83","1050-089", "TYPE 2 & TYPE 2 - Tesla","2"),
        Place("Rua santa marta", LatLng(38.7233082,-9.1462855),"Tv. Santa Marta 56","", "TYPE 2 ","2"),
        Place("Rua santa marta - mobi", LatLng(38.7234218,-9.1448063),"R. de Santa Marta 48","", "TYPE 2 ","2"),
        Place("Hotel Ramada", LatLng(38.7395974,-9.1249354),"Av. Eng. Arantes e Oliveira 9","1900-221", "TYPE 2 & TYPE 2 - Tesla","2"),
        Place("Avenida Almirante Gago Coutinho", LatLng(38.7485624,-9.1301241),"Av. Alm. Gago Coutinho 64","1700-031", "CHAdeMO & CCS2 & Type2 ","3"),
        Place("Parada Alto de São João", LatLng(38.7290094,-9.1232414),"Parada Alto de São João 16","1900-050", "TYPE 2 & cee 2p+E","1"),
        Place("Avenida Carlos Pinhão", LatLng(38.741353,-9.1195917),"Av. Carlos Pinhão","1900-804", "TYPE 2 & CHAdeMO & CCS2","3"),
        Place("Lidl Xabregas", LatLng(38.7246963,-9.1133766),"R. Bispo de Cochim Dom Joseph 215","1900-439", "TYPE 2 & CHAdeMO & CCS2","3"),
        Place("Avenida Santo Condestavel", LatLng(38.749877,-9.1171047),"Av. República da Bulgária 25","1950-284", "TYPE 2 & CCS2 & CHAdeMO ","3"),
        Place("Avenida Santo Condestavel", LatLng(38.749166,-9.1173117),"Av. República da Bulgária 25","1950-284", "TYPE 2 & CCS2 & CHAdeMO","3"),
        Place("Rua do Beato", LatLng(38.7343632,-9.1058469),"Rua do Beato 41","1950-109", "TYPE 2 ","1"),
        Place("Avenida Doutor Augusto de Castro", LatLng(38.753626,-9.1144657),"Av. Dr. Augusto de Castro","1950-244", "TYPE 2 ","1"),
        Place("Rua Conselheiro Emídio Navarro", LatLng(38.756777,-9.1157307),"R. Conselheiro Emídio Navarro","", "TYPE 2 ","2"),
        Place("McDonalds Chelas", LatLng(38.7573577,-9.1133855),"Av. Dr. Augusto de Castro 1950","", "TYPE 2 & CHAdeMO & CCS2 ","3"),
        Place("Avenida Marechal Gomes da Costa", LatLng(38.7592671,-9.1160465),"Av. Mar. Gomes da Costa","1800-255", "TYPE 2 ","2"),
        Place("Rua Doutor José Espirito Santo", LatLng(38.754195,-9.1092027),"R. Dr. José Espírito Santo","1950-096", "TYPE 2 ","1"),
        Place("Avenida Marechal Gomes da Costa", LatLng(38.756513,-9.1083447),"Av. Mar. Gomes da Costa","", "TYPE 2 ","2"),
        Place("United Lisbon International School", LatLng(38.7567231,-9.1077281),"Av. Mar. Gomes da Costa 19","1800-255", "CCS2","4"),
        Place("Rua Fernando Palha", LatLng(38.742878,-9.1026227),"R. Fernando Palha 5-1","1950-131", "TYPE 2 ","1"),
        Place("Rua do Vale Formoso", LatLng(38.7439621,-9.1176821),"R. do Vale Formoso","1950-278", "TYPE 2 ","2"),
        Place("Rua do Vale Formoso de Cima ", LatLng(38.7456553,-9.1009629),"R. do Vale Formos","1950-278", "TYPE 2 ","1"),
        Place("Rua 3 da Matinha", LatLng(38.7475157,-9.0982625),"R. 3 da Matinha 4","1950-326", "TYPE 2 ","2"),
        Place("Soauto Expo", LatLng(38.7525196,-9.0960645),"R. da Cintura do Porto de Lisboa 12","", "CCS2","3"),
        Place("Concessionaria SoAuto Expo", LatLng(38.7525196,-9.0960645),"Av. Mar. Gomes da Costa 15","1800-255", "TYPE 2 & CCS2","4"),
        Place("Rua das Musas(Parque das Nações)", LatLng(38.7547774,-9.096066),"R. das Musas","1990-164", "TYPE 2 ","2"),
        Place("Hub Parque das Nações (LEVE)", LatLng(38.7547774,-9.096066),"Alameda dos Oceanos 6","1990-221", "TYPE 2 & CHAdeMO & CCS2","3"),
        Place("Alamenda dos Oceanos", LatLng(38.7777449,-9.0956788),"Alameda dos Oceanos 48","1990-069", "TYPE 2 & CHAdeMO","3"),
        Place("Rua Mario Botas", LatLng(38.758289,-9.0997597),"R. Mário Botas S/N","1998-018", "TYPE 2 ","2"),
        Place("Pingo Doce Parque das Nações Sul", LatLng(38.7585499,-9.0983052),"Av. Fernando Pessoa","1998-014", "TYPE 2 & CCS2 & CHAdeMO","3"),
        Place("Avenida Fernando Pessoa", LatLng(38.756957,-9.0991177),"Av. Fernando Pessoa","1990-605", "TYPE 2 ","1"),
        Place("Rua dos Aventureiros", LatLng(38.7573674,-9.0952652),"R. Aventureiros","1990-024", "TYPE 2 ","2"),
        Place("Rua do Pedro e Ines", LatLng(38.7605541,-9.0949493),"R. Pedro e Inês 8B","1990-075", "TYPE 2 ","2"),
        Place("Av. do Mediterranio (Cassino Lisboa)", LatLng(38.7635023,-9.0969261),"1 Rua do, Av. Mediterrâneo","1990-156", "TYPE 2 ","2"),
        Place("Rua do Mar do Norte", LatLng(38.764073,-9.0972447),"R. Mar do Norte","1990-204", "CCS2 & CHAdeMO","3"),
        Place("Prq. Est. Cassino Lisboa", LatLng(38.764109,-9.0974677),"Alameda dos Oceanos 45","1990-204", "TYPE 2 ","2"),
        Place("Rua do Pólo Sul", LatLng(38.764109,-9.0974677),"R. Polo Sul 10b","1990-221", "TYPE 2 ","1"),
        Place("Av. Dom João II 27", LatLng(38.766624,-9.0989437),"Av. Dom João II 27","1990-083", "TYPE 2 ","2"),
        Place("Passeio do Baltico", LatLng(38.766624,-9.0989437),"Passeio do Báltico 13","1990-233", "TYPE 2 ","2"),
        Place("Centro Comercial Vasco da Gama", LatLng(38.768251,-9.0977677),"Av. Dom João II 40","1990-094", "TYPE 2 ","2"),
        Place("Centro Comercial Vasco da Gama", LatLng(38.768251,-9.0977677),"Av. do Índico 10503A","1990-203", "TYPE 2 ","2"),
        Place("Melia Lisboa Oriente ", LatLng(38.7694301,-9.098619),"Av. Dom João II","1990-083", "TYPE 2 ","2"),
        Place("Hotel Melia Oriente", LatLng(38.769433,-9.0990137),"Passeio do Cantábrico","1990-083", "TYPE 2  & TYPE 2 - Tesla","2"),
        Place("Rua do Pólo Norte", LatLng(38.770087,-9.0970051),"R. Polo Norte 10","1990-265", "TYPE 2 ","1"),
        Place("Avenida Dom João II", LatLng(38.771769,-9.0984757),"Av. Dom João II","1990-083", "TYPE 2 ","2"),
        Place("Rua do Bojador (Altice Arena)", LatLng(38.769145,-9.0949492),"R. do Bojador","1990-048", "TYPE 2 ","2"),
        Place("Rua do Bojador", LatLng(38.769504,-9.0929781),"R. do Bojador 2","1990-254", "TYPE 2 ","2"),
        Place("Parque Tejo", LatLng(38.7700424,-9.0926045),"R. do Bojador 47","1990-280", "TYPE 2 ","2"),
        Place("Rua do Bojador", LatLng(38.7728591,-9.0929472),"R. do Bojador 89","1990-305", "TYPE 2 ","1"),
        Place("Rua Cais das Naus", LatLng(38.77477,-9.0930007),"Parque das Nações","1990-173", "TYPE 2 ","2"),
        Place("Supercor Expo", LatLng(38.7748228,-9.0963103),"Av. Dom João II","1990-097", "TYPE 2 ","2"),
        Place("Alameda dos Oceanos", LatLng(38.775517,-9.0959107),"Alameda dos Oceanos 44301G","1990-234", "TYPE 2 ","1"),
        Place("Avenida Dom João", LatLng(38.774797,-9.0974297),"Av. Dom João II 1","1990-097", "TYPE 2 ","2"),
        Place("Rua Ilha dos Amores", LatLng(38.782912,-9.0964447),"Rua da Ilha dos Amores","1990-371", "TYPE 2 ","1"),
        Place("Avenida dom joao II", LatLng(38.7748869,-9.0983051),"Av. Dom João II","1990-085", "TYPE 2 ","2"),
        Place("Rua Ilha dos Amores", LatLng(38.7812636,-9.0964921),"Rua da Ilha dos Amores 413","1990-160", "TYPE 2 ","2"),
        Place("Avenida General Roçadas", LatLng(38.7223896,-9.1290421)," Av. Gen. Roçadas","1170-010", "TYPE 2 ","2"),
        Place("Parque Campo das Cebolas", LatLng(38.7087596,-9.1315238),"Rua da Alfândega","1100-279", "TYPE 2 ","2"),
        Place("Largo Museu da Artilharia", LatLng(38.7122695,-9.1251391),"Largo Museu da Artilharia","1100-366", "TYPE 2 ","1"),
        Place("Campo da Santa Clara", LatLng(38.715045,-9.1244595),"Campo de Santa Clara 15","1100-471", "TYPE 2 ","1"),
        Place("Rua da Veronica", LatLng(38.7163412,-9.1286253),"R. da Voz do Operário 50","1100-621", "TYPE 2 ","2"),
        Place("P. E. Emel Chão do Loureiro", LatLng(38.7124526,-9.1351021),"Calçada do Marquês de Tancos 3A","1100-006", "TYPE 2 ","2"),
        Place("Rua Aprígio Mafra ", LatLng(38.7569489,-9.1448324),"R. João Saraiva 114","1700-111", "TYPE 2 ","2"),
        Place("Pingo Doce Joao Saraiva", LatLng(38.7569573,-9.1408167),"R. João Saraiva 11A","1700-051", "TYPE 2 & CHAdeMO & CCS2","3"),
        Place("Avenida do Brasil", LatLng(38.7582241,-9.1454519),"São João de Brito","1700-076", "TYPE 2 ","1"),
        Place("Avenida Rio de Janeiro", LatLng(38.754262,-9.1398667),"Av. Rio de Janeiro 40","1700-330", "TYPE 2 ","1"),
        Place("Avenida Estados Unidos da America", LatLng(38.7490779,-9.139027),"Av. dos Estados Unidos da América 63B","1700-177", "TYPE 2 ","2"),
        Place("Rua Manuel Gouveia", LatLng(38.7442741,-9.1309555),"R. Manuel Gouveia","1900-314", "TYPE 2 ","2"),
        Place("Rua Presidente Wilson", LatLng(38.7414869,-9.1356653),"R. Pres. Wilson 4A","1000-226", "TYPE 2 ","1"),
        Place("Est. Avenida de Roma ", LatLng(38.7433266,-9.1383188),"Jardim Fernando Pessa","1000-143", "TYPE 2 ","2"),
        Place("Avenida de Roma", LatLng(38.7433261,-9.1383188),"Av. de Roma 14D","1000-195", "TYPE 2 ","2"),
        Place("Campo Grande ", LatLng(38.748962,-9.1500127),"Campo Grande 25B","1600-036", "CHAdeMO & CCS2","3"),
        Place("Campo Grande", LatLng(38.7490671,-9.1500852),"Campo Grande 25B","1600-036", "TYPE 2 ","2"),
        Place("Campo Grande (Emel)", LatLng(38.749098,-9.1501207),"Campo Grande 25B","1600-036", "TYPE 2 & CHAdeMO & CCS2","3"),
        Place("Campo Grande", LatLng(38.749311,-9.1504477),"Campo Grande 25B","1600-036", "TYPE 2 ","1"),
        Place("Entrecampos", LatLng(38.749443,-9.1505257),"Campo Grande 25B","1600-036", "TYPE 2 ","1"),
        Place("Campo Grande (CML)", LatLng(38.749443,-9.1505257),"Campo Grande 25","1600-036", "TYPE 2 ","2"),
        Place("Rua Sanches Coelho", LatLng(38.7474089,-9.152275),"R. Sanches Coelho 2","1600-035", "TYPE 2 ","2"),
        Place("Av. Professor Anibal Bettencourt", LatLng(38.7495131,-9.1579529),"Av. Prof. Aníbal Bettencourt 9","1649-026", "TYPE 2 ","2"),
        Place("Av. Rrofessor Egas Moniz", LatLng(38.7503346,-9.1600819),"Av. Prof. Egas Moniz","1649-035", "TYPE 2 ","1"),
        Place("Rua Soeiro Pereira Gomes", LatLng(38.7449422,-9.1600289),"R. Soeiro Pereira Gomes 1600","1600-198", "TYPE 2 ","2"),
        Place("Rua Laura Alves", LatLng(38.7415682,-9.1494253),"R. Laura Alves 9","1050-053", "TYPE 2 ","2"),
        Place("Avenida de Berna 26C", LatLng(38.7404242,-9.1505885),"Av. de Berna 26C","1050-099", "TYPE 2 ","2"),
        Place("Avenida Elias Garcia", LatLng(38.739132,-9.1485087),"Av. Elias Garcia 74-86","1050-100", "TYPE 2 ","2"),
        Place("Avenida Conde Valbom", LatLng(38.738011,-9.1518857),"Av. Elias Garcia","1050-099", "TYPE 2 ","2"),
        Place("Parque Estacionamento Valbom", LatLng(38.7365576,-9.1496165),"Av. Miguel Bombarda s/n","1050-161", "TYPE 2 ","2"),
        Place("Avenida Miguel Bombarda", LatLng(38.7372334,-9.1486912),"Avenida 5 de Outubro 81","1050-161", "TYPE 2 ","2"),
        Place("Prq. Est. Valbom", LatLng(38.735244,-9.1490377),"Av. Conde Valbom 2","1050-083", "TYPE 2 ","2"),
        Place("Hotel Duque de Avila", LatLng(38.7348517,-9.1509698),"Av. Duque de Ávila","1050-125", "TYPE 2 ","2"),
        Place("Rua Pedro Nunes ", LatLng(38.7327701,-9.1480481),"R. Pedro Nunes 18H","1050-172", "TYPE 2 ","2"),
        Place("Av. Antonio Augusto Aguiar", LatLng(38.7340963,-9.153714),"Av. António Augusto de Aguiar 132","1069-413", "TYPE 2 ","2"),
        Place("El Corte Ingles", LatLng(38.7338051,-9.1542477),"Av. António Augusto de Aguiar 31","1069-413", "TYPE 2 & TYPE 2 - Tesla","2"),
        Place("El Corte Ingles Lisboa", LatLng(38.7335143,-9.1544379),"Av. António Augusto de Aguiar 31","1069-413", "TYPE 2 ","2"),
        Place("El Corte Ingles Lisboa", LatLng(38.7331817,-9.1542484),"Av. António Augusto de Aguiar 31","1069-413", "Schuko (EU PLUG) ","1"),
        Place("Hotel Real Palacio", LatLng(38.732204,-9.1535117),"R. Tomás Ribeiro 115","1050-994", "TYPE 2 ","2"),
        Place("Avenida Sidonio Pais", LatLng(38.7320001,-9.1532418),"Av. Sidónio Pais 26","1069-413", "TYPE 2 ","2"),
        Place("Rua Marques Fronteira", LatLng(38.7309474,-9.1576864),"Rua Marquês de Fronteira 31","1070-051", "TYPE 2 ","2"),
        Place("Rua Castilho", LatLng(38.7302815,-9.1583464),"Rua Marquês de Fronteira 52","", "TYPE 2 ","2"),
        Place("Lux Lisboa Park Hotel", LatLng(38.727779,-9.1585526),"R. Padre António Vieira 32","1070-197", "TYPE 2 ","2"),
        Place("Rua Castilho", LatLng(38.7258346,-9.1549926),"R. Castilho 77C","1070-051", "TYPE 2 ","2"),
        Place("Hotel Iberostar ", LatLng(38.7242266,-9.1528048),"R. Castilho 72-A","1200-000", "TYPE 2 ","2"),
        Place("Rua Braamcamp", LatLng(38.7232026,-9.1518367),"R. Braamcamp 42","1250-096", "TYPE 2 ","2"),
        Place("Praça Marques de Pombal", LatLng(38.7254309,-9.151335),"Praça Marquês de Pombal 8","1250-160", "TYPE 2 ","2"),
        Place("Prq. Est. Marques de Pombal ", LatLng(38.7257697,-9.1513881),"Alameda Edgar Cardoso","1070-051", "TYPE 2 ","2"),
        Place("Hotel Florida", LatLng(38.724108,-9.1509387),"R. Duque de Palmela 36a","1250-097", "TYPE 2 & TYPE 2 - Tesla","2"),
        Place("Rua Rodrigo da Fonseca", LatLng(38.722476,-9.1534727),"R. Rodrigo da Fonseca","1250-193", "TYPE 2 ","2"),
        Place("Rua Alexandre Herculano", LatLng(38.7222643,-9.1511445),"R. Alexandre Herculano 46","1250-048", "TYPE 2 ","2"),
        Place("Prq. de Est. A. Herculano", LatLng(38.7220905,-9.1499113),"R. Mouzinho da Silveira 12","1250-098", "TYPE 2 ","2"),
        Place("Hotel Vincci Liberdade", LatLng(38.721952,-9.1500957),"R. Mouzinho da Silveira 8-12","1250-008", "TYPE 2 & TYPE2 - Tesla ","2"),
        Place("Rua Rosa Araujo", LatLng(38.7220905,-9.1499113),"R. Rosa Araújo 16","1250-096", "TYPE 2 & CHAdeMO & CCS2","3"),
        Place("Av. Engenheiro Duarte Pacheo", LatLng(38.7246329,-9.1575124),"Av. Eng. Duarte Pacheco 15","1250-038", "TYPE 2 ","2"),
        Place("Rua Seara Nova ", LatLng(38.723592,-9.1572407),"R. Seara Nova 5-25","", "TYPE 2 ","2"),
        Place("Travessa Legua da Povoa", LatLng(38.723427,-9.1565282),"Tv. Légua da Póvoa 11C","1250-096", "TYPE 2 ","2"),
        Place("Praça Ginasio Clube Portugues", LatLng(38.721449,-9.1574997),"Praça Ginásio Clube Português 1","1250-111", "TYPE 2 ","2"),
        Place("Pingo Doce Largo do Rato", LatLng(38.7209332,-9.1550599),"Calçada Bento da Rocha Cabral 16","1250-204", "TYPE 2 & CHAdeMO & CCS2","3"),
        Place("Avenida Jose Malhoa", LatLng(38.7374282,-9.1623247),"Av. José Malhoa 8","1070-045", "TYPE 2 ","2"),
        Place("Hotel Açores Lisboa", LatLng(38.7376726,-9.1599462),"Av. Columbano Bordalo Pinheiro 3A","1070-060", "TYPE 2 ","2"),
        Place("Rua da Mesquita", LatLng(38.735559,-9.1593628),"R. Mesquita 2","1070-238", "TYPE 2 ","2"),
        Place("Hotel Corinthia Lisbon", LatLng(38.7388004,-9.1665271),"Av. Columbano Bordalo Pinheiro","1099-031", "TYPE 2 ","2"),
        Place("Rua de Campolide", LatLng(38.738687,-9.1680957),"R. de Campolide 351","1070-041", "TYPE 2 ","2"),
        Place("Shooping Galerias Twin Towers", LatLng(38.738878,-9.1688107),"R. Canto da Maya 2I","1070-033", "TYPE 2 & CHAdeMO & CCS2","4"),
        Place("Twin Towers", LatLng(38.738414,-9.1690217),"R. Canto da Maya 2H","1070-034", "TYPE 2 ","2"),
        Place("Praça Humberto Delgado", LatLng(38.7427788,-9.1682877),"Praça Marechal Humberto Delgado s/n","1500-423", "TYPE 2 ","2"),
        Place("Rua Francisco Gentil Martins", LatLng(38.7403979,-9.1750978),"R. Francisco Gentil Martins R F","1500-281", "TYPE 2 & CHAdeMO & CCS2","3"),
        Place("Rua Antonio Macedo", LatLng(38.739409,-9.1781437),"R. António Macedo","", "TYPE 2 ","1"),
        Place("Caminho das Pedreiras", LatLng(38.734796,-9.1803647),"Lisboa","", "TYPE 2 ","2"),
        Place("Rua Cecilia Meireles", LatLng(38.7438286,-9.1798967),"R. Cecília Meireles 20","1500-154", "TYPE 2 ","1"),
        Place("Rua Conde de Almoster", LatLng(38.744484,-9.1838037),"R. Conde Almoster","1500-197", "TYPE 2 ","1"),
        Place("Estrada Barcal", LatLng(38.740791,-9.1878087),"Est. do Barcal","", "TYPE 2 ","1"),
        Place("Rua Virgilio Correia", LatLng(38.751766,-9.1729494),"R. Virgílio Correia 7B","1600-141", "TYPE 2 ","2"),
        Place("Hospital Lusiadas Lisboa", LatLng(38.749415,-9.1801847),"R. Abílio Mendes 12","1500-458", "TYPE 2 ","2"),
        Place("Rua Joao Freitas Branco", LatLng(38.7512116,-9.1802714),"R. João de Freitas Branco 12","1500-714", "TYPE 2 ","2"),
        Place("Rua Frei Luis Granada", LatLng(38.7506463,-9.1813296),"R. Frei Luis de Granada 12a","1500-680", "TYPE 2 ","2"),
        Place("Avenida Machado Santos", LatLng(38.7536024,-9.1820009),"Av. Machado Santos 1500","1500-313", "TYPE 2 ","2"),
        Place("Estadio da Luz", LatLng(38.7534563,-9.1834675),"Av. General Norton de Matos 313","1500-313", "CCS2 & CHAdeMO","4"),
        Place("Estadio da Luz", LatLng(38.753316,-9.1841137),"Av. General Norton de Matos 313","1500-313", "TYPE 2 & CHAdeMO & CCS2","3"),
        Place("Estadio da Luz", LatLng(38.753316,-9.1841137),"Av. General Norton de Matos 313","1500-313", "TYPE 2 ","2"),
        Place("Av. General Norton de Matos", LatLng(38.755462,-9.1829307),"Av. General Norton de Matos","1500-313", "TYPE 2 & CHAdeMO & CCS2","3"),
        Place("Pingo Doce Torres de Luz ", LatLng(38.754727,-9.1817037),"R. Luciana Stegagno Picchio","1500-912", "TYPE 2 & CHAdeMO & CCS2","3"),
        Place("Rua Inocencio Francisco da Silva", LatLng(38.7564454,-9.1789027),"R. Inocêncio Francisco da Silva 24","1500-348", "TYPE 2 ","2"),
        Place("Rua Alferes Malheiro", LatLng(38.7592261,-9.136623),"R. Alferes Malheiro 5","1700-111", "TYPE 2 ","1"),
        Place("Avenida Marechal Craveiro Lopes", LatLng(38.7595767,-9.1537055),"Av. Mal. Craveiro Lopes","1749-009", "TYPE 2 ","2"),
        Place("Avenida Marechal Craveiro Lopes", LatLng(38.7595051,-9.1545934),"Av. Mal. Craveiro Lopes","1749-009", "TYPE 2 ","1"),
        Place("Avenida Marechal Craveiro Lopes", LatLng(38.759272,-9.156707),"Av. Mal. Craveiro Lopes","1749-009", "CHAdeMO & CCS2","3"),
        Place("Repsol 2 Circular", LatLng(38.7631074,-9.14136),"Av. Mal. Craveiro Lopes 2","1700-288", "TYPE 2 & CHAdeMO & CCS2","3"),
        Place("Estação de Serviço Repsol 2a C", LatLng(38.7631074,-9.14136),"Av. Mal. Craveiro Lopes 2","1700-288", "TYPE 2 & CHAdeMO & CCS2","3"),
        Place("Centro Comercial Amoreiras", LatLng(38.723567,-9.1618841),"Av. Eng. Duarte Pacheco","1070-103", "TYPE 2 ","2"),
        Place("Centro Comercial Amoreiras", LatLng(38.7227965,-9.1620846),"Av. Eng. Duarte Pacheco","1070-103", "TYPE 2 ","2"),
        Place("Avenida Engenheiro Duarte Pacheco", LatLng(38.7227197,-9.1614619),"Av. Eng. Duarte Pacheco","1070-103", "TYPE 2 ","2"),
        Place("Amoreiras Plaza", LatLng(38.7226892,-9.1607894),"R. Silva Carvalho 321","1250-252", "TYPE 2 ","2"),
        Place("Square Amoreiras", LatLng(38.7215407,-9.1618627),"R. Carlos Alberto da Mota Pinto 17B","1250-096", "TYPE 2 ","2"),
        Place("Espaço Amoreiras", LatLng(38.72082,-9.1608957),"Rua Dom João V 33-25","1250-091", "TYPE 2 ","2"),
        Place("Espaço Amoreiras", LatLng(38.72082,-9.1608957),"Rua D. João V","1250-091", "TYPE 2 ","2"),
        Place("Espaço Amoreiras", LatLng(38.72082,-9.1608957),"Rua Dom João V 24","1250-091", "TYPE 2 ","2"),
        Place("Espaço Amoreiras", LatLng(38.720881,-9.1603597),"Rua Dom João V 24","1250-091", "TYPE 2 ","2"),
        Place("Rua Gorgel do Amaral", LatLng(38.721723,-9.1601737),"R. Gorgel do Amaral 3-1","1250-001", "TYPE 2 ","1"),
    )



    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_principal)
        setContentView(binding.root)

        binding.btnHistorico.setOnClickListener() {
            val irHistorico = Intent(this, Historico::class.java)
            startActivity(irHistorico)
        }

        binding.btnPerfil.setOnClickListener() {
            val irPerfil = Intent(this, perfilultilizador::class.java)
            startActivity(irPerfil)
        }

        binding.btnAlert.setOnClickListener(){
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.emergencia, null)

            builder.setView(view)
            val dialog = builder.create()

            view.findViewById<Button>(R.id.btnCancelar).setOnClickListener(){
                dialog.dismiss()

            }
            if (dialog.window != null){
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

      val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
       /* mapFragment.getMapAsync(){googleMap ->
            googleMap.setInfoWindowAdapter(MarkerInfoAdapter(this))
            addMarkers(googleMap)

            googleMap.setOnMapLoadedCallback {

                val bounds = LatLngBounds.builder()
               // googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(),200))


                 places.forEach(){
                    bounds.include(it.latLng)
                }
            }
        }*/
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)
        map.setInfoWindowAdapter(MarkerInfoAdapter(this))
        addMarkers(map)

        map.setOnMapLoadedCallback {

            val bounds = LatLngBounds.builder()

            places.forEach() {
                bounds.include(it.latLng)
            }
        }
        setUpMap()
    }


    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE)
            return
        }
        map.isMyLocationEnabled = true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this) {  location ->
            if (location != null){
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
               // placeMarkerOnMap(currentLatLng)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,12f))
            }
        }
    }

    private fun placeMarkerOnMap(currentLatLng: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLng)
        markerOptions.title("$currentLatLng")
        map.addMarker(markerOptions)
    }


    private fun addMarkers(googleMap: GoogleMap){
        places.forEach() { place ->
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .title(place.name)
                    .snippet(place.address)
                    .position(place.latLng)
                    .snippet(place.cp)
            )
            marker?.tag = place
        }
    }

    //override fun onMarkerClick(p0: Marker) = false

    override fun onMarkerClick(marker: Marker): Boolean {
        val place = marker.tag as? Place
        place?.let {
            fetchDirections(LatLng(lastLocation.latitude, lastLocation.longitude), it.latLng)
        }
        return false
    }


    private fun getDirectionsUrl(from: LatLng, to: LatLng): String {
        val origin = "origin=${from.latitude},${from.longitude}"
        val destination = "destination=${to.latitude},${to.longitude}"
        val sensor = "sensor=false"
        val params = "$origin&$destination&$sensor"
        return "https://maps.googleapis.com/maps/api/directions/json?$params"
    }

    private fun fetchDirections(from: LatLng, to: LatLng) {
        val url = getDirectionsUrl(from, to)
        GlobalScope.launch {
            val result = directionClient(url)
            drawPolyline(result.toString())
        }
    }

    suspend fun directionClient(url: String): DirectionsResponse {
        val client = HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }

        val directionsResponse: DirectionsResponse = client.get(url)
        client.close()
        return directionsResponse
    }

    private fun drawPolyline(polylinePoints: String) {
        val options = PolylineOptions()
        options.color(Color.RED)
        options.width(5f)
        options.addAll(decodePolyline(polylinePoints))
        map.addPolyline(options)
    }

    private fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index =  0
        var len = encoded.length
        var lat =  0
        var lng =  0

        while (index < len) {
            var b: Int
            var shift =  0
            var result =  0
            do {
                b = encoded[index++].toInt() -  63
                result = result or ((b and  0x1f).shl(shift))
                shift +=  5
            } while (b >=  0x20)
            val dlat = if (result and  1 !=  0) (result shr  1).inv() else result shr  1
            lat += dlat

            shift =  0
            result =  0
            do {
                b = encoded[index++].toInt() -  63
                result = result or ((b and  0x1f).shl(shift))
                shift +=  5
            } while (b >=  0x20)
            val dlng = if (result and  1 !=  0) (result shr  1).inv() else result shr  1
            lng += dlng

            val p = LatLng((lat.toDouble() /  1E5), (lng.toDouble() /  1E5))
            poly.add(p)
        }

        return poly
    }

}

data class Place (
    val name : String,
            val latLng: LatLng,
                    val address: String,
                        val cp: String,
                            val tipo : String,
                                val nivel : String
    )

@Serializable
data class DirectionsResponse(val routes: List<Route>) {
    @Serializable
    data class Route(val legs: List<Leg>, val overview_polyline: OverviewPolyline)

    @Serializable
    data class Leg(val steps: List<Step>)

    @Serializable
    data class Step(val start_location: LatLng, val end_location: LatLng, val polyline: Polyline)

    @Serializable
    data class OverviewPolyline(val points: String)

    @Serializable
    data class Polyline(val points: String)

    @Serializable
    data class LatLng(val lat: Double, val lng: Double)
}