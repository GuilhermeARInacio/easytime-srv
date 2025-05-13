package easytime.srv.api.model.pontos;

public enum EntradaESaida {
    E1,
    S1,
    E2,
    S2,
    E3,
    S3;

    public static EntradaESaida fromOrdinal(int ordinal) {
        return values()[ordinal];
    }
}
