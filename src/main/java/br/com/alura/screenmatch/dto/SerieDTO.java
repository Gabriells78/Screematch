package br.com.alura.screenmatch.dto;

import br.com.alura.screenmatch.model.Categoria;

public record SerieDTO(long id,

                       String titulo,

                       Integer totalTemporadas,

                       Double avaliacao,

                       Categoria genero,

                       String elenco,

                       String poster,

                       String sinopse) {
}
