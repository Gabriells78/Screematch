package br.com.alura.screenmatch.principal;


import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsultaMyMemory;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;
import br.com.alura.screenmatch.service.DadosTraducao;
import org.springframework.beans.factory.annotation.Autowired;
import org.yaml.snakeyaml.util.UriEncoder;
import java.util.*;
import java.util.stream.Collectors;



public class Principal {

    private Scanner leitura = new Scanner(System.in);

    private ConsumoAPI consumo= new ConsumoAPI();

    private ConverteDados conversor = new ConverteDados();

    private  final String ENDERECO = "https://www.omdbapi.com/?t=" ;

    private final String API_KEY ="&apikey=53de85b5";

    private ConsultaMyMemory consulta = new ConsultaMyMemory();

    private List<DadosSerie> dadosSeries = new ArrayList<>();

    private SerieRepository repositorio;

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu(){

int opcao;

        do {

            var menu = """
            1 - Buscar series
            2 - Buscar episodios
            3 - Listar series buscadas
            4 - Traduzir                
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
//        case 4:
//            traduzir();
//            break;
        default:
            System.out.println("Opcao invalida");

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
            var json = consumo.ObterDados(ENDERECO+ UriEncoder.encode(nomeSerie) + API_KEY);
            DadosSerie dados = conversor.obterDados(json,DadosSerie.class);
            return dados;
        }

        private void buscarEpisodioPorSerie(){
            DadosSerie dadosSerie = getDadosSerie();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for(int i = 1; i<=dadosSerie.totalTemporadas(); i++){
                var json = consumo.ObterDados(ENDERECO+ UriEncoder.encode(dadosSerie.titulo())+"&season="+ i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json,DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
           temporadas.forEach(System.out::println);
        }

        private void listarSeriesBuscadas(){
            List<Serie> series = repositorio.findAll();
            series.stream()
                    .sorted(Comparator.comparing(Serie::getGenero))
                    .forEach(System.out::println);
        }
//
//        public void traduzir(){
//        ConsultaMyMemory consulta = new ConsultaMyMemory();
//            System.out.println(consulta.obterTraducao("hello my name is gabriel"));
//        }




}
