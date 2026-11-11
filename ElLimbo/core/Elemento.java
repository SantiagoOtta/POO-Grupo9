package core;

public enum Elemento {
    VENENO, FUEGO, RAYO, AGUA;

    public Elemento debilidad() {
        switch (this) {
            case FUEGO:
                return AGUA;
            case AGUA:
                return RAYO;
            case RAYO:
                return VENENO;
            default:
                return FUEGO;
        }
    }
}