package es.fplumara.dam1.campeonato.model;

public abstract class Participante {
    private String id;
    private String nombre;
    private String pais;

    public Participante(String id, String nombre, String pais) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
    }


    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPais() {
        return pais;
    }
}
