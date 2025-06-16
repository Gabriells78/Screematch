package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repositorio;

    private List<SerieDTO> converteDados(List<Serie> series){
        return series.stream()
                .map(s -> new SerieDTO(s.getId(),s.getTitulo(),s.getTotalTemporadas(),s.getAvaliacao(),
                        s.getGenero(),s.getElenco(),s.getPoster(),s.getSinopse()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obterTodasAsSeries(){
        return converteDados(repositorio.findAll());
    }

    public List<SerieDTO> obterTop5Series() {
        return converteDados(repositorio.lancamentosMaisRecentes());
    }

    public List<SerieDTO> obterLancamentos(){
        return converteDados(repositorio.lancamentosMaisRecentes());
    }

    public SerieDTO obterPorId(Long id) {
        Optional<Serie> serie = repositorio.findById(id);

        if (serie.isPresent()){
            Serie s = serie.get();

            return new SerieDTO(s.getId(),s.getTitulo(),s.getTotalTemporadas(),s.getAvaliacao(),
                    s.getGenero(),s.getElenco(),s.getPoster(),s.getSinopse());
        }
        return null;
    }

    public List<EpisodioDTO> obterTodasAsTemporadas(Long id) {
        Optional<Serie> serie = repositorio.findById(id);

        if (serie.isPresent()){
            Serie s = serie.get();

            return s.getEpisodios().stream()
                    .map(e -> new EpisodioDTO(e.getTemporada(),e.getNumeroEpisodio(),e.getTitulo()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodioDTO> obterEpisodiosTemporadas(Long id, Long numero) {
      return repositorio.episodiosPorTemporada(id,numero)
              .stream().map(t -> new EpisodioDTO(t.getTemporada(),t.getNumeroEpisodio(),t.getTitulo()))
              .collect(Collectors.toList());

    }

    public List<SerieDTO> obterSeriesPorCategoria(String nomeGenero) {
        Categoria categorias = Categoria.FromPortugues(nomeGenero);
        return converteDados(repositorio.findByGenero(categorias));
    }

}
