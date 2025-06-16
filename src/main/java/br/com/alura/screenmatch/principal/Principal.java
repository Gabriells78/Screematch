package br.com.alura.screenmatch.principal;


import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.EpisodioRepository;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsultaMyMemory;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;
import br.com.alura.screenmatch.service.DadosTraducao;
import ch.qos.logback.core.encoder.JsonEscapeUtil;
import org.hibernate.type.descriptor.sql.internal.Scale6IntervalSecondDdlType;
import org.springframework.beans.factory.annotation.Autowired;
import org.yaml.snakeyaml.util.UriEncoder;

import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;


public class Principal {

    private Scanner leitura = new Scanner(System.in);

    private ConsumoAPI consumo= new ConsumoAPI();

    private ConverteDados conversor = new ConverteDados();

    private  final String ENDERECO = "https://www.omdbapi.com/?t=" ;

    private final String API_KEY ="&apikey=53de85b5";

    private ConsultaMyMemory consulta = new ConsultaMyMemory();

    private List<DadosSerie> dadosSeries = new ArrayList<>();

    private SerieRepository repositorio;

    private List<Serie> series = new ArrayList<>();

    public Principal(SerieRepository repositorio ) {
        this.repositorio = repositorio;

    }
    private  Optional<Serie> serieBuscada;

    public void exibeMenu(){

int opcao;

        do {

            var menu = """
            
            1 - Buscar series
            2 - Buscar episodios
            3 - Listar series buscadas
            4 - Traduzir   
            5 - Buscar serie por titulo 
            6 - Buscar serie por ator  
            7 - Buscar top 5 series 
            8 - Buscar serie por categoria   
            9-  Buscar serie por avaliacao e temporadas    
            10- Buscar episodio por trecho   
            11- Top 5 episodios Serie
            12- Buscar episodio Por data
            0 - Sair    
            """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

    switch (opcao) {
        case 1:
            buscarSerieWeb();
            break;
        case 2:
            buscarEpisodioPorSerie();
            break;
        case 0:
            System.out.println("Saindo....");
        case 3:
            listarSeriesBuscadas();
            break;
        case 5:
            buscarSeriePorTitulo();
            break;
        case 6:
            buscarSeriePorAtor();
        default:
            System.out.println("Opcao invalida");
        case 7:
            buscarTop5Series();
            break;
        case 8:
            buscarSeriesPorCategoria();
            break;
        case 9:
            buscarSeriePorTamanho();
            break;
        case 10:
            buscarEpisodioPorTrecho();
            break;
        case 11:
            topEpisodiosPorSerie();
            break;
        case 12:
            buscarEpisodiosApartirData();
            break;
        case 13:
            buscarEpisodiosPorId();
            break;
    }
}while( opcao!=0);
    }



    private void buscarSerieWeb() {


            DadosSerie dados = getDadosSerie();
//            dadosSeries.add(dados);
            Serie serie = new Serie(dados);
            repositorio.save(serie);
            System.out.println(serie);
        }

        private DadosSerie getDadosSerie(){
            System.out.println("Digite o nome da serie para busca: ");
            var nomeSerie = leitura.nextLine();
            var json = consumo.ObterDados(ENDERECO+ UriEncoder.encode(nomeSerie.toLowerCase(Locale.ROOT)) + API_KEY);
            DadosSerie dados = conversor.obterDados(json,DadosSerie.class);
            return dados;
        }

        private void buscarEpisodioPorSerie(){
            listarSeriesBuscadas();
            System.out.println("Escolha uma serie pelo nome: ");
        var nomeSerie = leitura.nextLine();

            Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

            if(serie.isPresent()) {

                var serieEncontrada = serie.get();

                List<DadosTemporada> temporadas = new ArrayList<>();

                for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                    var json = consumo.ObterDados(ENDERECO + UriEncoder.encode(serieEncontrada.getTitulo()) + "&season=" + i + API_KEY);
                    DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                    temporadas.add(dadosTemporada);
                }
                temporadas.forEach(System.out::println);

                List<Episodio> episodios = temporadas.stream()
                        .flatMap(d -> d.episodios().stream()
                                .map(e -> new Episodio(d.numero(), e)))
                        .collect(Collectors.toList());
                serieEncontrada.setEpisodios(episodios);
                repositorio.save(serieEncontrada);

            }else {

                System.out.println("Serie nao encontrada!!!");
            }

        }

        private void listarSeriesBuscadas(){
            series = repositorio.findAll();
            series.stream()
                    .sorted(Comparator.comparing(Serie::getGenero))
                    .forEach(System.out::println);
        }
//
//        public void traduzir(){
//        ConsultaMyMemory consulta = new ConsultaMyMemory();
//            System.out.println(consulta.obterTraducao("hello my name is gabriel"));
//        }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma serie pelo nome: ");
        var nomeSerie = leitura.nextLine();
        serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if(serieBuscada.isPresent()){
            System.out.println("Dados da serie: " + serieBuscada.get());
        }else {
            System.out.println("Serie nao encontrada!!");
        }
    }

    private void buscarSeriePorAtor() {
        System.out.println("Qual o nome para busca: ");
        var nomeAtor = leitura.nextLine();
        System.out.println("Qual e a avaliacao minima da serie: ");
        var avaliacao = leitura.nextDouble();
        List<Serie> seriesEncontradas = repositorio.findByElencoContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor,avaliacao);
        System.out.println("series em que " + nomeAtor + " trabalhou.");
        seriesEncontradas.forEach(s -> System.out.println(s.getTitulo() + " avaliacao: " + s.getAvaliacao()));

    }

    private void buscarTop5Series(){
        List<Serie> TopSeries = repositorio.lancamentosMaisRecentes();
        TopSeries.forEach(s -> System.out.println(s.getTitulo() + " avaliacao: " + s.getAvaliacao()));
    }


    private void buscarSeriesPorCategoria() {
        System.out.println("Deseja buscar series por qual categoria: ");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.FromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Serie da categoria " + nomeGenero);
       seriesPorCategoria.forEach(System.out::println);
    }

    private void buscarSeriePorTamanho(){
        System.out.println("Qual o tamanho da serie procurada por voce em temporadas: ");
        var serieTamanho = leitura.nextInt();
//        System.out.println("Qual a avaliacao media: ");
//        var avaliacaoMedia = leitura.nextDouble();
        System.out.println("qual a avaliacao minima: ");
        var avaliacao = leitura.nextDouble();

        List<Serie> listaTamanhos = repositorio.seriePorTemporadaEAvaliacao(serieTamanho,avaliacao);

        listaTamanhos.forEach(s -> System.out.println(s.getTitulo()));
    }

    private void buscarEpisodioPorTrecho(){
    System.out.println("Qual o nome do episodio para busca: ");
    var trechoEpisodio = leitura.nextLine();

    List<Episodio> episodiosEncontrados = repositorio.episodiosPorTrecho(trechoEpisodio);
    episodiosEncontrados.forEach(System.out::println);
    }

    private void topEpisodiosPorSerie(){
        buscarSeriePorTitulo();
        if(serieBuscada.isPresent()){
            Serie episodiosSerie = serieBuscada.get();
            List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(episodiosSerie);
            topEpisodios.forEach(e->
                    System.out.printf("Serie: %s Temporada %s - Episodio %s - %s - Avaliação   %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getNumeroEpisodio(), e.getTitulo(), e.getAvaliacao()));
        }
    }

    private void buscarEpisodiosApartirData(){
        buscarSeriePorTitulo();
        if(serieBuscada.isPresent()){
            Serie serie = serieBuscada.get();
            System.out.println("Digite o ano de lancamento ");
            var episodioLancamento = leitura.nextInt();
            leitura.nextLine();

            List<Episodio> episodiosAno = repositorio.episodiosPorSerieEAno( serie, episodioLancamento);
            episodiosAno.forEach( f->
                    System.out.printf("Serie: %s Temporada %s - Episodio %s - %s - Avaliacao %s - Lancamento %s\n ",
                            f.getSerie().getTitulo(), f.getTemporada(),
                            f.getNumeroEpisodio(), f.getTitulo(), f.getAvaliacao(), f.getDataLancamento()));
        }
    }

    private void buscarEpisodiosPorId(){
        System.out.println("digite o id da serie buscada: ");
        var id = leitura.nextLong();
        Optional<Serie> series = repositorio.findById(id);

        if(series.isPresent()){
            series.stream()
                    .forEach(System.out::println);

        }else {
            System.out.println("erro!! serie nao encontrada!!");
        }
    }

}
