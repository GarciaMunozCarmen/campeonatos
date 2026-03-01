package es.fplumara.dam1.campeonato.service;

import es.fplumara.dam1.campeonato.exception.DuplicadoException;
import es.fplumara.dam1.campeonato.exception.NoEncontradoException;
import es.fplumara.dam1.campeonato.exception.OperacionNoPermitidaException;
import es.fplumara.dam1.campeonato.model.Deportista;
import es.fplumara.dam1.campeonato.model.LineaRanking;
import es.fplumara.dam1.campeonato.model.Resultado;
import es.fplumara.dam1.campeonato.repository.DeportistaRepository;
import es.fplumara.dam1.campeonato.repository.ResultadoRepository;

import java.util.*;

public class CampeonatoService {
    ResultadoRepository resultadoRepo;
    DeportistaRepository deportistaRepo;

    public void registrarDeportista(Deportista d){
        if(d == null || d.getPais().isEmpty() || d.getId() == null || d.getNombre() == null || d.getPais() == null || d.getNombre().isEmpty() || d.getId().isEmpty()){
            throw new IllegalArgumentException("EL deportista no puede ser nulo ni tener ningún valor nulo");
        } else if (deportistaRepo.findById(d.getId()).isPresent()) {
            throw new DuplicadoException();
        }else{
            deportistaRepo.save(d);
        }
    }

    public void registrarResultado(Resultado r){
        if(r == null || r.getIdPrueba().isEmpty() || r.getId() == null || r.getIdDeportista() == null || r.getIdPrueba() == null || r.getIdDeportista().isEmpty() || r.getId().isEmpty()){
            throw new IllegalArgumentException("El resultado no puede ser nulo ni tener ningún id nulo");
        } else if (r.getTipoPrueba() == null) {
            throw new IllegalArgumentException("El tipo de prueba no puede ser nulo");
        } else if (r.getPosicion() <= 0) {
            throw new IllegalArgumentException("La posición no puede ser menor o igual a 0");
        } else if (resultadoRepo.listAll().contains(r)) {
            throw new DuplicadoException();
        }else if(deportistaRepo.findById(r.getIdDeportista()).isEmpty()){
            throw new NoEncontradoException();
        } else if (resultadoRepo.existsByPruebaYDeportista(r.getIdPrueba(), r.getIdDeportista())) {
            throw new OperacionNoPermitidaException("Un deportista solo puede tener 1 resultado por prueba");
        }else{
            resultadoRepo.save(r);
        }
    }

    public List<LineaRanking> ranking(){
        List<Deportista> deportistaList = deportistaRepo.listAll();
        List<Resultado> resultadoList = resultadoRepo.listAll();
        List<LineaRanking> lineaRankingList = new ArrayList<>();
        for (Deportista d : deportistaList) {
            String idDeportista = d.getId();
            int puntos = resultadoList.stream().filter(r -> r.getIdDeportista().equalsIgnoreCase(idDeportista)).mapToInt(Resultado::getPuntos).sum();
            lineaRankingList.add(new LineaRanking(idDeportista, d.getNombre(), d.getPais(), puntos));
        }
        return lineaRankingList;
        // FALTA EL ORDEN
    }

    public List<Resultado> resultadosDePais(String pais){
        List<Deportista> deportistas = deportistaRepo.listAll().stream().filter(d-> d.getPais().equalsIgnoreCase(pais)).toList();
        List<Resultado> resultados = resultadoRepo.listAll();
        List<List<Resultado>> resultadosFiltrados = new ArrayList<>();

        for (Deportista d : deportistas){
            resultadosFiltrados.add(resultados.stream().filter(r-> r.getIdDeportista().equalsIgnoreCase(d.getId())).toList());
        }

        return resultadosFiltrados.stream().flatMap(List::stream).toList();
    }

    public Set<String> paisesParticipantes(){
        Set<String> paises = new HashSet<>();
        List<Deportista> deportistas = deportistaRepo.listAll();
        for (Deportista d : deportistas){
            paises.add(d.getPais());
        }
        return paises;
    }
}
