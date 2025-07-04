package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie,Long> {

   Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

   List<Serie> findByElencoContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacao);

   List<Serie> findTop5ByOrderByAvaliacaoDesc();

   List<Serie> findByGenero(Categoria categoria);

   Optional<Serie> findById(Long id);

   //List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(Integer serieTamanho, Double avaliacao);

   @Query("select s from Serie s  WHERE s.totalTemporadas <= :serieTamanho AND s.avaliacao >= :avaliacao")
   List<Serie> seriePorTemporadaEAvaliacao(Integer serieTamanho, Double avaliacao);

   @Query("SELECT e from Serie s JOIN s.episodios e WHERE e.titulo  ILIKE %:trechoEpisodio%")
   List<Episodio> episodiosPorTrecho(String trechoEpisodio);


   @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :episodiosSerie ORDER BY e.avaliacao DESC LIMIT 5")
   List<Episodio> topEpisodiosPorSerie(Serie episodiosSerie);

   @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :episodioLancamento ")
   List<Episodio> episodiosPorSerieEAno(Serie serie, int episodioLancamento);

   @Query("SELECT s FROM Serie s " +
           "JOIN s.episodios e " +
           "GROUP BY s " +
           "ORDER BY MAX(e.dataLancamento) DESC LIMIT 5")

   List<Serie> lancamentosMaisRecentes();

   @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id AND e.temporada = :numero")
   List<Episodio> episodiosPorTemporada(Long id, Long numero);

}
