package es.fplumara.dam1.campeonato.model;

public class LineaRanking {
    private String idDeportista;
    private String nombre;
    private String pais;
    private int puntos;

    public LineaRanking(String idDeportista, String nombre, String pais, int puntos) {
        this.idDeportista = idDeportista;
        this.nombre = nombre;
        this.pais = pais;
        this.puntos = puntos;
    }

    public String getIdDeportista() {
        return idDeportista;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPais() {
        return pais;
    }

    public int getPuntos() {
        return puntos;
    }
}
