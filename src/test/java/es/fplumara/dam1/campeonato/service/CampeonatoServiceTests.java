package es.fplumara.dam1.campeonato.service;

import es.fplumara.dam1.campeonato.exception.OperacionNoPermitidaException;
import es.fplumara.dam1.campeonato.model.Deportista;
import es.fplumara.dam1.campeonato.model.Resultado;
import es.fplumara.dam1.campeonato.model.TipoPrueba;
import es.fplumara.dam1.campeonato.repository.DeportistaRepository;
import es.fplumara.dam1.campeonato.repository.ResultadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CampeonatoServiceTests {
    CampeonatoService campeonatoService;
    @Mock
    DeportistaRepository deportistaRepository;
    @Mock
    ResultadoRepository resultadoRepository;

    @BeforeEach
    public void setUp() {
        campeonatoService = new CampeonatoService(resultadoRepository, deportistaRepository);
    }

    @Test
    public void regla1ResultadoPorPruebaYDeportista(){
        when(deportistaRepository.findById("D001")).thenReturn(Optional.of(new Deportista("D001", "carmen", "ES")));
        when(resultadoRepository.findById("R001")).thenReturn(Optional.empty());
        when(resultadoRepository.existsByPruebaYDeportista("P001", "D001")).thenReturn(true);
        OperacionNoPermitidaException ex = assertThrows( OperacionNoPermitidaException.class, ()-> campeonatoService.registrarResultado(new Resultado("R001", "P001", TipoPrueba.CARRERA, "D001", 1)));
        assertEquals("Un deportista solo puede tener 1 resultado por prueba", ex.getMessage());
    }
}
