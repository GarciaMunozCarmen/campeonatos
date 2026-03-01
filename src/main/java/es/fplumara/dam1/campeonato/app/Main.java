package es.fplumara.dam1.campeonato.app;

import es.fplumara.dam1.campeonato.io.*;
import es.fplumara.dam1.campeonato.model.Deportista;
import es.fplumara.dam1.campeonato.model.LineaRanking;
import es.fplumara.dam1.campeonato.model.Resultado;
import es.fplumara.dam1.campeonato.model.TipoPrueba;
import es.fplumara.dam1.campeonato.repository.DeportistaRepository;
import es.fplumara.dam1.campeonato.repository.DeportistaRepositoryImpl;
import es.fplumara.dam1.campeonato.repository.ResultadoRepositoryImpl;
import es.fplumara.dam1.campeonato.service.CampeonatoService;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Main de ejemplo para demostrar el flujo mínimo del examen (sin menú complejo).
 * Debe leer ficheros de entrada y escribir un fichero de salida.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Examen DAM1 - Campeonato deportivo (Java 21)");

        /*
         * FLUJO MÍNIMO (lo que debe hacer tu main)
         *
         * 1) Crear repositorios en memoria
         *    - DeportistaRepositoryImpl
         *    - ResultadoRepositoryImpl
         */

        DeportistaRepositoryImpl deportistaRepository = new DeportistaRepositoryImpl();
        ResultadoRepositoryImpl resultadoRepository = new ResultadoRepositoryImpl();

        /*
         * 2) Crear el servicio
         *    - CampeonatoService (usa ambos repositorios)
         */

        CampeonatoService campeonatoService = new CampeonatoService(resultadoRepository, deportistaRepository);

        /*
         * 3) Leer datos de ficheros (CSV recomendado)
         *    - Leer "deportistas.csv" y por cada línea crear Deportista y llamar a registrarDeportista(...)
         *    - Leer "resultados.csv" y por cada línea crear Resultado (incluyendo tipoPrueba como enum) y llamar a registrarResultado(...)
         */

        DeportistaCsvIO deportistaCsvIO = new DeportistaCsvIO();
        List<RegistroDeportistaCsv> deportistaCsv = deportistaCsvIO.leer(Path.of("data/deportistas.csv"));

        for (RegistroDeportistaCsv d : deportistaCsv){
            campeonatoService.registrarDeportista(new Deportista(d.id(), d.nombre(), d.pais()));
        }

        ResultadoCsvIO resultadoCsvIO = new ResultadoCsvIO();
        List<RegistroResultadoCsv> resultadoCsv = resultadoCsvIO.leer(Path.of("data/resultados.csv"));

        for (RegistroResultadoCsv r : resultadoCsv){
            campeonatoService.registrarResultado(new Resultado(r.id(), r.idPrueba(), TipoPrueba.valueOf(r.tipoPrueba()), r.idDeportista(), r.posicion()));
        }

        /*
         * 4) Mostrar por consola
         *    - Países participantes (Set)
         *    - Ranking (List ordenada por puntos)
         *    - Resultados de un país (List filtrada)
         */

        List<LineaRanking> ranking = campeonatoService.ranking();
        System.out.println(campeonatoService.paisesParticipantes().toString());
        System.out.println(ranking.toString());
        System.out.println(campeonatoService.resultadosDePais("ES").toString());


        /*
         * 5) Escribir salida a fichero
         *    - Generar el ranking y escribir "ranking.csv" con: idDeportista,nombre,pais,puntos
         */

        RankingCsvWriter rankingCsvWriter = new RankingCsvWriter();
        List<RegistroRankingCsv> rankingCsv = new ArrayList<>();

        for(LineaRanking r : ranking){
            rankingCsv.add(new RegistroRankingCsv(r.getIdDeportista(), r.getNombre(), r.getPais(), r.getPuntos()));
        }

        rankingCsvWriter.escribir(Path.of("data/ranking.csv"), rankingCsv);
    }
}
