package br.com.alura.screenmatch.model;

public enum Categoria {
    ACAO("Action","Ação"),
    ROMANCE("Romance","Romance"),
    COMEDIA("Comedy","Comedia"),
    DRAMA("Drama","Drama"),
    CRIME("Crime","Drama");

    private String categoriaOmdb;

    private String categoriaPortugues;

    Categoria(String categoriaOmdb, String categoriaPortugues){
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaPortugues = categoriaPortugues;
    }

    public static Categoria fromString (String text){
        for(Categoria categoria: Categoria.values()){
            if(categoria.categoriaOmdb.equalsIgnoreCase(text)){
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a serie buscada !!");
    }

    public static Categoria FromPortugues (String text){
        for(Categoria categoria: Categoria.values()){
            if(categoria.categoriaPortugues.equalsIgnoreCase(text)){
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a serie buscada !!");
    }
}
